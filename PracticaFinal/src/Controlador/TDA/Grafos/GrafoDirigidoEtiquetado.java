
package Controlador.TDA.Grafos;

import Controlador.TDA.Grafos.Excepcion.EtiquetaNoEstablecida;
import Controlador.TDA.Grafos.Excepcion.VerticeException;
import Controlador.TDA.ListaDinamica.ListaDinamica;
import java.lang.reflect.Array;
import java.util.HashMap;

/**
 *
 * @author Elias
 * @param <E>
 */
public class GrafoDirigidoEtiquetado<E> extends GrafoDirigido{
    
    protected E[] etiquetas;
    protected HashMap<E, Integer> dicVertices;
    private Class<E> clazz;
    
    public GrafoDirigidoEtiquetado(Integer numVert, Class clazz) {
        super(numVert);
        this.clazz = clazz;
        etiquetas = (E[]) Array.newInstance(clazz, numVert+1);
        dicVertices = new HashMap<>(numVert);
    }
    
    public void borrarAristas() throws Exception {
        for (int i = 1; i <= num_vertice(); i++) {
            ListaDinamica<Adyacencia> adyacentes = adycentes(i);
            adyacentes.Limpiar();
        }
    }
    
    public Integer getVerticeE(E label) throws Exception {
        Integer aux = dicVertices.get(label);
        if (aux != null) {
            return aux;
        } else {
            throw new VerticeException("No se encuentra ese vertice asociado a esa etiqueta");
        }
    }
    
    public E getEtiquetaE(Integer codigo) throws Exception {
        if (codigo <= num_vertice()) {
            return etiquetas[codigo];
        } 
        else {
            throw new EtiquetaNoEstablecida();
        }
//        return etiquetas[codigo - 1];
    }
    
    public Boolean isEdge(E o, E d) throws Exception {
        if (isAllLabelsGraph()) {
            return existe_arista(getVerticeE(o), getVerticeE(d));
        } else {
            throw new EtiquetaNoEstablecida();
        }
    }
    
    public void insertEdgeE(E o, E d) throws Exception {
        if (isAllLabelsGraph()) {
            insertar_arista(getVerticeE(o), getVerticeE(d), Double.NaN);
        } 
        else {
            throw new EtiquetaNoEstablecida();
        }
    }

    public void insertEdgeE(E o, E d, Double peso) throws Exception {
        if (isAllLabelsGraph()) {
            insertar_arista(getVerticeE(o), getVerticeE(d), peso);
        } 
        else {
            throw new EtiquetaNoEstablecida();
        }
    }
    
    public ListaDinamica<Adyacencia> adjacents(E label) throws Exception {
        if (isAllLabelsGraph()) {
            return adycentes(getVerticeE(label));
        } else {
            throw new EtiquetaNoEstablecida();
        }
    }
    
    private Integer getIndiceE(E etiqueta) throws Exception {
        Integer aux = dicVertices.get(etiqueta);
        if (aux != null) {
            return aux;
        } 
        else {
            throw new VerticeException("No se encuentra asociado a esta etiqueta");
        }
    }

    
    
    

    public void etiquetarVertice(Integer vertice, E etiqueta) throws Exception {
        etiquetas[vertice] = etiqueta;
        dicVertices.put(etiqueta, vertice);
    }

    public Boolean isAllLabelsGraph() {
        Boolean band = true;
        for (int i = 1; i < etiquetas.length; i++) {
            if (etiquetas[i] == null) {
                band = false;
                break;
            }
        }
        return band;
    }
    
    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder("GRAFO").append("\n");
        try {
            for (int i = 1; i <= num_vertice(); i++) {
                grafo.append("[").append(i).append("] = ").append(getEtiquetaE(i)).append("\n");
                ListaDinamica<Adyacencia> list = adycentes(i);
                for (int j = 0; j < list.getLongitud(); j++) {
                    Adyacencia a = list.getInfo(j);
                    grafo.append("ady [").append(a.getDestino()).append("] - ").append(getEtiquetaE(a.getDestino())).append(" peso ").append(a.getPeso()).append("\n");
                }
            }
        } 
        catch (Exception e) {
            
        }
        return grafo.toString(); 
    }
    
}
