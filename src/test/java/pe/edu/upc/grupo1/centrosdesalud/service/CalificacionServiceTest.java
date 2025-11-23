package pe.edu.upc.grupo1.centrosdesalud.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.grupo1.centrosdesalud.entity.*;
import pe.edu.upc.grupo1.centrosdesalud.repository.CalificacionRepository;
import pe.edu.upc.grupo1.centrosdesalud.repository.CentroDeSaludRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private CentroDeSaludRepository centroDeSaludRepository;

    @InjectMocks
    private CalificacionService calificacionService;

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
    void guardar_debeGuardarYRetornarCalificacion() {
        // Given
        when(calificacionRepository.save(calificacion)).thenReturn(calificacion);

        // When
        Calificacion resultado = calificacionService.guardar(calificacion);

        // Then
        assertNotNull(resultado);
        assertEquals(88.25, resultado.getCalificacionFinal(), 0.01);
        verify(calificacionRepository, times(1)).save(calificacion);
    }

    @Test
    void listarPorCentro_debeRetornarCalificaciones() {
        // Given
        Calificacion cal2 = new Calificacion(LocalDate.now().minusDays(5), 80, 85, centro);
        when(calificacionRepository.findByCentroDeSaludOrderByFechaDesc(centro))
                .thenReturn(Arrays.asList(calificacion, cal2));

        // When
        List<Calificacion> resultado = calificacionService.listarPorCentro(centro);

        // Then
        assertEquals(2, resultado.size());
        verify(calificacionRepository, times(1)).findByCentroDeSaludOrderByFechaDesc(centro);
    }

    @Test
    void obtenerUltimaPorCentro_conCalificaciones_debeRetornarLaUltima() {
        // Given
        when(calificacionRepository.findTop1ByCentroDeSaludOrderByFechaDesc(centro))
                .thenReturn(calificacion);

        // When
        Calificacion resultado = calificacionService.obtenerUltimaPorCentro(centro);

        // Then
        assertNotNull(resultado);
        assertEquals(88.25, resultado.getCalificacionFinal(), 0.01);
        verify(calificacionRepository, times(1)).findTop1ByCentroDeSaludOrderByFechaDesc(centro);
    }

    @Test
    void obtenerUltimaPorCentro_sinCalificaciones_debeRetornarNull() {
        // Given
        when(calificacionRepository.findTop1ByCentroDeSaludOrderByFechaDesc(centro))
                .thenReturn(null);

        // When
        Calificacion resultado = calificacionService.obtenerUltimaPorCentro(centro);

        // Then
        assertNull(resultado);
        verify(calificacionRepository, times(1)).findTop1ByCentroDeSaludOrderByFechaDesc(centro);
    }
}