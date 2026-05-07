module ca.qc.cegep.g30.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens ca.qc.cegep.g30.snake to javafx.fxml;
    exports ca.qc.cegep.g30.snake;
}