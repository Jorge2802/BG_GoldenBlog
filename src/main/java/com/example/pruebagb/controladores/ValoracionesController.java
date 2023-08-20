package com.example.pruebagb.controladores;

import com.example.pruebagb.Servicios.LibroService;
import com.example.pruebagb.Servicios.UsuarioServicio;
import com.example.pruebagb.Servicios.ValoracionService;
import com.example.pruebagb.modelo.Libro;
import com.example.pruebagb.modelo.Usuario;
import com.example.pruebagb.modelo.Valoracion;
import com.example.pruebagb.repositorios.LibroRepository;
import com.example.pruebagb.repositorios.UsuarioRepositorio;
import com.example.pruebagb.util.Reportes.ValoracionPDF;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ValoracionesController {

    @Autowired
    private ValoracionService valoracionService;
    @Autowired
    private LibroService libroService;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private LibroRepository libroRepository;

    @GetMapping("/valoraciones")
    public String listarValoraciones(Model model) {
        List<Valoracion> valoraciones = valoracionService.obtenerTodasLasValoraciones();
        model.addAttribute("valoraciones", valoraciones);
        return "verValoraciones";
    }

    @GetMapping("/valorar/{libroId}")
    public String valorarLibro(@PathVariable Long libroId, Authentication authentication, Model model) {

        // Obtener título del libro por su ID
        String tituloLibro = libroService.obtenerTituloPorId(libroId);

        // Obtener ID del usuario autenticado
        Long userId = usuarioServicio.obtenerIdUsuarioAutenticado();

        // Obtener DNI del usuario por su ID
        Usuario usuario = usuarioServicio.findById(userId);
        int dniUsuario = usuario.getDNI();

        // Obtener fecha actual
        LocalDate fechaValoracion = LocalDate.now();

        model.addAttribute("tituloLibro", tituloLibro);
        model.addAttribute("dniUsuario", dniUsuario);
        model.addAttribute("fechaValoracion", fechaValoracion);



        return "formValoracion";
    }

    @PostMapping("/valorar/{libroId}")
    public String guardarValoracion(@ModelAttribute Valoracion valoracion, Model model) {

        LocalDate fechaValoracion = valoracion.getFecha_valoracion();

        // Guardar el préstamo utilizando el servicio de Prestamo
        valoracionService.guardarValoracion(valoracion);

        // Redirige a una página de éxito o a donde desees
        return "redirect:/valoraciones";
    }

    @GetMapping("/valoraciones/{id}")
    public String mostrarDetallesValoracion(@PathVariable Long id, Model model) {
        Optional<Valoracion> valoracion = valoracionService.obtenerValoracionesPorId(id);

        if (valoracion.isPresent()) {
            Valoracion valoracionDetalles = valoracion.get();

            Usuario usuario = usuarioRepositorio.findByDNI(valoracionDetalles.getDNI());
            Libro libro = libroRepository.findByTitulo(valoracionDetalles.getTitulo());

            model.addAttribute("valoraciones", valoracionDetalles);
            model.addAttribute("usuario", usuario);
            model.addAttribute("libro", libro);

            return "DetallesValoracion";
        } else {
            return "redirect:/valoraciones";
        }
    }

    @GetMapping("/buscarvaloraciones")
    public String buscarValoraciones(@RequestParam String filtro, Model model) {
        List<Valoracion> valoracionesEncontradas = valoracionService.buscarValoraciones(filtro);
        model.addAttribute("valoraciones", valoracionesEncontradas);
        return "verValoraciones";
    }

    @GetMapping("/eliminarvaloracion/{id}")
    public String eliminarValoracion(@PathVariable Long id) {
        valoracionService.eliminarValoracion(id);
        return "redirect:/valoraciones";
    }

    @GetMapping("/valoraciones/exportarPDF")
    public void exportarListadoDeLibrosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Valoraciones_" + fechaActual + ".pdf";

        response.setHeader(cabecera, valor);

        List<Valoracion> valoraciones = valoracionService.obtenerTodasLasValoraciones();

        ValoracionPDF exporter = new ValoracionPDF(valoraciones);
        exporter.exportar(response);


    }
}
