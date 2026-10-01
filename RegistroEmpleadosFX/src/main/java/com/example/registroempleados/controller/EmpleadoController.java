package com.example.registroempleados.controller;

import com.example.registroempleados.database.DatabaseConnection;
import com.example.registroempleados.model.Empleado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class EmpleadoController {

    // Controles del formulario
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCedula;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCargo;
    @FXML private ComboBox<String> cmbDepartamento;
    @FXML private TextField txtSalario;
    @FXML private DatePicker dpFechaContratacion;
    @FXML private ComboBox<String> cmbEstado;

    // Tabla y columnas
    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, Integer> colId;
    @FXML private TableColumn<Empleado, String> colNombres;
    @FXML private TableColumn<Empleado, String> colApellidos;
    @FXML private TableColumn<Empleado, String> colCedula;
    @FXML private TableColumn<Empleado, String> colCorreo;
    @FXML private TableColumn<Empleado, String> colTelefono;
    @FXML private TableColumn<Empleado, String> colCargo;
    @FXML private TableColumn<Empleado, String> colDepartamento;
    @FXML private TableColumn<Empleado, BigDecimal> colSalario;
    @FXML private TableColumn<Empleado, LocalDate> colFechaContratacion;
    @FXML private TableColumn<Empleado, String> colEstado;

    // Lista que se muestra en la tabla
    private ObservableList<Empleado> empleados = FXCollections.observableArrayList();

    // Se ejecuta automáticamente al abrir la ventana
    @FXML
    public void initialize() {
        configurarColumnas();
        cargarComboBox();
        cargarEmpleados();
    }

    // Cada columna toma su valor del getter con ese nombre (getNombres, getCedula...)
    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        colFechaContratacion.setCellValueFactory(new PropertyValueFactory<>("fechaContratacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tblEmpleados.setItems(empleados);
    }

    private void cargarComboBox() {
        cmbDepartamento.getItems().addAll(
                "Administración", "Contabilidad", "Recursos Humanos", "Tecnología", "Ventas");
        cmbEstado.getItems().addAll("Activo", "Inactivo");
    }

    // Consulta la tabla empleado y llena la lista
    private void cargarEmpleados() {
        String sql = "SELECT * FROM empleado ORDER BY id";
        empleados.clear();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Empleado empleado = new Empleado(
                        resultSet.getInt("id"),
                        resultSet.getString("nombres"),
                        resultSet.getString("apellidos"),
                        resultSet.getString("cedula"),
                        resultSet.getString("correo"),
                        resultSet.getString("telefono"),
                        resultSet.getString("cargo"),
                        resultSet.getString("departamento"),
                        resultSet.getBigDecimal("salario"),
                        resultSet.getDate("fecha_contratacion").toLocalDate(),
                        resultSet.getString("estado")
                );
                empleados.add(empleado);
            }

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos",
                    "No se pudieron cargar los empleados.\n" + e.getMessage());
        }
    }

    @FXML
    private void guardarEmpleado() {
        if (!validarFormulario()) {
            return;
        }

        // La columna correo es UNIQUE: si está vacío se guarda NULL
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            correo = null;
        }

        String sql = "INSERT INTO empleado (nombres, apellidos, cedula, correo, telefono, cargo, "
                + "departamento, salario, fecha_contratacion, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, txtNombres.getText().trim());
            statement.setString(2, txtApellidos.getText().trim());
            statement.setString(3, txtCedula.getText().trim());
            statement.setString(4, correo);
            statement.setString(5, txtTelefono.getText().trim());
            statement.setString(6, txtCargo.getText().trim());
            statement.setString(7, cmbDepartamento.getValue());
            statement.setBigDecimal(8, new BigDecimal(txtSalario.getText().trim()));
            statement.setDate(9, Date.valueOf(dpFechaContratacion.getValue()));
            statement.setString(10, cmbEstado.getValue());

            statement.executeUpdate();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso",
                    "El empleado fue guardado correctamente.");
            limpiarFormulario();
            cargarEmpleados();

        } catch (SQLException e) {
            String mensaje = e.getMessage();
            if ("23505".equals(e.getSQLState())) {
                mensaje = "La cédula o el correo ya están registrados.";
            }
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", mensaje);
        }
    }

    // Revisa los campos obligatorios. Si algo falta, avisa y devuelve false
    private boolean validarFormulario() {
        if (txtNombres.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Debe ingresar los nombres.");
            return false;
        }

        if (txtApellidos.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Debe ingresar los apellidos.");
            return false;
        }

        if (txtCedula.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Debe ingresar la cédula.");
            return false;
        }

        if (cmbDepartamento.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Debe seleccionar un departamento.");
            return false;
        }

        try {
            BigDecimal salario = new BigDecimal(txtSalario.getText().trim());
            if (salario.compareTo(BigDecimal.ZERO) < 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El salario no puede ser negativo.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El salario debe ser un número válido.");
            return false;
        }

        if (dpFechaContratacion.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Debe seleccionar la fecha de contratación.");
            return false;
        }

        if (cmbEstado.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Debe seleccionar un estado.");
            return false;
        }

        return true;
    }

    @FXML
    private void limpiarFormulario() {
        txtNombres.clear();
        txtApellidos.clear();
        txtCedula.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtCargo.clear();
        cmbDepartamento.setValue(null);
        txtSalario.clear();
        dpFechaContratacion.setValue(null);
        cmbEstado.setValue(null);
    }

    @FXML
    private void actualizarTabla() {
        cargarEmpleados();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
