package com.example.pruebagb.util.Reportes;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.example.pruebagb.modelo.Usuario;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExporterEXCEL
{
    private XSSFWorkbook usuario;

    private XSSFSheet hoja;

    private List<Usuario> listaUsuarios;

    public ExporterEXCEL(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
        usuario = new XSSFWorkbook();
        hoja = usuario.createSheet("Usuario");
    }

    private void escribirCabeceraDeTabla() {
        Row fila = hoja.createRow(0);

        CellStyle estilo = usuario.createCellStyle();
        XSSFFont fuente = usuario.createFont();
        fuente.setBold(true);
        fuente.setFontHeight(16);
        estilo.setFont(fuente);

        Cell celda = fila.createCell(0);
        celda.setCellValue("ID");
        celda.setCellStyle(estilo);

        celda = fila.createCell(1);
        celda.setCellValue("NOMBRE");
        celda.setCellStyle(estilo);

        celda = fila.createCell(2);
        celda.setCellValue("APELLIDOS");
        celda.setCellStyle(estilo);

        celda = fila.createCell(3);
        celda.setCellValue("DNI");
        celda.setCellStyle(estilo);

        celda = fila.createCell(4);
        celda.setCellValue("EMAIL");
        celda.setCellStyle(estilo);

    }

    private void escribirDatosDeLaTabla() {
        int nueroFilas = 1;

        CellStyle estilo = usuario.createCellStyle();
        XSSFFont fuente = usuario.createFont();
        fuente.setFontHeight(14);
        estilo.setFont(fuente);

        for(Usuario usuario : listaUsuarios) {
            Row fila = hoja.createRow(nueroFilas ++);

            Cell celda = fila.createCell(0);
            celda.setCellValue(usuario.getId());
            hoja.autoSizeColumn(0);
            celda.setCellStyle(estilo);

            celda = fila.createCell(1);
            celda.setCellValue(usuario.getNombre());
            hoja.autoSizeColumn(1);
            celda.setCellStyle(estilo);

            celda = fila.createCell(2);
            celda.setCellValue(usuario.getApellido());
            hoja.autoSizeColumn(2);
            celda.setCellStyle(estilo);

            celda = fila.createCell(3);
            celda.setCellValue(usuario.getDNI());
            hoja.autoSizeColumn(3);
            celda.setCellStyle(estilo);

            celda = fila.createCell(4);
            celda.setCellValue(usuario.getEmail());
            hoja.autoSizeColumn(4);
            celda.setCellStyle(estilo);

        }
    }

    public void exportar(HttpServletResponse response) throws IOException {
        escribirCabeceraDeTabla();
        escribirDatosDeLaTabla();

        ServletOutputStream outPutStream = response.getOutputStream();
        usuario.write(outPutStream);

        usuario.close();
        outPutStream.close();
    }
}