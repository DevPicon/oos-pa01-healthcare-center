package pe.edu.upc.grupo1.centrosdesalud.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CalificacionTest {

    @Test
    void calcularCalificacion_debeRetornarFormulaCorrecta() {
        // Given
        Region region = new Region("Lima");
        TipoCentro tipo = new TipoCentro("Hospital");
        CentroDeSalud centro = new CentroDeSalud("Hospital Test", tipo, region);

        Calificacion calificacion = new Calificacion(
            LocalDate.now(),
            85,  // infraestructura
            90,  // servicios
            centro
        );

        // When
        double resultado = calificacion.calcularCalificacion();

        // Then
        // Formula: (85 * 0.35) + (90 * 0.65) = 29.75 + 58.5 = 88.25
        assertEquals(88.25, resultado, 0.01);
    }

    @Test
    void constructor_debeCalcularCalificacionFinalAutomaticamente() {
        // Given
        Region region = new Region("Lima");
        TipoCentro tipo = new TipoCentro("Hospital");
        CentroDeSalud centro = new CentroDeSalud("Hospital Test", tipo, region);

        // When
        Calificacion calificacion = new Calificacion(
            LocalDate.now(),
            80,
            85,
            centro
        );

        // Then
        // Formula: (80 * 0.35) + (85 * 0.65) = 28 + 55.25 = 83.25
        assertEquals(83.25, calificacion.getCalificacionFinal(), 0.01);
    }

    @Test
    void constructor_debeAsignarEstadoAPROBADO_cuandoCalificacionMayorOIgualA80() {
        // Given
        Region region = new Region("Lima");
        TipoCentro tipo = new TipoCentro("Hospital");
        CentroDeSalud centro = new CentroDeSalud("Hospital Test", tipo, region);

        // When
        Calificacion calificacion = new Calificacion(
            LocalDate.now(),
            80,
            80,
            centro
        );

        // Then
        assertEquals(80.0, calificacion.getCalificacionFinal(), 0.01);
        assertEquals(Estado.APROBADO, calificacion.getEstado());
    }

    @Test
    void constructor_debeAsignarEstadoRECHAZADO_cuandoCalificacionMenorA80() {
        // Given
        Region region = new Region("Lima");
        TipoCentro tipo = new TipoCentro("Clinica");
        CentroDeSalud centro = new CentroDeSalud("Clinica Test", tipo, region);

        // When
        Calificacion calificacion = new Calificacion(
            LocalDate.now(),
            70,
            75,
            centro
        );

        // Then
        // Formula: (70 * 0.35) + (75 * 0.65) = 24.5 + 48.75 = 73.25
        assertEquals(73.25, calificacion.getCalificacionFinal(), 0.01);
        assertEquals(Estado.RECHAZADO, calificacion.getEstado());
    }

    @Test
    void calcularCalificacion_conValoresBajos_debeRetornarResultadoCorrecto() {
        // Given
        Region region = new Region("Arequipa");
        TipoCentro tipo = new TipoCentro("Hospital");
        CentroDeSalud centro = new CentroDeSalud("Hospital Regional", tipo, region);

        Calificacion calificacion = new Calificacion(
            LocalDate.now(),
            1,   // minimo infraestructura
            1,   // minimo servicios
            centro
        );

        // When
        double resultado = calificacion.calcularCalificacion();

        // Then
        // Formula: (1 * 0.35) + (1 * 0.65) = 0.35 + 0.65 = 1.0
        assertEquals(1.0, resultado, 0.01);
    }

    @Test
    void calcularCalificacion_conValoresAltos_debeRetornarResultadoCorrecto() {
        // Given
        Region region = new Region("Cusco");
        TipoCentro tipo = new TipoCentro("Hospital");
        CentroDeSalud centro = new CentroDeSalud("Hospital Cusco", tipo, region);

        Calificacion calificacion = new Calificacion(
            LocalDate.now(),
            100,  // maximo infraestructura
            100,  // maximo servicios
            centro
        );

        // When
        double resultado = calificacion.calcularCalificacion();

        // Then
        // Formula: (100 * 0.35) + (100 * 0.65) = 35 + 65 = 100.0
        assertEquals(100.0, resultado, 0.01);
    }
}