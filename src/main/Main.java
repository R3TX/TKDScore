package main;


import utils.DatabaseConnection;
import view.Board;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        new Board();
    }
}