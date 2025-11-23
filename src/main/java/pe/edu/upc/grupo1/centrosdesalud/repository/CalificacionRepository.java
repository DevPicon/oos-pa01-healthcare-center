package pe.edu.upc.grupo1.centrosdesalud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.grupo1.centrosdesalud.entity.Calificacion;
import pe.edu.upc.grupo1.centrosdesalud.entity.CentroDeSalud;

import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    List<Calificacion> findByCentroDeSaludOrderByFechaDesc(CentroDeSalud centro);

    Calificacion findTop1ByCentroDeSaludOrderByFechaDesc(CentroDeSalud centro);
}