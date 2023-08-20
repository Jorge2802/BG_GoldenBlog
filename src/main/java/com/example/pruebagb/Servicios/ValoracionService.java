package com.example.pruebagb.Servicios;

import com.example.pruebagb.modelo.Valoracion;
import com.example.pruebagb.repositorios.ValoracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValoracionService {

    @Autowired
    private ValoracionRepository valoracionRepository;

    public List<Valoracion> buscarValoraciones(String filtro) {
        return valoracionRepository.findByTituloIgnoreCaseContaining(
                filtro);
    }

    public List<Valoracion> obtenerTodasLasValoraciones() {
        return valoracionRepository.findAll();
    }

    public Valoracion guardarValoracion(Valoracion valoracion) {
        return valoracionRepository.save(valoracion);
    }

    public void eliminarValoracion(Long id) {
        valoracionRepository.deleteById(id);
    }

    public Optional<Valoracion> obtenerValoracionesPorId(Long id) {
        return valoracionRepository.findById(id);
    }


}