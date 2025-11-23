package pe.edu.upc.grupo1.centrosdesalud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upc.grupo1.centrosdesalud.entity.Ambulancia;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;
import pe.edu.upc.grupo1.centrosdesalud.entity.Region;
import pe.edu.upc.grupo1.centrosdesalud.entity.TipoCentro;
import pe.edu.upc.grupo1.centrosdesalud.service.CentroDeSaludService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CentroDeSaludController.class)
class CentroDeSaludControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CentroDeSaludService centroDeSaludService;

    private Region lima;
    private TipoCentro hospital;
    private CentroDeSalud centro;

    @BeforeEach
    void setUp() {
        lima = new Region("Lima");
        lima.setId(1L);
        hospital = new TipoCentro("Hospital");
        hospital.setId(1L);
        centro = new CentroDeSalud("Hospital Test", hospital, lima);
        centro.setId(1L);
    }

    @Test
    void listarTodos_debeRetornarListaDeCentros() throws Exception {
        // Given
        CentroDeSalud centro2 = new CentroDeSalud("Hospital 2", hospital, lima);
        when(centroDeSaludService.listarTodos()).thenReturn(Arrays.asList(centro, centro2));

        // When & Then
        mockMvc.perform(get("/centros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Hospital Test"))
                .andExpect(jsonPath("$[1].nombre").value("Hospital 2"));
    }

    @Test
    void listarConCalificaciones_debeRetornarCentrosConCalificaciones() throws Exception {
        // Given
        when(centroDeSaludService.listarCentrosConCalificaciones()).thenReturn(Arrays.asList(centro));

        // When & Then
        mockMvc.perform(get("/centros/con-calificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Hospital Test"));
    }

    @Test
    void listarPorTipo_debeRetornarCentrosDelTipo() throws Exception {
        // Given
        when(centroDeSaludService.buscarPorTipo("Hospital")).thenReturn(Arrays.asList(centro));

        // When & Then
        mockMvc.perform(get("/centros/tipo/Hospital"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Hospital Test"));
    }

    @Test
    void buscarPorId_conIdExistente_debeRetornarCentro() throws Exception {
        // Given
        when(centroDeSaludService.buscarPorId(1L)).thenReturn(Optional.of(centro));

        // When & Then
        mockMvc.perform(get("/centros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Hospital Test"));
    }

    @Test
    void buscarPorId_conIdInexistente_debeRetornarNotFound() throws Exception {
        // Given
        when(centroDeSaludService.buscarPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/centros/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerEstado_centroAprobado_debeRetornarTrue() throws Exception {
        // Given
        when(centroDeSaludService.obtenerEstadoAprobacion(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/centros/1/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aprobado").value(true));
    }

    @Test
    void obtenerEstado_centroInexistente_debeRetornarNotFound() throws Exception {
        // Given
        when(centroDeSaludService.obtenerEstadoAprobacion(999L))
                .thenThrow(new IllegalArgumentException("Centro no encontrado"));

        // When & Then
        mockMvc.perform(get("/centros/999/estado"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerAmbulancias_debeRetornarListaDeAmbulancias() throws Exception {
        // Given
        Ambulancia amb = new Ambulancia("ABC-123", "Toyota", centro);
        when(centroDeSaludService.obtenerAmbulancias(1L)).thenReturn(Arrays.asList(amb));

        // When & Then
        mockMvc.perform(get("/centros/1/ambulancias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].placa").value("ABC-123"));
    }

    @Test
    void obtenerAmbulancias_centroInexistente_debeRetornarNotFound() throws Exception {
        // Given
        when(centroDeSaludService.obtenerAmbulancias(999L))
                .thenThrow(new IllegalArgumentException("Centro no encontrado"));

        // When & Then
        mockMvc.perform(get("/centros/999/ambulancias"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerUltimaCalificacion_debeRetornarCalificacion() throws Exception {
        // Given
        when(centroDeSaludService.obtenerUltimaCalificacion(1L)).thenReturn(88.25);

        // When & Then
        mockMvc.perform(get("/centros/1/ultima-calificacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calificacion").value(88.25));
    }

    @Test
    void obtenerUltimaCalificacion_centroInexistente_debeRetornarNotFound() throws Exception {
        // Given
        when(centroDeSaludService.obtenerUltimaCalificacion(999L))
                .thenThrow(new IllegalArgumentException("Centro no encontrado"));

        // When & Then
        mockMvc.perform(get("/centros/999/ultima-calificacion"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_conDatosValidos_debeRetornarCreated() throws Exception {
        // Given
        when(centroDeSaludService.guardar(any(CentroDeSalud.class))).thenReturn(centro);

        // When & Then
        mockMvc.perform(post("/centros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(centro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Hospital Test"));
    }

    @Test
    void crear_conDatosInvalidos_debeRetornarBadRequest() throws Exception {
        // Given
        when(centroDeSaludService.guardar(any(CentroDeSalud.class)))
                .thenThrow(new IllegalArgumentException("Datos invalidos"));

        // When & Then
        mockMvc.perform(post("/centros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(centro)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarNombre_conDatosValidos_debeRetornarCentroActualizado() throws Exception {
        // Given
        centro.setNombre("Nuevo Nombre");
        when(centroDeSaludService.actualizarNombre(eq(1L), eq("Nuevo Nombre"))).thenReturn(centro);

        // When & Then
        mockMvc.perform(put("/centros/1/nombre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("nombre", "Nuevo Nombre"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo Nombre"));
    }

    @Test
    void actualizarNombre_conDatosInvalidos_debeRetornarBadRequest() throws Exception {
        // Given
        when(centroDeSaludService.actualizarNombre(eq(1L), any()))
                .thenThrow(new IllegalArgumentException("Datos invalidos"));

        // When & Then
        mockMvc.perform(put("/centros/1/nombre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("nombre", ""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornarNoContent() throws Exception {
        // When & Then
        mockMvc.perform(delete("/centros/1"))
                .andExpect(status().isNoContent());
    }
}