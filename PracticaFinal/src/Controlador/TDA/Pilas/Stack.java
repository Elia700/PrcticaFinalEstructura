
package Controlador.TDA.Pilas;

import Controlador.TDA.ListaDinamica.Excepcion.ListaVacia;
import Controlador.TDA.ListaDinamica.ListaDinamica;
import Controlador.TDA.Pilas.Excepcion.FullStackException;
/**
 *
 * @author Elias
 */
class Stack<E> extends ListaDinamica<E>{
    private Integer tope;

    public Stack(Integer Tope) {
        this.tope = Tope;
    }
    
    public Boolean isFull(){
        return getLongitud().intValue() >= tope.intValue();
    }
    
    public void push(E info) throws ListaVacia, FullStackException{
        if(isFull()){
            throw new FullStackException("Pilas llena");
//            error
        }
        else{
            Agregar(info, 0);
        }
    }
    
    public E pop() throws ListaVacia{
        E info = extractFirst();        
        return info;
    }
    
}
