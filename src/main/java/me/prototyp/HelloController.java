package me.prototyp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import me.prototyp.database.DatabaseController;
import me.prototyp.database.Towar;

import java.util.ArrayList;

public class HelloController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Label selectFrom;

    @FXML
    private TableView<Towar> towarTable;

    @FXML
    private TableColumn<Towar, String> idColumn;
    @FXML
    private TableColumn<Towar, String> nazwaColumn;
    @FXML
    private TableColumn<Towar, String> vatColumn;
    @FXML
    private TableColumn<Towar, String> jmColumn;
    @FXML
    private TableColumn<Towar, String> nettoColumn;
    @FXML
    private TableColumn<Towar, String> bruttoColumn;
    @FXML
    private TableColumn<Towar, String> grupaColumn;
    @FXML
    private TableColumn<Towar, String> barcodeColumn;

    //ObservableList<Towar> towarObservableList = FXCollections.observableArrayList();

    public void refreshTable(){
        //towarObservableList.clear();

        //POŁĄCZENIE Z BAZĄ PBRANIE REKORDÓW
        DatabaseController mysqlControl = new DatabaseController(
                "localhost",
                "3306",
                "sklep",
                "root",
                ""
        );
        //KONIEC POŁĄCZENIE Z BAZĄ PBRANIE REKORDÓW

        towarTable.setItems(mysqlControl.getAllTowarObservableList());
    }

    public void initialize() {
        loadDate();
    }

    public void loadDate() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        vatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
        jmColumn.setCellValueFactory(new PropertyValueFactory<>("jm"));
        nettoColumn.setCellValueFactory(new PropertyValueFactory<>("netto"));
        bruttoColumn.setCellValueFactory(new PropertyValueFactory<>("brutto"));
        grupaColumn.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));


        //POŁĄCZENIE Z BAZĄ PBRANIE REKORDÓW
        DatabaseController mysqlControl = new DatabaseController(
                "localhost",
                "3306",
                "sklep",
                "root",
                ""
        );
        //KONIEC POŁĄCZENIE Z BAZĄ PBRANIE REKORDÓW

        towarTable.setItems(mysqlControl.getAllTowarObservableList());
    }

    public void loadDate(String querry) {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        vatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
        jmColumn.setCellValueFactory(new PropertyValueFactory<>("jm"));
        nettoColumn.setCellValueFactory(new PropertyValueFactory<>("netto"));
        bruttoColumn.setCellValueFactory(new PropertyValueFactory<>("brutto"));
        grupaColumn.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));


        //POŁĄCZENIE Z BAZĄ PBRANIE REKORDÓW
        DatabaseController mysqlControl = new DatabaseController(
                "localhost",
                "3306",
                "sklep",
                "root",
                ""
        );
        //KONIEC POŁĄCZENIE Z BAZĄ PBRANIE REKORDÓW

        towarTable.setItems(mysqlControl.getAllTowarObservableList(querry));
    }

    public void search(){
        String sql_SELECT_INNER_JOIN = "SELECT towary.tid, towary.nazwa, vat.stawka AS 'vat', towary.jm, towary.netto, towary.brutto, grupy.nazwa AS 'grupa', towary.barcod FROM towary INNER JOIN grupy ON towary.grupa=grupy.grid INNER JOIN vat ON towary.vat=vat.vid";
        String searchQuerry = searchField.getText();
        String searchQuerrySql = sql_SELECT_INNER_JOIN + " WHERE towary.nazwa LIKE '%" + searchQuerry + "%';";
        loadDate(searchQuerrySql);
        searchField.setText("");
    }

}