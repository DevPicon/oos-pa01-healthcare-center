package pe.edu.upc.grupo1.centrosdesalud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;

import java.util.List;

@Repository
public interface CentroDeSaludRepository extends JpaRepository<CentroDeSalud, Long> {

    List<CentroDeSalud> findByTipo_Nombre(String nombre);

    @Query("SELECT DISTINCT c FROM CentroDeSalud c WHERE SIZE(c.calificaciones) > 0")
    List<CentroDeSalud> findCentrosConCalificaciones();
}
