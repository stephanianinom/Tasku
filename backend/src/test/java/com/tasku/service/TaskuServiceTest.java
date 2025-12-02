package com.tasku.service;

import com.tasku.model.Tasku;
import com.tasku.repository.TaskuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskuServiceTest {

    @Mock
    TaskuRepository repository;

    @InjectMocks
    TaskuService service;

    private Tasku tasku1;
    private Tasku tasku2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tasku1 = new Tasku("Tarea 1", "Descripción de la tarea 1");
        tasku1.setId(1L);
        tasku1.setCompletada(false);

        tasku2 = new Tasku("Tarea 2", "Descripción de la tarea 2");
        tasku2.setId(2L);
        tasku2.setCompletada(true);
    }

    @Test
    void testObtenerTodas() {
        // Given
        List<Tasku> taskus = Arrays.asList(tasku1, tasku2);
        when(repository.listAll()).thenReturn(taskus);

        // When
        List<Tasku> resultado = service.obtenerTodas();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado).contains(tasku1, tasku2);
        verify(repository).listAll();
    }

    @Test
    void testObtenerPorId_Exitoso() {
        // Given
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(tasku1));

        // When
        Optional<Tasku> resultado = service.obtenerPorId(1L);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get()).isEqualTo(tasku1);
        assertThat(resultado.get().getTitulo()).isEqualTo("Tarea 1");
        verify(repository).buscarPorId(1L);
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        // Given
        when(repository.buscarPorId(999L)).thenReturn(Optional.empty());

        // When
        Optional<Tasku> resultado = service.obtenerPorId(999L);

        // Then
        assertThat(resultado).isEmpty();
        verify(repository).buscarPorId(999L);
    }

    @Test
    void testObtenerPorId_IdInvalido() {
        // When
        Optional<Tasku> resultadoNull = service.obtenerPorId(null);
        Optional<Tasku> resultadoNegativo = service.obtenerPorId(-1L);
        Optional<Tasku> resultadoCero = service.obtenerPorId(0L);

        // Then
        assertThat(resultadoNull).isEmpty();
        assertThat(resultadoNegativo).isEmpty();
        assertThat(resultadoCero).isEmpty();
        verify(repository, never()).buscarPorId(any());
    }

    @Test
    void testObtenerPorCompletada() {
        // Given
        List<Tasku> completadas = Arrays.asList(tasku2);
        when(repository.findByCompletada(true)).thenReturn(completadas);

        // When
        List<Tasku> resultado = service.obtenerPorCompletada(true);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCompletada()).isTrue();
        verify(repository).findByCompletada(true);
    }

    @Test
    void testBuscarPorTitulo() {
        // Given
        List<Tasku> taskus = Arrays.asList(tasku1);
        when(repository.buscarPorTitulo("Tarea 1")).thenReturn(taskus);

        // When
        List<Tasku> resultado = service.buscarPorTitulo("Tarea 1");

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        verify(repository).buscarPorTitulo("Tarea 1");
    }

    @Test
    void testBuscarPorTitulo_Vacio() {
        // Given
        List<Tasku> todas = Arrays.asList(tasku1, tasku2);
        when(repository.listAll()).thenReturn(todas);

        // When
        List<Tasku> resultado = service.buscarPorTitulo("");

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        verify(repository).listAll();
    }

    @Test
    void testCrear_Exitoso() {
        // Given
        Tasku nuevaTasku = new Tasku("Nueva Tarea", "Nueva descripción");
        when(repository.guardar(any(Tasku.class))).thenReturn(nuevaTasku);

        // When
        Tasku resultado = service.crear(nuevaTasku);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo("Nueva Tarea");
        verify(repository).guardar(nuevaTasku);
    }

    @Test
    void testCrear_TaskuNula() {
        // When & Then
        assertThatThrownBy(() -> service.crear(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no puede ser nula");
        verify(repository, never()).guardar(any());
    }

    @Test
    void testCrear_TituloVacio() {
        // Given
        Tasku taskuSinTitulo = new Tasku("", "Descripción");

        // When & Then
        assertThatThrownBy(() -> service.crear(taskuSinTitulo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("título es obligatorio");
        verify(repository, never()).guardar(any());
    }

    @Test
    void testActualizar_Exitoso() {
        // Given
        Tasku taskuActualizada = new Tasku("Título Actualizado", "Nueva descripción");
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(tasku1));
        doNothing().when(repository).persist(any(Tasku.class));

        // When
        Optional<Tasku> resultado = service.actualizar(1L, taskuActualizada);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Título Actualizado");
        verify(repository).buscarPorId(1L);
        verify(repository).persist(any(Tasku.class));
    }

    @Test
    void testActualizar_NoEncontrado() {
        // Given
        Tasku taskuActualizada = new Tasku("Título", "Descripción");
        when(repository.buscarPorId(999L)).thenReturn(Optional.empty());

        // When
        Optional<Tasku> resultado = service.actualizar(999L, taskuActualizada);

        // Then
        assertThat(resultado).isEmpty();
        verify(repository).buscarPorId(999L);
        verify(repository, never()).persist(any(Tasku.class));
    }

    @Test
    void testEliminar_Exitoso() {
        // Given
        when(repository.eliminarPorId(1L)).thenReturn(true);

        // When
        boolean resultado = service.eliminar(1L);

        // Then
        assertThat(resultado).isTrue();
        verify(repository).eliminarPorId(1L);
    }

    @Test
    void testEliminar_NoEncontrado() {
        // Given
        when(repository.eliminarPorId(999L)).thenReturn(false);

        // When
        boolean resultado = service.eliminar(999L);

        // Then
        assertThat(resultado).isFalse();
        verify(repository).eliminarPorId(999L);
    }

    @Test
    void testMarcarCompletada_Exitoso() {
        // Given
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(tasku1));
        doNothing().when(repository).persist(any(Tasku.class));

        // When
        Optional<Tasku> resultado = service.marcarCompletada(1L, true);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCompletada()).isTrue();
        verify(repository).buscarPorId(1L);
        verify(repository).persist(any(Tasku.class));
    }

    @Test
    void testContarCompletadas() {
        // Given
        when(repository.contarPorCompletada(true)).thenReturn(5L);

        // When
        long resultado = service.contarCompletadas();

        // Then
        assertThat(resultado).isEqualTo(5L);
        verify(repository).contarPorCompletada(true);
    }

    @Test
    void testContarPendientes() {
        // Given
        when(repository.contarPorCompletada(false)).thenReturn(3L);

        // When
        long resultado = service.contarPendientes();

        // Then
        assertThat(resultado).isEqualTo(3L);
        verify(repository).contarPorCompletada(false);
    }
}

