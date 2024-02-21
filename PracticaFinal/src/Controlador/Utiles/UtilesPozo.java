
package Controlador.Utiles;

import Controlador.Dao.Modelo.PozoDao;
import Controlador.TDA.Grafos.Adyacencia;
import Controlador.TDA.Grafos.Excepcion.VerticeException;
import Controlador.TDA.Grafos.GrafoDirigidoEtiquetado;
import Controlador.TDA.ListaDinamica.ListaDinamica;
import Modelo.Pozo;
import java.io.FileWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.swing.JComboBox;

/**
 *
 * @author Elias
 */
public class UtilesPozo {
    public static void crearMapaEscuela(GrafoDirigidoEtiquetado<Pozo> ge) throws VerticeException , Exception {
         String maps = "var osmUrl = 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',\n"
                + "                    osmAttrib = '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors',\n"
                + "                    osm = L.tileLayer(osmUrl, {maxZoom: 15, attribution: osmAttrib});\n"
                + "\n"
                + "            var map = L.map('map').setView([-4.036, -79.201], 15);\n"
                + "\n"
                + "            L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {\n"
                + "                attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors'\n"
                + "            }).addTo(map);" + "\n";
        for (int i = 1; i <= ge.num_vertice(); i++) {
            Pozo ec = ge.getEtiquetaE(i);
            maps += "L.marker([" + ec.getUbicacionPozo().getLongitud() + ", " + ec.getUbicacionPozo().getLatitud() + "]).addTo(map)" + "\n";
            maps += ".bindPopup(\"" + ec.toString() + "\")" + "\n";
            maps += ".openPopup();" + "\n";
        }
        FileWriter file = new FileWriter("mapas/mapa.js");
        file.write(maps);
        file.close();

    }

    public static void cargarComboEscuela(JComboBox cbx) throws Exception {
        cbx.removeAllItems();
        ListaDinamica<Pozo> list = new PozoDao().getListaPozo();
        for (int i = 0; i < list.getLongitud(); i++) {
            cbx.addItem(list.getInfo(i));
        }
    }

    public static Double calcularDistanciaEscuelas(Pozo o, Pozo d) {
        Double dist = UtilesControlador.coordGpsToKm(
                o.getUbicacionPozo().getLatitud(),
                o.getUbicacionPozo().getLongitud(),
                d.getUbicacionPozo().getLatitud(), 
                d.getUbicacionPozo().getLongitud());
        return redondear(dist);
    }
    
    public static Double redondear(Double x){
        Double d = Math.round(x *100.0)/100.0;
        return d;
    }
    
    public static void aplicarBellmanFord(GrafoDirigidoEtiquetado<Pozo> grafo) throws Exception {
        int numVertices = grafo.num_vertice();
        int[] distancias = new int[numVertices + 1];

        for (int i = 1; i <= numVertices; i++) {
            distancias[i] = Integer.MAX_VALUE;
        }

        distancias[1] = 0;

        for (int i = 1; i <= numVertices - 1; i++) {
            for (Adyacencia ady : grafo.adycentes(i).toArray()) {
                int v = ady.getDestino();
                double peso = ady.getPeso();
                if (distancias[i] != Integer.MAX_VALUE && distancias[i] + peso < distancias[v]) {
                    distancias[v] = (int) (distancias[i] + peso);
                }
            }
        }

        for (int u = 1; u <= numVertices; u++) {
            for (Adyacencia ady : grafo.adycentes(u).toArray()) {
                int v = ady.getDestino();
                double peso = ady.getPeso();
                if (distancias[u] != Integer.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                    throw new Exception("El grafo contiene ciclos negativos");
                }
            }
        }

        for (int i = 1; i <= numVertices; i++) {
            System.out.println("Distancia desde el nodo 1 al nodo " + i + ": " + distancias[i]);
        }
    }

    public static void aplicarFloydWarshall(GrafoDirigidoEtiquetado<Pozo> grafo) throws Exception {
        int numVertices = grafo.num_vertice();
        double[][] distancias = new double[numVertices + 1][numVertices + 1];

        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                distancias[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int i = 1; i <= numVertices; i++) {
            distancias[i][i] = 0;
            for (Adyacencia ady : grafo.adycentes(i).toArray()) {
                int v = ady.getDestino();
                double peso = ady.getPeso();
                distancias[i][v] = peso;
            }
        }

        for (int k = 1; k <= numVertices; k++) {
            for (int i = 1; i <= numVertices; i++) {
                for (int j = 1; j <= numVertices; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }

        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                System.out.println("Distancia desde el nodo " + i + " al nodo " + j + ": " + distancias[i][j]);
            }
        }
    }

    public static ArrayList<Integer> recorridoAnchura(GrafoDirigidoEtiquetado<Pozo> grafo, int indiceOrigen) throws Exception {
        ArrayList<Integer> visitados = new ArrayList<>();
        Queue<Integer> cola = new ArrayDeque<>();
        boolean[] marcados = new boolean[grafo.num_vertice() + 1];
        cola.offer(indiceOrigen);
        marcados[indiceOrigen] = true;

        while (!cola.isEmpty()) {
            int verticeActual = cola.poll();
            visitados.add(verticeActual);
            for (Adyacencia ady : grafo.adycentes(verticeActual).toArray()) {
                int vecino = ady.getDestino();
                if (!marcados[vecino]) {
                    cola.offer(vecino);
                    marcados[vecino] = true;
                }
            }
        }

        return visitados;
    }

    public static ArrayList<Integer> recorridoProfundidad(GrafoDirigidoEtiquetado<Pozo> grafo, int indiceOrigen) throws Exception {
        ArrayList<Integer> visitados = new ArrayList<>();
        boolean[] marcados = new boolean[grafo.num_vertice() + 1];
        recorridoProfundidadAux(grafo, indiceOrigen, visitados, marcados);
        return visitados;
    }

    private static void recorridoProfundidadAux(GrafoDirigidoEtiquetado<Pozo> grafo, int verticeActual, ArrayList<Integer> visitados, boolean[] marcados) throws Exception {
        visitados.add(verticeActual);
        marcados[verticeActual] = true;
        for (Adyacencia ady : grafo.adycentes(verticeActual).toArray()) {
            int vecino = ady.getDestino();
            if (!marcados[vecino]) {
                recorridoProfundidadAux(grafo, vecino, visitados, marcados);
            }
        }
    }

    public static void guardarAdyacencias(GrafoDirigidoEtiquetado<Pozo> grafo, ListaDinamica<Adyacencia> listaAdyacencias) throws Exception {
        for (int i = 1; i <= grafo.num_vertice(); i++) {
            for (Adyacencia ady : grafo.adycentes(i).toArray()) {
                listaAdyacencias.Agregar(ady);
            }
        }
    }

    public static void cargarAdyacencias(GrafoDirigidoEtiquetado<Pozo> grafo, ListaDinamica<Adyacencia> listaAdyacencias) throws Exception {
        grafo.borrarAristas();
        for (Adyacencia ady : listaAdyacencias.toArray()) {
            grafo.insertar_arista(ady.getOrigen(), ady.getDestino(), ady.getPeso());
        }
    }
    
//    // Bellman-Ford
//    public static Map<Integer, Double> bellmanFord(GrafoDirigidoEtiquetado grafo, Integer origen) throws Exception {
//        Map<Integer, Double> distancias = new HashMap<>();
//        for (int i = 1; i <= grafo.num_vertice(); i++) {
//            distancias.put(i, Double.POSITIVE_INFINITY);
//        }
//        distancias.put(origen, 0.0);
//
//        for (int i = 1; i < grafo.num_vertice(); i++) {
//            for (int u = 1; u <= grafo.num_vertice(); u++) {
//                ListaDinamica<Adyacencia> adyacentes = grafo.adycentes(u);
//                for (int j = 0; j < adyacentes.getLongitud(); j++) {
//                    Adyacencia adyacente = adyacentes.getInfo(j);
//                    Integer v = adyacente.getDestino();
//                    Double pesoUV = adyacente.getPeso();
//                    if (distancias.get(u) + pesoUV < distancias.get(v)) {
//                        distancias.put(v, distancias.get(u) + pesoUV);
//                    }
//                }
//            }
//        }
//
//        for (int u = 1; u <= grafo.num_vertice(); u++) {
//            ListaDinamica<Adyacencia> adyacentes = grafo.adycentes(u);
//            for (int j = 0; j < adyacentes.getLongitud(); j++) {
//                Adyacencia adyacente = adyacentes.getInfo(j);
//                Integer v = adyacente.getDestino();
//                Double pesoUV = adyacente.getPeso();
//                if (distancias.get(u) + pesoUV < distancias.get(v)) {
//                    throw new Exception("El grafo contiene un ciclo de peso negativo");
//                }
//            }
//        }
//
//        return distancias;
//    }
//
//    // Floyd
//    public static Double[][] floyd(GrafoDirigidoEtiquetado grafo) throws Exception {
//        int n = grafo.num_vertice();
//        Double[][] distancias = new Double[n + 1][n + 1];
//
//        for (int i = 1; i <= n; i++) {
//            for (int j = 1; j <= n; j++) {
//                if (i == j) {
//                    distancias[i][j] = 0.0;
//                } else if (grafo.existe_arista(i, j)) {
//                    distancias[i][j] = grafo.peso_arista(i, j);
//                } else {
//                    distancias[i][j] = Double.POSITIVE_INFINITY;
//                }
//            }
//        }
//
//        for (int k = 1; k <= n; k++) {
//            for (int i = 1; i <= n; i++) {
//                for (int j = 1; j <= n; j++) {
//                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
//                        distancias[i][j] = distancias[i][k] + distancias[k][j];
//                    }
//                }
//            }
//        }
//
//        return distancias;
//    }
//
//    // Recorrido en anchura (BFS)
//    public static ListaDinamica<Integer> bfs(GrafoDirigidoEtiquetado grafo, Integer origen) throws Exception {
//        ListaDinamica<Integer> recorrido = new ListaDinamica<>();
//        Set<Integer> visitados = new HashSet<>();
//        Queue<Integer> cola = new LinkedList<>();
//
//        cola.add(origen);
//        visitados.add(origen);
//
//        while (!cola.isEmpty()) {
//            Integer actual = cola.poll();
//            recorrido.AgregarFinal(actual);
//
//            ListaDinamica<Adyacencia> adyacentes = grafo.adycentes(actual);
//            for (int i = 0; i < adyacentes.getLongitud(); i++) {
//                Integer vecino = adyacentes.getInfo(i).getDestino();
//                if (!visitados.contains(vecino)) {
//                    cola.add(vecino);
//                    visitados.add(vecino);
//                }
//            }
//        }
//
//        return recorrido;
//    }
//
//    // Recorrido en profundidad (DFS)
//    public static ListaDinamica<Integer> dfs(GrafoDirigidoEtiquetado grafo, Integer origen) throws Exception {
//        ListaDinamica<Integer> recorrido = new ListaDinamica<>();
//        Set<Integer> visitados = new HashSet<>();
//
//        dfsRecursivo(grafo, origen, visitados, recorrido);
//
//        return recorrido;
//    }
//
//    private static void dfsRecursivo(GrafoDirigidoEtiquetado grafo, Integer actual, Set<Integer> visitados, ListaDinamica<Integer> recorrido) throws Exception {
//        recorrido.AgregarFinal(actual);
//        visitados.add(actual);
//
//        ListaDinamica<Adyacencia> adyacentes = grafo.adycentes(actual);
//        for (int i = 0; i < adyacentes.getLongitud(); i++) {
//            Integer vecino = adyacentes.getInfo(i).getDestino();
//            if (!visitados.contains(vecino)) {
//                dfsRecursivo(grafo, vecino, visitados, recorrido);
//            }
//        }
//    }
}
