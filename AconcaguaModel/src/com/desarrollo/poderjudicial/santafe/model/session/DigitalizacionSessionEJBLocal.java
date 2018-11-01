package com.desarrollo.poderjudicial.santafe.model.session;

import com.desarrollo.poderjudicial.santafe.model.entidades.Daper;
import com.desarrollo.poderjudicial.santafe.model.entidades.Multimedia;
import com.desarrollo.poderjudicial.santafe.model.entidades.MultimediaPorPersona;
import com.desarrollo.poderjudicial.santafe.model.entidades.TipoExtension;
import com.desarrollo.poderjudicial.santafe.model.entidades.TipoMultimedia;

import java.math.BigDecimal;

import java.util.List;

import javax.ejb.Local;

@Local
public interface DigitalizacionSessionEJBLocal {
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    <T> T persistEntity(T entity);

    <T> T mergeEntity(T entity);

    List<Multimedia> getMultimediaFindAll();

    List<TipoExtension> getTipoExtensionFindAll();

    List<Daper> getDaperFindAll();

    List<TipoMultimedia> getTipoMultimediaFindAll();

    List<Multimedia> obtenerMultimediaByDni(Long nroDoc, Long tipoDoc, Integer tipoMultimedia);

    void removeMultimedia(Multimedia multimedia);

    Boolean persistMultimedia(List<Multimedia> multimedias);

    Integer obtenerProxNroSequencia(String nombreSequencia);
    
    List<MultimediaPorPersona> obtenerMultimediasPorPersona(Long nroDoc, Long tipoDoc);
    
    List<BigDecimal> obtenerTiposCursos();
    
    List<BigDecimal> obtenerTiposInstitutos();
}
