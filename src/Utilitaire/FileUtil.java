package Utilitaire;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

public class FileUtil {

    public  String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }

    public String[] extractAllName(Part[] parts)
    {
        String[] valiny=new String[parts.length];
        int indice=0;
        for(Part part : parts )
        {
            // refines the fileName in case it is an absolute path
            valiny[indice]=extractFileName(part);
            indice++;
        }
        return valiny;
    }

    public void uploadFile(String appPath,Part[] parts) throws IOException {
        // gets absolute path of the web application
        // constructs path of the directory to save uploaded file
        String SAVE_DIR="uploaded";
        String savePath = appPath + File.separator + SAVE_DIR;

        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        for (Part part : parts) {
            String fileName = extractFileName(part);
            // refines the fileName in case it is an absolute path
            fileName = new File(fileName).getName();
            part.write(savePath + File.separator + fileName);
        }
    }
}
