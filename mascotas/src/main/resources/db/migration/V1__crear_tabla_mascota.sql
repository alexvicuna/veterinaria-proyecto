CREATE TABLE IF NOT EXISTS mascota (
    id_mascota BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombreMasc VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50),
    edad INT NOT NULL,
    id_dueno BIGINT NOT NULL,
    CONSTRAINT fk_dueno FOREIGN KEY (id_dueno) REFERENCES dueno(id_dueno)
    );
