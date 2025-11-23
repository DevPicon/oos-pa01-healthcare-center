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
class CentroDeSaludRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CentroDeSaludRepository centroDeSaludRepository;

    private Region lima;
    private Region arequipa;
    private TipoCentro hospital;
    private TipoCentro clinica;

    @BeforeEach
    void setUp() {
        // Crear datos base
        lima = new Region("Lima");
        arequipa = new Region("Arequipa");
        hospital = new TipoCentro("Hospital");
        clinica = new TipoCentro("Clinica");

        entityManager.persist(lima);
        entityManager.persist(arequipa);
        entityManager.persist(hospital);
        entityManager.persist(clinica);
        entityManager.flush();
    }

    @Test
    void findByTipo_Nombre_debeEncontrarCentrosPorTipo() {
        // Given
        CentroDeSalud centro1 = new CentroDeSalud("Hospital Rebagliati", hospital, lima);
        CentroDeSalud centro2 = new CentroDeSalud("Hospital Regional", hospital, arequipa);
        CentroDeSalud centro3 = new CentroDeSalud("Clinica San Pablo", clinica, lima);

        entityManager.persist(centro1);
        entityManager.persist(centro2);
        entityManager.persist(centro3);
        entityManager.flush();

        // When
        List<CentroDeSalud> hospitales = centroDeSaludRepository.findByTipo_Nombre("Hospital");

        // Then
        assertEquals(2, hospitales.size());
        assertTrue(hospitales.stream().allMatch(c -> c.getTipo().getNombre().equals("Hospital")));
    }

    @Test
    void findByTipo_Nombre_conTipoInexistente_debeRetornarListaVacia() {
        // Given
        CentroDeSalud centro1 = new CentroDeSalud("Hospital Test", hospital, lima);
        entityManager.persist(centro1);
        entityManager.flush();

        // When
        List<CentroDeSalud> centros = centroDeSaludRepository.findByTipo_Nombre("Otro");

        // Then
        assertTrue(centros.isEmpty());
    }

    @Test
    void findCentrosConCalificaciones_debeEncontrarSoloCentrosConCalificaciones() {
        // Given
        CentroDeSalud centroConCal = new CentroDeSalud("Hospital Con Calificacion", hospital, lima);
        CentroDeSalud centroSinCal = new CentroDeSalud("Hospital Sin Calificacion", hospital, arequipa);

        entityManager.persist(centroConCal);
        entityManager.persist(centroSinCal);
        entityManager.flush();

        // Agregar calificacion solo al primer centro
        Calificacion cal = new Calificacion(LocalDate.now(), 85, 90, centroConCal);
        entityManager.persist(cal);
        entityManager.flush();

        // When
        List<CentroDeSalud> centros = centroDeSaludRepository.findCentrosConCalificaciones();

        // Then
        assertEquals(1, centros.size());
        assertEquals("Hospital Con Calificacion", centros.get(0).getNombre());
    }

    @Test
    void findCentrosConCalificaciones_sinCentrosConCalificaciones_debeRetornarListaVacia() {
        // Given
        CentroDeSalud centro1 = new CentroDeSalud("Hospital 1", hospital, lima);
        CentroDeSalud centro2 = new CentroDeSalud("Hospital 2", hospital, arequipa);

        entityManager.persist(centro1);
        entityManager.persist(centro2);
        entityManager.flush();

        // When
        List<CentroDeSalud> centros = centroDeSaludRepository.findCentrosConCalificaciones();

        // Then
        assertTrue(centros.isEmpty());
    }

    @Test
    void findCentrosConCalificaciones_conVariasCalificaciones_noDebeRetornarDuplicados() {
        // Given
        CentroDeSalud centro = new CentroDeSalud("Hospital Multiple", hospital, lima);
        entityManager.persist(centro);
        entityManager.flush();

        // Agregar multiples calificaciones al mismo centro
        Calificacion cal1 = new Calificacion(LocalDate.now().minusDays(10), 80, 85, centro);
        Calificacion cal2 = new Calificacion(LocalDate.now().minusDays(5), 85, 90, centro);
        Calificacion cal3 = new Calificacion(LocalDate.now(), 90, 95, centro);

        entityManager.persist(cal1);
        entityManager.persist(cal2);
        entityManager.persist(cal3);
        entityManager.flush();

        // When
        List<CentroDeSalud> centros = centroDeSaludRepository.findCentrosConCalificaciones();

        // Then
        assertEquals(1, centros.size());
        assertEquals("Hospital Multiple", centros.get(0).getNombre());
    }
}