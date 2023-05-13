module me.prototyp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens me.prototyp to javafx.fxml;
    opens me.prototyp.database to javafx.fxml;

    exports me.prototyp;
    exports me.prototyp.database;
}