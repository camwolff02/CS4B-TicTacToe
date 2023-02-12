package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PopupWindow {
   public static void display(String title, String message) {
      Stage window = new Stage();
      window.setResizable(false);
      
      // Specifying the owner Window (parent) for the current stage
      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle(title);
      window.setMinWidth(500);
      window.setMinHeight(150);
      
      Label label = new Label();
      label.setText(message);
      label.setStyle("-fx-background-color:#2a313a;-fx-font-size: 10pt; -fx-font-family: 'Public Pixel';-fx-text-fill: #CD5888;-fx-border-color: white; -fx-border-width: 2px;");
      label.setAlignment(Pos.CENTER);
      label.setPadding(new Insets(10));
      Scene scene = new Scene(label);
      window.setScene(scene);

      window.centerOnScreen();
      window.showAndWait();
   }
}
