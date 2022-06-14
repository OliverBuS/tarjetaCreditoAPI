package api.cc.ccapi.entity;

import java.math.BigDecimal;

public class Transaccion {


    private Long idcompra;

    private Tarjetascredito origen;
    private BigDecimal cantidad;

    public Tarjetascredito getOrigen() {
        return origen;
    }

    public void setOrigen(Tarjetascredito origen) {
        this.origen = origen;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Long getIdcompra() {
        return idcompra;
    }

    public void setIdcompra(Long idcompra) {
        this.idcompra = idcompra;
    }
}
