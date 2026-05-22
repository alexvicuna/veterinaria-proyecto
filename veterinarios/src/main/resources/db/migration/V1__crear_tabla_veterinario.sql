CREATE TABLE IF NOT EXISTS veterinario (
    id_veterinario BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut_vet VARCHAR(12) NOT NULL,
    nombre_vet VARCHAR(100) NOT NULL,
    apellido_vet VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(100) NOT NULL
    );