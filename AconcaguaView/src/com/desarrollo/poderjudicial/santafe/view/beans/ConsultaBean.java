package com.desarrollo.poderjudicial.santafe.view.beans;

import java.util.List;

import javax.faces.component.UIComponent;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import com.desarrollo.poderjudicial.santafe.view.utiles.ADFUtil;

import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.Criterion;

import org.apache.myfaces.trinidad.util.ComponentReference;

/**
 *
 * Bean Manejador del Tab de Consulta de Legajos
 * @author jalarcon
 */
public class ConsultaBean {

    // Componentes visuales
    private RichPanelTabbed tabbedPanel;
    private ComponentReference tablaBusqueda;

    // Otros Managed Bean
    private DetalleBean detalle;

    /**
     * Constructor
     */
    public ConsultaBean() {
    }

    // Getters and Setters
    public void setTabbedPanel(RichPanelTabbed tabbedPanel) {
        this.tabbedPanel = tabbedPanel;
    }

    public RichPanelTabbed getTabbedPanel() {
        return tabbedPanel;
    }

    public void setDetalle(DetalleBean detalle) {
        this.detalle = detalle;
    }

    public DetalleBean getDetalle() {
        return detalle;
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
     * Cambia programaticamente al tab de detalle
     */
    public void abrirTabDetalle() {
        RichPanelTabbed richPanelTabbed = getTabbedPanel();

        for (UIComponent child : richPanelTabbed.getChildren()) {
            RichShowDetailItem sdi = (RichShowDetailItem) child;
            if (sdi.getClientId().equals("tabDetalle")) {
                sdi.setDisclosed(true);
                this.getDetalle().initDetalle(null);
            } else {
                sdi.setDisclosed(false);
            }
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(richPanelTabbed);

    }

    /**
     * Redefine lo ingresado en la búsqueda por parte del usuario.
     * @param queryEvent
     */
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

        // Si realizo una busqueda nueva, limpio los últimos filtros en la tabla de detalle
        this.getDetalle().limpiarFiltrosTabla();
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


}
