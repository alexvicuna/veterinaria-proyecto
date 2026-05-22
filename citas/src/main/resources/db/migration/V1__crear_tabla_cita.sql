CREATE TABLE IF NOT EXISTS cita (
    id_cita BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_cita DATETIME NOT NULL,
    motivo_consulta VARCHAR(255) NOT NULL,
    estado_cita VARCHAR(50) NOT NULL,
    id_mascota BIGINT NOT NULL,
    id_dueno BIGINT NOT NULL,
    id_veterinario BIGINT NOT NULL
    );