package servlets;

import HTTPeXist.HTTPeXist;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewImage extends HttpServlet {
    private HTTPeXist eXist;
    public void init(ServletConfig config) {
        System.out.println("---> Entrando en init()de NewImage");
        eXist = new HTTPeXist("http://localHost:8080");
        System.out.println("---> Saliendo de init()de NewImage");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String image = req.getParameter("svgName");
        String collection = req.getParameter("collection");

        int status = eXist.subirString(collection, "<svg></svg>", image);

        if (status == 201) {
            req.setAttribute("informacion", image + " argazki hutsa sortu da " + collection + " kolekzioan.");
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
