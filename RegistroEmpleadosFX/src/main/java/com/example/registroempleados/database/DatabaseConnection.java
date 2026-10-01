package com.example.registroempleados.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    // Lee database.properties y devuelve la conexión a PostgreSQL
    public static Connection getConnection() throws SQLException {
        Properties datos = new Properties();

        try {
            datos.load(DatabaseConnection.class.getResourceAsStream("/database.properties"));
        } catch (IOException e) {
            throw new SQLException("No se pudo leer database.properties");
        }

        return DriverManager.getConnection(
                datos.getProperty("db.url"),
                datos.getProperty("db.user"),
                datos.getProperty("db.password")
        );
    }
}
