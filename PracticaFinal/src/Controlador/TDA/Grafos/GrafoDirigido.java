
package Controlador.TDA.Grafos;

import Controlador.TDA.Grafos.Excepcion.VerticeException;
import Controlador.TDA.ListaDinamica.ListaDinamica;

/**
 *
 * @author Elias
 */
public class GrafoDirigido extends Grafo{
    private Integer numeroVertices;
    private Integer numeroAristas;
    private ListaDinamica<Adyacencia> ListaAdyacencia[];
    
    public GrafoDirigido(Integer num_vertices) {
        this.numeroVertices = num_vertices;
        this.numeroAristas = 0;
        ListaAdyacencia = new ListaDinamica[num_vertices + 1];
        for (int i = 1; i <= this.numeroVertices; i++) {
            ListaAdyacencia[i] = new ListaDinamica<>();
        }
    }
    
    @Override
    public Integer num_vertice() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return numeroVertices;
    }

    @Override
    public Integer num_aristas() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return numeroAristas;
    }

    @Override
    public Boolean existe_arista(Integer v1, Integer v2) throws Exception{
        Boolean band = false;
        if(v1.intValue() <= numeroVertices && v2.intValue() <= numeroVertices){
            ListaDinamica<Adyacencia> listaA = ListaAdyacencia[v1];
            for(int i = 0; i < listaA.getLongitud(); i++){
                Adyacencia a = listaA.getInfo(i);
                if(a.getDestino().intValue() == v2.intValue()){
                    band = true;
                    break;
                }
            }
                
        }
        else
            throw new VerticeException();
        
        return band;
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Double peso_arista(Integer v1, Integer v2) throws Exception {
        Double peso = Double.NaN;
        if (existe_arista(v1, v2)) {
            ListaDinamica<Adyacencia> listaA = ListaAdyacencia[v1];
            for (int i = 0; i < listaA.getLongitud(); i++) {
                Adyacencia a = listaA.getInfo(i);
                if (a.getDestino().intValue() == v2.intValue()) {
                    peso = a.getPeso();
                    break;
                }
            }
        }
        return peso;
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insertar_arista(Integer v1, Integer v2, Double peso) throws Exception {
        if (v1.intValue() <= numeroVertices && v2.intValue() <= numeroVertices) {
            if (!existe_arista(v1, v2)) {
                numeroAristas++;
                ListaAdyacencia[v1].Agregar(new Adyacencia(v2, peso));
            }
        } 
        else 
            throw new VerticeException();
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insertar_arista(Integer v1, Integer v2) throws Exception{
        insertar_arista(v1, v2, Double.NaN);
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ListaDinamica<Adyacencia> adycentes(Integer v1) throws Exception{
        return ListaAdyacencia[v1];
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ListaDinamica<Adyacencia>[] getListaAdyacencia() {
        return ListaAdyacencia;
    }

    public void setListaAdyacencia(ListaDinamica<Adyacencia>[] ListaAdyacencia) {
        this.ListaAdyacencia = ListaAdyacencia;
    }

    public Integer getNumeroVertices() {
        return numeroVertices;
    }

    public void setNumeroVertices(Integer numeroVertices) {
        this.numeroVertices = numeroVertices;
    }

    public Integer getNumeroAristas() {
        return numeroAristas;
    }

    public void setNumeroAristas(Integer numeroAristas) {
        this.numeroAristas = numeroAristas;
    }
    
    public ListaDinamica<Integer> recorridoAnchura(Integer v) throws Exception {
        ListaDinamica<Integer> recorrido = new ListaDinamica<>();
        ListaDinamica<Integer> cola = new ListaDinamica<>();
        ListaDinamica<Integer> visitados = new ListaDinamica<>();
        Integer w;
        visitados.Agregar(v);
        recorrido.Agregar(v);
        cola.Agregar(v);
        while (!cola.EstaVacio()) {
            Integer u = cola.eliminar(0);
            ListaDinamica<Adyacencia> listaA = adycentes(u);
            for (int i = 0; i < listaA.getLongitud(); i++) {
                Adyacencia a = listaA.getInfo(i);
                w = a.getDestino();
                if (!visitados.contiene(w)) {
                    visitados.Agregar(w);
                    recorrido.Agregar(w);
                    cola.Agregar(w);
                }
            }
        }
        return recorrido;
    }

    public ListaDinamica<Integer> recorridoProfundidad(Integer v) throws Exception {
        ListaDinamica<Integer> recorrido = new ListaDinamica<>();
        ListaDinamica<Integer> pila = new ListaDinamica<>();
        ListaDinamica<Integer> visitados = new ListaDinamica<>();
        Integer w;
        pila.Agregar(v);
        while (!pila.EstaVacio()) {
            Integer u = pila.eliminar(pila.getLongitud() - 1);
            if (!visitados.contiene(u)) {
                visitados.Agregar(u);
                recorrido.Agregar(u);
                ListaDinamica<Adyacencia> listaA = adycentes(u);
                for (int i = 0; i < listaA.getLongitud(); i++) {
                    Adyacencia a = listaA.getInfo(i);
                    w = a.getDestino();
                    if (!visitados.contiene(w)) {
                        pila.Agregar(w);
                    }
                }
            }
        }
        return recorrido;
    }

    public void floyd() throws Exception {
        Double[][] distancias = new Double[num_vertice() + 1][num_vertice() + 1];

        for (int i = 1; i <= num_vertice(); i++) {
            for (int j = 1; j <= num_vertice(); j++) {
                if (i == j) {
                    distancias[i][j] = 0.0;
                } else if (existe_arista(i, j)) {
                    distancias[i][j] = peso_arista(i, j);
                } else {
                    distancias[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int k = 1; k <= num_vertice(); k++) {
            for (int i = 1; i <= num_vertice(); i++) {
                for (int j = 1; j <= num_vertice(); j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }

        for (int i = 1; i <= num_vertice(); i++) {
            for (int j = 1; j <= num_vertice(); j++) {
                if (distancias[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(distancias[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    public void bellmanFord(Integer v) throws Exception {
        Double[] distancias = new Double[num_vertice() + 1];
        Integer[] predecesores = new Integer[num_vertice() + 1];

        for (int i = 1; i <= num_vertice(); i++) {
            distancias[i] = Double.POSITIVE_INFINITY;
            predecesores[i] = null;
        }
        distancias[v] = 0.0;

        for (int i = 1; i <= num_vertice() - 1; i++) {
            for (int j = 1; j <= num_vertice(); j++) {
                ListaDinamica<Adyacencia> listaA = adycentes(j);
                for (int k = 0; k < listaA.getLongitud(); k++) {
                    Adyacencia a = listaA.getInfo(k);
                    if (distancias[j] + a.getPeso() < distancias[a.getDestino()]) {
                        distancias[a.getDestino()] = distancias[j] + a.getPeso();
                        predecesores[a.getDestino()] = j;
                    }
                }
            }
        }

        for (int i = 1; i <= num_vertice(); i++) {
            ListaDinamica<Adyacencia> listaA = adycentes(i);
            for (int j = 0; j < listaA.getLongitud(); j++) {
                Adyacencia a = listaA.getInfo(j);
                if (distancias[i] + a.getPeso() < distancias[a.getDestino()]) {
                    throw new Exception("El grafo contiene un ciclo negativo");
                }
            }
        }
    }
} 

