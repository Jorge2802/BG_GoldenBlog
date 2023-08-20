package com.example.pruebagb.controladores;

import com.example.pruebagb.Servicios.LibroService;
import com.example.pruebagb.Servicios.PrestamoService;
import com.example.pruebagb.Servicios.UsuarioServicio;
import com.example.pruebagb.modelo.Libro;
import com.example.pruebagb.modelo.Prestamo;
import com.example.pruebagb.modelo.Usuario;
import com.example.pruebagb.repositorios.LibroRepository;
import com.example.pruebagb.repositorios.UsuarioRepositorio;
import com.example.pruebagb.util.Reportes.PrestamoExporterPDF;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class PrestamosController {
    @Autowired
    private PrestamoService prestamoService;
    @Autowired
    private LibroService libroService;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private LibroRepository libroRepository;

    @GetMapping("/prestamos")
    public String listarPrestamos(Model model) {
        List<Prestamo> prestamos = prestamoService.obtenerTodosLosPrestamos();
        model.addAttribute("prestamos", prestamos);
        return "verPrestamos";
    }

    @GetMapping("/prestar/{libroId}")
    public String prestarLibro(@PathVariable Long libroId, Authentication authentication, Model model) {
        // Obtener título del libro por su ID
        String tituloLibro = libroService.obtenerTituloPorId(libroId);

        // Obtener ID del usuario autenticado
        Long userId = usuarioServicio.obtenerIdUsuarioAutenticado();

        // Obtener DNI del usuario por su ID
        Usuario usuario = usuarioServicio.findById(userId);
        int dniUsuario = usuario.getDNI();

        // Obtener fecha actual
        LocalDate fechaEntrega = LocalDate.now();

        model.addAttribute("tituloLibro", tituloLibro);
        model.addAttribute("dniUsuario", dniUsuario);
        model.addAttribute("fechaEntrega", fechaEntrega);


        return "formPrestamos";
    }

    @PostMapping("/prestar/{libroId}")
    public String guardarPrestamo(@ModelAttribute Prestamo prestamo, Model model) {
        // Validar que la fecha de devolución no supere los 15 días posteriores a la fecha de préstamo
        LocalDate fechaPrestamo = prestamo.getFecha_entrega();
        LocalDate fechaDevolucion = prestamo.getFecha_devolucion();

        if (fechaDevolucion.isAfter(fechaPrestamo.plusDays(15))) {
            model.addAttribute("error", "La fecha de devolución no puede ser posterior a 15 días del préstamo.");
            return "formPrestamos"; // Vuelve a la página de registro de préstamos con el mensaje de error
        }

        // Guardar el préstamo utilizando el servicio de Prestamo
        prestamoService.guardarPrestamo(prestamo);

        // Redirige a una página de éxito o a donde desees
        return "redirect:/prestamos";
    }

    @GetMapping("/prestamos/{id}")
    public String mostrarDetallesPrestamo(@PathVariable Long id, Model model) {
        Optional<Prestamo> prestamo = prestamoService.obtenerPrestamoPorId(id);

        if (prestamo.isPresent()) {
            Prestamo prestamoDetalles = prestamo.get();

            Usuario usuario = usuarioRepositorio.findByDNI(prestamoDetalles.getDNI());
            Libro libro = libroRepository.findByTitulo(prestamoDetalles.getTitulo());

            model.addAttribute("prestamo", prestamoDetalles);
            model.addAttribute("usuario", usuario);
            model.addAttribute("libro", libro);

            return "DetallesPrestamo";
        } else {
            return "redirect:/prestamos";
        }
    }

    @GetMapping("/buscarprestamo")
    public String buscarPrestamos(@RequestParam String filtro, Model model) {
        List<Prestamo> prestamosEncontrados = prestamoService.buscarPrestamos(filtro);
        model.addAttribute("prestamos", prestamosEncontrados);
        return "VerPrestamos";
    }

    @GetMapping("/eliminarprestamo/{id}")
    public String eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return "redirect:/prestamos";
    }

    @GetMapping("/prestamos/exportarPDF")
    public void exportarListadoDeLibrosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Prestamos_" + fechaActual + ".pdf";

        response.setHeader(cabecera, valor);

        List<Prestamo> prestamos = prestamoService.obtenerTodosLosPrestamos();

        PrestamoExporterPDF exporter = new PrestamoExporterPDF(prestamos);
        exporter.exportar(response);


    }


}
