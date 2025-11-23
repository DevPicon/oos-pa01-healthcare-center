package pe.edu.upc.grupo1.centrosdesalud.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CentroDeSaludTest {

    private Region lima;
    private TipoCentro hospital;
    private CentroDeSalud centro;

    @BeforeEach
    void setUp() {
        lima = new Region("Lima");
        hospital = new TipoCentro("Hospital");
        centro = new CentroDeSalud("Hospital Rebagliati", hospital, lima);
    }

    @Test
    void calcularUltimaCalificacion_sinCalificaciones_debeRetornarCero() {
        // When
        double resultado = centro.calcularUltimaCalificacion();

        // Then
        assertEquals(0.0, resultado);
    }

    @Test
    void calcularUltimaCalificacion_conUnaCalificacion_debeRetornarEsaCalificacion() {
        // Given
        Calificacion cal1 = new Calificacion(LocalDate.now().minusDays(5), 85, 90, centro);
        centro.getCalificaciones().add(cal1);

        // When
        double resultado = centro.calcularUltimaCalificacion();

        // Then
        assertEquals(88.25, resultado, 0.01);
    }

    @Test
    void calcularUltimaCalificacion_conVariasCalificaciones_debeRetornarLaMasReciente() {
        // Given
        Calificacion cal1 = new Calificacion(LocalDate.now().minusDays(10), 70, 75, centro);
        Calificacion cal2 = new Calificacion(LocalDate.now().minusDays(5), 85, 90, centro);
        Calificacion cal3 = new Calificacion(LocalDate.now().minusDays(15), 60, 65, centro);

        centro.getCalificaciones().add(cal1);
        centro.getCalificaciones().add(cal2);
        centro.getCalificaciones().add(cal3);

        // When
        double resultado = centro.calcularUltimaCalificacion();

        // Then
        // Debe retornar la calificacion de hace 5 dias (la mas reciente)
        assertEquals(88.25, resultado, 0.01);
    }

    @Test
    void estaAprobado_sinCalificaciones_debeRetornarFalse() {
        // When
        boolean resultado = centro.estaAprobado();

        // Then
        assertFalse(resultado);
    }

    @Test
    void estaAprobado_conCalificacionIgualA80_debeRetornarTrue() {
        // Given
        Calificacion cal = new Calificacion(LocalDate.now(), 80, 80, centro);
        centro.getCalificaciones().add(cal);

        // When
        boolean resultado = centro.estaAprobado();

        // Then
        assertTrue(resultado);
        assertEquals(80.0, centro.calcularUltimaCalificacion(), 0.01);
    }

    @Test
    void estaAprobado_conCalificacionMayorA80_debeRetornarTrue() {
        // Given
        Calificacion cal = new Calificacion(LocalDate.now(), 95, 92, centro);
        centro.getCalificaciones().add(cal);

        // When
        boolean resultado = centro.estaAprobado();

        // Then
        assertTrue(resultado);
    }

    @Test
    void estaAprobado_conCalificacionMenorA80_debeRetornarFalse() {
        // Given
        Calificacion cal = new Calificacion(LocalDate.now(), 70, 75, centro);
        centro.getCalificaciones().add(cal);

        // When
        boolean resultado = centro.estaAprobado();

        // Then
        assertFalse(resultado);
    }

    @Test
    void estaAprobado_conVariasCalificaciones_debeConsiderarSoloLaUltima() {
        // Given
        // Calificacion antigua: rechazada
        Calificacion cal1 = new Calificacion(LocalDate.now().minusDays(10), 60, 65, centro);
        // Calificacion reciente: aprobada
        Calificacion cal2 = new Calificacion(LocalDate.now().minusDays(2), 90, 95, centro);

        centro.getCalificaciones().add(cal1);
        centro.getCalificaciones().add(cal2);

        // When
        boolean resultado = centro.estaAprobado();

        // Then
        assertTrue(resultado);
    }

    @Test
    void estaAprobado_conVariasCalificaciones_ultimaRechazada_debeRetornarFalse() {
        // Given
        // Calificacion antigua: aprobada
        Calificacion cal1 = new Calificacion(LocalDate.now().minusDays(10), 90, 95, centro);
        // Calificacion reciente: rechazada
        Calificacion cal2 = new Calificacion(LocalDate.now().minusDays(2), 60, 65, centro);

        centro.getCalificaciones().add(cal1);
        centro.getCalificaciones().add(cal2);

        // When
        boolean resultado = centro.estaAprobado();

        // Then
        assertFalse(resultado);
    }
}