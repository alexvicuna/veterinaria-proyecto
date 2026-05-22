CREATE TABLE IF NOT EXISTS receta (
    id_receta BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_emision DATE NOT NULL,
    diagnostico VARCHAR(255) NOT NULL,
    medicamento VARCHAR(255) NOT NULL,
    dosis VARCHAR(100) NOT NULL,
    id_veterinario BIGINT NOT NULL,
    id_mascota BIGINT NOT NULL,
    id_cita BIGINT NOT NULL
    );