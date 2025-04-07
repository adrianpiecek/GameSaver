package com.gamesaver.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.gamesaver.model.GameDeal;
import com.gamesaver.service.CheapSharkService;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.List;

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

    @FXML
    public void initialize() {
        cheapSharkService = new CheapSharkService();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        storeColumn.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        savingsColumn.setCellValueFactory(new PropertyValueFactory<>("savings"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        dealsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2){
                GameDeal selectedDeal = dealsTable.getSelectionModel().getSelectedItem();
                if (selectedDeal != null){
                    String url = "https://www.cheapshark.com/redirect?dealID=" + selectedDeal.getStoreId();
                    openWebPage(url);
                }
            }
        });
    }

    @FXML
    private void onSearch() {
        String title = searchTextField.getText().trim();
        if (title.isEmpty()) return;

        try {
            List<GameDeal> deals = cheapSharkService.searchDeals(title);
            System.out.println("Deals fetched: " + deals.size());
            dealsTable.getItems().clear();
            currentDeals.setAll(deals);
            //dealsTable.setItems(currentDeals);
            dealsTable.getItems().addAll(deals);
            showingWishlist = false;
            showWishlistButton.setText("Show Wishlist");
        } catch (IOException e) {
            // Handle the exception, e.g., show an alert to the user
            showAlert("Error", "Failed to fetch game deals. Please try again.");
        }
    }

    @FXML
    private void onAddToWishlist() {
        // TODO: Add selected deal to wishlist
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
}
