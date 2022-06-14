package api.cc.ccapi.entity;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

@Entity
@Table(name = "historial")
public class Historial {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "origen")
    private Long origen;

    @Column(name = "cantidad", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidad;

    @Lob
    @Column(name = "estado", nullable = false)
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

}