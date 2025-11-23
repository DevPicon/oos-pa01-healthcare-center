package pe.edu.upc.grupo1.centrosdesalud.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.grupo1.centrosdesalud.entity.Region;
import pe.edu.upc.grupo1.centrosdesalud.repository.RegionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    private Region lima;
    private Region arequipa;

    @BeforeEach
    void setUp() {
        lima = new Region("Lima");
        lima.setId(1L);
        arequipa = new Region("Arequipa");
        arequipa.setId(2L);
    }

    @Test
    void listarTodas_debeRetornarListaDeRegiones() {
        // Given
        when(regionRepository.findAll()).thenReturn(Arrays.asList(lima, arequipa));

        // When
        List<Region> resultado = regionService.listarTodas();

        // Then
        assertEquals(2, resultado.size());
        verify(regionRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_conIdExistente_debeRetornarRegion() {
        // Given
        when(regionRepository.findById(1L)).thenReturn(Optional.of(lima));

        // When
        Optional<Region> resultado = regionService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Lima", resultado.get().getNombre());
        verify(regionRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_conIdInexistente_debeRetornarVacio() {
        // Given
        when(regionRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Region> resultado = regionService.buscarPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(regionRepository, times(1)).findById(999L);
    }

    @Test
    void guardar_debeRetornarRegionGuardada() {
        // Given
        when(regionRepository.save(lima)).thenReturn(lima);

        // When
        Region resultado = regionService.guardar(lima);

        // Then
        assertNotNull(resultado);
        assertEquals("Lima", resultado.getNombre());
        verify(regionRepository, times(1)).save(lima);
    }

    @Test
    void eliminar_debeInvocarRepositoryDelete() {
        // When
        regionService.eliminar(1L);

        // Then
        verify(regionRepository, times(1)).deleteById(1L);
    }
}
