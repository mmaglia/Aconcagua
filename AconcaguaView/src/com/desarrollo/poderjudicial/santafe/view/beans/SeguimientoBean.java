package com.desarrollo.poderjudicial.santafe.view.beans;

import com.desarrollo.poderjudicial.santafe.model.entidades.Daper;
import com.desarrollo.poderjudicial.santafe.model.entidades.MultimediaPorPersona;
import com.desarrollo.poderjudicial.santafe.view.utiles.ADFUtil;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;

import javax.naming.InitialContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.OperationBinding;
import oracle.adf.model.bean.DCDataRow;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.data.RichListView;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.Criterion;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;
import oracle.adf.view.rich.render.ClientEvent;

import oracle.binding.BindingContainer;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.util.ComponentReference;

/**
 * Bean Manejador del Tab de Seguimiento de Legajos
 * @author nstessens
 */
public class SeguimientoBean {

    // Componentes visuales
    private ComponentReference tabbedPanel;
    private ComponentReference richListView;
    private ComponentReference tablaBusqueda;
    private ComponentReference txtIdGrupo;

    // Atributos
    private List<MultimediaPorPersona> multimedias = null;
    private Integer grupo = null;

    // Otros Managed Bean
    private DetalleBean detalle;


    public void setGrupo(Integer grupo) {
        this.grupo = grupo;
    }

    public Integer getGrupo() {
        return grupo;
    }

    /**
     * Constructor de la clase
     */
    public SeguimientoBean() {
        this.multimedias = new ArrayList<MultimediaPorPersona>();
    }

    // Getters and Setters
    public void setTabbedPanel(RichPanelTabbed tabbedPanel) {
        this.tabbedPanel = ComponentReference.newUIComponentReference(tabbedPanel);
    }

    public RichPanelTabbed getTabbedPanel() {
        if (this.tabbedPanel != null) {
            return (RichPanelTabbed) this.tabbedPanel.getComponent();
        }
        return null;
    }

    public void setTxtIdGrupo(RichOutputText txtIdGrupo) {
        this.txtIdGrupo = ComponentReference.newUIComponentReference(txtIdGrupo);
    }

    public RichOutputText getTxtIdGrupo() {
        if (this.txtIdGrupo != null) {
            return (RichOutputText) this.txtIdGrupo.getComponent();
        }
        return null;
    }

    public void setRichListView(RichListView richListView) {
        this.richListView = ComponentReference.newUIComponentReference(richListView);
    }

    public RichListView getRichListView() {
        if (this.richListView != null) {
            return (RichListView) this.richListView.getComponent();
        }
        return null;
    }

    public void setMultimedias(List<MultimediaPorPersona> multimedias) {
        this.multimedias = multimedias;
    }

    public List<MultimediaPorPersona> getMultimedias() {
        return multimedias;
    }

    public void setTablaBusqueda(RichTable tablaBusqueda) {
        this.tablaBusqueda = ComponentReference.newUIComponentReference(tablaBusqueda);
    }

    public RichTable getTablaBusqueda() {
        if (this.tablaBusqueda != null) {
            return (RichTable) this.tablaBusqueda.getComponent();
        }
        return null;
    }

    public void setDetalle(DetalleBean detalle) {
        this.detalle = detalle;
    }

    public DetalleBean getDetalle() {
        return detalle;
    }

    /**
     * Cambia programaticamente la vista
     */
    public void verVista() {
        RichPanelTabbed richPanelTabbed = getTabbedPanel();
        RichListView richListView = getRichListView();

        AdfFacesContext.getCurrentInstance().addPartialTarget(richPanelTabbed);

    }

    /**
     * Refresca los valores de la ListView
     */
    @SuppressWarnings({ "oracle.jdeveloper.java.unchecked-conversion-or-cast", "unchecked" })
    public void refrescarListView() {

        Row selectedRow = (Row) ADFUtil.evaluateEL("#{bindings.daperFindAll.currentRow}");
        if (selectedRow != null) {
            Daper daper = (Daper) ((DCDataRow) selectedRow).getDataProvider();

            //this.setPersonaActual(daper.getDpapynom());
            //this.getTxtPersonalActual().setValue(daper.getDpapynom());
            // Obtengo el contenedor de binding
            BindingContainer bindingContainer = BindingContext.getCurrent().getCurrentBindingsEntry();

            // Invoco al método del ejb para recuperar los elementos multimedias para esta persona
            OperationBinding opb =
                (OperationBinding) bindingContainer.getOperationBinding("obtenerMultimediasPorPersona");
            opb.getParamsMap().put("nroDoc", daper.getDpnrodoc());
            opb.getParamsMap().put("tipoDoc", daper.getDptipdoc());
            this.multimedias = (List<MultimediaPorPersona>) opb.execute();
        } else {
            this.multimedias.clear();
        }

        // Refesco el List View para que tome los cambios
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getRichListView());
    }

    public void onQuery(QueryEvent queryEvent) {
        boolean invokeQuery = true;

        FilterableQueryDescriptor queryDescriptor =
            (FilterableQueryDescriptor) this.getTablaBusqueda().getFilterModel();

        List<Criterion> criteriosFiltrados = queryDescriptor.getFilterConjunctionCriterion().getCriterionList();

        for (Criterion criterio : criteriosFiltrados) {
            AttributeDescriptor attrDescription = ((AttributeCriterion) criterio).getAttribute();

            //valida nro legajo
            if (attrDescription.getName().equals("dpnroleg") && ((AttributeCriterion) criterio).getValue() != null) {
                String dpnroleg = ((AttributeCriterion) criterio).getValue().toString();
                if (dpnroleg != null && dpnroleg.length() > 2) {
                    try {
                        // try to parse String to integer
                        Long.parseLong(dpnroleg);
                    } catch (Exception ex) {
                        // not a string
                        invokeQuery = false;
                    }
                }
            }

            //valida nombre apellido
            if (attrDescription.getName().equals("dpapynom") && ((AttributeCriterion) criterio).getValue() != null) {
                String nombreApellido = ((AttributeCriterion) criterio).getValue().toString();
                if (nombreApellido != null && nombreApellido.length() > 2) {
                    // Determino si ingresa el comodín de búsqueda: %
                    String[] palabras = nombreApellido.split("%");
                    StringBuffer sbuf = new StringBuffer();
                    int i = 0;
                    for (String elemento : palabras) {
                        i++;

                        sbuf.append(elemento.substring(0, 1).toUpperCase());
                        sbuf.append(elemento.substring(1).toLowerCase());

                        // Si no es la última palabra ingresada le agrego el %
                        if (i != palabras.length) {
                            sbuf.append("%");
                        }
                        ((AttributeCriterion) criterio).setValue(sbuf.toString());
                    }
                } else {
                    invokeQuery = false;
                }
            }


            //valida nro doc
            if (attrDescription.getName().equals("dpnrodoc") && ((AttributeCriterion) criterio).getValue() != null) {
                String dpnrodoc = ((AttributeCriterion) criterio).getValue().toString();
                if (dpnrodoc != null && dpnrodoc.length() > 2) {
                    try {
                        // try to parse String to integer
                        Long.parseLong(dpnrodoc);
                    } catch (Exception ex) {
                        // not a string
                        invokeQuery = false;
                    }
                }
            }
        }

        if (invokeQuery) {
            // execute the defaul QueryListener code added by JDeveloper when creating the table
            this.invokeMethodExpression("#{bindings.daperFindAllQuery.processQuery}", Object.class, QueryEvent.class,
                                        queryEvent);
        }

        this.refrescarListView();
    }

    /**
     * Invoca a un método del binding
     * @param expr
     * @param returnType
     * @param argType
     * @param argument
     * @return
     */
    public Object invokeMethodExpression(String expr, Class returnType, Class argType, Object argument) {
        return ADFUtil.getInstance()
               .invokeMethodExpression(expr, returnType, new Class[] { argType }, new Object[] { argument });
    }

    /**
     * Cambia programaticamente al tab de detalle
     */
    public void abrirTabDetalle() {

        // Seteo el grupo como parametro del binding
        this.setGrupo(new Integer(this.getTxtIdGrupo()
                                      .getValue()
                                      .toString()));
        RichPanelTabbed richPanelTabbed = getTabbedPanel();

        for (UIComponent child : richPanelTabbed.getChildren()) {
            RichShowDetailItem sdi = (RichShowDetailItem) child;
            if (sdi.getClientId().equals("tabDetalle")) {
                sdi.setDisclosed(true);
                this.getDetalle().initDetalleSeguimiento(null);
            } else {
                sdi.setDisclosed(false);
            }
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(richPanelTabbed);

    }

    /**
     * Cambia programaticamente al tab de búsqueda
     */
    public void abrirTabBusqueda() {
        RichPanelTabbed richPanelTabbed = getTabbedPanel();

        for (UIComponent child : richPanelTabbed.getChildren()) {
            RichShowDetailItem sdi = (RichShowDetailItem) child;
            if (sdi.getClientId().equals("tabBusqueda")) {
                sdi.setDisclosed(true);
            } else {
                sdi.setDisclosed(false);
            }
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(richPanelTabbed);

    }

    /**
     * Click sobre tabla de consulta.
     * @param clientEvent
     */
    public void clickEnFila(ClientEvent clientEvent) {
        this.refrescarListView();
    }

    /**
     * Apertura de la pantalla
     * @param disclosureEvent
     */
    public void initSeguimiento(DisclosureEvent disclosureEvent) {
        this.refrescarListView();
    }
}
