
package Controlador.TDA.ListaDinamica;

import Controlador.TDA.ListaDinamica.Excepcion.ListaVacia;
import Controlador.TDA.ListaDinamica.Excepcion.PosicionNoEncontrada;

/**
 *
 * @author Elias
 * @param <E>
 */
public class ListaDinamica<E> {

    private Nodo<E> cabezera;
    private Nodo<E> ultimo;
    private Integer Longitud;

    public ListaDinamica() {
        cabezera = null;
        ultimo = null;
        Longitud = 0;
    }
    
    public Boolean EstaVacio(){
        return(cabezera == null || Longitud == 0 );
    }
    
    public void AgregarCabeza(E info){
        Nodo<E> Ayuda ;
        if(EstaVacio()){
            Ayuda = new Nodo<>(info);
            cabezera = Ayuda;
            ultimo = Ayuda;
            Longitud++;
        }
        else{
            Nodo<E> CabezaAyuda = cabezera;
            Ayuda = new Nodo<>(info, CabezaAyuda);
            cabezera = Ayuda;
            Longitud++;
        }
    }
    
    public void AgregarFinal(E info){
        Nodo<E> Ayuda;
        if(EstaVacio()){
            AgregarCabeza(info);
        }
        else{
            Ayuda = new Nodo<>(info, null);
            ultimo.setSiguiente(Ayuda);
            ultimo = Ayuda;
            Longitud++;
        }
    }
    
    public void Agregar(E info){
        AgregarFinal(info);
    }
    
    public E getInfo(Integer indice)throws ListaVacia, IndexOutOfBoundsException{
        return getNodo(indice).getInfo();
    }
    
    private Nodo<E> getNodo(Integer indice)throws ListaVacia, IndexOutOfBoundsException{
        if(EstaVacio()){
            throw new ListaVacia("La lista esta vacia");
        }
        else if(indice < 0 || indice.intValue() == Longitud){
            throw new IndexOutOfBoundsException("Fuera de lugar");
        }
        else if(indice == 0){
            return cabezera;
        }
        else if(indice == (Longitud -1)){
            return ultimo;
        }
        else{
            Nodo<E> Buscar = cabezera;
            int contador =0;
            while(contador < indice){
                contador++;
                Buscar = Buscar.getSiguiente();
            }
            return Buscar;
        }
    }
       
    public void Agregar (E info, Integer indice)throws ListaVacia{
        if(EstaVacio() || indice == 0){
            AgregarCabeza(info);
        }
        else if(indice.intValue() == Longitud){
            AgregarFinal(info);
        }
        else{
            Nodo<E> BuscarPrevio = getNodo(indice - 1);
            Nodo<E> Buscar= getNodo(indice);
            Nodo<E> Ayuda = new Nodo<>(info, Buscar);
            BuscarPrevio.setSiguiente(Ayuda);
            Longitud++;
        }
    }
    
    public void modificarPosicion(E dato, Integer pos) throws PosicionNoEncontrada{
        if(EstaVacio()){
            Agregar(dato);
        }
        else if(pos>=0 && pos < Longitud){
            if(pos == 0){
                cabezera.setInfo(dato);
            } 
            else{
                Nodo<E> aux = cabezera;
                for (int i = 0; i < pos; i++ ){
                    aux = aux.getSiguiente();
                }
                aux.setInfo(dato);
            }
        }
        else{
            throw new PosicionNoEncontrada();
        }
    }
    
    public ListaDinamica<E> obtenerLista() {
        ListaDinamica<E> lista = new ListaDinamica<>();
        Nodo<E> actual = cabezera;

        while (actual != null) {
            lista.AgregarFinal(actual.getInfo());
            actual = actual.getSiguiente();
        }

        return lista;
    }
    
    public E extractFirst() throws ListaVacia{
        if(EstaVacio()){
            throw new ListaVacia("Lista vacia");
        }
        else{
            E element = cabezera.getInfo();
            Nodo<E> help = cabezera.getSiguiente();
            cabezera = null;
            cabezera = help;
            if(Longitud ==1)
                ultimo = null;
            Longitud--;
            return element;
        }
    }
    
    public E extractLast() throws ListaVacia{
        if(EstaVacio()){
            throw new ListaVacia("Lista vacia");
        }
        else{
            E element = ultimo.getInfo();
            Nodo<E> help = getNodo(Longitud-2);
            if(help == null){
                ultimo =null;
                if(Longitud == 2){
                    ultimo = cabezera;
                }
                else{
                    cabezera = null;
                }
            }
            else{
               ultimo = null;
               ultimo = help;
               ultimo.setSiguiente(null);
            }
            Longitud--;
            return element;
        }
    }
    
    public E extract(Integer indice) throws ListaVacia{
        if(EstaVacio()){
            throw new ListaVacia("La lista esta vacia");
        }
        else if(indice < 0 || indice.intValue() == Longitud){
            throw new IndexOutOfBoundsException("Fuera de nodo");
        }
        else if(indice == 0){
            return extractFirst();
        }
        else if(indice == (Longitud -1)){
            return extractLast();
        }
        else{
            Nodo<E> nodo_previo = getNodo(indice-1);
            Nodo<E> nodo_actual = getNodo(indice);
            E info = nodo_actual.getInfo();
            Nodo<E> help_next = nodo_actual.getSiguiente();
            nodo_actual = null;
            nodo_previo.setSiguiente(help_next);
            Longitud--;
            return info;
        }
    }
    
    public E eliminar(Integer pos) throws ListaVacia, IndexOutOfBoundsException {
        E dato;
        if (EstaVacio()) {
            throw new ListaVacia("La lista está vacía");
        }
        if (pos < 0 || pos >= Longitud) {
            throw new IndexOutOfBoundsException("Posición fuera de rango");
        }
        if (pos == 0) {
            dato = cabezera.getInfo();
            cabezera = cabezera.getSiguiente();
            if (Longitud == 1) {
                ultimo = null;
            }
        } 
        else {
            Nodo<E> aux = cabezera;
            for (int i = 0; i < pos - 1; i++) {
                aux = aux.getSiguiente();
            }
            dato = aux.getSiguiente().getInfo();
            aux.setSiguiente(aux.getSiguiente().getSiguiente());
            if (pos == Longitud - 1) {
                ultimo = aux;
            }
        }
        Longitud--;
        return dato;
    }
    
    public void Limpiar(){
        cabezera = null;
        ultimo = null;
        Longitud = 0;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public E[] toArray() {
        Class clazz;
        E[] matriz = null;
        if (Longitud > 0) {
            clazz = cabezera.getInfo().getClass();
            matriz = (E[]) java.lang.reflect.Array.newInstance(clazz, Longitud);
            Nodo<E> actual = cabezera;
            for (int i = 0; i < Longitud; i++) {
                matriz[i] = actual.getInfo();
                actual = actual.getSiguiente();
            }
        }
        return matriz;
    }

    public ListaDinamica<E> toList(E[] listaMatriz) {
        Limpiar();
        for (E elemento : listaMatriz) {
            AgregarFinal(elemento);
        }
        return this;
    }
    
    public void concatenar(ListaDinamica<E> otraLista) {
        if (otraLista.EstaVacio()) {
            return;
        }

        if (EstaVacio()) {
            this.cabezera = otraLista.getCabezera();
            this.ultimo = otraLista.getUltimo();
            this.Longitud = otraLista.getLongitud();
        } 
        else {
            this.ultimo.setSiguiente(otraLista.getCabezera());
            this.ultimo = otraLista.getUltimo();
            this.Longitud += otraLista.getLongitud();
        }
    }
 
    public void concatenarList(ListaDinamica<E> otraLista) {
        if (otraLista == null || otraLista.EstaVacio()) {
            return;
        }

        if (this.EstaVacio()) {
            this.cabezera = otraLista.getCabezera();
            this.ultimo = otraLista.getUltimo();
            this.Longitud = otraLista.getLongitud();
        } 
        else {
            this.ultimo.setSiguiente(otraLista.getCabezera());
            this.ultimo = otraLista.getUltimo();
            this.Longitud += otraLista.getLongitud();
        }

        otraLista.Limpiar();
    }

    public Nodo<E> getCabezera() {
        return cabezera;
    }

    public void setCabezera(Nodo<E> cabezera) {
        this.cabezera = cabezera;
    }

    public Nodo<E> getUltimo() {
        return ultimo;
    }

    public void setUltimo(Nodo<E> ultimo) {
        this.ultimo = ultimo;
    }

    public Integer getLongitud() {
        return Longitud;
    }

    public void setLongitud(Integer Longitud) {
        this.Longitud = Longitud;
    }
    
    public boolean contiene(E destino) {
        Nodo<E> help = cabezera;
        while (help != null) {
            if (help.getInfo().equals(destino)) {
                return true;
            }
            help = help.getSiguiente();
        }
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder StringB = new StringBuilder("Datos de lista \n");
        try {
            EstaVacio();
            Nodo<E> ayuda = cabezera;
            while(ayuda != null){
                StringB.append(ayuda.getInfo().toString());
                ayuda = ayuda.getSiguiente();
            }
        } 
        catch (Exception e) {
            StringB.append(e.getMessage());
        }
        return StringB.toString();
    }
}
