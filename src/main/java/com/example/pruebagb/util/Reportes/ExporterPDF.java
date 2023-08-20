package com.example.pruebagb.util.Reportes;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.pruebagb.modelo.Usuario;
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
public class ExporterPDF {
    private List<Usuario> listaUsuarios;

    public ExporterPDF(List<Usuario> listaUsuarios) {
        super();
        this.listaUsuarios = listaUsuarios;
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

        celda.setPhrase(new Phrase("NOMBRE", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("APELLIDOS", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("DNI", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("EMAIL", fuente));
        tabla.addCell(celda);
    }

    private void escribirDatosDeLaTabla(PdfPTable tabla) {
        Font fuente = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
        fuente.setColor(Color.BLACK);

        for (Usuario usuario : listaUsuarios) {
            PdfPCell celda = new PdfPCell();
            celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            celda.setPhrase(new Phrase(String.valueOf(usuario.getId()), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(usuario.getNombre(), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(usuario.getApellido(), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(String.valueOf(usuario.getDNI()), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(usuario.getEmail(), fuente));
            tabla.addCell(celda);
        }
    }

    public void exportar(HttpServletResponse response) throws DocumentException, IOException {
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 30, Font.BOLD);
        fuenteTitulo.setColor(Color.BLACK);

        Paragraph titulo = new Paragraph("GESTION DE USUARIOS", fuenteTitulo);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);

        float[] anchosColumnas = {0.8f, 2f, 1.8f, 2.5f, 1.2f};
        tabla.setWidths(anchosColumnas);

        escribirCabeceraDeLaTabla(tabla);
        escribirDatosDeLaTabla(tabla);

        documento.add(tabla);
        documento.close();
    }
}

