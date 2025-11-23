package pe.edu.upc.grupo1.centrosdesalud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.grupo1.centrosdesalud.entity.TipoCentro;

@Repository
public interface TipoCentroRepository extends JpaRepository<TipoCentro, Long> {
}