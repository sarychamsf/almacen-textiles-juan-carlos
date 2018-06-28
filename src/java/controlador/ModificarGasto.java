/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.GastoDAO;
import dao.RegistroDiarioDAO;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Gasto;
import modelo.RegistroDiario;

/**
 *
 * @author Sary
 */
public class ModificarGasto extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        try {

            response.sendRedirect("vergastos.jsp");
            processRequest(request, response);

            int opcion = Integer.parseInt(request.getParameter("opcion"));
            String nombre = request.getParameter("nombre");
            Float monton = Float.parseFloat(request.getParameter("monto"));

            GastoDAO gastodao = new GastoDAO();
            Gasto gastov = gastodao.getGastoById(opcion);
            Date fecha = gastov.getFecha();

            Gasto gaston = new Gasto(opcion, fecha, nombre, monton);
            float montov = gastov.getMonto();
            
            gastodao.updateGasto(opcion, gaston);

            // MODIFICAR REGISTRO DIARIO: restar y actualizar.
            RegistroDiarioDAO registrodao = new RegistroDiarioDAO();
            RegistroDiario registro = registrodao.getRegistroById(fecha);

            float dif = registro.getGastos() - montov;
            RegistroDiario registron = new RegistroDiario(fecha, registro.getVentas(), dif, registro.getUtilidad() + montov);
            registrodao.updateRegistro(fecha, registron);

        } catch (URISyntaxException | SQLException ex) {
            Logger.getLogger(CrearProducto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
