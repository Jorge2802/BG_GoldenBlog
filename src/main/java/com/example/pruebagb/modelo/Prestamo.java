package com.example.pruebagb.modelo;

import javax.persistence.*;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "Prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private int DNI;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha_entrega;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha_devolucion;

    @ManyToOne // Relación muchos a uno con Usuario
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne // Relación muchos a uno con Libro
    @JoinColumn(name = "libro_id")
    private Libro libro;



    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Prestamo() {
    }

    public Prestamo(Long id, String titulo, int DNI, LocalDate fecha_entrega, LocalDate fecha_devolucion) {
        this.id = id;
        this.titulo = titulo;
        this.DNI = DNI;
        this.fecha_entrega = fecha_entrega;
        this.fecha_devolucion = fecha_devolucion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    public LocalDate getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(LocalDate fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public LocalDate getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(LocalDate fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }


}
