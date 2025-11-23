package pe.edu.upc.grupo1.centrosdesalud.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "centros_salud")
public class CentroDeSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoCentro tipo;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "centro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ambulancia> ambulancias = new ArrayList<>();

    @OneToMany(mappedBy = "centroDeSalud", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calificacion> calificaciones = new ArrayList<>();

    public CentroDeSalud() {
    }

    public CentroDeSalud(String nombre, TipoCentro tipo, Region region) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.region = region;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoCentro getTipo() {
        return tipo;
    }

    public void setTipo(TipoCentro tipo) {
        this.tipo = tipo;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<Ambulancia> getAmbulancias() {
        return ambulancias;
    }

    public void setAmbulancias(List<Ambulancia> ambulancias) {
        this.ambulancias = ambulancias;
    }

    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public double calcularUltimaCalificacion() {
        if (calificaciones == null || calificaciones.isEmpty()) {
            return 0.0;
        }
        return calificaciones.stream()
                .max((c1, c2) -> c1.getFecha().compareTo(c2.getFecha()))
                .map(Calificacion::getCalificacionFinal)
                .orElse(0.0);
    }

    public boolean estaAprobado() {
        return calcularUltimaCalificacion() >= 80.0;
    }
}