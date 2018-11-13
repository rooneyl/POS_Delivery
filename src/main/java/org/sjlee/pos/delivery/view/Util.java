package org.sjlee.pos.delivery.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.util.Optional;

public class Util {

    public static Button createButton(String imageDir, double size, boolean border) {
        ImageView imageView;
        Button button = new Button();

        try {
            Image image = new Image(new FileInputStream(imageDir));
            imageView = new ImageView(image);
        } catch (Exception e) {
            imageView = new ImageView();
            border = true;
        }

        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        button.setPrefSize(size, size);
        button.setGraphic(imageView);

        if (!border)
            button.setStyle("-fx-background-color: rgba(0,0,0,0)");

        return button;
    }

    public static VBox getVBox(double width, double height) {
        VBox vBox = new VBox(Layout.SPACING);
        vBox.setPrefWidth(width);
        vBox.setPrefHeight(height);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(Layout.SPACING));

        return vBox;
    }

    public static HBox getHBox(double width, double height) {
        HBox hBox = new HBox(Layout.SPACING);
        hBox.setPrefWidth(width);
        hBox.setPrefHeight(height);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(Layout.SPACING));

        return hBox;
    }

    public static Optional<String> getInputText(String title) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setGraphic(null);
        dialog.setHeaderText(title);

        Optional<String> input = dialog.showAndWait();
        if (input.isPresent() && !input.get().equals(""))
            return input;

        return Optional.empty();
    }

    public static boolean getConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to continue?", ButtonType.YES, ButtonType.CANCEL);
        alert.setGraphic(null);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }
}