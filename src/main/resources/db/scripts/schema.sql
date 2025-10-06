DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS usuarios CASCADE;
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre_completo VARCHAR(120) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    clave VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

DROP TABLE IF EXISTS usuario_roles CASCADE;
CREATE TABLE usuario_roles (
    usuario_id INTEGER NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    rol_id INTEGER NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, rol_id)
);

DROP TABLE IF EXISTS libros CASCADE;
CREATE TABLE libros (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    isbn VARCHAR(17) NOT NULL UNIQUE,
    anio_publicacion INTEGER NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    copias_totales INTEGER NOT NULL,
    copias_disponibles INTEGER NOT NULL
);

DROP TABLE IF EXISTS prestamos CASCADE;
CREATE TABLE prestamos (
    id SERIAL PRIMARY KEY,
    libro_id INTEGER NOT NULL REFERENCES libros(id),
    usuario_id INTEGER NOT NULL REFERENCES usuarios(id),
    fecha_prestamo DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    fecha_devolucion DATE,
    estado VARCHAR(15) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_prestamos_estado ON prestamos(estado);
CREATE INDEX IF NOT EXISTS idx_libros_categoria ON libros(categoria);

-- Inserts para la tabla roles, considerando roles típicos en Spring Security
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO roles (nombre) VALUES ('ROLE_LIBRARIAN');
INSERT INTO roles (nombre) VALUES ('ROLE_USER');

-- Inserts de ejemplo para la tabla libros
INSERT INTO libros (titulo, autor, isbn, anio_publicacion, categoria, copias_totales, copias_disponibles)
VALUES ('Cien años de soledad', 'Gabriel García Márquez', '978-0307474728', 1967, 'Novela', 10, 7);

INSERT INTO libros (titulo, autor, isbn, anio_publicacion, categoria, copias_totales, copias_disponibles)
VALUES ('Don Quijote de la Mancha', 'Miguel de Cervantes', '978-8491050297', 1605, 'Clásico', 8, 5);

INSERT INTO libros (titulo, autor, isbn, anio_publicacion, categoria, copias_totales, copias_disponibles)
VALUES ('La sombra del viento', 'Carlos Ruiz Zafón', '978-8408172177', 2001, 'Misterio', 6, 4);

INSERT INTO libros (titulo, autor, isbn, anio_publicacion, categoria, copias_totales, copias_disponibles)
VALUES ('El amor en los tiempos del cólera', 'Gabriel García Márquez', '978-0307389732', 1985, 'Romance', 7, 6);

INSERT INTO libros (titulo, autor, isbn, anio_publicacion, categoria, copias_totales, copias_disponibles)
VALUES ('Rayuela', 'Julio Cortázar', '978-8437604947', 1963, 'Experimental', 5, 3);