
package Controlador.Utiles;

import Controlador.TDA.Grafos.Grafo;
import Controlador.TDA.ListaDinamica.Excepcion.ListaVacia;
import Controlador.TDA.ListaDinamica.Excepcion.PosicionNoEncontrada;
import Controlador.TDA.ListaDinamica.ListaDinamica;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Elias
 */
public class UtilesControlador {
    
    private static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } 
            catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
    
    private static <T> boolean comparar(T elemento1, T elemento2, String campo, Integer orden) {
        try {
            Field field = getField(elemento1.getClass(), campo);
            field.setAccessible(true);

            if (Comparable.class.isAssignableFrom(field.getType())) {
                Comparable<Object> valor1 = (Comparable<Object>) field.get(elemento1);
                Comparable<Object> valor2 = (Comparable<Object>) field.get(elemento2);
                int resultadoComparacion = valor1.compareTo(valor2);
                return orden == 1 ? resultadoComparacion > 0 : resultadoComparacion < 0;
            } 
            else {
                throw new IllegalArgumentException("El campo no es comparable");
            }
        } 
        catch (IllegalAccessException e) {
            
            return false;
        }
    }
    
    public static <T> ListaDinamica<T> SelectSort(ListaDinamica<T> lista, Integer Orden, String Campo) throws ListaVacia, Exception {
        Integer n = lista.getLongitud();
        T[] elementos = lista.toArray();
        Field atributo = getField(elementos[0].getClass(), Campo);

        if (atributo != null) {
            for (int i = 0; i < n - 1; i++) {
                int k = i;
                T elementoOrden = elementos[i];

                for (int j = i + 1; j < n; j++) {
                    if (comparar(elementos[j], elementoOrden, Campo, Orden)) {
                        elementoOrden = elementos[j];
                        k = j;
                    }
                }
                elementos[k] = elementos[i];
                elementos[i] = elementoOrden;
            }
        } 
        else {
            throw new Exception("No existe el criterio de búsqueda");
        }

        return lista.toList(elementos);
    }

    public static <T> ListaDinamica<T> ShellSort(ListaDinamica<T> lista, Integer Orden, String Campo) {
        int n = lista.getLongitud();
        T[] elementos = lista.toArray();

        for (int intervalo = n / 2; intervalo > 0; intervalo /= 2) {
            for (int i = intervalo; i < n; i++) {
                T ayuda = elementos[i];
                int j;
                for (j = i; j >= intervalo && comparar(elementos[j - intervalo], ayuda, Campo, Orden); j -= intervalo) {
                    elementos[j] = elementos[j - intervalo];
                }
                elementos[j] = ayuda;
            }
        }
        return lista.toList(elementos);
    }

    public static <T> ListaDinamica<T> QuickSort(ListaDinamica<T> lista, Integer Orden, String Campo) throws ListaVacia, PosicionNoEncontrada {
        if (lista == null || lista.getLongitud() <= 1) {
            return lista;
        }
        QuickSortRecursivo(lista, 0, lista.getLongitud() - 1, Orden, Campo);
        return lista;
    }

    private static <T> void QuickSortRecursivo(ListaDinamica<T> lista, int inicio, int fin, Integer orden, String Campo) throws ListaVacia, PosicionNoEncontrada {
        if (inicio < fin) {
            int indiceParticion = Particionar(lista, inicio, fin, orden, Campo);
            QuickSortRecursivo(lista, inicio, indiceParticion - 1, orden, Campo);
            QuickSortRecursivo(lista, indiceParticion + 1, fin, orden, Campo);
        }
    }

    private static <T> int Particionar(ListaDinamica<T> lista, int inicio, int fin, Integer orden, String Campo) throws ListaVacia, PosicionNoEncontrada {
        T pivote = lista.getInfo(fin);
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (comparar(pivote, lista.getInfo(j), Campo, orden)) {
                i++;
                Intercambiar(lista, i, j);
            }
        }
        Intercambiar(lista, i + 1, fin);
        return i + 1;
    }

    private static <T> void Intercambiar(ListaDinamica<T> lista, int i, int j) throws ListaVacia, PosicionNoEncontrada {
        T ayuda = lista.getInfo(i);
        lista.modificarPosicion(lista.getInfo(j), i);
        lista.modificarPosicion(ayuda, j);
    }

    public static <T> ListaDinamica<T> BusquedaBinaria(ListaDinamica<T> lista, String Busqueda, String Campo) throws ListaVacia, PosicionNoEncontrada {
        ListaDinamica<T> listaOrdenada = QuickSort(lista, 1, Campo);
        ListaDinamica<T> ListaElementos = new ListaDinamica<>();

        boolean encontrado = false;
        int inicio = 0;
        int fin = listaOrdenada.getLongitud() - 1;

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            T Mitad = listaOrdenada.getInfo(medio);
            Field campo = getField(Mitad.getClass(), Campo);

            try {
                campo.setAccessible(true);
                Object ObjetoMitad = campo.get(Mitad);
                String valorMedio = (ObjetoMitad != null) ? ObjetoMitad.toString() : "";
                if (valorMedio.toLowerCase().contains(Busqueda.trim().toLowerCase())) {
                    encontrado = true;
                    ListaElementos.AgregarFinal(Mitad);
                    int j = medio - 1;
                    while (j >= 0) {
                        T elementoAnterior = listaOrdenada.getInfo(j);
                        Object valorAnteriorObj = campo.get(elementoAnterior);
                        String valorAnterior = (valorAnteriorObj != null) ? valorAnteriorObj.toString() : "";

                        if (valorAnterior.toLowerCase().contains(Busqueda.trim().toLowerCase())) {
                            ListaElementos.AgregarFinal(elementoAnterior);
                            j--;
                        } 
                        else {
                            break;
                        }
                    }
                    int i = medio + 1;
                    while (i < listaOrdenada.getLongitud()) {
                        T elementoSiguiente = listaOrdenada.getInfo(i);
                        Object valorSiguienteObj = campo.get(elementoSiguiente);
                        String valorSiguiente = (valorSiguienteObj != null) ? valorSiguienteObj.toString() : "";

                        if (valorSiguiente.toLowerCase().contains(Busqueda.trim().toLowerCase())) {
                            ListaElementos.AgregarFinal(elementoSiguiente);
                            i++;
                        } 
                        else {
                            break;
                        }
                    }
                    break;
                } 
                else {
                    String valorABuscar = Busqueda;
                    if (valorMedio.compareToIgnoreCase(valorABuscar) > 0) {
                        fin = medio - 1;
                    } 
                    else {
                        inicio = medio + 1;
                    }
                }
            } 
            catch (IllegalAccessException e) {
                
            }
        }
        return ListaElementos;
    }
    
    public static <T> ListaDinamica<T> BusquedaLineal(ListaDinamica<T> lista, String busqueda, String campo) throws ListaVacia, PosicionNoEncontrada {
        ListaDinamica<T> resultado = new ListaDinamica<>();
        Integer ultimaPosicionOcupada = lista.getLongitud();

        for (int i = 0; i < ultimaPosicionOcupada; i++) {
            T elemento = lista.getInfo(i);
            try {
                Field campoObjeto = getField(elemento.getClass(), campo);

                if (campoObjeto != null) {
                    campoObjeto.setAccessible(true);
                    Object valorObj = campoObjeto.get(elemento);
                    String valorCampo = (valorObj != null) ? valorObj.toString() : "";
                    if (buscarTipoEspecifico(valorCampo, busqueda)) {
                        resultado.AgregarFinal(elemento);
                        continue;
                    }
                }

                String[] subcampos = campo.split("\\.");
                Object objetoActual = elemento;

                for (String subcampo : subcampos) {
                    Field subcampoObjeto = getField(objetoActual.getClass(), subcampo);

                    if (subcampoObjeto != null) {
                        subcampoObjeto.setAccessible(true);
                        objetoActual = subcampoObjeto.get(objetoActual);
                    } 
                    else {
                        objetoActual = null;
                        break;
                    }
                }
                if (objetoActual != null) {
                    String valorCampo = objetoActual.toString();

                    if (buscarTipoEspecifico(valorCampo, busqueda)) {
                        resultado.AgregarFinal(elemento);
                    }
                }
            } 
            catch (IllegalAccessException e) {
                
            }
        }
        return resultado;
    }
    
    private static boolean buscarTipoEspecifico(String texto, String busqueda) {
        String textoSinEspacios = texto.replaceAll("\\s", "").replaceAll("[^a-zA-Z0-9]", "");
        String busquedaSinEspacios = busqueda.replaceAll("\\s", "").replaceAll("[^a-zA-Z0-9]", "");
        return textoSinEspacios.toLowerCase().startsWith(busquedaSinEspacios.toLowerCase());
    }
    
    public static double coordGpsToKm(double lat1, double lon1, double lat2, double lon2) {
        double lat1rad = Math.toRadians(lat1);
        double lon1rad = Math.toRadians(lon1);
        double lat2rad = Math.toRadians(lat2);
        double lon2rad = Math.toRadians(lon2);

        double difLatitud = lat1rad - lat2rad;
        double difLongitud = lon1rad - lon2rad;

        double a = Math.pow(Math.sin(difLatitud / 2), 2)
                + Math.cos(lat1rad)
                * Math.cos(lat2rad)
                * Math.pow(Math.sin(difLongitud / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double radioTierraKm = 6378.0;
        double distancia = radioTierraKm * c;

        return distancia;
    }
    
    
    public static void copiarArchivo(File origen, File destino) throws Exception {
        Files.copy(origen.toPath(),(destino).toPath(),StandardCopyOption.REPLACE_EXISTING);
    }
    
    public static String extension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
    
    private static final int INF = Integer.MAX_VALUE;

    public static void bellmanFord(int[][] grafo, int origen) {
        int V = grafo.length;
        int[] distancia = new int[V];
        for (int i = 0; i < V; i++) {
            distancia[i] = INF;
        }
        distancia[origen] = 0;

        for (int i = 1; i < V; ++i) {
            for (int u = 0; u < V; ++u) {
                for (int v = 0; v < V; ++v) {
                    if (grafo[u][v] != 0 && distancia[u] != INF && distancia[u] + grafo[u][v] < distancia[v]) {
                        distancia[v] = distancia[u] + grafo[u][v];
                    }
                }
            }
        }

        for (int u = 0; u < V; ++u) {
            for (int v = 0; v < V; ++v) {
                if (grafo[u][v] != 0 && distancia[u] != INF && distancia[u] + grafo[u][v] < distancia[v]) {
                    System.out.println("El grafo contiene ciclos de peso negativo");
                    return;
                }
            }
        }

        for (int i = 0; i < V; i++) {
            System.out.println("Distancia desde el nodo " + origen + " al nodo " + i + " es " + distancia[i]);
        }
    }

    public static void floydWarshall(int[][] grafo) {
        int V = grafo.length;
        int[][] distancias = new int[V][V];

        // Inicializar distancias con INF
        for (int i = 0; i < V; ++i) {
            for (int j = 0; j < V; ++j) {
                if (i == j) {
                    distancias[i][j] = 0;
                } else if (grafo[i][j] != 0) {
                    distancias[i][j] = grafo[i][j];
                } else {
                    distancias[i][j] = INF;
                }
            }
        }

        // Calcular las distancias mínimas entre cada par de vértices
        for (int k = 0; k < V; ++k) {
            for (int i = 0; i < V; ++i) {
                for (int j = 0; j < V; ++j) {
                    if (distancias[i][k] != INF && distancias[k][j] != INF && distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }

        // Imprimir las distancias mínimas
        for (int i = 0; i < V; ++i) {
            for (int j = 0; j < V; ++j) {
                if (distancias[i][j] == INF) {
                    System.out.print("INF ");
                } else {
                    System.out.print(distancias[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    
}