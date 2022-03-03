package base;

import java.sql.*;
import java.lang.*;
import java.lang.reflect.Method;
public class BdTable implements Cloneable
{

    public Boolean TestsiNum(Method m)throws Exception
    {
        Class typ=m.getReturnType();
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

    public ResultSetMetaData getTableMetadata(Connection c)throws Exception
    {
        Class ThisClass=this.getClass();
        String nomDeTable=ThisClass.getSimpleName();
        String requ="select * from "+nomDeTable;
        Statement stmt = c.createStatement();
        ResultSet res ;
        res = stmt.executeQuery(requ);
        ResultSetMetaData resMetad = res.getMetaData();
        return resMetad;
    }

    public Method[] AllFieldMethodGet(Connection c)throws Exception//toute les valeurs des attributs par ordre similaire a la base
    {
        
        //données pour le metadata
            //nom de la table
            Class ThisClass=this.getClass();
            String nomDeTable=ThisClass.getSimpleName();
        //requete et metadata
        ResultSetMetaData resMetad = this.getTableMetadata(c);
        //nbr de colonne
        int nbChamps = resMetad.getColumnCount();
        //
        Method[] valiny=new Method[nbChamps];
        int indvaliny=0;
        for (int i = 1 ; i <= nbChamps ; i++)
        {   
            String nm = resMetad.getColumnName(i) ;
            String nom= nm.toLowerCase();
            String maj=nom.substring(0,1);
            maj= maj.toUpperCase();
            String nomGet="get"+maj+nom.substring(1);
            Method meth=ThisClass.getMethod(nomGet);
            valiny[indvaliny]=meth;
            indvaliny++;
        }
        return valiny;
    }

    public String[] getAllFieldName(Connection c)throws Exception
    {
        
         //données pour le metadata
            //nom de la table
            Class ThisClass=this.getClass();
            String nomDeTable=ThisClass.getSimpleName();
        //requete et metadata
        ResultSetMetaData resMetad = this.getTableMetadata(c);
        //nbr de colonne
        int nbChamps = resMetad.getColumnCount();

        String[] valiny=new String[nbChamps];
        int indvaliny=0;
        for (int i = 1 ; i <= nbChamps ; i++)
        { 
            String nm = resMetad.getColumnName(i) ;
            String nom=nm.toLowerCase();
            valiny[indvaliny]=nom;
            indvaliny++;
        }
        return valiny;
    }

    public void insertValues(Connection con)throws Exception
    { 
        Class ThisClass=this.getClass();
        String nomTable=ThisClass.getSimpleName();
        Method[] fonctionGet=this.AllFieldMethodGet(con);
        //Valeur des attributs
        String attr="(";
        for(int i=0;i<fonctionGet.length;i++)
        {
            Object value=fonctionGet[i].invoke(this);
            String StrValeur="";
            if(TestsiNum(fonctionGet[i])==true)
            {
                StrValeur=value.toString();
            }
            else
            {
                StrValeur="'"+(String)value+"'";
            }
                if(i!=(fonctionGet.length-1))
                {
                    attr=attr+StrValeur+",";
                }
                else
                {
                    attr=attr+StrValeur+")";
                }
        }
        
        String requete="insert into "+nomTable+" values"+attr;
        Statement stmt= con.createStatement();
        int res= stmt.executeUpdate(requete);
    }


    public void update(Connection c)throws Exception
    {
        Method[] fonctionGet=this.AllFieldMethodGet(c);
        String[] nomAttr=this.getAllFieldName(c);
        Class ThisClass=this.getClass();
        String nomTable=ThisClass.getSimpleName();
        //attribut a actualiser
        String requete="Update "+nomTable+" set ";
        String subRequete="";
        //nom de la table
        //where
        //id de la table
        Object val=fonctionGet[0].invoke(this);
        String idTable=val.toString();
        String where=" where "+nomAttr[0]+"="+idTable;
        for(int i=0;i<nomAttr.length;i++)
        {
            Object value=fonctionGet[i].invoke(this);
            String StrValeur="";
            
            if(TestsiNum(fonctionGet[i])==true)
            {
                StrValeur=value.toString();
            }
            else
            {
                 StrValeur="'"+(String)value+"'";
            }

            if(i!=nomAttr.length-1)
            {
                subRequete=subRequete+nomAttr[i]+"="+StrValeur+",";
            }
            else
            {
                subRequete=subRequete+nomAttr[i]+"="+StrValeur+" ";
            }
            
        }
        String reqUpdate=requete+subRequete+where;
        Statement stmt=c.createStatement();
        int Result=stmt.executeUpdate(reqUpdate);
    }
    
    public String getTypeJava(String type)
    {
        String[] val=new String[3];
    	val= type.split("\\.");
    	if(type.compareTo("java.lang.Integer")==0)
    	{
    		String integ="Int";
    		return integ;
    	}
    	return val[2];
    }

    public BdTable[] ExecuteList(Connection c,BdTable filtre,String requete)throws Exception
    {
        Method[] fonction=filtre.AllFieldMethodGet(c);
        String[] nomAttribut=filtre.getAllFieldName(c);
        Statement stmt=c.createStatement();
        ResultSet Result=stmt.executeQuery(requete);
        ResultSetMetaData met=getTableMetadata(c);
        Class<? extends BdTable> cl=this.getClass();
        int nbrEnreg=0;
        while(Result.next())
        {
            nbrEnreg++;
        }
        BdTable[] Table=new BdTable[nbrEnreg];
        int indiceTable=0;
        int nbrCol=met.getColumnCount();
        Statement state=c.createStatement();
        ResultSet val=state.executeQuery(requete);
        while(val.next())
        {
            if(indiceTable==nbrEnreg)
            {
                break;
            }
            int indA=0;
            if(indA==nbrCol)
            {
                indA=0;
            }
            //fonction get
            Object obj=cl.newInstance();
            for (int i = 0 ; i < nbrCol ; i++)
            {
                //obtenir la valeur d une colonne
                String typeJav = met.getColumnClassName(i+1);//string de type java.lang.(Integer) count(java.lang.)=10
                String typeJava=getTypeJava(typeJav);//substring(10) typeJav
                String getType="get"+typeJava;//de type getString,getInt,.....
                Class[] param=new Class[1];
                param[0]=nomAttribut.getClass().getComponentType();
                Method get= val.getClass().getMethod(getType,param);//getString,......
                Object[] nameAttr=new Object[1];
                nameAttr[0]=nomAttribut[indA];
                Object value=get.invoke(val, nameAttr);
                indA++;
                //obtenir la method set de Table(reponse)
                String colName=met.getColumnName(i+1);
                String set="set"+colName.substring(0, 1)+colName.substring(1).toLowerCase();
                Class[] cla=new Class[1];
                cla[0]=get.getReturnType();

                Method setMethode=filtre.getClass().getMethod(set, cla);
                setMethode.invoke(obj, value);
            }
            BdTable objet=(BdTable) obj;
            Object obje2=objet.clone();
            Table[indiceTable]=(BdTable)obje2;
            indiceTable++;
        }
        return Table;
    }

    public BdTable[] Find(Connection c,BdTable filtre)throws Exception
    {
        BdTable valiny=new BdTable();
        Method[] fonction=filtre.AllFieldMethodGet(c);
        String[] nomAttribut=filtre.getAllFieldName(c);
        //attribut non null
        int nbrDesFNonNull=0;
        for(int i=0;i<fonction.length;i++)
        {
        	if(TestsiNum(fonction[i])==false)
        	{
        		if(fonction[i].invoke(filtre)!=null )
                        {
                            nbrDesFNonNull++;
                        }
                        else
                        {
                            continue;
                        }
        	}
        	else
        	{
        		if((int)fonction[i].invoke(filtre)!=0 )
                        {
                            nbrDesFNonNull++;
                        }
                        else
                        {
                            continue;
                        }
        	}
        }
        //indice des fonction get des attributs non null
        int[] FNonNull=new int[nbrDesFNonNull];
        int indF=0;
        for(int i=0;i<fonction.length;i++)
        {
            if(TestsiNum(fonction[i])==false)
        	{
        		if(fonction[i].invoke(filtre)!=null )
                        {
                            FNonNull[indF]=i;
                            indF++;
                        }
        	}
        	else
        	{
        		if((int)fonction[i].invoke(filtre)!=0 )
                        {
                            FNonNull[indF]=i;
                            indF++;
                        }
        	}
        }
        Class ThisClass=this.getClass();
        String nomTable=ThisClass.getSimpleName();
        String subRequ1="Select * from "+nomTable;
        String subRequ2=" Where ";
        if(FNonNull.length==0)
        {
            subRequ2="";
        }
        for(int i=0;i<FNonNull.length;i++)
        {
            Object value=fonction[FNonNull[i]].invoke(filtre);
            
                if(TestsiNum(fonction[FNonNull[i]])==true )
                {
                    if(i != FNonNull.length-1)
                    {
                        subRequ2=subRequ2+nomAttribut[FNonNull[i]]+"= "+value.toString()+" and ";
                    }
                    else
                    {
                        subRequ2=subRequ2+nomAttribut[FNonNull[i]]+"= "+value.toString();
                    }
                }
                else
                {
                    if(i != FNonNull.length-1)
                    {
                        subRequ2=subRequ2+"'"+nomAttribut[FNonNull[i]]+"'"+"= "+(String)value+" and ";
                    }
                    else
                    {
                        subRequ2=subRequ2+nomAttribut[FNonNull[i]]+"= "+"'"+(String)value.toString()+"'";
                    }
                }
        }
        String requete=subRequ1+subRequ2;
        BdTable[] Table=this.ExecuteList(c,filtre,requete);
        return Table;
    }

}

