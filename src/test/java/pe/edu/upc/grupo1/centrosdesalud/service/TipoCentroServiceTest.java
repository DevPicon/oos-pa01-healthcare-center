package pe.edu.upc.grupo1.centrosdesalud.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.grupo1.centrosdesalud.entity.TipoCentro;
import pe.edu.upc.grupo1.centrosdesalud.repository.TipoCentroRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoCentroServiceTest {

    @Mock
    private TipoCentroRepository tipoCentroRepository;

    @InjectMocks
    private TipoCentroService tipoCentroService;

    private TipoCentro hospital;
    private TipoCentro clinica;

    @BeforeEach
    void setUp() {
        hospital = new TipoCentro("Hospital");
        hospital.setId(1L);
        clinica = new TipoCentro("Clinica");
        clinica.setId(2L);
    }

    @Test
    void listarTodos_debeRetornarListaDeTipos() {
        // Given
        when(tipoCentroRepository.findAll()).thenReturn(Arrays.asList(hospital, clinica));

        // When
        List<TipoCentro> resultado = tipoCentroService.listarTodos();

        // Then
        assertEquals(2, resultado.size());
        verify(tipoCentroRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_conIdExistente_debeRetornarTipo() {
        // Given
        when(tipoCentroRepository.findById(1L)).thenReturn(Optional.of(hospital));

        // When
        Optional<TipoCentro> resultado = tipoCentroService.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Hospital", resultado.get().getNombre());
        verify(tipoCentroRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_conIdInexistente_debeRetornarVacio() {
        // Given
        when(tipoCentroRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<TipoCentro> resultado = tipoCentroService.buscarPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(tipoCentroRepository, times(1)).findById(999L);
    }

    @Test
    void guardar_debeRetornarTipoGuardado() {
        // Given
        when(tipoCentroRepository.save(hospital)).thenReturn(hospital);

        // When
        TipoCentro resultado = tipoCentroService.guardar(hospital);

        // Then
        assertNotNull(resultado);
        assertEquals("Hospital", resultado.getNombre());
        verify(tipoCentroRepository, times(1)).save(hospital);
    }

    @Test
    void eliminar_debeInvocarRepositoryDelete() {
        // When
        tipoCentroService.eliminar(1L);

        // Then
        verify(tipoCentroRepository, times(1)).deleteById(1L);
    }
}
