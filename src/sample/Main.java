package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Double> prices = new ArrayList<>();
    ListView<String> booksInCart = new ListView<>();

    double subtotal;
    double total;
    double tax;
    final double SALES_TAX = .07;
    private Label subtotalLabel;
    private Label taxLabel;
    private Label totalLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Book Store");
        getBookInfo();

        //Converts the titles array list to an observable list so it can be stored in listview
        ObservableList<String> bookTitleObservable =
                FXCollections.observableArrayList(titles);

        ListView<String> bookTitlesList = new ListView<>(bookTitleObservable);

        //Stores the selected item into the shopping cart
        bookTitlesList.getSelectionModel().selectedIndexProperty().addListener(event ->
        {
            int index = bookTitlesList.getSelectionModel().getSelectedIndex();
            booksInCart.getItems().add(titles.get(index));
        });

        Button clear = new Button("Clear");
        Button remove = new Button("Remove");
        Button checkout = new Button("Checkout");

        subtotalLabel = new Label();
        taxLabel = new Label();
        totalLabel = new Label();

        clear.setOnAction(event -> clearButtonHandler());
        remove.setOnAction(event -> removeButtonHandler());
        checkout.setOnAction(event -> checkoutButtonHandler());

        VBox vbox = new VBox(10, clear, remove, checkout, subtotalLabel, taxLabel, totalLabel);
        HBox hbox = new HBox(35, bookTitlesList, vbox, booksInCart);
        hbox.setPadding(new Insets(10));
        Scene scene = new Scene(hbox,700,500);
        hbox.setAlignment(Pos.CENTER);
        scene.getStylesheets().add("sample/fgfghfh/fhhh/styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Reads the BookPrices.txt, and seperates the title from the price
     */
    private void getBookInfo() throws IOException {
        File file = new File("BookPrices.txt");
        Scanner inputFile = new Scanner(file);
        String currentLine;
        String[] tokens;

        while (inputFile.hasNext()) {
            currentLine = inputFile.nextLine();
            tokens = currentLine.split(",");

            titles.add(tokens[0]);
            prices.add(Double.parseDouble(tokens[1]));
        }
        inputFile.close();
    }

    /**
     * Clears the books in the cart and prices
     */
    private void clearButtonHandler() {
        booksInCart.getItems().clear();
        subtotalLabel.setText("");
        taxLabel.setText("");
        totalLabel.setText("");
        subtotal = 0;
        tax = 0;
        total = 0;
    }

    /**
     * Removes the item selected in cart and prices
     */
    private void removeButtonHandler() {
        booksInCart.getItems().remove(booksInCart.getSelectionModel().getSelectedItem());
        subtotalLabel.setText("");
        taxLabel.setText("");
        totalLabel.setText("");
        subtotal = 0;
        tax = 0;
        total = 0;
    }

    /**
     * Displays the subtotal, sales tax, and total
     */
    private void checkoutButtonHandler() {
        subtotal = 0;
        tax = 0;
        total = 0;

        // Goes through books in shopping cart, and compares the cart item with all the titles to get the array index for the pricing
        for (int i = 0; i < titles.size(); i++) {
            for (int j = 0; j < booksInCart.getItems().size(); j++) {
                if (titles.get(i).equals(booksInCart.getItems().get(j)))
                    subtotal += prices.get(i);
            }
        }
        tax = subtotal * SALES_TAX;
        total = subtotal + tax;
        subtotalLabel.setText(String.format("Subtotal: $%.2f", subtotal));
        taxLabel.setText(String.format("Sales tax: $%.2f", tax));
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
}
