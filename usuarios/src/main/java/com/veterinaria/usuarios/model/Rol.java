package com.veterinaria.usuarios.model;

public enum Rol {
    ADMINISTRADOR,  // Acceso total: gestión de usuarios, reportes, configuración
    VETERINARIO,    // Gestión operativa: citas, historiales, recetas, mascotas
    CLIENTE         // Acceso limitado: sus mascotas, sus citas, sus boletas
}