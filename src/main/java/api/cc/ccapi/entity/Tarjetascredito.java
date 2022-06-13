package api.cc.ccapi.entity;

import javax.persistence.*;

@Entity
@Table(name = "tarjetascredito")
public class Tarjetascredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "numero", nullable = false, length = 16)
    private String numero;

    @Column(name = "fechaexpiracion", nullable = false, length = 7)
    private String fechaexpiracion;

    @Column(name = "cvv", nullable = false, length = 4)
    private String cvv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFechaexpiracion() {
        return fechaexpiracion;
    }

    public void setFechaexpiracion(String fechaexpiracion) {
        this.fechaexpiracion = fechaexpiracion;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

}