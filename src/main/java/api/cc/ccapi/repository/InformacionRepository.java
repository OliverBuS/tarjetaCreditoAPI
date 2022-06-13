package api.cc.ccapi.repository;

import api.cc.ccapi.entity.Informacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformacionRepository extends JpaRepository<Informacion,String> {
}
