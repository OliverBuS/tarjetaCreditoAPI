package api.cc.ccapi.repository;

import api.cc.ccapi.entity.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends JpaRepository<Historial,Long> {
}
