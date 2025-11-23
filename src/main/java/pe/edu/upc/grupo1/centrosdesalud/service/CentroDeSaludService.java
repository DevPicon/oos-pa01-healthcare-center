package pe.edu.upc.grupo1.centrosdesalud.service;

import org.springframework.stereotype.Service;
import pe.edu.upc.grupo1.centrosdesalud.entity.Ambulancia;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;
import pe.edu.upc.grupo1.centrosdesalud.repository.CentroDeSaludRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CentroDeSaludService {

    private final CentroDeSaludRepository centroDeSaludRepository;

    public CentroDeSaludService(CentroDeSaludRepository centroDeSaludRepository) {
        this.centroDeSaludRepository = centroDeSaludRepository;
    }

    public List<CentroDeSalud> listarTodos() {
        return centroDeSaludRepository.findAll();
    }

    public Optional<CentroDeSalud> buscarPorId(Long id) {
        return centroDeSaludRepository.findById(id);
    }

    public List<CentroDeSalud> buscarPorTipo(String tipo) {
        return centroDeSaludRepository.findByTipo_Nombre(tipo);
    }

    public List<CentroDeSalud> listarCentrosConCalificaciones() {
        return centroDeSaludRepository.findCentrosConCalificaciones();
    }

    public CentroDeSalud guardar(CentroDeSalud centroDeSalud) {
        if (centroDeSalud.getNombre() == null || centroDeSalud.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del centro de salud no puede estar vacío");
        }
        if (centroDeSalud.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de centro no puede ser nulo");
        }
        if (centroDeSalud.getRegion() == null) {
            throw new IllegalArgumentException("La región no puede ser nula");
        }
        return centroDeSaludRepository.save(centroDeSalud);
    }

    public CentroDeSalud actualizarNombre(Long id, String nuevoNombre) {
        CentroDeSalud centro = centroDeSaludRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Centro de salud no encontrado"));

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        centro.setNombre(nuevoNombre);
        return centroDeSaludRepository.save(centro);
    }

    public List<Ambulancia> obtenerAmbulancias(Long id) {
        CentroDeSalud centro = centroDeSaludRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Centro de salud no encontrado"));
        return centro.getAmbulancias();
    }

    public double obtenerUltimaCalificacion(Long id) {
        CentroDeSalud centro = centroDeSaludRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Centro de salud no encontrado"));
        return centro.calcularUltimaCalificacion();
    }

    public boolean obtenerEstadoAprobacion(Long id) {
        CentroDeSalud centro = centroDeSaludRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Centro de salud no encontrado"));
        return centro.estaAprobado();
    }

    public void eliminar(Long id) {
        centroDeSaludRepository.deleteById(id);
    }
}