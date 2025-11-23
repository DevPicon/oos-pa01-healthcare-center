package pe.edu.upc.grupo1.centrosdesalud.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pe.edu.upc.grupo1.centrosdesalud.entity.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CalificacionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CalificacionRepository calificacionRepository;

    private CentroDeSalud centro;

    @BeforeEach
    void setUp() {
        Region lima = new Region("Lima");
        TipoCentro hospital = new TipoCentro("Hospital");
        centro = new CentroDeSalud("Hospital Test", hospital, lima);

        entityManager.persist(lima);
        entityManager.persist(hospital);
        entityManager.persist(centro);
        entityManager.flush();
    }

    @Test
    void findByCentroDeSaludOrderByFechaDesc_debeRetornarCalificacionesOrdenadas() {
        // Given
        Calificacion cal1 = new Calificacion(LocalDate.now().minusDays(10), 80, 85, centro);
        Calificacion cal2 = new Calificacion(LocalDate.now().minusDays(5), 85, 90, centro);
        Calificacion cal3 = new Calificacion(LocalDate.now(), 90, 95, centro);

        entityManager.persist(cal1);
        entityManager.persist(cal2);
        entityManager.persist(cal3);
        entityManager.flush();

        // When
        List<Calificacion> calificaciones = calificacionRepository.findByCentroDeSaludOrderByFechaDesc(centro);

        // Then
        assertEquals(3, calificaciones.size());
        // Verificar que estan ordenadas de mas reciente a mas antigua
        assertTrue(calificaciones.get(0).getFecha().isAfter(calificaciones.get(1).getFecha()));
        assertTrue(calificaciones.get(1).getFecha().isAfter(calificaciones.get(2).getFecha()));
    }

    @Test
    void findByCentroDeSaludOrderByFechaDesc_sinCalificaciones_debeRetornarListaVacia() {
        // When
        List<Calificacion> calificaciones = calificacionRepository.findByCentroDeSaludOrderByFechaDesc(centro);

        // Then
        assertTrue(calificaciones.isEmpty());
    }

    @Test
    void findTop1ByCentroDeSaludOrderByFechaDesc_debeRetornarLaMasReciente() {
        // Given
        Calificacion cal1 = new Calificacion(LocalDate.now().minusDays(10), 80, 85, centro);
        Calificacion cal2 = new Calificacion(LocalDate.now().minusDays(5), 85, 90, centro);
        Calificacion cal3 = new Calificacion(LocalDate.now(), 90, 95, centro);

        entityManager.persist(cal1);
        entityManager.persist(cal2);
        entityManager.persist(cal3);
        entityManager.flush();

        // When
        Calificacion ultima = calificacionRepository.findTop1ByCentroDeSaludOrderByFechaDesc(centro);

        // Then
        assertNotNull(ultima);
        assertEquals(LocalDate.now(), ultima.getFecha());
        assertEquals(90, ultima.getInfraestructura());
        assertEquals(95, ultima.getServicios());
    }

    @Test
    void findTop1ByCentroDeSaludOrderByFechaDesc_sinCalificaciones_debeRetornarNull() {
        // When
        Calificacion ultima = calificacionRepository.findTop1ByCentroDeSaludOrderByFechaDesc(centro);

        // Then
        assertNull(ultima);
    }

    @Test
    void findTop1ByCentroDeSaludOrderByFechaDesc_conUnaCalificacion_debeRetornarEsa() {
        // Given
        Calificacion cal = new Calificacion(LocalDate.now().minusDays(5), 85, 90, centro);
        entityManager.persist(cal);
        entityManager.flush();

        // When
        Calificacion ultima = calificacionRepository.findTop1ByCentroDeSaludOrderByFechaDesc(centro);

        // Then
        assertNotNull(ultima);
        assertEquals(cal.getFecha(), ultima.getFecha());
        assertEquals(88.25, ultima.getCalificacionFinal(), 0.01);
    }
}