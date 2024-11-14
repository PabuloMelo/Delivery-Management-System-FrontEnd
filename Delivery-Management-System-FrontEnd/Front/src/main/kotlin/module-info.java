module pabulo.teste.front {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.sql;
    requires com.google.gson;
    requires okhttp3;


    opens pabulo.teste.front to javafx.fxml;

    opens pabulo.teste.front.controllers to javafx.fxml;

    opens pabulo.teste.front.controllers.customer to javafx.fxml;

    opens pabulo.teste.front.controllers.order to javafx.fxml;

    opens pabulo.teste.front.controllers.seller to javafx.fxml;

    opens pabulo.teste.front.controllers.driver to javafx.fxml;

    opens pabulo.teste.front.controllers.load to javafx.fxml;

    opens pabulo.teste.front.controllers.state to javafx.fxml;

    opens pabulo.teste.front.controllers.address to javafx.fxml;

    opens pabulo.teste.front.entity to com.google.gson;

    opens pabulo.teste.front.dtoConverter.customer to com.google.gson;

    opens pabulo.teste.front.dtoConverter.order to com.google.gson;

    opens pabulo.teste.front.dtoConverter.load to com.google.gson;

    opens pabulo.teste.front.dtoConverter.state to com.google.gson;

    opens pabulo.teste.front.dtoConverter.seller to com.google.gson;




    exports pabulo.teste.front.controllers.customer to javafx.fxml;

    exports pabulo.teste.front.controllers.order to javafx.fxml;

    exports pabulo.teste.front.controllers.seller to javafx.fxml;

    exports pabulo.teste.front.controllers.driver to javafx.fxml;

    exports pabulo.teste.front.controllers.load to javafx.fxml;

    exports pabulo.teste.front.controllers.state to javafx.fxml;

    exports pabulo.teste.front.controllers.address to javafx.fxml;

    exports pabulo.teste.front;
}