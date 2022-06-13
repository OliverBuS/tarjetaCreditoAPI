package api.cc.ccapi.entity;

import java.math.BigDecimal;

public class Transaccion {

    private Tarjetascredito origen;
    private String destino;
    private BigDecimal cantidad;

    public Tarjetascredito getOrigen() {
        return origen;
    }

    public void setOrigen(Tarjetascredito origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
}
