package api.cc.ccapi.repository;

import api.cc.ccapi.entity.Tarjetascredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface TarjetasCreditoRepository extends JpaRepository<Tarjetascredito,Long> {
    @Query(nativeQuery = true,
    value = "select count(*) from tarjetascredito where numero=?1 and fechaexpiracion=?2 and cvv=?3")
    public int verificarTarjeta(String numero,String fecha, String cvv);

    public Optional<Tarjetascredito> findByNumero(String numero);
    @Query(nativeQuery = true,
    value = "select fondos from tarjetascredito where numero=?1 and fechaexpiracion=?2 and cvv=?3")
    public BigDecimal saldoDeCuenta(String numero,String fechaexpiracion, String cvv);
}
