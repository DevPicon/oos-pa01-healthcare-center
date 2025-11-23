package pe.edu.upc.grupo1.centrosdesalud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.grupo1.centrosdesalud.entity.Region;
import pe.edu.upc.grupo1.centrosdesalud.service.RegionService;

import java.util.List;

@RestController
@RequestMapping("/regiones")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public ResponseEntity<List<Region>> listarTodas() {
        return ResponseEntity.ok(regionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> buscarPorId(@PathVariable Long id) {
        return regionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Region> crear(@RequestBody Region region) {
        try {
            Region nuevaRegion = regionService.guardar(region);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRegion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        regionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}