module com.example {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires java.desktop;
    requires javafx.fxml;
    requires javafx.swing;
    

    opens com.example to javafx.fxml;
    exports com.example;
}