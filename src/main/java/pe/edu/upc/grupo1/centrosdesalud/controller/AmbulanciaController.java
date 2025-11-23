package pe.edu.upc.grupo1.centrosdesalud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.grupo1.centrosdesalud.entity.Ambulancia;
import pe.edu.upc.grupo1.centrosdesalud.service.AmbulanciaService;

import java.util.List;

@RestController
@RequestMapping("/ambulancias")
public class AmbulanciaController {

    private final AmbulanciaService ambulanciaService;

    public AmbulanciaController(AmbulanciaService ambulanciaService) {
        this.ambulanciaService = ambulanciaService;
    }

    @GetMapping
    public ResponseEntity<List<Ambulancia>> listarTodas() {
        return ResponseEntity.ok(ambulanciaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ambulancia> buscarPorId(@PathVariable Long id) {
        return ambulanciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ambulancia> crear(@RequestBody Ambulancia ambulancia) {
        try {
            Ambulancia nuevaAmbulancia = ambulanciaService.guardar(ambulancia);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAmbulancia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ambulanciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}