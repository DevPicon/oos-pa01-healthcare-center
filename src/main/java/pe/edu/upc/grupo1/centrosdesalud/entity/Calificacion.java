package pe.edu.upc.grupo1.centrosdesalud.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "calificaciones")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private int infraestructura;

    @Column(nullable = false)
    private int servicios;

    @Column(nullable = false)
    private double calificacionFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "centro_id", nullable = false)
    private CentroDeSalud centroDeSalud;

    public Calificacion() {
    }

    public Calificacion(LocalDate fecha, int infraestructura, int servicios, CentroDeSalud centroDeSalud) {
        this.fecha = fecha;
        this.infraestructura = infraestructura;
        this.servicios = servicios;
        this.centroDeSalud = centroDeSalud;
        this.calificacionFinal = calcularCalificacion();
        this.estado = this.calificacionFinal >= 80 ? Estado.APROBADO : Estado.RECHAZADO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getInfraestructura() {
        return infraestructura;
    }

    public void setInfraestructura(int infraestructura) {
        this.infraestructura = infraestructura;
    }

    public int getServicios() {
        return servicios;
    }

    public void setServicios(int servicios) {
        this.servicios = servicios;
    }

    public double getCalificacionFinal() {
        return calificacionFinal;
    }

    public void setCalificacionFinal(double calificacionFinal) {
        this.calificacionFinal = calificacionFinal;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public CentroDeSalud getCentroDeSalud() {
        return centroDeSalud;
    }

    public void setCentroDeSalud(CentroDeSalud centroDeSalud) {
        this.centroDeSalud = centroDeSalud;
    }

    public double calcularCalificacion() {
        return (infraestructura * 0.35) + (servicios * 0.65);
    }
}