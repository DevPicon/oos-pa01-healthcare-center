package pe.edu.upc.grupo1.centrosdesalud.service;

import org.springframework.stereotype.Service;
import pe.edu.upc.grupo1.centrosdesalud.entity.TipoCentro;
import pe.edu.upc.grupo1.centrosdesalud.repository.TipoCentroRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TipoCentroService {

    private final TipoCentroRepository tipoCentroRepository;

    public TipoCentroService(TipoCentroRepository tipoCentroRepository) {
        this.tipoCentroRepository = tipoCentroRepository;
    }

    public List<TipoCentro> listarTodos() {
        return tipoCentroRepository.findAll();
    }

    public Optional<TipoCentro> buscarPorId(Long id) {
        return tipoCentroRepository.findById(id);
    }

    public TipoCentro guardar(TipoCentro tipoCentro) {
        if (tipoCentro.getNombre() == null || tipoCentro.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo de centro no puede estar vacío");
        }
        String nombre = tipoCentro.getNombre();
        if (!nombre.equals("Hospital") && !nombre.equals("Clinica")) {
            throw new IllegalArgumentException("El tipo de centro debe ser 'Hospital' o 'Clinica'");
        }
        return tipoCentroRepository.save(tipoCentro);
    }

    public void eliminar(Long id) {
        tipoCentroRepository.deleteById(id);
    }
}