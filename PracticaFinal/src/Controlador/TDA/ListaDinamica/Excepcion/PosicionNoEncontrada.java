
package Controlador.TDA.ListaDinamica.Excepcion;

/**
 *
 * @author Elias
 */
public class PosicionNoEncontrada extends Exception{
    
    public PosicionNoEncontrada(String msg){
        super(msg);
    }
    
    public PosicionNoEncontrada(){
        super("La posición dada está fuera de los límites");
    }
    
}
