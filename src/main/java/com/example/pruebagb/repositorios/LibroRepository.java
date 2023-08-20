package com.example.pruebagb.repositorios;

import com.example.pruebagb.modelo.Libro;
import com.example.pruebagb.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByTituloIgnoreCaseContainingOrAutorIgnoreCaseContainingOrEditorialIgnoreCaseContaining(
            String titulo, String autor, String editorial);

    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    Libro obtenerLibroPorTitulo(@Param("titulo") String titulo);

    Libro findByTitulo(String titulo);


}

