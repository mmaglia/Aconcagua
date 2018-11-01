package com.desarrollo.poderjudicial.santafe.view.servlets;

import com.desarrollo.poderjudicial.santafe.view.utiles.AppProperties;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "Servlet1", urlPatterns = { "/previewFileServlet" })
public class PreviewFileServlet extends HttpServlet {
    @SuppressWarnings("compatibility:-7600808191444479285")
    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        OutputStream os = null;
        InputStream inputStream = null;
        try {
            String path = request.getParameter("path");
            os = response.getOutputStream();

            // Si el path es nulo o no viene seteado muestro la imagen vacia como preview
            if (path.equalsIgnoreCase("No") || request.getParameter("path") == "") {
                path = AppProperties.getInstance().getPropiedad("PATH.IMAGEN.TEMP") + AppProperties.getInstance().getPropiedad("IMAGEN.VACIO");               
            } else {

                File outputFile = new File(path);
                inputStream = new FileInputStream(outputFile);
                BufferedInputStream in = new BufferedInputStream(inputStream);
                int b;
                byte[] buffer = new byte[10240];
                while ((b = in.read(buffer, 0, 10240)) != -1) {
                    os.write(buffer, 0, b);
                }
            }

        } catch (Exception e) {
            System.out.println("-------------- Sidile Exception ---------------");
            System.out.println("------ Error en Clase PreviewFileServlet. -----");
            e.printStackTrace();
            
            File outputFile = new File(AppProperties.getInstance().getPropiedad("PATH.IMAGEN.TEMP") + AppProperties.getInstance().getPropiedad("IMAGEN.VACIO"));
            inputStream = new FileInputStream(outputFile);
            BufferedInputStream in = new BufferedInputStream(inputStream);
            int b;
            byte[] buffer = new byte[10240];
            while ((b = in.read(buffer, 0, 10240)) != -1) {
                os.write(buffer, 0, b);
            }
        } finally {
            if (os != null) {
                os.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
