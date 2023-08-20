package com.example.pruebagb.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Valoracion")
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private int DNI;

    private String comentario;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha_valoracion;

    @ManyToOne // Relación muchos a uno con Usuario
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne // Relación muchos a uno con Libro
    @JoinColumn(name = "libro_id")

    private Libro libro;

    public Valoracion() {
    }

    public Valoracion(Long id, String titulo, int DNI, String comentario, LocalDate fecha_valoracion, Usuario usuario, Libro libro) {
        this.id = id;
        this.titulo = titulo;
        this.DNI = DNI;
        this.comentario = comentario;
        this.fecha_valoracion = fecha_valoracion;
        this.usuario = usuario;
        this.libro = libro;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha_valoracion() {
        return fecha_valoracion;
    }

    public void setFecha_valoracion(LocalDate fecha_valoracion) {
        this.fecha_valoracion = fecha_valoracion;
    }

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
}




