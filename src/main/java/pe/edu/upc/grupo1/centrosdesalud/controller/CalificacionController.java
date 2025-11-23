package pe.edu.upc.grupo1.centrosdesalud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.grupo1.centrosdesalud.entity.Calificacion;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;
import pe.edu.upc.grupo1.centrosdesalud.service.CalificacionService;
import pe.edu.upc.grupo1.centrosdesalud.service.CentroDeSaludService;

import java.util.List;

@RestController
@RequestMapping("/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;
    private final CentroDeSaludService centroDeSaludService;

    public CalificacionController(CalificacionService calificacionService,
                                  CentroDeSaludService centroDeSaludService) {
        this.calificacionService = calificacionService;
        this.centroDeSaludService = centroDeSaludService;
    }

    @GetMapping
    public ResponseEntity<List<Calificacion>> listarTodas() {
        return ResponseEntity.ok(calificacionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> buscarPorId(@PathVariable Long id) {
        return calificacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/centro/{centroId}")
    public ResponseEntity<List<Calificacion>> listarPorCentro(@PathVariable Long centroId) {
        return centroDeSaludService.buscarPorId(centroId)
                .map(centro -> {
                    List<Calificacion> calificaciones = calificacionService.listarPorCentro(centro);
                    return ResponseEntity.ok(calificaciones);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Calificacion> crear(@RequestBody Calificacion calificacion) {
        try {
            Calificacion nuevaCalificacion = calificacionService.guardar(calificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCalificacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        calificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}