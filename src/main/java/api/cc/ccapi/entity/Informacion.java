package api.cc.ccapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "informacion")
public class Informacion {
    @JsonIgnore
    @Id
    @Column(name = "bin", nullable = false, length = 6)
    private String bin;

    @Column(name = "banco", nullable = false, length = 100)
    private String banco;

    @Lob
    @Column(name = "pais", nullable = false)
    private String pais;

    @Lob
    @Column(name = "metodopago", nullable = false)
    private String metodopago;

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getMetodopago() {
        return metodopago;
    }

    public void setMetodopago(String metodopago) {
        this.metodopago = metodopago;
    }

}