package com.example.pruebagb.controladores;

import com.example.pruebagb.Servicios.LibroService;
import com.example.pruebagb.modelo.Libro;
import com.example.pruebagb.util.Reportes.LibroExporterExcel;
import com.example.pruebagb.util.Reportes.LibroExporterPDF;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping("/libros")
    public String listarLibros(Model model) {
        List<Libro> libros = libroService.obtenerTodosLosLibros();
        model.addAttribute("libros", libros);
        return "BuscarLibros";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoLibro(Model model) {
        model.addAttribute("libro", new Libro());
        return "form";
    }

    @PostMapping("/nuevo")
    public String guardarLibro(@ModelAttribute Libro libro) {
        libroService.guardarLibro(libro);
        return "redirect:/libros";
    }

    @GetMapping("/{id}")
    public String mostrarDetallesLibro(@PathVariable Long id, Model model) {
        Optional<Libro> libro = libroService.obtenerLibroPorId(id);
        if (libro.isPresent()) {
            model.addAttribute("libro", libro.get());
            return "verLibros";
        } else {
            return "redirect:/libros";
        }
    }

    @GetMapping("/buscar")
    public String buscarLibros(@RequestParam String filtro, Model model) {
        List<Libro> librosEncontrados = libroService.buscarLibros(filtro);
        model.addAttribute("libros", librosEncontrados);
        return "BuscarLibros";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarLibro(@PathVariable Long id, Model model) {
        Optional<Libro> libro = libroService.obtenerLibroPorId(id);
        if (libro.isPresent()) {
            model.addAttribute("libro", libro.get());
            return "EditarLibro";
        } else {
            return "redirect:/libros";
        }
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicionLibro(@PathVariable Long id, @ModelAttribute Libro libro) {
        libro.setId(id); // Aseguramos que el ID se mantenga
        libroService.guardarLibro(libro);
        return "redirect:/libros";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return "redirect:/libros";
    }

    @GetMapping("/libros/exportarPDF")
    public void exportarListadoDeLibrosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Libros_" + fechaActual + ".pdf";

        response.setHeader(cabecera, valor);

        List<Libro> libros = libroService.obtenerTodosLosLibros();

        LibroExporterPDF exporter = new LibroExporterPDF(libros);
        exporter.exportar(response);


    }

    @GetMapping("/libros/exportarExcel")
    public void exportarListadoDeLibrosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Libros_" + fechaActual + ".xlsx";

        response.setHeader(cabecera, valor);

        List<Libro> libros = libroService.obtenerTodosLosLibros();

        LibroExporterExcel exporter = new LibroExporterExcel(libros);
        exporter.exportar(response);
    }



}
