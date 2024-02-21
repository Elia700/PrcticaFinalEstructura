
package Controlador.TDA.ListaDinamica.Excepcion;

/**
 *
 * @author Elias
 */
public class ListaVacia extends Exception{
    
    public ListaVacia(){
        
    }
    
    public ListaVacia(String msg) {
        super(msg);
    }
    
}
