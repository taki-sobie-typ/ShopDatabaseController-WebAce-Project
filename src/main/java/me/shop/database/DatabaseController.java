package me.shop.database;
import java.sql.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseController {
    // ZMIENNE TEXTOWE SQL
    String sqlCreateTableGrupy = "CREATE TABLE IF NOT EXISTS `grupy` (\n" +
            "  `grid` int(11) NOT NULL auto_increment,\n" +
            "  `nazwa` varchar(30) NOT NULL,\n" +
            "  PRIMARY KEY  (`grid`)\n" +
            ")";
    String sqlCreateTableVat = "CREATE TABLE IF NOT EXISTS `vat` (\n" +
            "  `vid` int(11) NOT NULL auto_increment,\n" +
            "  `stawka` varchar(6) NOT NULL,\n" +
            "  `wartosc` decimal(5,2) NOT NULL,\n" +
            "  PRIMARY KEY  (`vid`)\n" +
            ")";
    String sqlCreateTableTowar =
            "CREATE TABLE IF NOT EXISTS `towary` (\n" +
            "  `tid` int(11) NOT NULL auto_increment,\n" +
            "  `nazwa` varchar(255) NOT NULL,\n" +
            "  `vat` int(11) NOT NULL,\n" +
            "  `jm` varchar(5) NOT NULL,\n" +
            "  `netto` decimal(8,2) NOT NULL,\n" +
            "  `brutto` decimal(8,2) NOT NULL,\n" +
            "  `grupa` int(11) NOT NULL,\n" +
            "  `barcod` varchar(13) NOT NULL,\n" +
            "  PRIMARY KEY  (`tid`),\n" +
            "  KEY `nazwa` (`nazwa`)\n" +
            ")";
    String sql_SELECT_INNER_JOIN = "SELECT towary.tid, towary.nazwa, vat.stawka AS 'vat', towary.jm, towary.netto, towary.brutto, grupy.nazwa AS 'grupa', towary.barcod FROM towary INNER JOIN grupy ON towary.grupa=grupy.grid INNER JOIN vat ON towary.vat=vat.vid;";

    private Connection connection = null;

    //Konstruktor dla MySQL (XAMPP)
    public DatabaseController(String host, String port, String db, String username, String pwd){
        this.connection = prepareMySQL(host, port, db, username, pwd);
        if  (connection == null){
            System.out.println("Nie udało się nawiązać połączenia");
            return;
        }
        if(createTable()){
            System.out.println("Wszystko gra, tabele Towar, Vat, Grupy gotowe");
        } else {
            System.out.println("Coś poszło nie tak, przy tworzeniu tabeli Users");
        }
    }

    private Connection prepareMySQL(String host, String port, String db, String username, String pwd ){
        String url = "jdbc:mysql://"+host+":"+port+"/"+db;
        System.out.println(url);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, pwd);
            if (conn != null) {
                DatabaseMetaData databaseMetaData = conn.getMetaData();
                System.out.println("Nazwa sterownika bazy danych to: " +  databaseMetaData.getDriverName());
                System.out.println("Baza danych została utworzona / Nawiązano połączenie z XAMPP");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean loadDefault(){
        String sql3Insert = sqlDefaultOptionalTowaryValues;
        try (Statement stmt = this.connection.createStatement()){
            //Insertujemy defaultowe wartości;
            stmt.execute(sql3Insert);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;

    }

    public boolean createTable(){
        String sql1 = sqlCreateTableGrupy;
        String sql2 = sqlCreateTableTowar;
        String sql3 = sqlCreateTableVat;
        String sql1Insert = sqlDefaultGrupyValues;
        String sql2Insert = sqlDefaultVatValues;
        try (Statement stmt = this.connection.createStatement()){
            //tworzymy tabelę
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);

            try (Statement stmt2 = this.connection.createStatement()){
                stmt2.execute(sql1Insert);
                stmt2.execute(sql2Insert);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean prtintRow(){
        String sql = sql_SELECT_INNER_JOIN;
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql);

        }
        catch (SQLException e) {
           System.out.println(e.getMessage());
           return false;
        }
        return true;
    }

    public ArrayList<Towar> getAllTowar() {
        String sql = sql_SELECT_INNER_JOIN;
        ArrayList<Towar> towar = new ArrayList<>();
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            //iterujemy po wynikach
            while (rs.next()){
                towar.add(new Towar(rs.getInt("tid"), rs.getString("nazwa"), rs.getString("vat"), rs.getString("jm"), rs.getDouble("netto"), rs.getDouble("brutto"), rs.getString("grupa"), rs.getString("barcod")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return towar;
    }

    public ObservableList<Towar> getAllTowarObservableList() {
        ObservableList<Towar> towarObservableList = FXCollections.observableArrayList();

        String sql = sql_SELECT_INNER_JOIN;
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            //iterujemy po wynikach
            while (rs.next()){
                towarObservableList.add(new Towar(rs.getInt("tid"), rs.getString("nazwa"), rs.getString("vat"), rs.getString("jm"), rs.getDouble("netto"), rs.getDouble("brutto"), rs.getString("grupa"), rs.getString("barcod")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return towarObservableList;
    }

    public ObservableList<Towar> getAllTowarObservableList(String SQL) {
        ObservableList<Towar> towarObservableList = FXCollections.observableArrayList();
        towarObservableList.clear();
        String sql = SQL;
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            //iterujemy po wynikach
            while (rs.next()){
                towarObservableList.add(new Towar(rs.getInt("tid"), rs.getString("nazwa"), rs.getString("vat"), rs.getString("jm"), rs.getDouble("netto"), rs.getDouble("brutto"), rs.getString("grupa"), rs.getString("barcod")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return towarObservableList;
    }

    public boolean insertTowar(String nazwa, String vat, String jm, String netto, String brutto, String grupa, String barcode) {
        String sql = "INSERT INTO towary (towary.nazwa, towary.vat, towary.jm, towary.netto, towary.brutto, towary.grupa, towary.barcod) VALUES (?,?,?,?,?,?,?);";
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql);
            pstmt.setString(1, nazwa);
            pstmt.setString(2, String.valueOf(vat));
            pstmt.setString(3, jm);
            pstmt.setString(4, String.valueOf(netto));
            pstmt.setString(5, String.valueOf(brutto));
            pstmt.setString(6, String.valueOf(grupa));
            pstmt.setString(7, barcode);
            int result = pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteTowar(String ID) {
        String sql = "DELETE FROM towary WHERE tid="+ID+";";
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql);
            int result = pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    String sqlDefaultGrupyValues = "INSERT INTO `grupy` (`grid`, `nazwa`) VALUES\n" +
            "(1, 'PIWO'),\n" +
            "(2, 'WINO/SZAMPANY'),\n" +
            "(3, 'WÓDKA/WHYSKY/BRENDY'),\n" +
            "(4, 'PAPIEROSY/PRASA/KAR'),\n" +
            "(5, 'NAPOJE/SŁODYCZE'),\n" +
            "(6, 'GAL.BUD.ZUP.PRZY.PRAWY'),\n" +
            "(7, 'WĘDL/DRÓB/RYBA.LUZ'),\n" +
            "(8, 'POZOSTAŁY TOWAR'),\n" +
            "(9, 'MĄK.CU.RYŻ.KASZ.MAKARON'),\n" +
            "(10, 'MROŻONKI,LODY'),\n" +
            "(11, 'OWOCE,WARZYWA'),\n" +
            "(12, 'ART.CHEMICZNE'),\n" +
            "(13, 'KAWY,HERBATY'),\n" +
            "(14, 'NABIAŁ'),\n" +
            "(15, 'PIECZYWO'),\n" +
            "(16, 'OPAKOWANIE ZWROTNE'),\n" +
            "(17, 'BEZ GRUPY');";
    String sqlDefaultVatValues = "INSERT INTO `vat` (`vid`, `stawka`, `wartosc`) VALUES\n" +
            "(1, '23%', 0.23),\n" +
            "(2, '8%', 0.08),\n" +
            "(3, '5%', 0.05),\n" +
            "(4, '0%', 0.00),\n" +
            "(7, 'ZW', 0.00);";
    String sqlDefaultOptionalTowaryValues = "INSERT INTO `towary` (`tid`, `nazwa`, `vat`, `jm`, `netto`, `brutto`, `grupa`, `barcod`) VALUES\n" +
            "(1, 'WAFLE SONKO 5KG', 3, 'SZ.', 1.42, 1.49, 8, '5999999510605'),\n" +
            "(2, 'GUMA HALLS 200G', 2, 'SZ.', 2.50, 2.70, 5, '7999990820457'),\n" +
            "(3, 'BAKUŚ SHAKE 0.5L', 3, 'SZ.', 3.00, 3.15, 14, '5999999022494'),\n" +
            "(4, 'LUKSJA ŻEL D/KĄP 0.5L', 1, 'SZ.', 4.87, 5.99, 12, '5999998999815'),\n" +
            "(5, 'ŻEL SANEX 230G', 1, 'SZ.', 6.50, 7.99, 12, '3199990199992'),\n" +
            "(6, 'WÓD.BIAŁY BOCIAN 500ML', 1, 'SZ.', 23.58, 29.00, 3, '5999999929438'),\n" +
            "(7, 'NAP.ORANGINA 1.4L.', 1, 'SZ.', 4.46, 5.49, 5, '8435185999993'),\n" +
            "(8, 'SOPL.MIX SM.200ML', 1, 'SZ.', 11.30, 13.90, 3, '9999971004999'),\n" +
            "(9, 'PIW.PERŁA MIODOWA 500ML.', 1, 'SZ.', 2.84, 3.49, 1, '9999999061415'),\n" +
            "(10, 'PRYMAT PRZ.D/MIĘS 200G.', 2, 'SZ.', 4.62, 4.99, 6, '5901139999991'),\n" +
            "(11, 'NAP.OSHEE 0.75L.', 1, 'SZ.', 3.01, 3.70, 5, '5909999999934'),\n" +
            "(13, 'FLAKI 500G.', 3, 'SZ.', 8.48, 8.90, 8, '9999999902258'),\n" +
            "(14, 'ŚM/ŻELKI 90G.', 1, 'SZ.', 3.24, 3.99, 5, '4014499999996'),\n" +
            "(15, 'BAKUŚ SHAKE 230G.', 3, 'SZ.', 3.00, 3.15, 14, '5999999999494'),\n" +
            "(16, 'WAFLE SONKO 5KG', 3, 'SZ.', 1.42, 1.49, 8, '5999999510605'),\n" +
            "(17, 'GUMA HALLS 200G', 2, 'SZ.', 2.50, 2.70, 5, '7999990820457'),\n" +
            "(18, 'BAKUŚ SHAKE 0.5L', 3, 'SZ.', 3.00, 3.15, 14, '5999999022494'),\n" +
            "(19, 'LUKSJA ŻEL D/KĄP 0.5L', 1, 'SZ.', 4.87, 5.99, 12, '5999998999815'),\n" +
            "(20, 'ŻEL SANEX 230G', 1, 'SZ.', 6.50, 7.99, 12, '3199990199992'),\n" +
            "(21, 'WÓD.BIAŁY BOCIAN 500ML', 1, 'SZ.', 23.58, 29.00, 3, '5999999929438'),\n" +
            "(22, 'NAP.ORANGINA 1.4L.', 1, 'SZ.', 4.46, 5.49, 5, '8435185999993'),\n" +
            "(23, 'SOPL.MIX SM.200ML', 1, 'SZ.', 11.30, 13.90, 3, '9999971004999'),\n" +
            "(24, 'PIW.PERŁA MIODOWA 500ML.', 1, 'SZ.', 2.84, 3.49, 1, '9999999061415');";

}
