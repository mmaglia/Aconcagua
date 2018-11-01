package com.desarrollo.poderjudicial.santafe.view.beans;


import com.desarrollo.poderjudicial.santafe.model.entidades.Multimedia;

import com.desarrollo.poderjudicial.santafe.model.entidades.TipoExtension;
import com.desarrollo.poderjudicial.santafe.model.entidades.TipoMultimedia;
import com.desarrollo.poderjudicial.santafe.view.utiles.ADFUtil;
import com.desarrollo.poderjudicial.santafe.view.utiles.AppProperties;

import com.desarrollo.poderjudicial.santafe.view.utiles.FilaDigitalizacion;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

import javax.servlet.ServletContext;

import oracle.adf.view.rich.event.DialogEvent.Outcome;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.output.RichInlineFrame;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.model.BindingContext;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.adf.view.rich.render.ClientEvent;

import oracle.binding.BindingContainer;

import oracle.adf.model.OperationBinding;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.Criterion;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlListBinding;

import oracle.jbo.Row;

import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.util.ComponentReference;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

/**
 *
 * Bean Manejador del Tab de Detalles de Legajos
 * @author nstessens
 */
public class DetalleBean {

    // Componentes visuales
    private ComponentReference tablaDetalles;
    private ComponentReference frameVP;
    private ComponentReference richPopup;
    private ComponentReference txtNroDocumentoPersona;
    private ComponentReference txtTipoDocumentoPersona;
    private ComponentReference txtNombreArchivo;
    private ComponentReference txtNombreArchivoSeguimiento;
    private ComponentReference inputFile;
    private ComponentReference cbxGrupos;
    private ComponentReference txtVersion;

    // Atributos
    private String path = null, version = null;
    private List<UploadedFile> archivosACargar;
    private ArrayList<SelectItem> tiposMultimedias;
    private List<FilaDigitalizacion> filasTabla = null;
    private List<BigDecimal> tiposCurso = null;
    private List<BigDecimal> tiposInstituto = null;

    // Mensajes y constantes
    private static String mensajeAlerta = "Error Inesperado. Consulte a Informática";
    private static String circunscripcion = AppProperties.getInstance().getPropiedad("CIRCUNSCRIPCION");
    private static String pathRepositorio = AppProperties.getInstance().getPropiedad("PATH.IMAGEN.REPOSITORIO");
    private static String pathTemporal = AppProperties.getInstance().getPropiedad("PATH.IMAGEN.TEMP");
    private static String separador = AppProperties.getInstance().getPropiedad("SLAH");
    private static String versionApp = AppProperties.getInstance().getPropiedad("VERSION");
    private static String SEQUENCIA_NUMERADORA_DOCS = "NUM_DOC_MULTIMEDIA_SEQ";

    List<String> archivosCargados = null;

    // Contructor
    public DetalleBean() {
        this.filasTabla = new ArrayList<FilaDigitalizacion>();
        this.archivosCargados = new ArrayList<String>();

        // Cargo los tipos de cursos e instituciones
        this.cargarTiposCursosInstituciones();

    }

    /**
     * Setea el label de la versión
     */
    public void setLabelVersion(ClientEvent clientEvent) {
        // Seteo el label de la versión
        this.setVersion("Versión: " + DetalleBean.versionApp);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTxtVersion());
    }

    @SuppressWarnings({ "unchecked" })
    private void cargarTiposCursosInstituciones() {

        BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding opb = (OperationBinding) bc.getOperationBinding("obtenerTiposCursos");
        this.tiposCurso = (List<BigDecimal>) opb.execute();

        opb = (OperationBinding) bc.getOperationBinding("obtenerTiposInstitutos");
        this.tiposInstituto = (List<BigDecimal>) opb.execute();

    }

    // Setters and Getters
    public void setTablaDetalles(RichTable tablaDetalles) {
        this.tablaDetalles = ComponentReference.newUIComponentReference(tablaDetalles);
    }

    public RichTable getTablaDetalles() {
        if (this.tablaDetalles != null) {
            return (RichTable) this.tablaDetalles.getComponent();
        }
        return null;
    }

    public void setFrameVP(RichInlineFrame frameVP) {
        this.frameVP = ComponentReference.newUIComponentReference(frameVP);
    }

    public RichInlineFrame getFrameVP() {
        if (this.frameVP != null) {
            return (RichInlineFrame) this.frameVP.getComponent();
        }
        return null;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setRichPopup(RichPopup richPopup) {
        this.richPopup = ComponentReference.newUIComponentReference(richPopup);
    }

    public RichPopup getRichPopup() {
        if (this.richPopup != null) {
            return (RichPopup) this.richPopup.getComponent();
        }
        return null;
    }

    public void setTxtNroDocumentoPersona(RichOutputText txtNroDocumentoPersona) {
        this.txtNroDocumentoPersona = ComponentReference.newUIComponentReference(txtNroDocumentoPersona);
    }

    public RichOutputText getTxtNroDocumentoPersona() {
        if (this.txtNroDocumentoPersona != null) {
            return (RichOutputText) this.txtNroDocumentoPersona.getComponent();
        }
        return null;
    }

    public void setTxtTipoDocumentoPersona(RichOutputText txtTipoDocumentoPersona) {
        this.txtTipoDocumentoPersona = ComponentReference.newUIComponentReference(txtTipoDocumentoPersona);
    }

    public RichOutputText getTxtTipoDocumentoPersona() {
        if (this.txtTipoDocumentoPersona != null) {
            return (RichOutputText) this.txtTipoDocumentoPersona.getComponent();
        }
        return null;
    }

    public void setItemsTipoMultimedias(ArrayList<SelectItem> items) {
    }

    public ArrayList<SelectItem> getItemsTipoMultimedias() {
        BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        FacesCtrlListBinding iteratorBinding = (FacesCtrlListBinding) bc.get("tipoMultimediaFindAll");

        this.tiposMultimedias = new ArrayList<SelectItem>();

        SelectItem item = null;
        SelectItem itemTodos = null;
        for (int i = 0; i < iteratorBinding.getViewObject().getRowCount(); i++) {
            Row rw = iteratorBinding.getRowAtRangeIndex(i);
            if (rw != null) {
                if (rw.getAttribute("idTipoMultimedia")
                      .toString()
                      .equals("0")) {
                    itemTodos = new SelectItem();
                    itemTodos.setValue(rw.getAttribute("idTipoMultimedia").toString());
                    itemTodos.setLabel(rw.getAttribute("descripcion").toString());
                } else {
                    item = new SelectItem();
                    item.setValue(rw.getAttribute("idTipoMultimedia").toString());
                    item.setLabel(rw.getAttribute("descripcion").toString());
                    this.tiposMultimedias.add(item);
                }
            }
        }

        // Agrego el "Todos" primero
        this.tiposMultimedias.add(0, itemTodos);
        return this.tiposMultimedias;
    }

    public void setTxtNombreArchivo(RichInputText txtNombreArchivo) {
        this.txtNombreArchivo = ComponentReference.newUIComponentReference(txtNombreArchivo);
    }

    public RichInputText getTxtNombreArchivo() {
        if (this.txtNombreArchivo != null) {
            return (RichInputText) this.txtNombreArchivo.getComponent();
        }
        return null;
    }

    public void setTxtNombreArchivoSeguimiento(RichInputText txtNombreArchivoSeguimiento) {
        this.txtNombreArchivoSeguimiento = ComponentReference.newUIComponentReference(txtNombreArchivoSeguimiento);
    }

    public RichInputText getTxtNombreArchivoSeguimiento() {
        if (this.txtNombreArchivoSeguimiento != null) {
            return (RichInputText) this.txtNombreArchivoSeguimiento.getComponent();
        }
        return null;
    }

    public void setInputFile(RichInputFile inputFile) {
        this.inputFile = ComponentReference.newUIComponentReference(inputFile);
    }

    public RichInputFile getInputFile() {
        if (this.inputFile != null) {
            return (RichInputFile) this.inputFile.getComponent();
        }
        return null;
    }

    public void setCbxGrupos(RichSelectOneChoice cbxGrupos) {
        this.cbxGrupos = ComponentReference.newUIComponentReference(cbxGrupos);
    }

    public RichSelectOneChoice getCbxGrupos() {
        if (this.cbxGrupos != null) {
            return (RichSelectOneChoice) this.cbxGrupos.getComponent();
        }
        return null;
    }

    public void setArchivosACargar(List<UploadedFile> archivosACargar) {
        this.archivosACargar = archivosACargar;
    }

    public List<UploadedFile> getArchivosACargar() {
        return archivosACargar;
    }

    public void setFilasTabla(List<FilaDigitalizacion> filasTabla) {
        this.filasTabla = filasTabla;
    }

    public List<FilaDigitalizacion> getFilasTabla() {
        return filasTabla;
    }

    public void setTxtVersion(RichOutputText txtVersion) {
        this.txtVersion = ComponentReference.newUIComponentReference(txtVersion);
    }

    public RichOutputText getTxtVersion() {
        if (this.txtVersion != null) {
            return (RichOutputText) this.txtVersion.getComponent();
        }
        return null;
    }

    /**
     * Refresco Grilla al Click en el row
     * @param selectionEvent
     */
    public void refrescarGrilla(SelectionEvent selectionEvent) {

        // Marco la fila actual de la grilla
        ADFUtil.invokeEL("#{bindings.Multimedia.collectionModel.makeCurrent}", new Class[] { SelectionEvent.class },
                         new Object[] { selectionEvent });

        // Refresco el IFRAME
        this.refrescarPrevisualizacion();
    }

    /**
     * Muestra un popup con un mensaje de error/alerta/info
     * @param mensaje
     * @param severidad
     */
    private void mostrarMensajeAlerta(String mensaje, FacesMessage.Severity severidad) {

        FacesMessage fm = new FacesMessage(mensaje);
        fm.setSeverity(severidad);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, fm);

    }

    /**
     * Muestra Popup de confirmar eliminar
     * @param actionEvent
     */
    public void mostrarPopupEliminar(ActionEvent actionEvent) {
        this.getRichPopup().show(new RichPopup.PopupHints());
    }

    @SuppressWarnings("unchecked")
    public void confirmarEliminacion(DialogEvent dialogEvent) {
        Outcome outcome = dialogEvent.getOutcome();

        // Ok en el dialogo
        if (outcome == Outcome.ok) {

            try {
                // Obtengo el id del multimedia a borrar de la fila seleccionada
                Row selectedRow = (Row) ADFUtil.evaluateEL("#{bindings.obtenerMultimediaByDniIterator.currentRow}");

                if (selectedRow != null) {
                    // Primero procedo a eliminar la imagen del repositorio de imagenes
                    if (eliminarImagenDelRepositorio(selectedRow)) {

                        Long multimediaId = (Long) selectedRow.getAttribute("idMultimedia");

                        // Creo el objeto multimedia a borrar e invoco al método del EJB para eliminarlo
                        Multimedia multimedia = new Multimedia();
                        multimedia.setIdMultimedia(multimediaId);

                        BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
                        OperationBinding opb = (OperationBinding) bc.getOperationBinding("removeMultimedia");
                        opb.getParamsMap().put("multimedia", multimedia);
                        opb.execute();

                        // Vuelvo a obtener el multimedia para refrescar los objetos multimedias sin el que borro
                        this.initDetalle(null);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaDetalles());
                    }
                } else {
                    this.mostrarMensajeAlerta("No hay archivo para eliminar.", FacesMessage.SEVERITY_WARN);
                }
            } catch (Exception e) {
                this.mostrarMensajeAlerta(DetalleBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
                System.out.println("-------------------- Sidile Exception --------------------");
                System.out.println("---- Error en Clase DetalleBean: confirmarEliminacion ----");
                e.printStackTrace();
            }

        }
    }

    /**
     * Refresca la previsualización del IFrame
     * @return
     */
    public String refrescarPrevisualizacion() {

        // Obtengo el dni de la persona actual
        String dniPersona = this.getDNIPersona();

        if (dniPersona != null) {
            String pathPersona = circunscripcion + "-" + dniPersona;
            String nombre = "";

            Row selectedRow = (Row) ADFUtil.evaluateEL("#{bindings.obtenerMultimediaByDniIterator.currentRow}");
            if (selectedRow != null) {
                nombre = selectedRow.getAttribute("nombreArchivo").toString();

                this.setPath(pathRepositorio + circunscripcion + separador + pathPersona + separador + nombre);
            } else {
                // Muestro iamgen de sinImagen
                this.setPath(null);
            }

            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFrameVP());
        }
        return null;
    }

    /**
     * Obtiene el DNI de la persona actual
     * @return String  (el nro de documento)
     */
    private String getDNIPersona() {

        // Obtengo el nro de documento de la persona actual
        String auxiliar = this.getTxtNroDocumentoPersona()
                              .getValue()
                              .toString();
        if (auxiliar == null || auxiliar.isEmpty()) {
            this.mostrarMensajeAlerta("Nro. Documento Inválido. Consulte a Informática.", FacesMessage.SEVERITY_WARN);
            return null;
        }

        String dniPersona = auxiliar.split(" ")[1];

        return dniPersona;
    }

    /**
     * Obtiene el Tipo de DNI de la persona actual
     * @return (el nro de tipo)
     */
    private String getTipoDNIPersona() {

        String tipoDoc = this.getTxtTipoDocumentoPersona()
                             .getValue()
                             .toString();
        if (tipoDoc == null || tipoDoc.isEmpty()) {
            this.mostrarMensajeAlerta("Tipo Documento Inválido. Consulte a Informática.", FacesMessage.SEVERITY_WARN);
            return null;
        }

        return tipoDoc;
    }

    /**
     * Acciones al iniciar la pantalla de detalle.
     * Invocado desde el ConsultaBean
     * @param disclosureEvent
     */
    @SuppressWarnings("oracle.jdeveloper.java.unchecked-conversion-or-cast")
    public void initDetalle(DisclosureEvent disclosureEvent) {

        this.archivosCargados.clear();

        // Deshabilito el InputFile de arranque
        this.getInputFile().setDisabled(true);

        // Vuelvo a obtener el multimedia para refrescar los objetos multimedias sin el que borro
        if (this.getDNIPersona() != null && this.getTipoDNIPersona() != null) {
            BindingContainer bindingContainer = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationBinding =
                (OperationBinding) bindingContainer.getOperationBinding("obtenerMultimediaByDni");
            operationBinding.getParamsMap().put("nroDoc", new Long(this.getDNIPersona()));
            operationBinding.getParamsMap().put("tipDoc", new Long(this.getTipoDNIPersona()));
            operationBinding.getParamsMap().put("tipoMultimedia", -1);
            operationBinding.execute();
        }

        this.refrescarPrevisualizacion();
    }

    /**
     * Acciones al iniciar la pantalla de seguimiento.
     * Invocado desde el SeguimientoBean
     * @param disclosureEvent
     */
    public void initDetalleSeguimiento(DisclosureEvent disclosureEvent) {

        // Vuelvo a obtener el multimedia para refrescar los objetos multimedias sin el que borro
        if (this.getDNIPersona() != null && this.getTipoDNIPersona() != null) {
            BindingContainer bindingContainer = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding operationBinding =
                (OperationBinding) bindingContainer.getOperationBinding("obtenerMultimediaByDni");
            operationBinding.execute();
        }
        this.refrescarPrevisualizacion();
    }

    /**
     * Elimina la imagen del servidor repositorio
     * @param selectedRow
     * @return True en caso de haber borrado el archivo. False en caso contrario
     */
    private boolean eliminarImagenDelRepositorio(Row selectedRow) {

        String nombre = selectedRow.getAttribute("nombreArchivo").toString();

        if (nombre != null) {

            // Obtengo el dni de la persona actual
            String dniPersona = this.getDNIPersona();

            if (dniPersona != null) {

                String pathPersona = circunscripcion + "-" + dniPersona;

                // Borro el archivo del servidor
                File archivoEliminar =
                    new File(pathRepositorio + circunscripcion + separador + pathPersona + separador + nombre);
                try {
                    boolean eliminicacion = archivoEliminar.delete();
                    if (!eliminicacion) {
                        this.mostrarMensajeAlerta("No se ha podido eliminar el archivo. Consulte a Informática.",
                                                  FacesMessage.SEVERITY_ERROR);
                    }
                    return eliminicacion;
                } catch (Exception ex) {
                    System.out.println("-------------------- Sidile Exception --------------------");
                    System.out.println("---- Error en Clase DetalleBean: eliminarImagenDelRepositorio ----");
                    System.out.println("---- No se ha podido eliminar el archivo: " + nombre + " ----");
                    ex.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * Limpia los filtros posiblemente seteados con anterioridad en la tabla
     * cuando se filtra por grupos
     */
    public void limpiarFiltrosTabla() {

        FilterableQueryDescriptor queryDescriptor =
            (FilterableQueryDescriptor) this.getTablaDetalles().getFilterModel();
        if (queryDescriptor != null && queryDescriptor.getFilterConjunctionCriterion() != null) {
            List<Criterion> criteriosFiltrados = queryDescriptor.getFilterConjunctionCriterion().getCriterionList();
            for (Criterion criterio : criteriosFiltrados) {
                AttributeDescriptor attrDescription = ((AttributeCriterion) criterio).getAttribute();
                if (attrDescription.getName().equals("nombreArchivo")) {
                    ((AttributeCriterion) criterio).setValue("");
                }
            }
            this.getTablaDetalles().queueEvent(new QueryEvent(this.getTablaDetalles(), queryDescriptor));
        }

        this.getCbxGrupos().setValue("0");
    }

    @SuppressWarnings("oracle.jdeveloper.java.unchecked-conversion-or-cast")
    public void filtrarMultimedias(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() != null &&
            (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue())) {

            FilterableQueryDescriptor queryDescriptor =
                (FilterableQueryDescriptor) this.getTablaDetalles().getFilterModel();

            List<Criterion> criteriosFiltrados = queryDescriptor.getFilterConjunctionCriterion().getCriterionList();

            // Obtengo el tipo multimedia par filtrar
            String tipoMultimedia = valueChangeEvent.getNewValue().toString();

            if (tipoMultimedia != null && tipoMultimedia.length() < 3) {
                String dniPersona = this.getDNIPersona();

                for (Criterion criterio : criteriosFiltrados) {
                    AttributeDescriptor attrDescription = ((AttributeCriterion) criterio).getAttribute();
                    if (attrDescription.getName().equals("nombreArchivo")) {
                        if (tipoMultimedia != null && !tipoMultimedia.equals("0")) {
                            ((AttributeCriterion) criterio)
                                .setValue("%" + dniPersona + "-" +
                                          ("00" + tipoMultimedia).substring(tipoMultimedia.length()));
                        } else {
                            ((AttributeCriterion) criterio).setValue("");
                        }
                    }
                }

                this.getTablaDetalles().queueEvent(new QueryEvent(this.getTablaDetalles(), queryDescriptor));

                // Dependiendo del valor elegido en el combo habilito/deshabilito el InputFile
                this.getInputFile().setDisabled(tipoMultimedia.equals("0"));
                AdfFacesContext.getCurrentInstance().addPartialTarget(this.getInputFile());
            }
        }

        this.setPath(null);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFrameVP());
    }

    /**
     * Refresca la previsualización cuando se hace click en el boton refrecar de la pantalla de seguimiento
     * @param actionEvent
     */
    public void refrescarPrevisualizacionSeguimiento(ActionEvent actionEvent) {
        if (this.getTxtNombreArchivoSeguimiento().getValue() != null) {
            String dniPersona = this.getDNIPersona();
            String pathPersona = circunscripcion + "-" + dniPersona;
            this.setPath(pathRepositorio + circunscripcion + separador + pathPersona + separador + this.getTxtNombreArchivoSeguimiento()
                                                                                                       .getValue()
                                                                                                       .toString());
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFrameVP());
        }
    }

    /**
     * Refresca la previsualización cuando se hace click en el boton refrecar de la pantalla de digitalizacion
     * @param actionEvent
     */
    public void refrescarPrevisualizacion(ActionEvent actionEvent) {
        if (this.getTxtNombreArchivo().getValue() != null) {
            String dniPersona = this.getDNIPersona();
            String pathPersona = circunscripcion + "-" + dniPersona;
            this.setPath(pathRepositorio + circunscripcion + separador + pathPersona + separador + this.getTxtNombreArchivo()
                                                                                                       .getValue()
                                                                                                       .toString());
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFrameVP());
        }
    }

    /** Descar el archivo de la fila seleccionada
     * @param facesContext
     * @param outputStream
     */
    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public void descargarArchivo(FacesContext facesContext, OutputStream outputStream) {

        FileInputStream fis = null;
        try {

            // Obtengo el dni de la persona actual
            String dniPersona = this.getDNIPersona();

            String pathPersona = circunscripcion + "-" + dniPersona;

            Row selectedRow = (Row) ADFUtil.evaluateEL("#{bindings.obtenerMultimediaByDniIterator.currentRow}");

            if (selectedRow != null) {
                String nombre = selectedRow.getAttribute("nombreArchivo").toString();

                if (nombre != null) {
                    File filed =
                        new File(pathRepositorio + circunscripcion + separador + pathPersona + separador + nombre);

                    byte[] b;
                    try {
                        fis = new FileInputStream(filed);

                        int n;
                        while ((n = fis.available()) > 0) {
                            b = new byte[n];
                            int result = fis.read(b);
                            outputStream.write(b, 0, b.length);
                            if (result == -1)
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    outputStream.flush();
                } else {
                    this.mostrarMensajeAlerta("No se a podido recuperar el nombre del arcivo. Consulte a Informática",
                                              FacesMessage.SEVERITY_WARN);
                }
            } else {
                this.mostrarMensajeAlerta("Debe seleccionar una fila", FacesMessage.SEVERITY_WARN);
            }
        } catch (IOException ioe) {
            this.mostrarMensajeAlerta(DetalleBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("-------------------- Sidile Exception --------------------");
            System.out.println("---- Error en Clase DetalleBean: cargarDigitalizacion ----");
            ioe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Controla que el archivo subido sea pdf o jpg o jpeg
     * @param elemento
     * @return true en caso afirmativo
     */
    private boolean controlarExtensionArchivo(String elemento) {

        // Determino la extensión del archivo
        String extensionArchivo = FilenameUtils.getExtension(elemento);

        if (extensionArchivo != null)
            return (extensionArchivo.toLowerCase().equals("jpg") || extensionArchivo.toLowerCase().equals("jpeg") ||
                    extensionArchivo.toLowerCase().equals("pdf"));
        else
            return false;
    }

    /**
     * Separa en dos List<UploadedFile> los archivos candidados a cargarse en la tabla de digitalizados. Los correctos pasan, los demas no
     * @return Array<Object> [0] List<UploadedFile> de archivos correctos, [1] List<String> de archivos incorrectos más el motivo
     */
    @SuppressWarnings({ "oracle.jdeveloper.java.unchecked-conversion", "unchecked" })
    private Object[] separarArchivosACargar(String grupo) {

        Object[] retorno = new Object[2];
        List<UploadedFile> correctos = new ArrayList<UploadedFile>();
        List<String> incorrectos = new ArrayList<String>();

        for (UploadedFile elemento : this.getArchivosACargar()) {
            // Controlo que el tamaño del archivo no supere los 1000 KB
            if (elemento.getLength() <= 1000000) {
                // Controlo la extensión del archivo
                if (this.controlarExtensionArchivo(elemento.getFilename())) {
                    // Controlo que no se ingrese dos veces el mismo archivo
                    if (!this.archivosCargados.contains(elemento.getFilename())) {
                        // Si se estan cargando cursos, chequeo el nombre del archivo
                        if (this.controlarNombreArchivo(elemento.getFilename(), grupo)) {
                            // Una vez pasado todos los controles lo agrego a la lista de los que pasan
                            correctos.add(elemento);
                        } else {
                            // Al no pasar la validación del formato de nombre correcto lo agrego a los que no pasan
                            incorrectos.add(elemento.getFilename() +
                                            " -- No cumple con el formato de nombre establecido.");
                        }
                    } else {
                        // Al ya estar cargado en la tabla lo agrego a los que no pasan
                        incorrectos.add(elemento.getFilename() + " -- Ya se encuenta cargado.");
                    }
                } else {
                    // Al tener una extensión no permitida lo agrego a los que no pasan
                    incorrectos.add(elemento.getFilename() + " -- Extensión no permitida.");
                }
            } else {
                // Al superar el tamaño permitido de Upload lo agrego a los que no pasan
                incorrectos.add(elemento.getFilename() + " -- Tamaño de archivo excedido.");
            }
        }

        retorno[0] = correctos;
        retorno[1] = incorrectos;
        return retorno;
    }

    /**
     * Chequea que el nombre del archivo contenga el formato correcto cuando se trata de escaneos de tipo Cursos
     * Formato acordado: "codTipoCur_codTipoInst_nombreArchivo"
     * @param nombreArchivo
     * @return
     */
    private boolean controlarNombreArchivo(String nombreArchivo, String grupo) {

        // Si es un tipo distinto a Cursos retorna true
        if (!grupo.equals("8"))
            return true;

        // Obtengo el nombre real
        String nombre = FilenameUtils.getBaseName(nombreArchivo);
        String[] tokens = nombre.split("_");
        if (tokens.length > 2) {
            return this.esCursoValido(new BigDecimal(tokens[0])) && this.esInstitutoValido(new BigDecimal(tokens[1]));
        }
        return false;
    }

    /**
     * Chequea que el curso pasado por parámetro sea uno válido
     * @param curso
     * @return boolean
     */
    private boolean esCursoValido(BigDecimal curso) {
        return this.tiposCurso.contains(curso);
    }

    /**
     * Chequea que el instituto pasado por parámetro sea uno válido
     * @param curso
     * @return boolean
     */
    private boolean esInstitutoValido(BigDecimal instituto) {
        return this.tiposInstituto.contains(instituto);
    }

    /**
     * Realiza el upload de las imágenes cargadas por el usuario a un directorio temporal
     * @param file
     * @return String
     */
    @SuppressWarnings({ "oracle.jdeveloper.java.nested-assignment", "oracle.jdeveloper.java.insufficient-catch-block" })
    private String uploadFile(UploadedFile file) throws IOException, FileNotFoundException {

        String path = null;
        if (file == null) {
            this.mostrarMensajeAlerta("file == null en UploadFile -> DetalleBean", FacesMessage.SEVERITY_WARN);
            return null;
        } else {

            path = pathTemporal + file.getFilename();

            InputStream inputStream = null;
            try {
                FileOutputStream out = new FileOutputStream(path);
                inputStream = file.getInputStream();
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                out.close();
            } catch (Exception ex) {
                System.out.println("------------------ Sidile Exception -------------------");
                System.out.println("---- Error en Clase DetalleBean: uploadFile ----");
                ex.printStackTrace();
                throw ex;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }

        }
        //Returns the path where file is stored
        return path;
    }

    /**
     * Mueve las imágenes del directorio temporal hacia el servidor repositorio
     * @param nroDocumento
     * @param tipoDocumento
     * @param bindingContainer
     * @return List<Multimedia>
     * @throws Exception
     */
    @SuppressWarnings("oracle.jdeveloper.java.null-collection-return")
    private List<Multimedia> moverImagenesAlRepositorio(String nroDocumento, int tipoDocumento,
                                                        BindingContainer bindingContainer) {
        List<Multimedia> multimedias = new ArrayList<>();
        String extensionArchivo;

        // Formateos varios
        SimpleDateFormat formaterFecha = new SimpleDateFormat("yyyyMMdd");
        String fechaDiaria = formaterFecha.format(new Date());

        try {
            // Obtengo el usuario logueado
            String userName = ADFContext.getCurrent()
                                        .getSecurityContext()
                                        .getUserName();


            // Consulto si en el servidor existe la carpeta de la circunscripción: pathServidor/01
            File carpetaCircunscripcion = new File(pathRepositorio + circunscripcion);
            if (!carpetaCircunscripcion.exists() || !carpetaCircunscripcion.isDirectory()) {
                // Sino existe o no es un directorio lo creo
                carpetaCircunscripcion.mkdir();
            }

            // Consulto si en el servidor existe la carpeta del documento de la persona actual: pathServidor/01/01-31419011
            String pathPersona = pathRepositorio + circunscripcion + separador + circunscripcion + "-" + nroDocumento;
            File carpetaPersona = new File(pathPersona);
            if (!carpetaPersona.exists() || !carpetaPersona.isDirectory()) {
                // Sino existe o no es un directorio lo creo
                carpetaPersona.mkdir();
            }

            // Buffer donde se va formando el nombre del archivo
            StringBuffer nombreArchivo = new StringBuffer();

            // Comienzo a pasar las imágenes de la carpeta temporal hacia la el servidor de imágenes y crear los objetos multimedias
            for (FilaDigitalizacion elemento : this.filasTabla) {

                // Creo el archivo multimedia
                Multimedia objetoMultimedia = new Multimedia();

                // Seteo el usuario logueado
                objetoMultimedia.setUsuarioApp(userName);


                // Obtengo el file original
                File archivoTemporal = new File(pathTemporal + separador + elemento.getNombre());

                // Por cada archivo se arma el nombre del mismo respetando el formato establecido en el archivo Anteproyecto.odt
                nombreArchivo.append(circunscripcion);
                nombreArchivo.append("-");
                objetoMultimedia.setIdSede(Integer.parseInt(circunscripcion));

                // Determino el nombre del archivo y lo seteo como descripcion
                String nombreArchivoFS = FilenameUtils.getBaseName(elemento.getNombre());
                objetoMultimedia.setDescripcion(nombreArchivoFS);

                // Determino la extensión del archivo
                extensionArchivo = FilenameUtils.getExtension(elemento.getNombre());

                if (extensionArchivo != null) {
                    if (extensionArchivo.toLowerCase().equals("jpg")) {
                        nombreArchivo.append("02");
                        objetoMultimedia.setTipoExtension(new TipoExtension("jpg", 2));
                    } else if (extensionArchivo.toLowerCase().equals("jpeg")) {
                        nombreArchivo.append("02");
                        objetoMultimedia.setTipoExtension(new TipoExtension("jpeg", 3));
                    } else if (extensionArchivo.toLowerCase().equals("pdf")) {
                        nombreArchivo.append("01");
                        objetoMultimedia.setTipoExtension(new TipoExtension("pdf", 1));
                    } else {
                        throw new Exception("Tipo de archivo no manejado en la digitalización: " +
                                            extensionArchivo.toLowerCase());
                    }
                } else {
                    throw new Exception("No se ha podido recuperar la extensión del archivo.");
                }
                nombreArchivo.append("-");

                // Agrego el nro de documento
                nombreArchivo.append(("00000000" + nroDocumento).substring(nroDocumento.length()));
                nombreArchivo.append("-");
                objetoMultimedia.setNroDoc(new Long(nroDocumento));
                objetoMultimedia.setTipoDoc(new Long(tipoDocumento));

                // Obtengo el tipo de archivo escaneado. (Ej: Legajo, Titulo, etc...)
                nombreArchivo.append(("00" + elemento.getIdGrupo()).substring(elemento.getIdGrupo().length()));
                nombreArchivo.append("-");
                objetoMultimedia.setTipoMultimedia(new TipoMultimedia(elemento.getDescripcionGrupo(),
                                                                      Integer.parseInt(elemento.getIdGrupo())));

                // Agrego la fecha
                nombreArchivo.append(fechaDiaria);
                nombreArchivo.append("-");
                objetoMultimedia.setFecha(new Date());

                // Numerador de documentos para esta fecha
                int numerador = this.obtenerProximoNumeroDiario(bindingContainer);
                if (numerador == -1) {
                    throw new Exception("Error al recuperar próximo Nro. de Secuencia: " +
                                        DetalleBean.SEQUENCIA_NUMERADORA_DOCS);
                }
                Formatter formatoSecuencua = new Formatter();
                nombreArchivo.append(formatoSecuencua.format("%03d", numerador));
                nombreArchivo.append("-");
                objetoMultimedia.setOrden(numerador);
                formatoSecuencua = null;

                Formatter formatoFojas = new Formatter();
                // Cantidad de fojas del archivo
                if (extensionArchivo.equals("pdf")) {
                    PDDocument documentoPDF = PDDocument.load(archivoTemporal);
                    int cantPaginas = documentoPDF.getNumberOfPages();
                    nombreArchivo.append(formatoFojas.format("%03d", cantPaginas));
                    objetoMultimedia.setFojas(cantPaginas);

                    // Cierro el archivo pdf
                    documentoPDF.close();
                    documentoPDF = null;
                } else {
                    // extension .jpg .jpeg
                    nombreArchivo.append("001");
                    objetoMultimedia.setFojas(1);
                }
                formatoFojas = null;

                // Agrego la extencion del archivo en el nombre
                nombreArchivo.append(".");
                nombreArchivo.append(extensionArchivo);
                objetoMultimedia.setNombreArchivo(nombreArchivo.toString());
                multimedias.add(objetoMultimedia);
                objetoMultimedia = null;

                // Una vez formado el nuevo nombre, muevo al servidor el archivo renombrandolo
                Path moverDesde = FileSystems.getDefault().getPath(archivoTemporal.getAbsolutePath());
                Path moverHacia =
                    FileSystems.getDefault()
                    .getPath(carpetaPersona.getAbsolutePath() + separador + nombreArchivo.toString());

                // Muevo el archivo al servidor renombrandolo con el nombre correcto
                Files.copy(moverDesde, moverHacia, StandardCopyOption.REPLACE_EXISTING);

                // Elimino el archivo de la carpeta temporal
                archivoTemporal.delete();

                // Vacio el stringBuffer del nombre del archivo
                nombreArchivo.delete(0, nombreArchivo.length());

                archivoTemporal = null;
            }

            nombreArchivo = null;
            return multimedias;
        } catch (InvalidPasswordException ipe) {
            this.mostrarMensajeAlerta(DetalleBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("-------------------------- Sidile Exception ---------------------------");
            System.out.println("---- Error en Clase DetalleBean: armarColeccionDeImagenesACargar ----");
            ipe.printStackTrace();
            this.archivosCargados.clear();
            this.filasTabla.clear();
            return null;
        } catch (IOException ioe) {
            this.mostrarMensajeAlerta(DetalleBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("-------------------------- Sidile Exception ---------------------------");
            System.out.println("---- Error en Clase DetalleBean: armarColeccionDeImagenesACargar ----");
            ioe.printStackTrace();
            this.archivosCargados.clear();
            this.filasTabla.clear();
            return null;
        } catch (Exception ex) {
            this.mostrarMensajeAlerta(DetalleBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("-------------------------- Sidile Excepción ---------------------------");
            System.out.println("---- Error en Clase DetalleBean: moverImagenesAlRepositorio ----");
            ex.printStackTrace();
            this.archivosCargados.clear();
            this.filasTabla.clear();
            return null;
        }
    }


    /**
     * Invoca a EJB al fin de obtener el proximo numero de secuencia para numerar el documento
     * @param bindingContainer
     * @return nroSecuencia
     */
    @SuppressWarnings("oracle.jdeveloper.java.unchecked-conversion-or-cast")
    private int obtenerProximoNumeroDiario(BindingContainer bindingContainer) {
        // Obtengo el contenedor de binding
        OperationBinding opb = (OperationBinding) bindingContainer.getOperationBinding("obtenerProxNroSequencia");
        opb.getParamsMap().put("nombreSequencia", DetalleBean.SEQUENCIA_NUMERADORA_DOCS);
        Integer retorno = (Integer) opb.execute();

        return retorno;
    }

    /**
     * Recorre las imágenes cargadas en filasTabla, guarda la imagenes en el servidor de imagenes y persiste los objetos multimedias.
     * @return
     */
    @SuppressWarnings("oracle.jdeveloper.java.unchecked-conversion-or-cast")
    public boolean guardarImagenes() {

        if (this.filasTabla.isEmpty()) {
            this.mostrarMensajeAlerta("No hay archivos cargados para guardar.", FacesMessage.SEVERITY_WARN);
            return false;
        }

        try {

            // Obtengo el nro y tipo de documento de la persona actual
            String dniPersona = this.getDNIPersona();
            String tipoDoc = this.getTipoDNIPersona();

            // Obtengo el contenedor de binding
            BindingContainer bindingContainer = BindingContext.getCurrent().getCurrentBindingsEntry();

            // Muevo las imagenes al servidor
            List<Multimedia> listMultimedia =
                this.moverImagenesAlRepositorio(dniPersona, Integer.parseInt(tipoDoc), bindingContainer);

            if (listMultimedia != null && !listMultimedia.isEmpty()) {

                OperationBinding opb = (OperationBinding) bindingContainer.getOperationBinding("persistMultimedia");
                opb.getParamsMap().put("multimedias", listMultimedia);
                Boolean retorno = (Boolean) opb.execute();

                if (!retorno.booleanValue()) {
                    this.mostrarMensajeAlerta("Error en la carga de documentos. Consulte a Informática.",
                                              FacesMessage.SEVERITY_ERROR);
                }
            } else {
                return false;
            }

        } catch (Exception ex) {
            this.mostrarMensajeAlerta(DetalleBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("--------------------- Sidile Exception ---------------------");
            System.out.println("---- Error en Clase DetalleBean: guardarImagenes ----");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Agrega un row a la colección de documentos digitalizados
     * @param nombre
     * @param path
     * @param grupo
     */
    private void agregarRow(String nombre, String path, String idGrupo) {

        int nroGrupo = Integer.parseInt(idGrupo);
        String grupo = this.getDescripcionTipoMultimedia(idGrupo);

        // Agrego la fila
        this.filasTabla.add(new FilaDigitalizacion(nombre, path, grupo, idGrupo));
    }

    private String getDescripcionTipoMultimedia(String nroGrupo) {
        for (SelectItem elemento : this.tiposMultimedias) {
            if (elemento.getValue()
                        .toString()
                        .equals(nroGrupo))
                return elemento.getLabel();
        }
        return null;
    }

    /**
     * Comienza los procesos de carga de digitalizacion del documento/s elegido/s por el usuario
     * @param actionEvent
     */
    @SuppressWarnings("unchecked")
    public void cargarArchivosATabla(ActionEvent actionEvent) {
        try {

            if (this.getArchivosACargar() != null) {

                if (!this.getCbxGrupos()
                         .getValue()
                         .toString()
                         .equals("0")) {
                    // Obtengo el grupo seleccionado
                    String grupo = this.getCbxGrupos()
                                       .getValue()
                                       .toString();
                    // Separo los archivos en aptos o no para cargar
                    Object[] candidatos = this.separarArchivosACargar(grupo);

                    //Recorro los archivos válidos y los cargo a la tabla
                    List<UploadedFile> correctos = (List<UploadedFile>) candidatos[0];
                    for (UploadedFile archivo : correctos) {

                        String path = uploadFile(archivo);
                        this.agregarRow(archivo.getFilename(), path, grupo);
                        this.archivosCargados.add(archivo.getFilename());
                    }

                    // Si hubo nuevos archivos cargados refresco los componentes visuales
                    boolean guardadoCorrecto = false;
                    if (correctos.size() > 0) {
                        // Reseteo el componente luego de cargar todas las imagenes
                        ResetUtils.reset(this.getInputFile());

                        // Guardo las imagenes
                        guardadoCorrecto = this.guardarImagenes();
                        this.initDetalle(null);

                        // Vuelvo a setear el grupo en el combo
                        this.getCbxGrupos().setValue(grupo);

                        // Limpio la coleccion de filas tabla ya que se cargo todo con exito
                        this.filasTabla.clear();
                    } else {
                        guardadoCorrecto = true;
                    }

                    // Si hay componentes que no son válidos muestro un cartel de advertencia, siempre y cuando el método guardarImagenes haya
                    // retornado true (en caso de retornar false, lanza una excepción con un mensaje de alerta y por ende no muestro este próximo cartel de alerta)
                    List<String> erroneos = (List<String>) candidatos[1];
                    if (erroneos.size() > 0 && guardadoCorrecto) {
                        StringBuilder mensaje = new StringBuilder();
                        mensaje.append("<html><body>");
                        mensaje.append("<p><b>Los siguientes archivos no fueron cargados:</b></p>");
                        for (String elemento : erroneos) {
                            mensaje.append("<p>" + elemento + "</p>");
                        }
                        if (mensaje.indexOf("establecido") > 0) {
                            mensaje.append("<p><b>Formato de nombres de archivos para cursos: <i>codCurso_codInstituto_nombreDelArchivo</i></b></p>");
                        }
                        mensaje.append("</body></html>");
                        this.mostrarMensajeAlerta(mensaje.toString(), FacesMessage.SEVERITY_WARN);
                    }
                } else {
                    this.mostrarMensajeAlerta("Se debe elejir un grupo válido", FacesMessage.SEVERITY_WARN);
                }
            }
        } catch (Exception ex) {
            this.mostrarMensajeAlerta(DetalleBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("------------------------ Sidile Exception -----------------------");
            System.out.println("---- Error en Clase DetalleBean: cargarArchivosATabla ----");
            ex.printStackTrace();
        }
    }

    public void confirmarEliminacion(ActionEvent actionEvent) {
        try {
            // Obtengo el id del multimedia a borrar de la fila seleccionada
            Row selectedRow = (Row) ADFUtil.evaluateEL("#{bindings.obtenerMultimediaByDniIterator.currentRow}");

            if (selectedRow != null) {
                // Primero procedo a eliminar la imagen del repositorio de imagenes
                if (eliminarImagenDelRepositorio(selectedRow)) {

                    Long multimediaId = (Long) selectedRow.getAttribute("idMultimedia");
                    
                    // Obtengo el usuario logueado
                    String userName = ADFContext.getCurrent()
                                                .getSecurityContext()
                                                .getUserName();

                    // Creo el objeto multimedia a borrar e invoco al método del EJB para eliminarlo
                    Multimedia multimedia = new Multimedia();
                    multimedia.setIdMultimedia(multimediaId);
                    multimedia.setUsuarioApp(userName);

                    BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
                    OperationBinding opb = (OperationBinding) bc.getOperationBinding("removeMultimedia");
                    opb.getParamsMap().put("multimedia", multimedia);
                    opb.execute();

                    // Vuelvo a obtener el multimedia para refrescar los objetos multimedias sin el que borro
                    this.initDetalle(null);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaDetalles());
                }
            } else {
                this.mostrarMensajeAlerta("No hay archivo para eliminar.", FacesMessage.SEVERITY_WARN);
            }
        } catch (Exception e) {
            this.mostrarMensajeAlerta(DetalleBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("-------------------- Sidile Exception --------------------");
            System.out.println("---- Error en Clase DetalleBean: confirmarEliminacion ----");
            e.printStackTrace();
        }
    }
}
