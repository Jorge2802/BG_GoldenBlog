package com.example.pruebagb.Servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import com.example.pruebagb.controladores.DTO.UsuarioRegistroDTO;
import com.example.pruebagb.modelo.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;



public interface UsuarioServicio extends UserDetailsService {

    Usuario guardar(UsuarioRegistroDTO registroDTO);

    List<Usuario> listarUsuarios();

    Usuario findById(Long id);

    Usuario findByEmail(String email);

    Usuario findByDni(int dni);

    Long obtenerIdUsuarioAutenticado();

    




}
