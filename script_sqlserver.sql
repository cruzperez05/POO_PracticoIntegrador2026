-- =====================================================================
-- SCRIPT DE BASE DE DATOS RELACIONAL - SQL SERVER
-- Proyecto: Gestión de Biblioteca (TP7 Integrador - POO 2026)
-- Equipo: NullPointers (Grupo 2)
-- =====================================================================

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'BibliotecaPOO')
BEGIN
    CREATE DATABASE BibliotecaPOO;
END
GO

USE BibliotecaPOO;
GO

-- 1. Tabla de Socios (almacena tanto Estudiantes como Docentes)
IF OBJECT_ID('dbo.Prestamos', 'U') IS NOT NULL DROP TABLE dbo.Prestamos;
IF OBJECT_ID('dbo.Libros', 'U') IS NOT NULL DROP TABLE dbo.Libros;
IF OBJECT_ID('dbo.Socios', 'U') IS NOT NULL DROP TABLE dbo.Socios;
GO

CREATE TABLE Socios (
    dni INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,          -- 'Estudiante' o 'Docente'
    carrera_o_area VARCHAR(100) NOT NULL,
    dias_prestamo INT NOT NULL
);

-- 2. Tabla de Libros
CREATE TABLE Libros (
    id_libro INT IDENTITY(1,1) PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    edicion INT NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    anio INT NOT NULL
);

-- 3. Tabla de Préstamos (Relaciona Socios y Libros con Claves Foráneas)
CREATE TABLE Prestamos (
    id_prestamo INT IDENTITY(1,1) PRIMARY KEY,
    id_libro INT NOT NULL,
    dni_socio INT NOT NULL,
    fecha_retiro DATE NOT NULL,
    fecha_devolucion DATE NULL,
    CONSTRAINT FK_Prestamos_Libros FOREIGN KEY (id_libro) REFERENCES Libros(id_libro),
    CONSTRAINT FK_Prestamos_Socios FOREIGN KEY (dni_socio) REFERENCES Socios(dni)
);
GO

-- =====================================================================
-- DATOS INICIALES DE PRUEBA (Los mismos de ProcesoVentana)
-- =====================================================================
INSERT INTO Socios (dni, nombre, tipo, carrera_o_area, dias_prestamo) VALUES
(1, 'Juan Perez', 'Docente', 'Programacion', 5),
(2, 'Juan Fernandez', 'Docente', 'Matematica', 5),
(3, 'Maria Alegre', 'Docente', 'Literatura', 5),
(4, 'Francisco Paenza', 'Estudiante', 'Sistemas', 20),
(5, 'Cesar Milstein', 'Estudiante', 'Biologia', 20);


INSERT INTO Libros (titulo, edicion, editorial, anio) VALUES
('Java', 1, 'Pearson', 2012),
('Diccionario', 3, 'Longman', 2010),
('Vivir para contarla', 1, 'Sudamericana', 2002),
('Programando', 2, 'Alfaomega', 2015);
GO


