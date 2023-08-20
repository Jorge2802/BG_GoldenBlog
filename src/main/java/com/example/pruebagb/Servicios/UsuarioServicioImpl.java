package com.example.pruebagb.Servicios;

import com.example.pruebagb.controladores.DTO.UsuarioRegistroDTO;
import com.example.pruebagb.modelo.Libro;
import com.example.pruebagb.modelo.Rol;
import com.example.pruebagb.modelo.Usuario;
import com.example.pruebagb.repositorios.LibroRepository;
import com.example.pruebagb.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class UsuarioServicioImpl implements UsuarioServicio {


    private UsuarioRepositorio usuarioRepositorio;
    private LibroRepository libroRepository;

    private UsuarioServicio usuarioServicio;



    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio) {
        super();
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }
    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepositorio.findByEmail(email);
    }

    @Override
    public Usuario findByDni(int dni) {
        return usuarioServicio.findByDni(dni);
    }

    @Override
    public Usuario guardar(UsuarioRegistroDTO registroDTO) {
        Usuario usuario = new Usuario(registroDTO.getNombre(),
                registroDTO.getApellido(), registroDTO.getDNI(), registroDTO.getEmail(),
                passwordEncoder.encode(registroDTO.getPassword()),Arrays.asList(new Rol("ROLE_USER")));
        return usuarioRepositorio.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(username);
        if(usuario == null) {
            throw new UsernameNotFoundException("Usuario o password inválidos");
        }
        return new User(usuario.getEmail(),usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Long obtenerIdUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByEmail(emailUsuario);

        if (usuario != null) {
            return usuario.getId();
        } else {
            throw new UsernameNotFoundException("No se pudo encontrar el ID del usuario autenticado");
        }
    }

    public boolean validarFechaDevolucion(LocalDate fechaDevolucion, LocalDate fechaEntrega) {
        LocalDate maxFechaDevolucion = fechaEntrega.plusDays(15);
        return !fechaDevolucion.isBefore(fechaEntrega) && !fechaDevolucion.isAfter(maxFechaDevolucion);
    }

    public Usuario obtenerUsuarioPorDNI(int dni) {
        return usuarioRepositorio.findByDNI(dni);
    }



}