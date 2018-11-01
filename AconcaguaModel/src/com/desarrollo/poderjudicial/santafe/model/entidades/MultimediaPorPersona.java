package com.desarrollo.poderjudicial.santafe.model.entidades;

import java.io.Serializable;

import java.util.Date;

/**
 * Clase contenedora de Objetos Multimedias agrupados para una persona
 * @author jalarcon
 */
public class MultimediaPorPersona implements Serializable {
    @SuppressWarnings("compatibility:3351730431002036456")
    private static final long serialVersionUID = 1L;

    private Integer cantidadDigitalizados;
    private TipoMultimedia tipoMultimedia;
    private String rutaIcono;
    private Date ultimaDigitalizaci�n;
    
    // Constructor default
    public MultimediaPorPersona() {
        super();
    }

    /**
     * Constructor
     * @param cantidadDigitalizados
     * @param tipoMultimedia
     * @param rutaIcono
     * @param ultimaDigitalizaci�n
     */
    public MultimediaPorPersona(Integer cantidadDigitalizados, TipoMultimedia tipoMultimedia, String rutaIcono,
                                Date ultimaDigitalizaci�n) {
        this.cantidadDigitalizados = cantidadDigitalizados;
        this.tipoMultimedia = tipoMultimedia;
        this.rutaIcono = rutaIcono;
        this.ultimaDigitalizaci�n = ultimaDigitalizaci�n;
    }

    public void setCantidadDigitalizados(Integer cantidadDigitalizados) {
        this.cantidadDigitalizados = cantidadDigitalizados;
    }

    public Integer getCantidadDigitalizados() {
        return cantidadDigitalizados;
    }

    public void setTipoMultimedia(TipoMultimedia tipoMultimedia) {
        this.tipoMultimedia = tipoMultimedia;
    }

    public TipoMultimedia getTipoMultimedia() {
        return tipoMultimedia;
    }

    public void setRutaIcono(String rutaIcono) {
        this.rutaIcono = rutaIcono;
    }

    public String getRutaIcono() {
        return rutaIcono;
    }

    public void setUltimaDigitalizaci�n(Date ultimaDigitalizaci�n) {
        this.ultimaDigitalizaci�n = ultimaDigitalizaci�n;
    }

    public Date getUltimaDigitalizaci�n() {
        return ultimaDigitalizaci�n;
    }
}
