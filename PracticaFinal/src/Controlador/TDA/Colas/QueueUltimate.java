
package Controlador.TDA.Colas;

import Controlador.TDA.ListaDinamica.Excepcion.ListaVacia;
import Controlador.TDA.Pilas.Excepcion.FullStackException;


/**
 *
 * @author Elias
 * @param <E>
 */
public class QueueUltimate<E> {
    private Queue<E> tail;

    public QueueUltimate(Integer legngt) {
        this.tail = new Queue<>(legngt);
    }
    
    public void queue(E info) throws ListaVacia, FullStackException{
        tail.queue(info);
    }
    
    public E dequeue() throws ListaVacia{
        return tail.dequeue();
    }
    
    public Integer length(){
        return tail.getLongitud();
    }
    
    public Boolean isfull(){
        return tail.isFull();
    }
    
    public void Imprimir(){
        System.out.println("Queue");
        System.out.println(tail.toString());
        System.out.println("");
    }
    
}
