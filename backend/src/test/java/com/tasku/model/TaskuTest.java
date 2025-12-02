package com.tasku.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TaskuTest {

    private Tasku tasku;

    @BeforeEach
    void setUp() {
        tasku = new Tasku();
    }

    @Test
    void testConstructorVacio() {
        // When
        Tasku nuevaTasku = new Tasku();

        // Then
        assertThat(nuevaTasku).isNotNull();
        assertThat(nuevaTasku.getFechaCreacion()).isNotNull();
        assertThat(nuevaTasku.getCompletada()).isFalse();
    }

    @Test
    void testConstructorConParametros() {
        // When
        Tasku nuevaTasku = new Tasku("Título", "Descripción");

        // Then
        assertThat(nuevaTasku).isNotNull();
        assertThat(nuevaTasku.getTitulo()).isEqualTo("Título");
        assertThat(nuevaTasku.getDescripcion()).isEqualTo("Descripción");
        assertThat(nuevaTasku.getFechaCreacion()).isNotNull();
    }

    @Test
    void testGettersYSetters() {
        // Given
        Long id = 1L;
        String titulo = "Título de prueba";
        String descripcion = "Descripción de prueba";
        Boolean completada = true;
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime fechaActualizacion = LocalDateTime.now().plusHours(1);

        // When
        tasku.setId(id);
        tasku.setTitulo(titulo);
        tasku.setDescripcion(descripcion);
        tasku.setCompletada(completada);
        tasku.setFechaCreacion(fechaCreacion);
        tasku.setFechaActualizacion(fechaActualizacion);

        // Then
        assertThat(tasku.getId()).isEqualTo(id);
        assertThat(tasku.getTitulo()).isEqualTo(titulo);
        assertThat(tasku.getDescripcion()).isEqualTo(descripcion);
        assertThat(tasku.getCompletada()).isEqualTo(completada);
        assertThat(tasku.getFechaCreacion()).isEqualTo(fechaCreacion);
        assertThat(tasku.getFechaActualizacion()).isEqualTo(fechaActualizacion);
    }

    @Test
    void testSetCompletada_ActualizaFechaActualizacion() {
        // Given
        LocalDateTime fechaInicial = LocalDateTime.now();
        tasku.setFechaCreacion(fechaInicial);
        
        // Pequeño delay para asegurar diferencia de tiempo
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When
        tasku.setCompletada(true);

        // Then
        assertThat(tasku.getCompletada()).isTrue();
        assertThat(tasku.getFechaActualizacion()).isNotNull();
        assertThat(tasku.getFechaActualizacion()).isAfterOrEqualTo(fechaInicial);
    }
}

