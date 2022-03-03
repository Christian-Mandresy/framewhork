/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;


import java.io.*;

import static java.lang.Class.forName;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.lang.reflect.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.RequestDispatcher;

import AccessClass.AccesFichier;
import Annotation.*;
import modeleview.ModelView;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author ITU
 */
public class ServletController extends HttpServlet {

    public HashMap GetAnnot(String packagename)
    {
        AccesFichier fichier=new AccesFichier();
        HashMap valin=new HashMap();
        try {
            Class[] allClasses=fichier.getClasses(packagename);
            Vector<Class> ClassMisyAnnotation=new Vector();
            for(int ii=0;ii<allClasses.length;ii++){
                if(allClasses[ii].isAnnotationPresent(FonctionAnnot.class))
                {
                    ClassMisyAnnotation.add(allClasses[ii]);
                }
            }
            for(int i=0;i<ClassMisyAnnotation.size();i++)
            {
                Method[] ListMethodeClass=ClassMisyAnnotation.get(i).getDeclaredMethods();
                for(int ii=0;ii<ListMethodeClass.length;ii++)
                {
                    if(ListMethodeClass[ii].isAnnotationPresent(FonctionAnnot.class))
                    {
                        FonctionAnnot annot = ListMethodeClass[ii].getAnnotation(FonctionAnnot.class) ;
                        valin.put(annot.url(),ListMethodeClass[ii]);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return valin;
    }

    public Boolean TestsiNum(Class typ)throws Exception
    {
        Class[] typnum=new Class[5];
        typnum[0]=Integer.TYPE;
        typnum[1]=Short.TYPE;
        typnum[2]=Short.TYPE;
        typnum[3]=Float.TYPE;
        typnum[4]=Double.TYPE;
        for(int i=0;i<typnum.length;i++)
        {
            if(typnum[i]==typ)
            {
                return true;
            }
        }
        return false;
    }

    /*
     * Fonction pour avoir le nom de methode correspondant pour convertir
     * un String en type primitif java
     * */
    public String Convert(Class typ)throws Exception
    {
        Class[] typnum=new Class[4];
        typnum[0]=Integer.TYPE;
        typnum[1]=Short.TYPE;
        typnum[2]=Float.TYPE;
        typnum[3]=Double.TYPE;
        String[] FonctionConvert=new String[4];
        FonctionConvert[0]="Integer";
        FonctionConvert[1]="Short";
        FonctionConvert[2]="Float";
        FonctionConvert[3]="Double";

        for(int i=0;i<typnum.length;i++)
        {
            if(typnum[i]==typ)
            {
                return FonctionConvert[i];
            }
        }
        return "";
    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private static String getValue(Part part) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            value.append(buffer, 0, length);
        }
        return value.toString();
    }


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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */



            /* Maka ny class sy fonction avy amin'ny Url */
            StringBuffer url=request.getRequestURL();
            String StrUrl=url.toString();
            String[] tableau=StrUrl.split("/");
            for(int i=0;i<tableau.length;i++)
            {
                out.println("<p>url : " + tableau[i] + "</p>");
            }
            String ilaina=tableau[(tableau.length-1)];
            /*String fonction=ilaina.split("-")[0];
            String Withdo=ilaina.split("-")[1];
            String[] Without=Withdo.split(".do");
            String Classe="servlet."+Without[0];*/


            String[] urlReel=ilaina.split(".do");
            String urlVerifiena=urlReel[0];
            HashMap Controle=null;
            if(request.getServletContext().getAttribute("hash")==null) {
                Controle = this.GetAnnot("Test");
            }
            else{
                Controle=(HashMap) request.getServletContext().getAttribute("hash");
            }

            Method LeMethode=(Method) Controle.get(urlVerifiena);
            int test=0;
            Class AnleFonction=LeMethode.getDeclaringClass();
            Object NewObj=new Object();
            /*
            * Liste des attribut formulaire
            * */
            Enumeration<String> Attrib=request.getParameterNames();
            Vector ListAttrib=new Vector();
            /*
            Transfert des enum dans un  vector
             */
            FileUpload fileUpload=new FileUpload();

            Boolean isMultipartRequest=ServletFileUpload.isMultipartContent(request);
            Class[] parametre=LeMethode.getParameterTypes();
            Object[] argument=new Object[parametre.length];
            if(!isMultipartRequest)
            {
                if(Attrib.hasMoreElements())
                {
                    while (Attrib.hasMoreElements())
                    {
                        ListAttrib.add((String)Attrib.nextElement());
                    }
                }
                /*
            conversion des attributs au type d'argument du fonction
             */
                for(int p=0;p<parametre.length;p++)
                {
                    try {
                        if(TestsiNum(parametre[p])==true)
                        {
                            String nomConvert="";
                            try {
                                nomConvert=Convert(parametre[p]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Maka anle methode ParseInt.......
                            //Argument anle fonction ParseInt donc string eto
                            Class[] param=new Class[1];
                            param[0]=nomConvert.getClass();
                            String parse="";
                            if(nomConvert.equals("Integer")==true)
                            {
                                parse="parse"+"Int";
                            }
                            else
                            {
                                parse="parse"+nomConvert;
                            }
                            Class Nombre=Class.forName("java.lang."+nomConvert);
                            Method fonctConversion=Nombre.getMethod(parse,param);
                            Object[] args=new Object[2];

                            //Parametre dans un formulaire
                            //appelle du fonction de conversion

                            if(request.getParameter((String) ListAttrib.get(p))=="")
                            {
                                args[0]="0";
                            }
                            else
                            {
                                args[0]=request.getParameter((String) ListAttrib.get(p));
                            }
                            //classe comme Integer,Float,......
                            Class[] typParamInteg=new Class[1];
                            typParamInteg[0]=String.class;
                            Constructor Intege=Nombre.getConstructor(typParamInteg);
                            Object Nbr=Intege.newInstance(args[0]);
                            argument[p]=Nbr;
                        }
                        else if ((parametre[p]==String.class)==true)
                        {
                            if(request.getParameter((String) ListAttrib.get(p))=="")
                            {
                                argument[p]=null;
                            }
                            else
                            {
                                argument[p]=request.getParameter((String) ListAttrib.get(p));
                            }
                        }
                        else if(parametre[p]==Date.class)
                        {
                            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                            String nom=request.getParameter((String) ListAttrib.get(p));
                            java.util.Date date=null;
                            if(nom.equals(""))
                            {
                                Date sqlndate=null;
                                argument[p]=sqlndate;
                            }
                            else
                            {
                                date =format.parse(request.getParameter(nom));
                                Date sqlDate=new java.sql.Date(date.getTime());
                                argument[p]=sqlDate;
                            }
                        }
                        else if(parametre[p]== HttpSession.class)
                        {
                            argument[p]=request.getSession();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            else
            {
                int p=0;
                for (Part part : request.getParts()) {
                    String filename = getFilename(part);
                    if (filename == null)
                    {
                        // Traiter les champs classiques ici (input type="text|radio|checkbox|etc", select, etc).
                        String fieldname = part.getName();
                        String fieldvalue = getValue(part);

                            /*
                            conversion des attributs au type d'argument du fonction
                            */
                            try {
                                if(TestsiNum(parametre[p])==true)
                                {
                                    String nomConvert="";
                                    try {
                                        nomConvert=Convert(parametre[p]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //Maka anle methode ParseInt.......
                                    //Argument anle fonction ParseInt donc string eto
                                    Class[] param=new Class[1];
                                    param[0]=nomConvert.getClass();
                                    String parse="";
                                    if(nomConvert.equals("Integer")==true)
                                    {
                                        parse="parse"+"Int";
                                    }
                                    else
                                    {
                                        parse="parse"+nomConvert;
                                    }
                                    Class Nombre=Class.forName("java.lang."+nomConvert);
                                    Method fonctConversion=Nombre.getMethod(parse,param);
                                    Object[] args=new Object[2];

                                    //Parametre dans un formulaire
                                    //appelle du fonction de conversion

                                    if(getValue(part) =="")
                                    {
                                        args[0]="0";
                                    }
                                    else
                                    {
                                        args[0]=getValue(part);
                                    }
                                    //classe comme Integer,Float,......
                                    Class[] typParamInteg=new Class[1];
                                    typParamInteg[0]=String.class;
                                    Constructor Intege=Nombre.getConstructor(typParamInteg);
                                    Object Nbr=Intege.newInstance(args[0]);
                                    argument[p]=Nbr;
                                    p++;
                                }
                                else if ((parametre[p]==String.class)==true)
                                {
                                    if(getValue(part)=="")
                                    {
                                        argument[p]=null;
                                        p++;
                                    }
                                    else
                                    {
                                        argument[p]=getValue(part);
                                        p++;
                                    }
                                }
                                else if(parametre[p]==Date.class)
                                {
                                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                                    String nom=getValue(part);
                                    java.util.Date date=null;
                                    if(nom.equals(""))
                                    {
                                        Date sqlndate=null;
                                        argument[p]=sqlndate;
                                        p++;
                                    }
                                    else
                                    {
                                        date =format.parse(nom);
                                        Date sqlDate=new Date(date.getTime());
                                        argument[p]=sqlDate;
                                        p++;
                                    }
                                }
                                /*else if(parametre[p]== Part[].class)
                                {
                                    Part[] listpart=new Part[request.getParts().size()];
                                    int indice=0;
                                    for (Part part : request.getParts()) {
                                        listpart[indice]=part;
                                        indice++;
                                    }
                                    argument[p]=listpart;
                                }*/
                                else if(parametre[p]== HttpSession.class)
                                {
                                    argument[p]=request.getSession();
                                    p++;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        // ... (traitement à faire)
                    } else if (!filename.isEmpty()) {
                        // Traiter les champs de type fichier (input type="file").
                        String fieldname = part.getName();
                        filename = filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
                        InputStream filecontent = part.getInputStream();
                        // ... (traitement à faire)
                    }
                }
                Part[] listpart=new Part[request.getParts().size()];
                int indice=0;
                for (Part part : request.getParts()) {
                    listpart[indice]=part;
                    indice++;
                }
                argument[p]=listpart;
            }

            if(argument.length!=0)
            {
                try {
                    NewObj=AnleFonction.newInstance();
                    Class[] param=new Class[1];
                    param[0]=null;
                    try {
                        ModelView retour=(ModelView) LeMethode.invoke(NewObj,argument);
                        HashMap valiny=retour.getHash();
                        String vue=retour.getVue();
                        Set entrees = valiny.entrySet () ; // entrees est un ensemble de "paires"
                        Iterator iter = entrees.iterator() ; // itérateur sur les paires
                        while (iter.hasNext()) // boucle sur les paires
                        { Map.Entry entree = (Map.Entry)iter.next() ; // paire courante
                            Object cle = entree.getKey () ; // clé de la paire courante
                            Object valeur = entree.getValue() ; // valeur de la paire courante
                            request.setAttribute((String) cle,valeur);
                        }
                        int stop=0;
                        RequestDispatcher dispat=request.getRequestDispatcher(vue);
                        dispat.forward(request,response);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    NewObj=AnleFonction.newInstance();
                    Class[] param=new Class[1];
                    param[0]=null;
                    try {
                        ModelView retour=(ModelView) LeMethode.invoke(NewObj);
                        HashMap valiny=retour.getHash();
                        String vue=retour.getVue();
                        Set entrees = valiny.entrySet () ; // entrees est un ensemble de "paires"
                        Iterator iter = entrees.iterator() ; // itérateur sur les paires
                        while (iter.hasNext()) // boucle sur les paires
                        { Map.Entry entree = (Map.Entry)iter.next() ; // paire courante
                            Object cle = entree.getKey () ; // clé de la paire courante
                            Object valeur = entree.getValue() ; // valeur de la paire courante
                            request.setAttribute((String) cle,valeur);
                        }
                        RequestDispatcher dispat=request.getRequestDispatcher(vue);
                        dispat.forward(request,response);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
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

