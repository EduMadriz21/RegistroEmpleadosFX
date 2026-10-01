-- =============================================================
-- PRÁCTICA: REGISTRO Y CONSULTA DE EMPLEADOS
-- Base de datos: biblioteca_fx
-- PostgreSQL
-- =============================================================

-- IMPORTANTE:
-- Conéctese primero a la base de datos biblioteca_fx.
-- No es necesario crear una base nueva.

DROP TABLE IF EXISTS empleado;

CREATE TABLE empleado (
    id SERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(150) UNIQUE,
    telefono VARCHAR(20),
    cargo VARCHAR(100),
    departamento VARCHAR(100) NOT NULL,
    salario NUMERIC(10,2) NOT NULL CHECK (salario >= 0),
    fecha_contratacion DATE NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('Activo', 'Inactivo'))
);

-- =============================================================
-- 5 EMPLEADOS DE PRUEBA
-- =============================================================

INSERT INTO empleado
(nombres, apellidos, cedula, correo, telefono, cargo, departamento, salario, fecha_contratacion, estado)
VALUES
('Carlos', 'López', '001-010101-0001A', 'carlos.lopez@empresa.com', '88881111', 'Programador', 'Tecnología', 25000.00, '2026-01-10', 'Activo'),
('María', 'González', '001-020202-0002B', 'maria.gonzalez@empresa.com', '88882222', 'Contadora', 'Contabilidad', 22000.00, '2025-11-15', 'Activo'),
('José', 'Martínez', '001-030303-0003C', 'jose.martinez@empresa.com', '88883333', 'Vendedor', 'Ventas', 18000.00, '2026-02-01', 'Activo'),
('Ana', 'Ramírez', '001-040404-0004D', 'ana.ramirez@empresa.com', '88884444', 'Analista', 'Recursos Humanos', 21000.00, '2025-09-20', 'Inactivo'),
('Luis', 'Hernández', '001-050505-0005E', 'luis.hernandez@empresa.com', '88885555', 'Administrador', 'Administración', 30000.00, '2024-07-08', 'Activo');

-- =============================================================
-- CONSULTAS SELECT SOLICITADAS
-- =============================================================

-- 1. Mostrar todos los empleados.
SELECT *
FROM empleado;

-- 2. Mostrar únicamente nombres, apellidos y cargo.
SELECT nombres, apellidos, cargo
FROM empleado;

-- 3. Mostrar empleados de un departamento determinado.
SELECT *
FROM empleado
WHERE departamento = 'Tecnología';

-- 4. Mostrar empleados cuyo salario sea mayor a un valor establecido.
SELECT *
FROM empleado
WHERE salario > 20000;

-- 5. Ordenar los empleados por salario de mayor a menor.
SELECT *
FROM empleado
ORDER BY salario DESC;

-- 6. Contar la cantidad total de empleados registrados.
SELECT COUNT(*) AS total_empleados
FROM empleado;

-- 7. Obtener el salario promedio de los empleados.
SELECT AVG(salario) AS salario_promedio
FROM empleado;

-- 8. Obtener la suma de todos los salarios.
SELECT SUM(salario) AS suma_salarios
FROM empleado;

-- 9. Mostrar únicamente empleados activos.
SELECT *
FROM empleado
WHERE estado = 'Activo';

-- 10. Agrupar empleados por departamento y mostrar cuántos hay en cada uno.
SELECT departamento, COUNT(*) AS cantidad_empleados
FROM empleado
GROUP BY departamento
ORDER BY departamento;

-- =============================================================
-- CONSULTAS EXTRA PARA PROBAR DESDE JAVA
-- =============================================================

-- Empleados activos
SELECT * FROM empleado WHERE estado = 'Activo';

-- Departamento específico
SELECT * FROM empleado WHERE departamento = 'Ventas';

-- Salarios superiores a 22000
SELECT * FROM empleado WHERE salario > 22000;

-- Orden por salario
SELECT * FROM empleado ORDER BY salario DESC;

-- Orden alfabético por apellido
SELECT * FROM empleado ORDER BY apellidos ASC;
