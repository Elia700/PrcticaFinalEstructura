
package Controlador.TDA.Grafos;

import Controlador.TDA.ListaDinamica.ListaDinamica;

/**
 *
 * @author Elias
 */
public abstract class Grafo {

    public abstract Integer num_vertice();
    public abstract Integer num_aristas();
    
    public abstract Boolean existe_arista(Integer v1, Integer v2) throws Exception;
    public abstract Double peso_arista(Integer v1, Integer v2) throws Exception;
    public abstract void insertar_arista(Integer v1, Integer v2, Double peso) throws Exception;
    public abstract void insertar_arista(Integer v1, Integer v2) throws Exception;
    public abstract ListaDinamica<Adyacencia> adycentes(Integer v1) throws Exception;

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder("GRAFO").append("\n");
        try {
            for (int i = 1; i <= num_vertice(); i++) {
                grafo.append("V").append(i).append("\n");
                ListaDinamica<Adyacencia> list = adycentes(i);
                for (int j = 0; j < list.getLongitud(); j++) {
                    Adyacencia a = list.getInfo(j);
                    grafo.append("ady ").append(a.getDestino()).append(" peso ").append(a.getPeso()).append("\n");
                }
            }
        } 
        catch (Exception e) {
            
        }
        return grafo.toString(); 
    }
    
}
