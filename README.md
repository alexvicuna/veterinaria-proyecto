# 🐾 Sistema de Gestión Veterinaria — Microservicios

Sistema de gestión para clínica veterinaria desarrollado con arquitectura de microservicios usando Spring Boot.

---

## 📋 Tecnologías

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- OpenFeign (comunicación entre microservicios)
- MySQL 8
- Lombok
- Maven

---

## 🧩 Microservicios

| Microservicio       | Puerto | Base de datos                   | Descripción                        |
|---------------------|--------|---------------------------------|------------------------------------|
| duenos              | 8080   | db_veterinaria_duenos           | Gestión de dueños de mascotas      |
| inventario          | 8081   | db_veterinaria_inventario       | Control de productos e insumos     |
| mascotas            | 8082   | db_veterinaria_mascotas         | Gestión de mascotas                |
| recetas             | 8084   | db_veterinaria_recetas          | Emisión de recetas médicas         |
| citas               | 8085   | db_veterinaria_citas            | Agendamiento de citas              |
| historiales-medicos | 8086   | db_veterinaria_historial        | Historial clínico de mascotas      |
| veterinarios        | 8087   | db_veterinaria_veterinarios     | Gestión de veterinarios            |
| pagos               | 8088   | db_veterinaria_pagos            | Procesamiento de pagos             |
| boleta              | 8089   | db_veterinaria_boletas          | Generación de boletas              |
| usuarios            | 8090   | db_veterinaria_usuarios         | Gestión de usuarios y roles        |

---

## 🔗 Comunicación entre microservicios

Cada microservicio se comunica con otros a través de IDs usando OpenFeign. No hay JOINs entre bases de datos.

```
duenos        ←── mascotas
mascotas      ←── duenos
citas         ←── duenos, mascotas, veterinarios
historiales   ←── mascotas, veterinarios
recetas       ←── citas, mascotas, veterinarios
pagos         ←── citas
boleta        ←── citas, pagos
```

---

## 👥 Roles de usuario

| Rol           | Descripción                               |
|---------------|-------------------------------------------|
| ADMINISTRADOR | Acceso total al sistema                   |
| VETERINARIO   | Gestión de citas, historiales y recetas   |
| CLIENTE       | Consulta de sus mascotas, citas y boletas |

---

## 📊 Estados

**Citas**
- `PENDIENTE` → `CONFIRMADA` → `COMPLETADA`
- `PENDIENTE` → `CANCELADA`

**Pagos**
- `PENDIENTE` → `COMPLETADO`
- `PENDIENTE` → `RECHAZADO` → `REEMBOLSADO`

**Métodos de pago**
- `EFECTIVO`, `TARJETA_DEBITO`, `TARJETA_CREDITO`, `TRANSFERENCIA`

---

## 🚀 Instalación y ejecución

### Prerrequisitos

- Java 17+
- MySQL 8 en puerto `3306`
- Maven 3.8+

### 1. Clonar el repositorio

```bash
git clone https://github.com/alexvicuna/veterinaria-proyecto.git
cd veterinaria-proyecto
```

### 2. Configurar credenciales MySQL

En cada `application.properties` ajustar usuario y contraseña:

```properties
spring.datasource.username=root
spring.datasource.password=
```

### 3. Levantar los microservicios

Las tablas se crean automáticamente al levantar cada microservicio gracias a `ddl-auto=update`.

Se recomienda levantar en este orden para respetar las dependencias:

1. duenos `8080`
2. mascotas `8082`
3. veterinarios `8087`
4. inventario `8081`
5. usuarios `8090`
6. citas `8085`
7. historiales-medicos `8086`
8. recetas `8084`
9. pagos `8088`
10. boleta `8089`

Desde la carpeta de cada microservicio:

```bash
mvn spring-boot:run
```

### 4. Insertar datos de prueba

Una vez levantados todos los microservicios, ejecutar el script de datos de prueba desde HeidiSQL u otro cliente MySQL:

```
scripts/02_insertar_datos.sql
```

---

## 📡 Endpoints principales

### Dueños `localhost:8080`
| Método | Endpoint                | Descripción      |
|--------|-------------------------|------------------|
| GET    | /api/v1/duenos          | Listar todos     |
| GET    | /api/v1/duenos/{id}     | Buscar por ID    |
| POST   | /api/v1/duenos          | Crear dueño      |
| PUT    | /api/v1/duenos/{id}     | Actualizar dueño |
| DELETE | /api/v1/duenos/{id}     | Eliminar dueño   |

### Mascotas `localhost:8082`
| Método | Endpoint                | Descripción        |
|--------|-------------------------|--------------------|
| GET    | /api/v1/mascotas        | Listar todas       |
| GET    | /api/v1/mascotas/{id}   | Buscar por ID      |
| POST   | /api/v1/mascotas        | Crear mascota      |
| PUT    | /api/v1/mascotas/{id}   | Actualizar mascota |
| DELETE | /api/v1/mascotas/{id}   | Eliminar mascota   |

### Veterinarios `localhost:8087`
| Método | Endpoint                    | Descripción            |
|--------|-----------------------------|------------------------|
| GET    | /api/v1/veterinarios        | Listar todos           |
| GET    | /api/v1/veterinarios/{id}   | Buscar por ID          |
| POST   | /api/v1/veterinarios        | Crear veterinario      |
| PUT    | /api/v1/veterinarios/{id}   | Actualizar veterinario |
| DELETE | /api/v1/veterinarios/{id}   | Eliminar veterinario   |

### Citas `localhost:8085`
| Método | Endpoint                        | Descripción       |
|--------|---------------------------------|-------------------|
| GET    | /api/v1/citas                   | Listar todas      |
| GET    | /api/v1/citas/{id}              | Buscar por ID     |
| GET    | /api/v1/citas/mascota/{id}      | Buscar por mascota|
| GET    | /api/v1/citas/fecha/{fecha}     | Buscar por fecha  |
| POST   | /api/v1/citas                   | Crear cita        |
| PUT    | /api/v1/citas/{id}              | Actualizar cita   |
| PATCH  | /api/v1/citas/{id}/estado       | Actualizar estado |
| DELETE | /api/v1/citas/{id}              | Eliminar cita     |

### Historial médico `localhost:8086`
| Método | Endpoint                                            | Descripción              |
|--------|-----------------------------------------------------|--------------------------|
| GET    | /api/v1/historiales                                 | Listar todos             |
| GET    | /api/v1/historiales/{id}                            | Buscar por ID            |
| GET    | /api/v1/historiales/mascota/{id}                    | Buscar por mascota       |
| GET    | /api/v1/historiales/mascota/{id}/veterinario/{id}   | Buscar por mascota y vet |
| GET    | /api/v1/historiales/fechas                          | Buscar por rango fechas  |
| POST   | /api/v1/historiales                                 | Crear historial          |
| PUT    | /api/v1/historiales/{id}                            | Actualizar historial     |
| DELETE | /api/v1/historiales/{id}                            | Eliminar historial       |

### Recetas `localhost:8084`
| Método | Endpoint                                          | Descripción               |
|--------|---------------------------------------------------|---------------------------|
| GET    | /api/v1/recetas                                   | Listar todas              |
| GET    | /api/v1/recetas/{id}                              | Buscar por ID             |
| GET    | /api/v1/recetas/mascota/{id}                      | Buscar por mascota        |
| GET    | /api/v1/recetas/veterinario/{id}                  | Buscar por veterinario    |
| GET    | /api/v1/recetas/mascota/{id}/veterinario/{id}     | Buscar por mascota y vet  |
| GET    | /api/v1/recetas/fechas                            | Buscar por rango fechas   |
| POST   | /api/v1/recetas                                   | Crear receta              |
| PUT    | /api/v1/recetas/{id}                              | Actualizar receta         |
| DELETE | /api/v1/recetas/{id}                              | Eliminar receta           |

### Pagos `localhost:8088`
| Método | Endpoint                        | Descripción              |
|--------|---------------------------------|--------------------------|
| GET    | /api/v1/pagos                   | Listar todos             |
| GET    | /api/v1/pagos/{id}              | Buscar por ID            |
| GET    | /api/v1/pagos/estado/{estado}   | Buscar por estado        |
| GET    | /api/v1/pagos/metodo/{metodo}   | Buscar por método        |
| GET    | /api/v1/pagos/rango             | Buscar por rango fechas  |
| POST   | /api/v1/pagos                   | Registrar pago           |
| PUT    | /api/v1/pagos/{id}              | Actualizar pago          |
| PATCH  | /api/v1/pagos/{id}/estado       | Actualizar estado        |
| DELETE | /api/v1/pagos/{id}              | Eliminar pago            |

### Boleta `localhost:8089`
| Método | Endpoint                        | Descripción             |
|--------|---------------------------------|-------------------------|
| GET    | /api/v1/boletas                 | Listar todas            |
| GET    | /api/v1/boletas/{id}            | Buscar por ID           |
| GET    | /api/v1/boletas/fecha/{fecha}   | Buscar por fecha        |
| GET    | /api/v1/boletas/rango           | Buscar por rango fechas |
| POST   | /api/v1/boletas                 | Crear boleta            |
| PUT    | /api/v1/boletas/{id}            | Actualizar boleta       |
| DELETE | /api/v1/boletas/{id}            | Eliminar boleta         |

### Inventario `localhost:8081`
| Método | Endpoint                            | Descripción              |
|--------|-------------------------------------|--------------------------|
| GET    | /api/v1/inventario                  | Listar todos             |
| GET    | /api/v1/inventario/{id}             | Buscar por ID            |
| GET    | /api/v1/inventario/nombre/{nombre}  | Buscar por nombre        |
| GET    | /api/v1/inventario/categoria/{cat}  | Buscar por categoría     |
| GET    | /api/v1/inventario/stock/{cantidad} | Buscar stock bajo        |
| POST   | /api/v1/inventario                  | Crear producto           |
| PUT    | /api/v1/inventario/{id}             | Actualizar producto      |
| DELETE | /api/v1/inventario/{id}             | Eliminar producto        |

### Usuarios `localhost:8090`
| Método | Endpoint                | Descripción        |
|--------|-------------------------|--------------------|
| GET    | /api/v1/usuarios        | Listar todos       |
| GET    | /api/v1/usuarios/{id}   | Buscar por ID      |
| POST   | /api/v1/usuarios        | Crear usuario      |
| PUT    | /api/v1/usuarios/{id}   | Actualizar usuario |
| DELETE | /api/v1/usuarios/{id}   | Eliminar usuario   |

---

## 🗄️ Estructura del proyecto

```
veterinaria-proyecto/
├── boleta/
├── citas/
├── duenos/
├── historiales-medicos/
├── inventario/
├── mascotas/
├── pagos/
├── recetas/
├── usuarios/
├── veterinarios/
├── scripts/
│   └── 02_insertar_datos.sql
└── README.md
```

---

## ⚠️ Notas importantes

- Cada microservicio tiene su propia base de datos independiente
- Las tablas se crean automáticamente al levantar cada microservicio (`ddl-auto=update`)
- Los microservicios se comunican entre sí mediante IDs, no hay JOINs entre bases de datos
- Levantar los microservicios en el orden indicado para evitar errores de conexión
- MySQL debe estar corriendo en el puerto `3306` con usuario `root` sin contraseña

---

## 👩‍💻 Autores

Proyecto desarrollado como parte del curso de Desarrollo FullStack 2026
por Alexandra Vicuña y Valentina Martinez