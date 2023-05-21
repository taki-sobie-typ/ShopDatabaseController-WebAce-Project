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

    //SEARCH
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Label selectFrom;

    //TABLE
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

    //INSERT and DELETE

    @FXML
    private TextField id2Field;
    @FXML
    private TextField nazwa2Field;
    @FXML
    private TextField vat2Field;
    @FXML
    private TextField jm2Field;
    @FXML
    private TextField netto2Field;
    @FXML
    private TextField brutto2Field;
    @FXML
    private TextField grupa2Field;
    @FXML
    private TextField barcode2Field;



    //POŁĄCZENIE Z BAZĄ PBRANIE REKORDÓW
    DatabaseController mysqlControl = new DatabaseController(
            "localhost",
            "3306",
            "sklep",
            "root",
            ""
    );
    //KONIEC POŁĄCZENIE Z BAZĄ PBRANIE REKORDÓW

    //ObservableList<Towar> towarObservableList = FXCollections.observableArrayList();

    public void refreshTable(){
        //towarObservableList.clear();


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


        towarTable.setItems(mysqlControl.getAllTowarObservableList(querry));
    }

    public void search(){
        String sql_SELECT_INNER_JOIN = "SELECT towary.tid, towary.nazwa, vat.stawka AS 'vat', towary.jm, towary.netto, towary.brutto, grupy.nazwa AS 'grupa', towary.barcod FROM towary INNER JOIN grupy ON towary.grupa=grupy.grid INNER JOIN vat ON towary.vat=vat.vid";
        String searchQuerry = searchField.getText();
        String searchQuerrySql = sql_SELECT_INNER_JOIN + " WHERE towary.nazwa LIKE '%" + searchQuerry + "%';";
        loadDate(searchQuerrySql);
        searchField.setText("");
    }


    public void insertInto(){
        String nazwa = nazwa2Field.getText();
        String vat = vat2Field.getText();
        String jm = jm2Field.getText();
        String netto = netto2Field.getText();
        String brutto = brutto2Field.getText();
        String grupa = grupa2Field.getText();
        String barcode = barcode2Field.getText();

        mysqlControl.insertTowar(nazwa, vat, jm, netto, brutto, grupa, barcode);

        nazwa2Field.setText("");
        vat2Field.setText("");
        jm2Field.setText("");
        netto2Field.setText("");
        brutto2Field.setText("");
        grupa2Field.setText("");
        barcode2Field.setText("");

        loadDate();

    }

    public void deleteFrom(){
        String id = id2Field.getText();

        mysqlControl.deleteTowar(id);

        id2Field.setText("");

        loadDate();

    }


}