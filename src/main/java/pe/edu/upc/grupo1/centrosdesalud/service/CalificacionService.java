package pe.edu.upc.grupo1.centrosdesalud.service;

import org.springframework.stereotype.Service;
import pe.edu.upc.grupo1.centrosdesalud.entity.Calificacion;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;
import pe.edu.upc.grupo1.centrosdesalud.repository.CalificacionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;

    public CalificacionService(CalificacionRepository calificacionRepository) {
        this.calificacionRepository = calificacionRepository;
    }

    public List<Calificacion> listarTodas() {
        return calificacionRepository.findAll();
    }

    public Optional<Calificacion> buscarPorId(Long id) {
        return calificacionRepository.findById(id);
    }

    public List<Calificacion> listarPorCentro(CentroDeSalud centro) {
        return calificacionRepository.findByCentroDeSaludOrderByFechaDesc(centro);
    }

    public Calificacion obtenerUltimaPorCentro(CentroDeSalud centro) {
        return calificacionRepository.findTop1ByCentroDeSaludOrderByFechaDesc(centro);
    }

    public Calificacion guardar(Calificacion calificacion) {
        if (calificacion.getCentroDeSalud() == null) {
            throw new IllegalArgumentException("El centro de salud no puede ser nulo");
        }
        if (calificacion.getFecha() == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (calificacion.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }
        if (calificacion.getInfraestructura() < 1 || calificacion.getInfraestructura() > 100) {
            throw new IllegalArgumentException("La calificación de infraestructura debe estar entre 1 y 100");
        }
        if (calificacion.getServicios() < 1 || calificacion.getServicios() > 100) {
            throw new IllegalArgumentException("La calificación de servicios debe estar entre 1 y 100");
        }
        return calificacionRepository.save(calificacion);
    }

    public void eliminar(Long id) {
        calificacionRepository.deleteById(id);
    }
}