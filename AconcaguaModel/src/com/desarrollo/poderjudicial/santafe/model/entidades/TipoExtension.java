package com.desarrollo.poderjudicial.santafe.model.entidades;

import java.io.Serializable;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "TipoExtension.findAll", query = "select o from TipoExtension o") })
@Table(name = "TIPO_EXTENSION")
public class TipoExtension implements Serializable {
    private static final long serialVersionUID = 3390814039413608401L;
    @Column(length = 10)
    private String descripcion;
    @Id
    @Column(name = "ID_TIPO_EXTENSION", nullable = false)
    private Integer idTipoExtension;
    @OneToMany(mappedBy = "tipoExtension", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Multimedia> multimediaList1;

    public TipoExtension() {
    }

    public TipoExtension(String descripcion, Integer idTipoExtension) {
        this.descripcion = descripcion;
        this.idTipoExtension = idTipoExtension;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdTipoExtension() {
        return idTipoExtension;
    }

    public void setIdTipoExtension(Integer idTipoExtension) {
        this.idTipoExtension = idTipoExtension;
    }

    public List<Multimedia> getMultimediaList1() {
        return multimediaList1;
    }

    public void setMultimediaList1(List<Multimedia> multimediaList1) {
        this.multimediaList1 = multimediaList1;
    }

    public Multimedia addMultimedia(Multimedia multimedia) {
        getMultimediaList1().add(multimedia);
        multimedia.setTipoExtension(this);
        return multimedia;
    }

    public Multimedia removeMultimedia(Multimedia multimedia) {
        getMultimediaList1().remove(multimedia);
        multimedia.setTipoExtension(null);
        return multimedia;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName() + "@" + Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("descripcion=");
        buffer.append(getDescripcion());
        buffer.append(',');
        buffer.append("idTipoExtension=");
        buffer.append(getIdTipoExtension());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
