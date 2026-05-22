CREATE TABLE IF NOT EXISTS mascota (
    id_mascota BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_masc VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50) NOT NULL,
    edad INT NOT NULL,
    id_dueno BIGINT NOT NULL
    );