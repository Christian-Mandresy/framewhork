/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Annotation.FonctionAnnot;
import Utilitaire.FileUtil;
import bas.*;
import modeleview.ModelView;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;

/**
 *
 * @author ITU
 */
@FonctionAnnot(url="raha")
public class Test extends BdTable{

    String attribut1;
    int attribut2;

    @FonctionAnnot(url="IzayTiako")
    public ModelView fonction(String izay)
    {
        String vue="TestDate.jsp";
        ModelView mod=new ModelView();
        HashMap map=new HashMap();
        mod.setVue(vue);
        mod.setHash(map);
        System.out.println("Mety le invoke"+izay);
        return mod;
    }

    @FonctionAnnot(url = "testRehetra")
    public ModelView TestRehetra(String nom, Date daty,int inty){
        String vue = "Test.jsp";
        ModelView mod=new ModelView();
        HashMap map=new HashMap();
        map.put("mety"," "+daty.toString()+" ");
        mod.setVue(vue);
        mod.setHash(map);
        System.out.println(daty.toString());
        return mod;
    }

    @FonctionAnnot(url="ajout-Tiako")
    public void ajout()throws Exception
    {
        Connex co=new Connex();
        Connection connex=co.getConnexion();
        this.insertValues(connex);
    }

    @FonctionAnnot(url = "TestHash")
    public ModelView TestHash(String anarana,Date ndaty,int inty)
    {
        ModelView retour=new ModelView();
        HashMap valiny=new HashMap();
        valiny.put("Nom",ndaty);
        valiny.put("Nomnombre",inty);
        retour.setHash(valiny);
        retour.setVue("TestHash.jsp");
        return retour;
    }

    @FonctionAnnot(url = "TestFile")
    public ModelView TestFile(String anarana, Date ndaty, int inty, Part[] parts)
    {
        ModelView retour=new ModelView();
        HashMap valiny=new HashMap();
        valiny.put("Nom",ndaty);
        valiny.put("Nomnombre",inty);
        FileUtil fileUtil=new FileUtil();
        valiny.put("NomFichier",fileUtil.extractAllName(parts));
        String path="http://localhost:8080/Framewhorek_war_exploded/";
        try {
            fileUtil.uploadFile(path,parts);
        } catch (IOException e) {
            e.printStackTrace();
        }
        retour.setHash(valiny);
        retour.setVue("TestFich.jsp");
        return retour;
    }

    @FonctionAnnot(url = "TestSession")
    public ModelView TestSession(String user, String password, HttpSession session)
    {
        ModelView retour=new ModelView();
        HashMap valiny=new HashMap();
        valiny.put("User",user);
        valiny.put("password",password);
        session.setAttribute("utilisateur",user);
        session.setAttribute("password",password);
        String vue="TestSession.jsp";
        retour.setVue(vue);
        retour.setHash(valiny);
        return  retour;
    }



    public String getAttribut1() {
        return attribut1;
    }

    public void setAttribut1(String attribut1) {
        this.attribut1 = attribut1;
    }

    public int getAttribut2() {
        return attribut2;
    }

    public void setAttribut2(int attribut2) {
        this.attribut2 = attribut2;
    }



}
