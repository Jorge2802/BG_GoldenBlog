package com.example.pruebagb.util.Reportes;

import com.example.pruebagb.modelo.Prestamo;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PrestamoExporterPDF {
    private List<Prestamo> listaPrestamos;

    public PrestamoExporterPDF(List<Prestamo> listaPrestamos) {
        super();
        this.listaPrestamos = listaPrestamos;
    }

    private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell();

        celda.setBackgroundColor(Color.LIGHT_GRAY);
        celda.setPadding(5);
        celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        com.lowagie.text.Font fuente = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, com.lowagie.text.Font.BOLD, Color.WHITE);

        celda.setPhrase(new Phrase("ID", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("DNI", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("TITULO", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("FECHA DE PRESTAMO", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("FECHA DE ENTREGA", fuente));
        tabla.addCell(celda);
    }

    private void escribirDatosDeLaTabla(PdfPTable tabla) {
        com.lowagie.text.Font fuente = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
        fuente.setColor(Color.BLACK);

        for (Prestamo prestamo : listaPrestamos) {
            PdfPCell celda = new PdfPCell();
            celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            celda.setPhrase(new Phrase(String.valueOf(prestamo.getId()), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(String.valueOf(prestamo.getDNI()), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(prestamo.getTitulo(), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(String.valueOf(prestamo.getFecha_entrega()), fuente));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(String.valueOf(prestamo.getFecha_entrega()), fuente));
            tabla.addCell(celda);
        }
    }

    public void exportar(HttpServletResponse response) throws DocumentException, IOException {
        Document documento = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();

        com.lowagie.text.Font fuenteTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 30, Font.BOLD);
        fuenteTitulo.setColor(Color.BLACK);

        Paragraph titulo = new Paragraph("LISTA DE PRESTAMOS", fuenteTitulo);
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
