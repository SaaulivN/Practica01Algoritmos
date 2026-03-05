module com.example.solitario {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    // Solo abrimos los paquetes que contienen CLASES (.java)
    opens com.example.solitario to javafx.graphics, javafx.fxml;
    opens com.example.solitario.GUI to javafx.graphics;

    exports com.example.solitario;
    exports com.example.solitario.GUI;
    exports DeckOfCards;
}