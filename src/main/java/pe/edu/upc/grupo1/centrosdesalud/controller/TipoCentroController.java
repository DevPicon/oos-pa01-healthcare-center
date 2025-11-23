package pe.edu.upc.grupo1.centrosdesalud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.grupo1.centrosdesalud.entity.TipoCentro;
import pe.edu.upc.grupo1.centrosdesalud.service.TipoCentroService;

import java.util.List;

@RestController
@RequestMapping("/tipos-centro")
public class TipoCentroController {

    private final TipoCentroService tipoCentroService;

    public TipoCentroController(TipoCentroService tipoCentroService) {
        this.tipoCentroService = tipoCentroService;
    }

    @GetMapping
    public ResponseEntity<List<TipoCentro>> listarTodos() {
        return ResponseEntity.ok(tipoCentroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCentro> buscarPorId(@PathVariable Long id) {
        return tipoCentroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoCentro> crear(@RequestBody TipoCentro tipoCentro) {
        try {
            TipoCentro nuevoTipoCentro = tipoCentroService.guardar(tipoCentro);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipoCentro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoCentroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}