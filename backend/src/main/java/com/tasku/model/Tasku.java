package com.tasku.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "taskus")
public class Tasku {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(length = 1000)
    private String descripcion;
    
    @Column(nullable = false)
    private Boolean completada = false;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Tasku() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Tasku(String titulo, String descripcion) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Boolean getCompletada() {
        return completada;
    }
    
    public void setCompletada(Boolean completada) {
        this.completada = completada;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
