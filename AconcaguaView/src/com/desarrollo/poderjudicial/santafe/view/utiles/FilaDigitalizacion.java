package com.desarrollo.poderjudicial.santafe.view.utiles;

/**
 * Clase auxiliar para representar una fila de la grilla de digitalizaciones
 * @author jalarcon
 */
public class FilaDigitalizacion {
     
    private String nombre = null;
    private String path = null;
    private String descripcionGrupo = null;
    private String idGrupo = null;
    /**
     * Constructor
     */
    public FilaDigitalizacion(String nombre, String path, String descripcionGrupo, String idGrupo) {               
        this.nombre = nombre;
        this.path = path;
        this.descripcionGrupo = descripcionGrupo;
        this.idGrupo = idGrupo;
    }
    

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setDescripcionGrupo(String descripcionGrupo) {
        this.descripcionGrupo = descripcionGrupo;
    }

    public String getDescripcionGrupo() {
        return descripcionGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIdGrupo() {
        return idGrupo;
    }
}
