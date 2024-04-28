package servlets;

import HTTPeXist.HTTPeXist;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class DeleteSvg extends HttpServlet {
    private HTTPeXist eXist;
    public void init(ServletConfig config) {
        System.out.println("---> Entrando en init()de DeleteSvg");
        eXist = new HTTPeXist("http://localHost:8080");
        System.out.println("---> Saliendo de init()de DeleteSvg");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String collection= req.getParameter("collection");
        String svgName = req.getParameter("svgName");

        //HTTPeXist eXist = new HTTPeXist("http://localHost:8080");
        int status = eXist.delete(collection, svgName);

        if (status == 200) {
            req.setAttribute("informacion", svgName + " argazkia ezabatu egin da " + collection + " kolekziotik.");
        }
        else {
            req.setAttribute("informacion", "Arazo bat egon da. Saiatu berriro. (Código "+status+")");
        }

        String data = eXist.list(collection);
        Document doc = convertStringToXMLDocument(data);
        NodeList valorNode = doc.getElementsByTagName("exist:resource");
        Map<String, String> listaSVG = new HashMap<String, String>();
        for (int i = 0; i < valorNode.getLength(); i++) {
            String nombre = valorNode.item(i).getAttributes().getNamedItem("name").getNodeValue();
            String imagen = eXist.read(collection, nombre);
            System.out.println("nombre: " + nombre);
            listaSVG.put(nombre, imagen);
        }
        req.setAttribute("listaSVG", listaSVG);
        req.setAttribute("collection", collection);


        RequestDispatcher rd;
        if(listaSVG.isEmpty()){
            System.out.println("\tRedirecting the user to index.jsp");
            rd  = req.getRequestDispatcher("/jsp/index.jsp");
        }
        else{
            System.out.println("\tRedirecting the user to imagenList.jsp");
            rd = req.getRequestDispatcher("/jsp/imagenList.jsp");
        }

        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        rd.forward(req, resp);

        System.out.println("---> Exiting doGet() CreateCollection");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private static Document convertStringToXMLDocument(String xmlString) {
        // Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            // Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            // Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
