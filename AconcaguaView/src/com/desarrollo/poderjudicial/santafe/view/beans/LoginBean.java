package com.desarrollo.poderjudicial.santafe.view.beans;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.render.ClientEvent;

import org.apache.myfaces.trinidad.util.ComponentReference;

import weblogic.security.URLCallbackHandler;
import weblogic.security.services.Authentication;

import weblogic.servlet.security.ServletAuthentication;


/**
 *
 * Bean Manejador del Login de la Aplicación
 * @author jalarcon
 */
public class LoginBean {

    // Mensajes y constantes
    private static String mensajeAlerta = "Error Inesperado. Consulte a Informática";

    // Componentes visuales
    private String inputUsuario;
    private String inputPassword;

    // Constructor
    public LoginBean() {
    }

    public void setInputUsuario(String inputUsuario) {
        this.inputUsuario = inputUsuario;
    }

    public String getInputUsuario() {
        return inputUsuario;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public String getInputPassword() {
        return inputPassword;
    }


    /**
     * Realiza el login. LLamado desde click en boton ingresar
     * @param actionEvent
     */
    public void realizarLoginBoton(ActionEvent actionEvent) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        CallbackHandler handler = new URLCallbackHandler(this.getInputUsuario(), this.getInputPassword());
        try {
            Subject mySubject = Authentication.login(handler);
            ServletAuthentication.runAs(mySubject, request);
            ServletAuthentication.generateNewSessionID(request);
            String loginUrl = "/adfAuthentication?success_url=/faces" + ctx.getViewRoot().getViewId();
            System.out.println();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            sendForward(request, response, loginUrl);
        } catch (FailedLoginException e) {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña incorrecta",
                                 "El usuario o la contraseña son incorrectos.");
            ctx.addMessage(null, msg);
        } catch (LoginException e) {
            this.mostrarMensajeAlerta(LoginBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("--------------------- Sidile Exception -----------------------");
            System.out.println("---- Error en Clase LoginBean: realizarLogin(ActionEvent) ----");
            e.printStackTrace();
        }
    }

    private void sendForward(HttpServletRequest request, HttpServletResponse response, String forwardUrl) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException se) {
            this.mostrarMensajeAlerta(LoginBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("-------------- Sidile Exception ---------------");
            System.out.println("---- Error en Clase LoginBean: sendForward ----");
            se.printStackTrace();
        } catch (IOException ie) {
            this.mostrarMensajeAlerta(LoginBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("-------------- Sidile Exception ---------------");
            System.out.println("---- Error en Clase LoginBean: sendForward ----");
            ie.printStackTrace();
        }
        ctx.responseComplete();
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
     * Realiza el login. LLamado desde enter en textfield de password
     * @param actionEvent
     */
    public void realizarLogin(ClientEvent clientEvent) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        String password = (String) clientEvent.getParameters().get("password");
        String usuario = (String) clientEvent.getParameters().get("usuario");
        CallbackHandler handler = new URLCallbackHandler(usuario, password);
        try {
            Subject mySubject = Authentication.login(handler);
            ServletAuthentication.runAs(mySubject, request);
            ServletAuthentication.generateNewSessionID(request);
            String loginUrl = "/adfAuthentication?success_url=/faces" + ctx.getViewRoot().getViewId();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            sendForward(request, response, loginUrl);
        } catch (FailedLoginException e) {
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña incorrecta",
                                 "El usuario o la contraseña son incorrectos.");
            ctx.addMessage(null, msg);
        } catch (LoginException e) {
            this.mostrarMensajeAlerta(LoginBean.mensajeAlerta, FacesMessage.SEVERITY_ERROR);
            System.out.println("--------------------- Sidile Exception -----------------------");
            System.out.println("---- Error en Clase LoginBean: realizarLogin(ActionEvent) ----");
            e.printStackTrace();
        }
    }

    /**
     * Realiza el deslogueo de ADF
     * @param clientEvent
     */
    public void realizarLogout(ClientEvent clientEvent) {

        try {
            FacesContext fctx = FacesContext.getCurrentInstance();

            // Procedo a cerrar la session y desloguarse
            ExternalContext ectx = fctx.getExternalContext();
            HttpSession session = (HttpSession) ectx.getSession(false);
            session.invalidate();
            HttpServletRequest request = (HttpServletRequest) ectx.getRequest();
            ServletAuthentication.invalidateAll(request);
            ServletAuthentication.killCookie(request);
            fctx.responseComplete();
        } catch (Exception e) {            
            System.out.println("--------------------- Sidile Exception -----------------------");
            System.out.println("---- Error en Clase LoginBean: realizarLogout(ClientEvent) ----");
            e.printStackTrace();
        }
    }
}
