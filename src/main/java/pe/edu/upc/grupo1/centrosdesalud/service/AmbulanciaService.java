package pe.edu.upc.grupo1.centrosdesalud.service;

import org.springframework.stereotype.Service;
import pe.edu.upc.grupo1.centrosdesalud.entity.Ambulancia;
import pe.edu.upc.grupo1.centrosdesalud.repository.AmbulanciaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AmbulanciaService {

    private final AmbulanciaRepository ambulanciaRepository;

    public AmbulanciaService(AmbulanciaRepository ambulanciaRepository) {
        this.ambulanciaRepository = ambulanciaRepository;
    }

    public List<Ambulancia> listarTodas() {
        return ambulanciaRepository.findAll();
    }

    public Optional<Ambulancia> buscarPorId(Long id) {
        return ambulanciaRepository.findById(id);
    }

    public Ambulancia guardar(Ambulancia ambulancia) {
        if (ambulancia.getPlaca() == null || ambulancia.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa de la ambulancia no puede estar vacía");
        }
        if (ambulancia.getModelo() == null || ambulancia.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo de la ambulancia no puede estar vacío");
        }
        if (ambulancia.getCentro() == null) {
            throw new IllegalArgumentException("El centro de salud no puede ser nulo");
        }
        return ambulanciaRepository.save(ambulancia);
    }

    public void eliminar(Long id) {
        ambulanciaRepository.deleteById(id);
    }
}