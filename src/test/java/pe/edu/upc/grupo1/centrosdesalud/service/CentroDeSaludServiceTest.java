package pe.edu.upc.grupo1.centrosdesalud.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.grupo1.centrosdesalud.entity.*;
import pe.edu.upc.grupo1.centrosdesalud.repository.CentroDeSaludRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CentroDeSaludServiceTest {

    @Mock
    private CentroDeSaludRepository centroDeSaludRepository;

    @InjectMocks
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
    void listarTodos_debeRetornarListaDeCentros() {
        // Given
        CentroDeSalud centro2 = new CentroDeSalud("Hospital 2", hospital, lima);
        when(centroDeSaludRepository.findAll()).thenReturn(Arrays.asList(centro, centro2));

        // When
        List<CentroDeSalud> resultado = centroDeSaludService.listarTodos();

        // Then
        assertEquals(2, resultado.size());
        verify(centroDeSaludRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_conIdExistente_debeRetornarCentro() {
        // Given
        when(centroDeSaludRepository.findById(1L)).thenReturn(Optional.of(centro));

        // When
        Optional<CentroDeSalud> resultado = centroDeSaludService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Hospital Test", resultado.get().getNombre());
        verify(centroDeSaludRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_conIdInexistente_debeRetornarVacio() {
        // Given
        when(centroDeSaludRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<CentroDeSalud> resultado = centroDeSaludService.buscarPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(centroDeSaludRepository, times(1)).findById(999L);
    }

    @Test
    void guardar_debeRetornarCentroGuardado() {
        // Given
        when(centroDeSaludRepository.save(centro)).thenReturn(centro);

        // When
        CentroDeSalud resultado = centroDeSaludService.guardar(centro);

        // Then
        assertNotNull(resultado);
        assertEquals("Hospital Test", resultado.getNombre());
        verify(centroDeSaludRepository, times(1)).save(centro);
    }

    @Test
    void eliminar_debeInvocarRepositoryDelete() {
        // When
        centroDeSaludService.eliminar(1L);

        // Then
        verify(centroDeSaludRepository, times(1)).deleteById(1L);
    }

    @Test
    void buscarPorTipo_debeRetornarCentrosDelTipo() {
        // Given
        CentroDeSalud centro2 = new CentroDeSalud("Hospital 2", hospital, lima);
        when(centroDeSaludRepository.findByTipo_Nombre("Hospital"))
                .thenReturn(Arrays.asList(centro, centro2));

        // When
        List<CentroDeSalud> resultado = centroDeSaludService.buscarPorTipo("Hospital");

        // Then
        assertEquals(2, resultado.size());
        verify(centroDeSaludRepository, times(1)).findByTipo_Nombre("Hospital");
    }

    @Test
    void obtenerAmbulancias_conCentroExistente_debeRetornarAmbulancias() {
        // Given
        Ambulancia amb1 = new Ambulancia("ABC-123", "Toyota", centro);
        centro.getAmbulancias().add(amb1);
        when(centroDeSaludRepository.findById(1L)).thenReturn(Optional.of(centro));

        // When
        List<Ambulancia> resultado = centroDeSaludService.obtenerAmbulancias(1L);

        // Then
        assertEquals(1, resultado.size());
        assertEquals("ABC-123", resultado.get(0).getPlaca());
        verify(centroDeSaludRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerAmbulancias_conCentroInexistente_debeLanzarExcepcion() {
        // Given
        when(centroDeSaludRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            centroDeSaludService.obtenerAmbulancias(999L);
        });
        verify(centroDeSaludRepository, times(1)).findById(999L);
    }

    @Test
    void obtenerUltimaCalificacion_conCalificaciones_debeRetornarCalificacion() {
        // Given
        Calificacion cal = new Calificacion(LocalDate.now(), 85, 90, centro);
        centro.getCalificaciones().add(cal);
        when(centroDeSaludRepository.findById(1L)).thenReturn(Optional.of(centro));

        // When
        double resultado = centroDeSaludService.obtenerUltimaCalificacion(1L);

        // Then
        assertEquals(88.25, resultado, 0.01);
        verify(centroDeSaludRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerUltimaCalificacion_sinCalificaciones_debeRetornarCero() {
        // Given
        when(centroDeSaludRepository.findById(1L)).thenReturn(Optional.of(centro));

        // When
        double resultado = centroDeSaludService.obtenerUltimaCalificacion(1L);

        // Then
        assertEquals(0.0, resultado);
        verify(centroDeSaludRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerUltimaCalificacion_conCentroInexistente_debeLanzarExcepcion() {
        // Given
        when(centroDeSaludRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            centroDeSaludService.obtenerUltimaCalificacion(999L);
        });
        verify(centroDeSaludRepository, times(1)).findById(999L);
    }

    @Test
    void obtenerEstadoAprobacion_centroAprobado_debeRetornarTrue() {
        // Given
        Calificacion cal = new Calificacion(LocalDate.now(), 90, 95, centro);
        centro.getCalificaciones().add(cal);
        when(centroDeSaludRepository.findById(1L)).thenReturn(Optional.of(centro));

        // When
        boolean resultado = centroDeSaludService.obtenerEstadoAprobacion(1L);

        // Then
        assertTrue(resultado);
        verify(centroDeSaludRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerEstadoAprobacion_centroRechazado_debeRetornarFalse() {
        // Given
        Calificacion cal = new Calificacion(LocalDate.now(), 60, 65, centro);
        centro.getCalificaciones().add(cal);
        when(centroDeSaludRepository.findById(1L)).thenReturn(Optional.of(centro));

        // When
        boolean resultado = centroDeSaludService.obtenerEstadoAprobacion(1L);

        // Then
        assertFalse(resultado);
        verify(centroDeSaludRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerEstadoAprobacion_conCentroInexistente_debeLanzarExcepcion() {
        // Given
        when(centroDeSaludRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            centroDeSaludService.obtenerEstadoAprobacion(999L);
        });
        verify(centroDeSaludRepository, times(1)).findById(999L);
    }

    @Test
    void listarCentrosConCalificaciones_debeRetornarCentrosConCalificaciones() {
        // Given
        Calificacion cal = new Calificacion(LocalDate.now(), 85, 90, centro);
        centro.getCalificaciones().add(cal);
        when(centroDeSaludRepository.findCentrosConCalificaciones()).thenReturn(Arrays.asList(centro));

        // When
        List<CentroDeSalud> resultado = centroDeSaludService.listarCentrosConCalificaciones();

        // Then
        assertEquals(1, resultado.size());
        verify(centroDeSaludRepository, times(1)).findCentrosConCalificaciones();
    }

    @Test
    void actualizarNombre_conCentroExistente_debeActualizarNombre() {
        // Given
        when(centroDeSaludRepository.findById(1L)).thenReturn(Optional.of(centro));
        when(centroDeSaludRepository.save(any(CentroDeSalud.class))).thenReturn(centro);

        // When
        CentroDeSalud resultado = centroDeSaludService.actualizarNombre(1L, "Nuevo Nombre");

        // Then
        assertEquals("Nuevo Nombre", resultado.getNombre());
        verify(centroDeSaludRepository, times(1)).findById(1L);
        verify(centroDeSaludRepository, times(1)).save(centro);
    }

    @Test
    void actualizarNombre_conCentroInexistente_debeLanzarExcepcion() {
        // Given
        when(centroDeSaludRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            centroDeSaludService.actualizarNombre(999L, "Nuevo Nombre");
        });
        verify(centroDeSaludRepository, times(1)).findById(999L);
        verify(centroDeSaludRepository, never()).save(any());
    }
}