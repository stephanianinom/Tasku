package com.tasku.service;

import com.tasku.model.Tasku;
import com.tasku.repository.TaskuRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskuService {
    
    @Inject
    TaskuRepository repository;
    
    public List<Tasku> obtenerTodas() {
        return repository.listAll();
    }
    
    public Optional<Tasku> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return repository.buscarPorId(id);
    }
    
    public List<Tasku> obtenerPorCompletada(Boolean completada) {
        return repository.findByCompletada(completada);
    }
    
    public List<Tasku> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return repository.listAll();
        }
        return repository.buscarPorTitulo(titulo.trim());
    }
    
    @Transactional
    public Tasku crear(Tasku tasku) {
        if (tasku == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula");
        }
        if (tasku.getTitulo() == null || tasku.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        return repository.guardar(tasku);
    }
    
    @Transactional
    public Optional<Tasku> actualizar(Long id, Tasku taskuActualizada) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        if (taskuActualizada == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula");
        }
        
        Optional<Tasku> taskuExistente = repository.buscarPorId(id);
        if (taskuExistente.isEmpty()) {
            return Optional.empty();
        }
        
        Tasku tasku = taskuExistente.get();
        if (taskuActualizada.getTitulo() != null && !taskuActualizada.getTitulo().trim().isEmpty()) {
            tasku.setTitulo(taskuActualizada.getTitulo().trim());
        }
        if (taskuActualizada.getDescripcion() != null) {
            tasku.setDescripcion(taskuActualizada.getDescripcion());
        }
        if (taskuActualizada.getCompletada() != null) {
            tasku.setCompletada(taskuActualizada.getCompletada());
        }
        
        repository.persist(tasku);
        return Optional.of(tasku);
    }
    
    @Transactional
    public boolean eliminar(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return repository.eliminarPorId(id);
    }
    
    @Transactional
    public Optional<Tasku> marcarCompletada(Long id, Boolean completada) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        
        Optional<Tasku> tasku = repository.buscarPorId(id);
        if (tasku.isEmpty()) {
            return Optional.empty();
        }
        
        tasku.get().setCompletada(completada != null ? completada : true);
        repository.persist(tasku.get());
        return tasku;
    }
    
    public long contarCompletadas() {
        return repository.contarPorCompletada(true);
    }
    
    public long contarPendientes() {
        return repository.contarPorCompletada(false);
    }
}
