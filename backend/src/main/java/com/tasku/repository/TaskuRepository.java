package com.tasku.repository;

import com.tasku.model.Tasku;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskuRepository implements PanacheRepository<Tasku> {
    
    public List<Tasku> findByCompletada(Boolean completada) {
        return find("completada", completada).list();
    }
    
    public List<Tasku> buscarPorTitulo(String titulo) {
        return find("titulo LIKE ?1", "%" + titulo + "%").list();
    }
    
    public Optional<Tasku> buscarPorId(Long id) {
        return findByIdOptional(id);
    }
    
    public Tasku guardar(Tasku tasku) {
        persist(tasku);
        return tasku;
    }
    
    public boolean eliminarPorId(Long id) {
        return deleteById(id);
    }
    
    public long contarPorCompletada(Boolean completada) {
        return count("completada", completada);
    }
}
