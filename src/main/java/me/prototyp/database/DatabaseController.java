package me.prototyp.database;
import java.sql.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.prototyp.database.Towar;
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

    private boolean createTable(){
        String sql1 = sqlCreateTableGrupy;
        String sql2 = sqlCreateTableTowar;
        String sql3 = sqlCreateTableVat;
        try (Statement stmt = this.connection.createStatement()){
            //tworzymy tabelę
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
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

    /*
    public boolean addUser(User user) {
        String sql = "INSERT INTO Users (username, email) VALUES (?,?)";
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            int result = pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ArrayList<User> getAllUsers() {
        String sql = "SELECT username, email FROM Users";
        ArrayList<User> users = new ArrayList<>();
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            //iterujemy po wynikach
            while (rs.next()){
                users.add(new User(rs.getString("username"), rs.getString("email")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public ArrayList<User> getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        ArrayList<User> users = new ArrayList<>();
        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)){
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                User user = new User(rs.getString("username"), rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }
    */
}
