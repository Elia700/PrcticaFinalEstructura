
package Controlador.Dao;

import Controlador.TDA.ListaDinamica.ListaDinamica;
import com.thoughtworks.xstream.XStream;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Elias
 * @param <T>
 */
public class DaoImplement<T> implements DaoInterface<T>{
    @SuppressWarnings("unused")
    private Class<T> clazz;
    private XStream conection;
    private String URL;
    
    public DaoImplement(Class<T> clazz) {
        this.clazz = clazz;
        conection = Bridge.getConection();
        URL = Bridge.URL + clazz.getSimpleName() + ".json";
    }
    
    @Override
    public Boolean Persist(T dato) {
        ListaDinamica<T> ld = all();
        ld.Agregar(dato);
        
        try {
            conection.toXML(ld,new FileWriter(URL));
            return true;
        } 
        catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Boolean Merge(T data, Integer indice) {
        ListaDinamica<T> ListaModificar = all();

        if (indice >= 0 && indice < ListaModificar.getLongitud()) {
            try {
                ListaModificar.modificarPosicion(data, indice);
            } 
            catch (Exception e) {
                
            }
            try {
                conection.toXML(ListaModificar, new FileWriter(URL));
                return true;
            } 
            catch (Exception e) {
                return false;
            }
        } 
        else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListaDinamica<T> all() {
        ListaDinamica<T> dl = new ListaDinamica<>();
        try {
            dl = (ListaDinamica<T>)conection.fromXML(new FileReader(URL));
        } 
        catch (Exception e) {
            
        }
        return dl;
    }

    @Override
    public T get(Integer id) {

        ListaDinamica<T> lista = all();

        for (int i = 0; i < lista.getLongitud(); i++) {
            
            try {
                T elemento = lista.getInfo(i);
                Integer elementoId = (Integer) elemento.getClass().getMethod("getId").invoke(elemento);

                if (elementoId.equals(id)) {
                    return elemento;
                }
            } 
            catch (Exception e) {
                
            }
        }
        return null;
        
    }
    
    public Boolean Eliminar(Integer index) {
        ListaDinamica<T> listaActualizada = all();

        try {
            listaActualizada.eliminar(index);
            conection.toXML(listaActualizada, new FileWriter(URL));
            return true;
        } 
        catch (Exception e) {
            return false;
        }
    }
    
}
