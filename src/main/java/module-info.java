module com.tv.dateprint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.graphics;
	requires java.desktop;

    opens com.tv.dateprint to javafx.fxml;
    exports com.tv.dateprint;
}
