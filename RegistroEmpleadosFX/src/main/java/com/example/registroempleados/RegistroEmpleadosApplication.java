package com.example.registroempleados;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistroEmpleadosApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                RegistroEmpleadosApplication.class.getResource("/com/example/registroempleados/views/empleado-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setTitle("Registro de Empleados");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
