
package Controlador.Dao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.File;

/**
 *
 * @author Elias
 */
public class Bridge {
    public static String URL = "Files"+ File.separatorChar;
    private static XStream conection;

    public static XStream getConection() {
        if(conection == null){
            conection = new XStream(new JettisonMappedXmlDriver());
            conection.addPermission(AnyTypePermission.ANY);
        }
        return conection;
    }

    public static void setConection(XStream conection) {
        Bridge.conection = conection;
    }
    
}
