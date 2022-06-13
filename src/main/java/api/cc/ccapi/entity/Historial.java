package api.cc.ccapi.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "historial")
public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "origen")
    private Long origen;

    @Column(name = "destino", nullable = false)
    private Long destino;

    @Column(name = "cantidad", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrigen() {
        return origen;
    }

    public void setOrigen(Long origen) {
        this.origen = origen;
    }

    public Long getDestino() {
        return destino;
    }

    public void setDestino(Long destino) {
        this.destino = destino;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

}