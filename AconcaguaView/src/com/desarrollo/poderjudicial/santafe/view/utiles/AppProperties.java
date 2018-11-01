package com.desarrollo.poderjudicial.santafe.view.utiles;

import java.io.IOException;
import java.util.Properties;

/**
 * Clase utilitaria para levantar Archivo App.properties
 */
public class AppProperties {
    
    private Properties props;
    private static AppProperties appProperties;
    
    // Constructor privado
    private AppProperties() {        
        
        props = new Properties();
        try {
            // cargo el archivo properties 
            props.load(this.getClass().getClassLoader().getResourceAsStream("App.properties"));
        } catch (IOException e) {
            System.out.println("---- Error en clase AppProperties -----");
            e.printStackTrace();
        }
    }
    
    public static AppProperties getInstance(){
        if(appProperties == null){
            appProperties = new AppProperties();
        }
        return appProperties;
    }
    
    /**
     * Obtiene el valor para la clave obtenida por parámetro.
     * @param nombrePropieadad
     * @return String (value)
     */
    public String getPropiedad(String nombrePropieadad){
        if(this.props.containsKey(nombrePropieadad)){
            return this.props.getProperty(nombrePropieadad);
        }
        System.out.println("---- Propiedad no encontrada en archivo App.properties. Clave: " + nombrePropieadad + " -----");
        return "";
    }

}
