module pabulo.teste.teste {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens pabulo.teste.teste to javafx.fxml;
    exports pabulo.teste.teste;
}