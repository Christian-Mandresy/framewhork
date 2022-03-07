package Utilitaire;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

public class FileUtil {

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public String[] extractAllName(Part[] parts)
    {
        int indice=0;
        int nombre=0;
        for(Part part : parts )
        {
            // refines the fileName in case it is an absolute path
            if(getFilename(part)==null)
            {

            }
            else
            {
                nombre++;
            }
        }
        String[] valiny=new String[nombre];
        for(Part part : parts )
        {
            // refines the fileName in case it is an absolute path
            if(getFilename(part)==null)
            {

            }
            else
            {
                valiny[indice]=getFilename(part);
                indice++;
            }
        }
        return valiny;
    }

    public void uploadFile(Part[] parts) throws IOException {
        // gets absolute path of the web application
        // constructs path of the directory to save uploaded file

        for (Part part : parts) {
            if(getFilename(part)==null)
            {

            }
            else
            {
                String fileName = getFilename(part);
                // refines the fileName in case it is an absolute path
                part.write("D:\\uploaded" + File.separator + fileName);
            }
        }
    }
}
