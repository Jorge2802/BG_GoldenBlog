package com.example.pruebagb.repositorios;

import com.example.pruebagb.modelo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByTituloIgnoreCaseContaining(
            String titulo);
}
