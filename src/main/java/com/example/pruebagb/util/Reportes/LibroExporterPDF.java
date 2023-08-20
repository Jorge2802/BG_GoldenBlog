package com.example.pruebagb.util.Reportes;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.pruebagb.modelo.Libro;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
public class LibroExporterPDF {
    private List<Libro> listaLibros;

    public LibroExporterPDF(List<Libro> listaLibros) {
        super();
        this.listaLibros = listaLibros;
    }

    private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell();

        celda.setBackgroundColor(Color.LIGHT_GRAY);
        celda.setPadding(5);
        celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        Font fuente = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);

        celda.setPhrase(new Phrase("ID", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("TITULO", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("AUTOR", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("EDITORIAL", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("AÑO DE PUBLICACIÓN", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("TIPO_ARCHIVO", fuente));
        tabla.addCell(celda);
    }

    private void escribirDatosDeLaTabla(PdfPTable tabla) {
        Font fuente = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
        fuente.setColor(Color.BLACK);

        for (Libro libro : listaLibros) {
            PdfPCell celda = new PdfPCell();
            celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            celda.setPhrase(new Phrase(String.valueOf(libro.getId()), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(libro.getTitulo(), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(libro.getAutor(), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(libro.getEditorial(), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(String.valueOf(libro.getAno_publicacion()), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(libro.getTipo_archivo(), fuente));
            tabla.addCell(celda);
        }
    }

    public void exportar(HttpServletResponse response) throws DocumentException, IOException {
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 30, Font.BOLD);
        fuenteTitulo.setColor(Color.BLACK);

        Paragraph titulo = new Paragraph("Lista de Libros", fuenteTitulo);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);

        float[] anchosColumnas = {0.8f, 2f, 1.8f, 2.5f, 1.2f, 1.8f};
        tabla.setWidths(anchosColumnas);

        escribirCabeceraDeLaTabla(tabla);
        escribirDatosDeLaTabla(tabla);

        documento.add(tabla);
        documento.close();
    }
}
