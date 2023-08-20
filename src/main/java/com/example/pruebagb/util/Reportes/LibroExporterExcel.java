package com.example.pruebagb.util.Reportes;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.example.pruebagb.modelo.Libro;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LibroExporterExcel {
    private XSSFWorkbook libro;

    private XSSFSheet hoja;

    private List<Libro> listaLibros;

    public LibroExporterExcel(List<Libro> listaLibros) {
        this.listaLibros = listaLibros;
        libro = new XSSFWorkbook();
        hoja = libro.createSheet("Libro");
    }

    private void escribirCabeceraDeTabla() {
        Row fila = hoja.createRow(0);

        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setBold(true);
        fuente.setFontHeight(16);
        estilo.setFont(fuente);

        Cell celda = fila.createCell(0);
        celda.setCellValue("ID");
        celda.setCellStyle(estilo);

        celda = fila.createCell(1);
        celda.setCellValue("TITULO");
        celda.setCellStyle(estilo);

        celda = fila.createCell(2);
        celda.setCellValue("AUTOR");
        celda.setCellStyle(estilo);

        celda = fila.createCell(3);
        celda.setCellValue("EDITORIAL");
        celda.setCellStyle(estilo);

        celda = fila.createCell(4);
        celda.setCellValue("AÑO DE PUBLICACION");
        celda.setCellStyle(estilo);

        celda = fila.createCell(5);
        celda.setCellValue("TIPO DE ARCHIVO");
        celda.setCellStyle(estilo);

        celda = fila.createCell(6);
        celda.setCellValue("DESCRIPCION");
        celda.setCellStyle(estilo);

    }

    private void escribirDatosDeLaTabla() {
        int nueroFilas = 1;

        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setFontHeight(14);
        estilo.setFont(fuente);

        for(Libro libro : listaLibros) {
            Row fila = hoja.createRow(nueroFilas ++);

            Cell celda = fila.createCell(0);
            celda.setCellValue(libro.getId());
            hoja.autoSizeColumn(0);
            celda.setCellStyle(estilo);

            celda = fila.createCell(1);
            celda.setCellValue(libro.getTitulo());
            hoja.autoSizeColumn(1);
            celda.setCellStyle(estilo);

            celda = fila.createCell(2);
            celda.setCellValue(libro.getAutor());
            hoja.autoSizeColumn(2);
            celda.setCellStyle(estilo);

            celda = fila.createCell(3);
            celda.setCellValue(libro.getEditorial());
            hoja.autoSizeColumn(3);
            celda.setCellStyle(estilo);

            celda = fila.createCell(4);
            celda.setCellValue(libro.getAno_publicacion());
            hoja.autoSizeColumn(4);
            celda.setCellStyle(estilo);

            celda = fila.createCell(5);
            celda.setCellValue(libro.getTipo_archivo());
            hoja.autoSizeColumn(5);
            celda.setCellStyle(estilo);

            celda = fila.createCell(6);
            celda.setCellValue(libro.getDescripcion());
            hoja.autoSizeColumn(6);
            celda.setCellStyle(estilo);


        }
    }

    public void exportar(HttpServletResponse response) throws IOException {
        escribirCabeceraDeTabla();
        escribirDatosDeLaTabla();

        ServletOutputStream outPutStream = response.getOutputStream();
        libro.write(outPutStream);

        libro.close();
        outPutStream.close();
    }
}
