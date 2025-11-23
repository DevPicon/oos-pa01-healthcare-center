package pe.edu.upc.grupo1.centrosdesalud.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.grupo1.centrosdesalud.entity.Ambulancia;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;
import pe.edu.upc.grupo1.centrosdesalud.entity.Region;
import pe.edu.upc.grupo1.centrosdesalud.entity.TipoCentro;
import pe.edu.upc.grupo1.centrosdesalud.repository.AmbulanciaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmbulanciaServiceTest {

    @Mock
    private AmbulanciaRepository ambulanciaRepository;

    @InjectMocks
    private AmbulanciaService ambulanciaService;

    private CentroDeSalud centro;
    private Ambulancia ambulancia;

    @BeforeEach
    void setUp() {
        Region lima = new Region("Lima");
        TipoCentro hospital = new TipoCentro("Hospital");
        centro = new CentroDeSalud("Hospital Test", hospital, lima);
        centro.setId(1L);
        ambulancia = new Ambulancia("ABC-123", "Toyota", centro);
        ambulancia.setId(1L);
    }

    @Test
    void listarTodas_debeRetornarListaDeAmbulancias() {
        // Given
        Ambulancia amb2 = new Ambulancia("DEF-456", "Mercedes", centro);
        when(ambulanciaRepository.findAll()).thenReturn(Arrays.asList(ambulancia, amb2));

        // When
        List<Ambulancia> resultado = ambulanciaService.listarTodas();

        // Then
        assertEquals(2, resultado.size());
        verify(ambulanciaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_conIdExistente_debeRetornarAmbulancia() {
        // Given
        when(ambulanciaRepository.findById(1L)).thenReturn(Optional.of(ambulancia));

        // When
        Optional<Ambulancia> resultado = ambulanciaService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("ABC-123", resultado.get().getPlaca());
        verify(ambulanciaRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_conIdInexistente_debeRetornarVacio() {
        // Given
        when(ambulanciaRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Ambulancia> resultado = ambulanciaService.buscarPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(ambulanciaRepository, times(1)).findById(999L);
    }

    @Test
    void guardar_debeRetornarAmbulanciaGuardada() {
        // Given
        when(ambulanciaRepository.save(ambulancia)).thenReturn(ambulancia);

        // When
        Ambulancia resultado = ambulanciaService.guardar(ambulancia);

        // Then
        assertNotNull(resultado);
        assertEquals("ABC-123", resultado.getPlaca());
        assertEquals("Toyota", resultado.getModelo());
        verify(ambulanciaRepository, times(1)).save(ambulancia);
    }

    @Test
    void eliminar_debeInvocarRepositoryDelete() {
        // When
        ambulanciaService.eliminar(1L);

        // Then
        verify(ambulanciaRepository, times(1)).deleteById(1L);
    }
}
