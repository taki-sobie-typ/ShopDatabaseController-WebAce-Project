module me.shop {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;


    opens me.shop to javafx.fxml;
    opens me.shop.database to javafx.fxml;

    exports me.shop;
    exports me.shop.database;
}
