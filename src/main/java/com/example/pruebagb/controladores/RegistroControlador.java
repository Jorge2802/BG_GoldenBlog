package com.example.pruebagb.controladores;

import com.example.pruebagb.Servicios.UsuarioServicio;
import com.example.pruebagb.modelo.Usuario;
import com.example.pruebagb.repositorios.UsuarioRepositorio;
import com.example.pruebagb.util.Reportes.ExporterEXCEL;
import com.example.pruebagb.util.Reportes.ExporterPDF;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class RegistroControlador {

    @Autowired
    private UsuarioServicio servicio;

    //Maneja la Vista del Login
    @GetMapping("/login")
    public String iniciarSesion() {
        return "login";
    }


    @GetMapping({"/", "/menu"})
    public String verPaginaDeInicio(Model modelo) {
        // Obtiene el usuario actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = null;

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            usuarioActual = servicio.findByEmail(email);
        }

        modelo.addAttribute("usuario", usuarioActual);
        modelo.addAttribute("usuarios", servicio.listarUsuarios());
        return "index";
    }

    @GetMapping("/micuenta/{id}")
    public String verUsuario(@PathVariable(value = "id") Long id, Model model) {
        Usuario usuario = servicio.findById(id);
        if(usuario == null) {
            return "redirect:/menu";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("nombre", "Detalles del Usuario " + usuario.getNombre());
        return "micuenta";
    }


    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @GetMapping("/verusuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuario = usuarioRepositorio.findAll();
        model.addAttribute("usuario", usuario);
        return "VerUsuarios";
    }

    @GetMapping("/formusuario")
    public String nuevoUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("modo", "creacion"); // Agrega el atributo modo a creacion
        return "formUsuario";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/formusuario")
    public String guardarNuevoUsuario(@ModelAttribute Usuario usuario) {
        // Encripta la contraseña antes de guardarla
        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);

        usuarioRepositorio.save(usuario);
        return "redirect:/verusuarios";
    }

    @GetMapping("/editarusuario/{id}")
    public String editarUsuarioForm(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepositorio.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);
        model.addAttribute("modo", "edicion"); // Agrega el atributo modo a edicion
        return "editar_usuario";
    }

    @PostMapping("/editarusuario/{id}")
    public String guardareditarUsuario(@ModelAttribute Usuario usuario, @PathVariable Long id) {
        Usuario usuarioExistente = servicio.findById(id);

        if (usuarioExistente != null) {
            // Si la contraseña ha sido modificada, encripta la nueva contraseña
            if (!usuario.getPassword().equals(usuarioExistente.getPassword())) {
                String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
                usuario.setPassword(contraseñaEncriptada);
            }
            usuarioRepositorio.save(usuario);
        }
        return "redirect:/verusuarios";
    }

    @GetMapping("/eliminarusuario/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioRepositorio.deleteById(id);
        return "redirect:/verusuarios";
    }


    @GetMapping("/exportarPDF")
    public void exportarListadoDeUsuariosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Usuarios_" + fechaActual + ".pdf";

        response.setHeader(cabecera, valor);

        List<Usuario> usuarios = usuarioRepositorio.findAll();

        ExporterPDF exporter = new ExporterPDF(usuarios);
        exporter.exportar(response);


    }

    @GetMapping("/exportarExcel")
    public void exportarListadoDeUsuariosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Usuarios_" + fechaActual + ".xlsx";

        response.setHeader(cabecera, valor);

        List<Usuario> usuarios = usuarioRepositorio.findAll();

        ExporterEXCEL exporter = new ExporterEXCEL(usuarios);
        exporter.exportar(response);
    }


}