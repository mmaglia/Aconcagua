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
@NamedQueries({ @NamedQuery(name = "TipoMultimedia.findAll", query = "select o from TipoMultimedia o order by o.descripcion asc") })
@Table(name = "TIPO_MULTIMEDIA")
public class TipoMultimedia implements Serializable {
    private static final long serialVersionUID = -3573309804561177421L;
    @Column(length = 50)
    private String descripcion;
    @Id
    @Column(name = "ID_TIPO_MULTIMEDIA", nullable = false)
    private Integer idTipoMultimedia;
    @OneToMany(mappedBy = "tipoMultimedia", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Multimedia> multimediaList;

    public TipoMultimedia() {
    }

    public TipoMultimedia(String descripcion, Integer idTipoMultimedia) {
        this.descripcion = descripcion;
        this.idTipoMultimedia = idTipoMultimedia;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdTipoMultimedia() {
        return idTipoMultimedia;
    }

    public void setIdTipoMultimedia(Integer idTipoMultimedia) {
        this.idTipoMultimedia = idTipoMultimedia;
    }

    public List<Multimedia> getMultimediaList() {
        return multimediaList;
    }

    public void setMultimediaList(List<Multimedia> multimediaList) {
        this.multimediaList = multimediaList;
    }

    public Multimedia addMultimedia(Multimedia multimedia) {
        getMultimediaList().add(multimedia);
        multimedia.setTipoMultimedia(this);
        return multimedia;
    }

    public Multimedia removeMultimedia(Multimedia multimedia) {
        getMultimediaList().remove(multimedia);
        multimedia.setTipoMultimedia(null);
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
        buffer.append("idTipoMultimedia=");
        buffer.append(getIdTipoMultimedia());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
