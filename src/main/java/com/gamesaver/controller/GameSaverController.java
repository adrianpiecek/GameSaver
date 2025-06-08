package com.gamesaver.controller;

import com.gamesaver.db.DatabaseHelper;
import com.gamesaver.events.GameDealDoubleClickEvent;
import com.gamesaver.events.GlobalEventBus;
import com.gamesaver.service.DatabaseService;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.gamesaver.model.GameDeal;
import com.gamesaver.service.CheapSharkService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.List;
import com.google.common.eventbus.Subscribe;

public class GameSaverController {
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private Button showWishlistButton;
    @FXML private Button addToWishlistButton;
    @FXML private TableView<GameDeal> dealsTable;
    @FXML private TableColumn<GameDeal, String> titleColumn;
    @FXML private TableColumn<GameDeal, String> storeColumn;
    @FXML private TableColumn<GameDeal, String> savingsColumn;
    @FXML private TableColumn<GameDeal, String> priceColumn;

    private boolean showingWishlist = false;
    private ObservableList<GameDeal> currentDeals = FXCollections.observableArrayList();
    private ObservableList<GameDeal> wishlistDeals = FXCollections.observableArrayList();

    private CheapSharkService cheapSharkService;
    private final DatabaseService databaseService = DatabaseService.getInstance();


    @FXML
    public void initialize() {
        cheapSharkService = new CheapSharkService();
        GlobalEventBus.getInstance().register(this);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        storeColumn.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        storeColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(32);
                imageView.setFitHeight(32);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String storeId, boolean empty) {
                super.updateItem(storeId, empty);
                if (empty || storeId == null) {
                    setGraphic(null);
                } else {
                    int storeIdAdjusted = Integer.parseInt(storeId) - 1; // nazwy logo zaczynają się od 0, więc odejmujemy 1
                    String imageUrl = "https://www.cheapshark.com/img/stores/logos/" + storeIdAdjusted + ".png";
                    imageView.setImage(new javafx.scene.image.Image(imageUrl, true));
                    setGraphic(imageView);
                }
            }
        });
        savingsColumn.setCellValueFactory(new PropertyValueFactory<>("savings"));
        savingsColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String savings, boolean empty) {
                super.updateItem(savings, empty);
                if (empty || savings == null) {
                    setText(null);
                } else {
                    try {
                        double savingsValue = Double.parseDouble(savings);
                        int rounded = (int) Math.round(savingsValue);
                        setText(rounded + "%");
                    } catch (NumberFormatException e) {
                        setText(savings);
                    }
                }
            }
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        dealsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2){
                GameDeal selectedDeal = dealsTable.getSelectionModel().getSelectedItem();
                if (selectedDeal != null){
                    GlobalEventBus.getInstance().post(new GameDealDoubleClickEvent(selectedDeal));
                }
            }
        });

    }

    @Subscribe
    public void onGameDealDoubleClick(GameDealDoubleClickEvent event) {
        GameDeal deal = event.getDeal();
        String url = "https://www.cheapshark.com/redirect?dealID=" + deal.getDealID();
        openWebPage(url);
    }

    @FXML
    private void onSearch() {
        String title = searchTextField.getText().trim();
        if (title.isEmpty()) return;

        searchButton.setDisable(true);

        Task<List<GameDeal>> searchTask = new Task<>() {
            @Override
            protected List<GameDeal> call() throws Exception {
                return cheapSharkService.searchDeals(title);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<GameDeal> results = searchTask.getValue();
            currentDeals.setAll(results);
            dealsTable.getItems().setAll(currentDeals);
            showingWishlist = false;
            showWishlistButton.setText("Show Wishlist");
            searchButton.setDisable(false);
        });

        searchTask.setOnFailed(event -> {
            showAlert("Error", "Failed to fetch game deals: " + searchTask.getException().getMessage());
            searchButton.setDisable(false);
        });

        new Thread(searchTask).start();
    }

    @FXML
    private void onShowWishlist() {
        if (!showingWishlist) {
            // Wczytywanie wishlist z bazy danych w tle
            showWishlistButton.setDisable(true);

            Task<List<GameDeal>> loadWishlistTask = new Task<>() {
                @Override
                protected List<GameDeal> call() {
                    return databaseService.getAllWishlistDeals();
                }
            };

            loadWishlistTask.setOnSucceeded(event -> {
                wishlistDeals.setAll(loadWishlistTask.getValue());
                dealsTable.getItems().setAll(wishlistDeals);
                showWishlistButton.setText("Back to Search");
                showingWishlist = true;
                showWishlistButton.setDisable(false); // odblokowanie
            });

            loadWishlistTask.setOnFailed(event -> {
                showAlert("Database Error", "Failed to load wishlist: " + loadWishlistTask.getException().getMessage());
                showWishlistButton.setDisable(false); // odblokowanie
            });

            new Thread(loadWishlistTask).start();

        } else { // powrót do wyników wyszukiwania
            dealsTable.getItems().setAll(currentDeals);
            showWishlistButton.setText("Show Wishlist");
            showingWishlist = false;
        }
    }

    @FXML
    private void onAddToWishlist() {
        GameDeal selected = dealsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a game from the table.");
            return;
        }

        databaseService.addDealToWishlist(
                new GameDeal(
                        selected.getTitle(),
                        selected.getStoreId(),
                        selected.getSavings(),
                        selected.getPrice(),
                        selected.getDealID()
                )
        );

        showAlert("Success", "Game added to wishlist!");
    }

    private void openWebPage(String url){
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException e) {
            showAlert("Error", "Failed to open web page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void cleanup() {
        GlobalEventBus.getInstance().unregister(this);
        DatabaseHelper.getInstance().close();
    }
}
