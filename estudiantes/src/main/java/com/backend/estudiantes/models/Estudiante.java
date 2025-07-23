package com.backend.estudiantes.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estudiantes")
@Data //getters, setters, construcor, equals, hashCode
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne //de uno a uno
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "codigo_estudiantil" ,nullable = false, unique = true, length = 10)
    private String codigoEstudiantil;

    @Column(
            name = "horas_acomuladas",
            nullable = false
    )
    private Integer horasAcomuladas;

    @Column(
            nullable = false,
            length = 100
    )
    private String programaAcademico;

    @Column(nullable = false)
    private Integer semestre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEstudiante estado = EstadoEstudiante.ACTIVO;

    /*
    public String getNombreCompleto() {
        return usuario.getNombre();
    }

     */
}