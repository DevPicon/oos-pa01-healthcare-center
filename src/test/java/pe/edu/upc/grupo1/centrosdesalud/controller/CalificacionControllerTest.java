package pe.edu.upc.grupo1.centrosdesalud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upc.grupo1.centrosdesalud.entity.Calificacion;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;
import pe.edu.upc.grupo1.centrosdesalud.entity.Region;
import pe.edu.upc.grupo1.centrosdesalud.entity.TipoCentro;
import pe.edu.upc.grupo1.centrosdesalud.service.CalificacionService;
import pe.edu.upc.grupo1.centrosdesalud.service.CentroDeSaludService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalificacionController.class)
class CalificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CalificacionService calificacionService;

    @MockBean
    private CentroDeSaludService centroDeSaludService;

    private Region lima;
    private TipoCentro hospital;
    private CentroDeSalud centro;
    private Calificacion calificacion;

    @BeforeEach
    void setUp() {
        lima = new Region("Lima");
        hospital = new TipoCentro("Hospital");
        centro = new CentroDeSalud("Hospital Test", hospital, lima);
        centro.setId(1L);
        calificacion = new Calificacion(LocalDate.now(), 85, 90, centro);
        calificacion.setId(1L);
    }

    @Test
    void listarTodas_debeRetornarListaDeCalificaciones() throws Exception {
        // Given
        Calificacion cal2 = new Calificacion(LocalDate.now().minusDays(5), 80, 85, centro);
        when(calificacionService.listarTodas()).thenReturn(Arrays.asList(calificacion, cal2));

        // When & Then
        mockMvc.perform(get("/calificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void buscarPorId_conIdExistente_debeRetornarCalificacion() throws Exception {
        // Given
        when(calificacionService.buscarPorId(1L)).thenReturn(Optional.of(calificacion));

        // When & Then
        mockMvc.perform(get("/calificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.infraestructura").value(85))
                .andExpect(jsonPath("$.servicios").value(90));
    }

    @Test
    void buscarPorId_conIdInexistente_debeRetornarNotFound() throws Exception {
        // Given
        when(calificacionService.buscarPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/calificaciones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPorCentro_conCentroExistente_debeRetornarCalificaciones() throws Exception {
        // Given
        when(centroDeSaludService.buscarPorId(1L)).thenReturn(Optional.of(centro));
        when(calificacionService.listarPorCentro(centro)).thenReturn(Arrays.asList(calificacion));

        // When & Then
        mockMvc.perform(get("/calificaciones/centro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarPorCentro_conCentroInexistente_debeRetornarNotFound() throws Exception {
        // Given
        when(centroDeSaludService.buscarPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/calificaciones/centro/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_conDatosValidos_debeRetornarCreated() throws Exception {
        // Given
        when(calificacionService.guardar(any(Calificacion.class))).thenReturn(calificacion);

        // When & Then
        mockMvc.perform(post("/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calificacion)))
                .andExpect(status().isCreated());
    }

    @Test
    void crear_conDatosInvalidos_debeRetornarBadRequest() throws Exception {
        // Given
        when(calificacionService.guardar(any(Calificacion.class)))
                .thenThrow(new IllegalArgumentException("Datos invalidos"));

        // When & Then
        mockMvc.perform(post("/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calificacion)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornarNoContent() throws Exception {
        // When & Then
        mockMvc.perform(delete("/calificaciones/1"))
                .andExpect(status().isNoContent());
    }
}
