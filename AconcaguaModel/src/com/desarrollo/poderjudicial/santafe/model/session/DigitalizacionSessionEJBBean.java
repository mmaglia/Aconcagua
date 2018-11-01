package com.desarrollo.poderjudicial.santafe.model.session;

import com.desarrollo.poderjudicial.santafe.model.entidades.Daper;
import com.desarrollo.poderjudicial.santafe.model.entidades.Multimedia;

import com.desarrollo.poderjudicial.santafe.model.entidades.MultimediaPorPersona;
import com.desarrollo.poderjudicial.santafe.model.entidades.TipoExtension;

import com.desarrollo.poderjudicial.santafe.model.entidades.TipoMultimedia;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "DigitalizacionSessionEJB", mappedName = "Aconcagua-AconcaguaModel-DigitalizacionSessionEJB")
public class DigitalizacionSessionEJBBean implements DigitalizacionSessionEJB, DigitalizacionSessionEJBLocal {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "AconcaguaModel")
    private EntityManager em;
    private Long nroDocumento;
    private Long tipoDocumento;

    public DigitalizacionSessionEJBBean() {
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    public <T> T persistEntity(T entity) {
        em.persist(entity);
        return entity;
    }

    public <T> T mergeEntity(T entity) {
        return em.merge(entity);
    }

    /** <code>select o from Multimedia o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Multimedia> getMultimediaFindAll() {
        return em.createNamedQuery("Multimedia.findAll", Multimedia.class).getResultList();
    }

    /** <code>select o from TipoExtension o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoExtension> getTipoExtensionFindAll() {
        return em.createNamedQuery("TipoExtension.findAll", TipoExtension.class).getResultList();
    }

    /** <code>select o from Daper o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Daper> getDaperFindAll() {
        return em.createNamedQuery("Daper.findAll", Daper.class).getResultList();
    }

    /** <code>select o from TipoMultimedia o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoMultimedia> getTipoMultimediaFindAll() {
        return em.createNamedQuery("TipoMultimedia.findAll", TipoMultimedia.class).getResultList();
    }

    /* Guarda los objetos Multimedias en la Base de Datos
     * @param multimedias
     * @return
     */
    public Boolean persistMultimedia(List<Multimedia> multimedias) {
        Boolean retorno = true;

        try {
            for (Multimedia elem : multimedias) {
                em.persist(elem);
            }
        } catch (Exception ex) {
            retorno = false;
            System.out.println("---- Error en Clase DigitalizacionSessionEJBBean: persistMultimedia ----");
            ex.printStackTrace();
        }

        return retorno;
    }

    /**
     * Retorna el próximo nro de la secuencia pasada por parametro
     * @param nombreSequencia
     * @return
     */
    public Integer obtenerProxNroSequencia(String nombreSequencia) {

        try {
            Query query = em.createNativeQuery("SELECT " + nombreSequencia + ".nextval from DUAL");
            BigDecimal result = (BigDecimal) query.getSingleResult();
            return result.intValue();
        } catch (Exception ex) {
            System.out.println("---- Error en Clase DigitalizacionSessionEJBBean: obtenerProxNroSequencia ----");
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Borra un registro de multimedia
     * @param multimedia
     */
    public void removeMultimedia(Multimedia multimedia) {

        try {
            String usuarioAppBorrado = multimedia.getUsuarioApp();
            
            // Obtengo el objeto a borrar de la base
            multimedia = em.find(Multimedia.class, multimedia.getIdMultimedia());
            multimedia.setUsuarioApp(usuarioAppBorrado);
            
            // Guardo primero el objeto multimedia para que en la auditoria quede seteado el usuario que borra
            em.merge(multimedia);            
            em.flush();
            
            // Una ves guardado procedo a borrarlo  
            em.remove(multimedia);
        } catch (Exception ex) {
            System.out.println("---- Error en Clase DigitalizacionSessionEJBBean: removeMultimedia ----");
            ex.printStackTrace();
        }
    }

    /**
     * Retorna multimedias por DNI de personas
     * @return List<Multimedia>
     */
    public List<Multimedia> obtenerMultimediaByDni(Long nroDoc, Long tipoDoc, Integer tipoMultimedia) {
        List<Multimedia> retorno = new ArrayList<Multimedia>();
        if (tipoMultimedia != null && tipoMultimedia != -1) {
            retorno = em.createNamedQuery("Multimedia.findByDniAndMultimedia", Multimedia.class)
                        .setParameter("nroDoc", nroDoc)
                        .setParameter("tipoDoc", tipoDoc)
                        .setParameter("tipoMultimedia", tipoMultimedia)
                        .getResultList();
        } else {
            retorno = em.createNamedQuery("Multimedia.findByDni", Multimedia.class)
                        .setParameter("nroDoc", nroDoc)
                        .setParameter("tipoDoc", tipoDoc)
                        .getResultList();
        }

        return retorno;
    }

    /**
     * Retorna una lista de MultimediaPorPersona para ListView de seguimiento de personas
     * @param nroDoc
     * @param tipoDoc
     * @return List<MultimediaPorPersona>
     */
    public List<MultimediaPorPersona> obtenerMultimediasPorPersona(Long nroDoc, Long tipoDoc) {
        List<Multimedia> multimedias = new ArrayList<Multimedia>();
        List<MultimediaPorPersona> retorno = new ArrayList<MultimediaPorPersona>();

        multimedias = em.createNamedQuery("Multimedia.findByDni", Multimedia.class)
                        .setParameter("nroDoc", nroDoc)
                        .setParameter("tipoDoc", tipoDoc)
                        .getResultList();

        // Contador de tipos multimedias[Legajo, Título, Carpeta Médica, Foto, Declaración Jurada, Sansiones, Observaciones, Cursos]
        Integer[] contadorTipoMultimedia = new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        MultimediaPorPersona datosPersonales = null, varios = null, carpetaMedica = null, foto =
            null, ddjjIncompatibilidades = null, ddjjGrupoFliar = null, carpetadeFinanzas = null, cursos =
            null, calificaciones = null;

        // Recorro los objetos multimedias obtenidos y ordenados por fecha y voy contando la cantidad de cada uno
        for (Multimedia elemento : multimedias) {

            switch (elemento.getTipoMultimedia()
                            .getIdTipoMultimedia()
                            .intValue()) {
            case 1:
                contadorTipoMultimedia[0]++;
                if (datosPersonales == null) {
                    datosPersonales = new MultimediaPorPersona();
                    datosPersonales.setTipoMultimedia(elemento.getTipoMultimedia());
                    datosPersonales.setUltimaDigitalización(elemento.getFecha());
                    datosPersonales.setRutaIcono("legajo.jpg");
                }
                break;
            case 2:
                contadorTipoMultimedia[1]++;
                if (varios == null) {
                    varios = new MultimediaPorPersona();
                    varios.setTipoMultimedia(elemento.getTipoMultimedia());
                    varios.setUltimaDigitalización(elemento.getFecha());
                    varios.setRutaIcono("cursos.jpg");
                }
                break;
            case 3:
                contadorTipoMultimedia[2]++;
                if (carpetaMedica == null) {
                    carpetaMedica = new MultimediaPorPersona();
                    carpetaMedica.setTipoMultimedia(elemento.getTipoMultimedia());
                    carpetaMedica.setUltimaDigitalización(elemento.getFecha());
                    carpetaMedica.setRutaIcono("carpetaMedica.jpg");
                }
                break;
            case 4:
                contadorTipoMultimedia[3]++;
                if (foto == null) {
                    foto = new MultimediaPorPersona();
                    foto.setTipoMultimedia(elemento.getTipoMultimedia());
                    foto.setUltimaDigitalización(elemento.getFecha());
                    foto.setRutaIcono("foto.jpg");
                }
                break;
            case 5:
                contadorTipoMultimedia[4]++;
                if (ddjjIncompatibilidades == null) {
                    ddjjIncompatibilidades = new MultimediaPorPersona();
                    ddjjIncompatibilidades.setTipoMultimedia(elemento.getTipoMultimedia());
                    ddjjIncompatibilidades.setUltimaDigitalización(elemento.getFecha());
                    ddjjIncompatibilidades.setRutaIcono("sancion.jpg");
                }
                break;
            case 6:
                contadorTipoMultimedia[5]++;
                if (ddjjGrupoFliar == null) {
                    ddjjGrupoFliar = new MultimediaPorPersona();
                    ddjjGrupoFliar.setTipoMultimedia(elemento.getTipoMultimedia());
                    ddjjGrupoFliar.setUltimaDigitalización(elemento.getFecha());
                    ddjjGrupoFliar.setRutaIcono("grupoFamiliar.jpg");
                }
                break;
            case 7:
                contadorTipoMultimedia[6]++;
                if (carpetadeFinanzas == null) {
                    carpetadeFinanzas = new MultimediaPorPersona();
                    carpetadeFinanzas.setTipoMultimedia(elemento.getTipoMultimedia());
                    carpetadeFinanzas.setUltimaDigitalización(elemento.getFecha());
                    carpetadeFinanzas.setRutaIcono("declaracionJurada.jpg");
                }
                break;
            case 8:
                contadorTipoMultimedia[7]++;
                if (cursos == null) {
                    cursos = new MultimediaPorPersona();
                    cursos.setTipoMultimedia(elemento.getTipoMultimedia());
                    cursos.setUltimaDigitalización(elemento.getFecha());
                    cursos.setRutaIcono("titulo.jpg");
                }
                break;
            case 9:
                contadorTipoMultimedia[8]++;
                if (calificaciones == null) {
                    calificaciones = new MultimediaPorPersona();
                    calificaciones.setTipoMultimedia(elemento.getTipoMultimedia());
                    calificaciones.setUltimaDigitalización(elemento.getFecha());
                    calificaciones.setRutaIcono("calificaciones.jpg");
                }
                break;
            }
        }

        // Agrego a la coleccion de retorno los objetos creados
        for (int i = 0; i < contadorTipoMultimedia.length; i++) {
            if (contadorTipoMultimedia[i] > 0) {
                switch (i) {
                case 0:
                    datosPersonales.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(datosPersonales);
                    break;
                case 1:
                    varios.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(varios);
                    break;
                case 2:
                    carpetaMedica.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(carpetaMedica);
                    break;
                case 3:
                    foto.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(foto);
                    break;
                case 4:
                    ddjjIncompatibilidades.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(ddjjIncompatibilidades);
                    break;
                case 5:
                    ddjjGrupoFliar.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(ddjjGrupoFliar);
                    break;
                case 6:
                    carpetadeFinanzas.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(carpetadeFinanzas);
                    break;
                case 7:
                    cursos.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(cursos);
                    break;
                case 8:
                    calificaciones.setCantidadDigitalizados(contadorTipoMultimedia[i]);
                    retorno.add(calificaciones);
                    break;
                }
            }
        }

        return retorno;
    }

    @Override
    public List<BigDecimal> obtenerTiposCursos() {
        @SuppressWarnings("unchecked")
        List<BigDecimal> retorno = em.createNativeQuery("SELECT TPCODI FROM TIPOCURS").getResultList();
        return retorno;
    }

    @Override
    public List<BigDecimal> obtenerTiposInstitutos() {
        @SuppressWarnings("unchecked")
        List<BigDecimal> retorno = em.createNativeQuery("SELECT TOCODI FROM TIPOINST").getResultList();
        return retorno;
    }
}
