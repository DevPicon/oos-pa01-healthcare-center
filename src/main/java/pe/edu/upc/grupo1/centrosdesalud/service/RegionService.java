package pe.edu.upc.grupo1.centrosdesalud.service;

import org.springframework.stereotype.Service;
import pe.edu.upc.grupo1.centrosdesalud.entity.Region;
import pe.edu.upc.grupo1.centrosdesalud.repository.RegionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public List<Region> listarTodas() {
        return regionRepository.findAll();
    }

    public Optional<Region> buscarPorId(Long id) {
        return regionRepository.findById(id);
    }

    public Region guardar(Region region) {
        if (region.getNombre() == null || region.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la región no puede estar vacío");
        }
        return regionRepository.save(region);
    }

    public void eliminar(Long id) {
        regionRepository.deleteById(id);
    }
}