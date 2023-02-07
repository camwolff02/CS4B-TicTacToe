package com.example;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupWindow {
   public static void display(String title, String message) {
      Stage window = new Stage();
      window.setResizable(false);
      
      // Specifying the owner Window (parent) for the current stage
      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle(title);
      window.setMinWidth(250);
      window.setMinHeight(150);
      
      Label label = new Label();
      label.setText(message);
      label.setStyle("-fx-background-color:#20262E;-fx-font-size: 8pt; -fx-font-family: 'Public Pixel';-fx-text-fill: #8f3174;");
      Scene scene = new Scene(label);
      window.setScene(scene);
      window.showAndWait();
   }
}
