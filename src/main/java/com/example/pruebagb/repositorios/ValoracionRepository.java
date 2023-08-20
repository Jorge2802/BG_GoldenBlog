package com.example.pruebagb.repositorios;

import com.example.pruebagb.modelo.Prestamo;
import com.example.pruebagb.modelo.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {
    List<Valoracion> findByTituloIgnoreCaseContaining(
            String titulo);
}

