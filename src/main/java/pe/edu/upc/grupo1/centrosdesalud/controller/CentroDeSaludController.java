package pe.edu.upc.grupo1.centrosdesalud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.grupo1.centrosdesalud.entity.Ambulancia;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;
import pe.edu.upc.grupo1.centrosdesalud.service.CentroDeSaludService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/centros")
public class CentroDeSaludController {

    private final CentroDeSaludService centroDeSaludService;

    public CentroDeSaludController(CentroDeSaludService centroDeSaludService) {
        this.centroDeSaludService = centroDeSaludService;
    }

    @GetMapping
    public ResponseEntity<List<CentroDeSalud>> listarTodos() {
        return ResponseEntity.ok(centroDeSaludService.listarTodos());
    }

    @GetMapping("/con-calificaciones")
    public ResponseEntity<List<CentroDeSalud>> listarConCalificaciones() {
        return ResponseEntity.ok(centroDeSaludService.listarCentrosConCalificaciones());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CentroDeSalud>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(centroDeSaludService.buscarPorTipo(tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroDeSalud> buscarPorId(@PathVariable Long id) {
        return centroDeSaludService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<Map<String, Boolean>> obtenerEstado(@PathVariable Long id) {
        try {
            boolean aprobado = centroDeSaludService.obtenerEstadoAprobacion(id);
            return ResponseEntity.ok(Map.of("aprobado", aprobado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/ambulancias")
    public ResponseEntity<List<Ambulancia>> obtenerAmbulancias(@PathVariable Long id) {
        try {
            List<Ambulancia> ambulancias = centroDeSaludService.obtenerAmbulancias(id);
            return ResponseEntity.ok(ambulancias);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/ultima-calificacion")
    public ResponseEntity<Map<String, Double>> obtenerUltimaCalificacion(@PathVariable Long id) {
        try {
            double calificacion = centroDeSaludService.obtenerUltimaCalificacion(id);
            return ResponseEntity.ok(Map.of("calificacion", calificacion));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CentroDeSalud> crear(@RequestBody CentroDeSalud centroDeSalud) {
        try {
            CentroDeSalud nuevoCentro = centroDeSaludService.guardar(centroDeSalud);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCentro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/nombre")
    public ResponseEntity<CentroDeSalud> actualizarNombre(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoNombre = body.get("nombre");
            CentroDeSalud centroActualizado = centroDeSaludService.actualizarNombre(id, nuevoNombre);
            return ResponseEntity.ok(centroActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        centroDeSaludService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}