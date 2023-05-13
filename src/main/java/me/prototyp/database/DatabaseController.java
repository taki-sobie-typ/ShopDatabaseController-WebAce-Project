package me.prototyp.database;
import java.sql.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.prototyp.database.Towar;
public class DatabaseController {

    private Connection connection = null;

    //Konstruktor dla MySQL (XAMPP)
    public DatabaseController(String host, String port, String db, String username, String pwd){
        this.connection = prepareMySQL(host, port, db, username, pwd);
        if  (connection == null){
            System.out.println("Nie udało się nawiązać połączenia");
            return;
        }
        if(createTable()){
            System.out.println("Wszystko gra, tabela Users gotowa");
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
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n" +
                "id integer PRIMARY KEY AUTO_INCREMENT, \n" +
                "username text NOT NULL,\n" +
                "email text NOT NULL);";
        try (Statement stmt = this.connection.createStatement()){
            //tworzymy tabelę
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean prtintRow(){
        String sql = "SELECT * FROM 'towary'SELECT * FROM `towary` WHERE tid=1;";
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
        String sql = "SELECT * FROM `towary`;";
        ArrayList<Towar> towar = new ArrayList<>();
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            //iterujemy po wynikach
            while (rs.next()){
                towar.add(new Towar(rs.getInt("tid"), rs.getString("nazwa"), rs.getInt("vat"), rs.getString("jm"), rs.getDouble("netto"), rs.getDouble("brutto"), rs.getInt("grupa"), rs.getString("barcod")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return towar;
    }

    public ObservableList<Towar> getAllTowarObservableList() {
        ObservableList<Towar> towarObservableList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM `towary`;";
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            //iterujemy po wynikach
            while (rs.next()){
                towarObservableList.add(new Towar(rs.getInt("tid"), rs.getString("nazwa"), rs.getInt("vat"), rs.getString("jm"), rs.getDouble("netto"), rs.getDouble("brutto"), rs.getInt("grupa"), rs.getString("barcod")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return towarObservableList;
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
