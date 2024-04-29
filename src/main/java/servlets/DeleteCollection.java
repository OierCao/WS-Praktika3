package servlets;

import HTTPeXist.HTTPeXist;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCollection extends HttpServlet {
    private HTTPeXist eXist;
    public void init(ServletConfig config) {
        System.out.println("---> Entrando en init()de DeleteCollection");
        eXist = new HTTPeXist("http://localHost:8080");
        System.out.println("---> Saliendo de init()de DeleteCollection");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String collection = req.getParameter("collection");

        int status = eXist.delete(collection);

        if (status == 200) {
            req.setAttribute("informacion", collection + " kolekzioa ezabatu egin da.");
        } else {
            req.setAttribute("informacion", "Arazo bat egon da. Saiatu berriro. (Código "+status+")");
        }

        System.out.println("\tRedirecting the user to index.jsp");
        RequestDispatcher rd = req.getRequestDispatcher("/jsp/index.jsp");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        rd.forward(req, resp);

        System.out.println("---> Exiting doGet() CreateCollection");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
