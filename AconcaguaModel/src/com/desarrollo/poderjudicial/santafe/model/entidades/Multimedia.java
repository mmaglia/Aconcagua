package com.desarrollo.poderjudicial.santafe.model.entidades;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "Multimedia.findAll", query = "select o from Multimedia o"),
                @NamedQuery(name = "Multimedia.findByDni",
                            query = "select o from Multimedia o where o.nroDoc = :nroDoc and o.tipoDoc = :tipoDoc order by o.idMultimedia desc"),
                @NamedQuery(name = "Multimedia.findByDniAndMultimedia",
                            query =
                            "select o from Multimedia o where o.nroDoc = :nroDoc and o.tipoDoc = :tipoDoc and o.tipoMultimedia.idTipoMultimedia = :tipoMultimedia order by o.idMultimedia desc")
    })
public class Multimedia implements Serializable {
    private static final long serialVersionUID = -4779491495491540423L;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private Integer fojas;
    @Id
    @Column(name = "ID_MULTIMEDIA", nullable = false)
    private Long idMultimedia;
    @Column(name = "ID_SEDE")
    private Integer idSede;
    @Column(name = "NOMBRE_ARCHIVO", length = 100)
    private String nombreArchivo;
    @Column(name = "NRO_DOC")
    private Long nroDoc;
    private Integer orden;
    @Column(name = "TIPO_DOC")
    private Long tipoDoc;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_MULTIMEDIA")
    private TipoMultimedia tipoMultimedia;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_EXTENSION")
    private TipoExtension tipoExtension;
    @Column(name = "DESCRIPCION", length = 100)
    private String descripcion;
    @Column(name = "USUARIO_APP", length = 50)
    private String usuarioApp;

    public Multimedia() {
    }

    public Multimedia(Date fecha, Integer fojas, Long idMultimedia, Integer idSede, TipoExtension tipoExtension,
                      TipoMultimedia tipoMultimedia, String nombreArchivo, Long nroDoc, Integer orden, Long tipoDoc, String usuarioApp) {
        this.fecha = fecha;
        this.fojas = fojas;
        this.idMultimedia = idMultimedia;
        this.idSede = idSede;
        this.tipoExtension = tipoExtension;
        this.tipoMultimedia = tipoMultimedia;
        this.nombreArchivo = nombreArchivo;
        this.nroDoc = nroDoc;
        this.orden = orden;
        this.tipoDoc = tipoDoc;
        this.usuarioApp = usuarioApp;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getFojas() {
        return fojas;
    }

    public void setFojas(Integer fojas) {
        this.fojas = fojas;
    }

    public Long getIdMultimedia() {
        return idMultimedia;
    }

    public void setIdMultimedia(Long idMultimedia) {
        this.idMultimedia = idMultimedia;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }


    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Long getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(Long nroDoc) {
        this.nroDoc = nroDoc;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Long getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(Long tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public TipoMultimedia getTipoMultimedia() {
        return tipoMultimedia;
    }

    public void setTipoMultimedia(TipoMultimedia tipoMultimedia) {
        this.tipoMultimedia = tipoMultimedia;
    }

    public TipoExtension getTipoExtension() {
        return tipoExtension;
    }

    public void setTipoExtension(TipoExtension tipoExtension) {
        this.tipoExtension = tipoExtension;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setUsuarioApp(String usuarioApp) {
        this.usuarioApp = usuarioApp;
    }

    public String getUsuarioApp() {
        return usuarioApp;
    }
}
