package com.example.pruebagb.Servicios;

import com.example.pruebagb.modelo.Libro;
import com.example.pruebagb.modelo.Usuario;
import com.example.pruebagb.repositorios.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> buscarLibros(String filtro) {
        return libroRepository.findByTituloIgnoreCaseContainingOrAutorIgnoreCaseContainingOrEditorialIgnoreCaseContaining(
                filtro, filtro, filtro);
    }

    public List<Libro> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    public Libro guardarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }

    public Optional<Libro> obtenerLibroPorId(Long id) {
        return libroRepository.findById(id);
    }

    public Libro obtenerLibroPorTitulo(String titulo) {
        return libroRepository.obtenerLibroPorTitulo(titulo);
    }


    public String obtenerTituloPorId(Long id) {
        Libro libro = libroRepository.findById(id).orElse(null);
        if (libro != null) {
            return libro.getTitulo();
        }
        return null;
    }


}
