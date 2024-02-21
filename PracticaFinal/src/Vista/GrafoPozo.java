/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Controlador.Dao.Modelo.PozoDao;
import Controlador.TDA.Grafos.Adyacencia;
import Controlador.TDA.Grafos.DibujarGrafo;
import Controlador.TDA.Grafos.Grafo;
import Controlador.TDA.ListaDinamica.Excepcion.ListaVacia;
import Controlador.TDA.ListaDinamica.ListaDinamica;
import Controlador.Utiles.UtilesPozo;
import Vista.ModeloTabla.ModeloAdyancencia;
import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Elias
 */
public class GrafoPozo extends javax.swing.JFrame {
    PozoDao pozoControlDao = new PozoDao();
    ModeloAdyancencia mtae = new ModeloAdyancencia();

    /**
     * Creates new form VistaGrafoPozo
     * @throws Controlador.TDA.ListaDinamica.Excepcion.ListaVacia
     */
    public GrafoPozo() throws ListaVacia{
        initComponents();
        this.setLocationRelativeTo(null);
        limpiar();
    }

    private void limpiar() throws ListaVacia {
        try {
//            UtilesPozo.cargarComboEscuela(cbxOrigen);
//            UtilesPozo.cargarComboEscuela(cbxDestino);
            cargarTabla();
        } 
        catch (Exception e) {

        }
    }

    private void cargarTabla() throws Exception {
        mtae.setGrafo(pozoControlDao.getGrafo());
        mtae.fireTableDataChanged();
        tblA.setModel(mtae);
        tblA.updateUI();
    }

    private void mostrarGrafo() throws Exception {
        DibujarGrafo p = new DibujarGrafo();
        p.updateFile(pozoControlDao.getGrafo());
        File nav = new File("d3/grafo.html");
        java.awt.Desktop.getDesktop().open(nav);
    }

    private void mostrarMapa() throws Exception {
        UtilesPozo.crearMapaEscuela(pozoControlDao.getGrafo());
        File nav = new File("mapas/index.html");
        java.awt.Desktop.getDesktop().open(nav);
    }
    
    public void conectarAleatoriamente() throws Exception {
        try {
            Random random = new Random();
            int v1 = random.nextInt(pozoControlDao.getListaPozo().getLongitud());
            int v2;

            do {
                v2 = random.nextInt(pozoControlDao.getListaPozo().getLongitud());
            } 
            while (v1 == v2);

            Double distancia = UtilesPozo.calcularDistanciaEscuelas(
                    pozoControlDao.getListaPozo().getInfo(v1),
                    pozoControlDao.getListaPozo().getInfo(v2)
            );
            distancia = UtilesPozo.redondear(distancia);

            pozoControlDao.getGrafo().insertEdgeE(
                    pozoControlDao.getListaPozo().getInfo(v1),
                    pozoControlDao.getListaPozo().getInfo(v2),
                    distancia
            );
        } 
        catch (Exception e) {
            mostrarMensajeError("No se pudo generar adecuadamente las adyacencias");
        }
        System.out.println(pozoControlDao.getGrafo().toString());
    }
    
    public void generarAdyacencias() throws ListaVacia, Exception {
        Random random = new Random();
        int maxAdyacencias = 2;

        for (int i = 0; i < pozoControlDao.getListaPozo().getLongitud(); i++) {
            int numAdyacencias = random.nextInt(maxAdyacencias - 1) + 2;

            ListaDinamica<Integer> disponibles = obtenerNodosDisponibles(i);

            for (int k = 0; k < numAdyacencias && !disponibles.EstaVacio(); k++) {
                int indiceAleatorio = random.nextInt(disponibles.getLongitud());
                int indiceNodo = disponibles.getInfo(indiceAleatorio);

                Double distancia = UtilesPozo.calcularDistanciaEscuelas(
                        pozoControlDao.getListaPozo().getInfo(i),
                        pozoControlDao.getListaPozo().getInfo(indiceNodo)
                );
                distancia = UtilesPozo.redondear(distancia);
                pozoControlDao.getGrafo().insertEdgeE(
                        pozoControlDao.getListaPozo().getInfo(i),
                        pozoControlDao.getListaPozo().getInfo(indiceNodo),
                        distancia
                );
                disponibles.eliminar(indiceAleatorio);
            }
        }
        pozoControlDao.guardarGrafo();
    }

//    public void generarAdyacencias() throws ListaVacia, Exception {
//        Random random = new Random();
//        int maxAdyacencias = 2;
//
//        for (int i = 0; i < pozoControlDao.getListaPozo().getLongitud(); i++) {
//            int numAdyacencias = random.nextInt(maxAdyacencias - 1) + 2;
//
//            ListaDinamica<Integer> disponibles = obtenerNodosDisponibles(i);
//
//            for (int k = 0; k < numAdyacencias && !disponibles.EstaVacio(); k++) {
//                int indiceAleatorio = random.nextInt(disponibles.getLongitud());
//                int indiceNodo = disponibles.getInfo(indiceAleatorio);
//
//                Double distancia = UtilesPozo.calcularDistanciaEscuelas(
//                        pozoControlDao.getListaPozo().getInfo(i),
//                        pozoControlDao.getListaPozo().getInfo(indiceNodo)
//                );
//                distancia = UtilesPozo.redondear(distancia);
//                pozoControlDao.getGrafo().insertEdgeE(
//                        pozoControlDao.getListaPozo().getInfo(i),
//                        pozoControlDao.getListaPozo().getInfo(indiceNodo),
//                        distancia
//                );
//                disponibles.eliminar(indiceAleatorio);
//            }
//        }
//    }

    private ListaDinamica<Integer> obtenerNodosDisponibles(int indiceActual) {
        ListaDinamica<Integer> disponibles = new ListaDinamica<>();
        for (int j = indiceActual + 1; j < pozoControlDao.getListaPozo().getLongitud(); j++) {
            disponibles.Agregar(j);
        }
        return disponibles;
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "INFORMACION", JOptionPane.INFORMATION_MESSAGE);
    }

    public static final double INF = Double.POSITIVE_INFINITY;

   public static String floydWarshall(Grafo grafo) throws Exception {
    int numVertices = grafo.num_vertice();
    double[][] distancias = new double[numVertices + 1][numVertices + 1];
    int[][] predecesores = new int[numVertices + 1][numVertices + 1];

    for (int i = 1; i <= numVertices; i++) {
        for (int j = 1; j <= numVertices; j++) {
            if (i == j) {
                distancias[i][j] = 0;
            } else if (grafo.existe_arista(i, j)) {
                distancias[i][j] = grafo.peso_arista(i, j);
            } else {
                distancias[i][j] = Double.POSITIVE_INFINITY;
            }
            predecesores[i][j] = -1;
        }
    }

    for (int k = 1; k <= numVertices; k++) {
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                if (distancias[i][k] != Double.POSITIVE_INFINITY && distancias[k][j] != Double.POSITIVE_INFINITY
                        && distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                    distancias[i][j] = distancias[i][k] + distancias[k][j];
                    predecesores[i][j] = k;
                }
            }
        }
    }

    StringBuilder resultado = new StringBuilder();
    resultado.append("Distancias mínimas entre todos los pares de vértices:\n");
    for (int i = 1; i <= numVertices; i++) {
        for (int j = 1; j <= numVertices; j++) {
            resultado.append(distancias[i][j] == Double.POSITIVE_INFINITY ? "INF\t" : distancias[i][j] + "\t");
        }
        resultado.append("\n");
    }
    return resultado.toString();
}

    public static String bellmanFord(Grafo grafo, int inicio) throws Exception {
    int numVertices = grafo.num_vertice();
    double[] distancias = new double[numVertices + 1];
    int[] predecesores = new int[numVertices + 1];

    for (int i = 1; i <= numVertices; i++) {
        distancias[i] = Double.POSITIVE_INFINITY;
        predecesores[i] = -1;
    }
    distancias[inicio] = 0;

    for (int i = 1; i <= numVertices - 1; i++) {
        for (int j = 1; j <= numVertices; j++) {
            ListaDinamica<Adyacencia> listaA = grafo.adycentes(j);
            if (listaA != null) {
                for (int k = 0; k < listaA.getLongitud(); k++) {
                    Adyacencia a = listaA.getInfo(k);
                    if (distancias[j] != Double.POSITIVE_INFINITY && distancias[j] + a.getPeso() < distancias[a.getDestino()]) {
                        distancias[a.getDestino()] = distancias[j] + a.getPeso();
                        predecesores[a.getDestino()] = j;
                    }
                }
            }
        }
    }

    StringBuilder resultado = new StringBuilder();
    resultado.append("Distancias mínimas desde el vértice ").append(inicio).append(":\n");
    for (int i = 1; i <= numVertices; i++) {
        resultado.append("Vértice ").append(i).append(": ").append(distancias[i]).append("\n");
    }
    return resultado.toString();
}
    
public ListaDinamica<Integer> recorridoAnchura(Integer v) throws Exception {
    ListaDinamica<Integer> recorrido = new ListaDinamica<>();
    ListaDinamica<Integer> cola = new ListaDinamica<>();
    ListaDinamica<Integer> visitados = new ListaDinamica<>();

    if (pozoControlDao.getGrafo() == null) {
        throw new Exception("Error al realizar el recorrido en anchura: El grafo es nulo.");
    }

    cola.Agregar(v);
    visitados.Agregar(v);

    while (!cola.EstaVacio()) {
        Integer u = cola.eliminar(0);
        recorrido.Agregar(u);

        ListaDinamica<Adyacencia> listaA = pozoControlDao.getGrafo().adycentes(u);
        if (listaA != null) {
            for (int i = 0; i < listaA.getLongitud(); i++) {
                Adyacencia a = listaA.getInfo(i);
                Integer w = a.getDestino();
                if (!visitados.contiene(w)) {
                    visitados.Agregar(w);
                    cola.Agregar(w);
                }
            }
        } else {
            throw new Exception(
                    "Error al realizar el recorrido en anchura: Lista de adyacentes nula para el vértice " + u);
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
            ListaDinamica<Adyacencia> listaA = pozoControlDao.getGrafo().adycentes(u);
            if (listaA != null) { // Agregar verificación de nulidad para adyacentes
                for (int i = 0; i < listaA.getLongitud(); i++) {
                    Adyacencia a = listaA.getInfo(i);
                    w = a.getDestino();
                    if (!visitados.contiene(w)) {
                        pila.Agregar(w);
                    }
                }
            } else {
                throw new Exception("Error al realizar el recorrido en profundidad: Lista de adyacentes nula.");
            }
        }
    }
    return recorrido;
}

    private void mostrarRecorridoEnTextArea(ListaDinamica<Integer> recorrido, javax.swing.JTextArea textArea) throws ListaVacia {
        textArea.setText(""); 
        for (int i = 0; i < recorrido.getLongitud(); i++) {
            textArea.append(recorrido.getInfo(i) + " -> ");
        }
    }
    
    private void mostrarResultadoEnTextArea(String resultado, javax.swing.JTextArea textArea) {
        textArea.setText(resultado);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnMostrarMapa = new javax.swing.JButton();
        btnMostrarGrafo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblA = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txaProfundidad = new javax.swing.JTextArea();
        btnProfundidad = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txaAnchura = new javax.swing.JTextArea();
        btnAnchura = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaFloyd = new javax.swing.JTextArea();
        btnFloyd = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaBellman = new javax.swing.JTextArea();
        btnBellman = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 255));
        jPanel1.setForeground(new java.awt.Color(51, 51, 255));

        btnMostrarMapa.setBackground(new java.awt.Color(102, 102, 102));
        btnMostrarMapa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnMostrarMapa.setText("Ver mapa");
        btnMostrarMapa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarMapaActionPerformed(evt);
            }
        });

        btnMostrarGrafo.setBackground(new java.awt.Color(102, 102, 102));
        btnMostrarGrafo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnMostrarGrafo.setText("Ver grafo");
        btnMostrarGrafo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarGrafoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Grafo de pozo de luz");

        jButton2.setBackground(new java.awt.Color(102, 102, 102));
        jButton2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton2.setText("Generar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tblA.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblA);

        txaProfundidad.setEditable(false);
        txaProfundidad.setColumns(20);
        txaProfundidad.setLineWrap(true);
        txaProfundidad.setRows(5);
        txaProfundidad.setAutoscrolls(false);
        txaProfundidad.setEnabled(false);
        jScrollPane5.setViewportView(txaProfundidad);

        btnProfundidad.setBackground(new java.awt.Color(102, 102, 102));
        btnProfundidad.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnProfundidad.setText("Busqueda en profundidad");
        btnProfundidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfundidadActionPerformed(evt);
            }
        });

        txaAnchura.setEditable(false);
        txaAnchura.setColumns(20);
        txaAnchura.setRows(5);
        txaAnchura.setWrapStyleWord(true);
        txaAnchura.setEnabled(false);
        jScrollPane4.setViewportView(txaAnchura);

        btnAnchura.setBackground(new java.awt.Color(102, 102, 102));
        btnAnchura.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnAnchura.setText("Busqueda en anchura");
        btnAnchura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnchuraActionPerformed(evt);
            }
        });

        txaFloyd.setEditable(false);
        txaFloyd.setColumns(20);
        txaFloyd.setRows(5);
        txaFloyd.setEnabled(false);
        jScrollPane3.setViewportView(txaFloyd);

        btnFloyd.setBackground(new java.awt.Color(102, 102, 102));
        btnFloyd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnFloyd.setText("Meodo floyd");
        btnFloyd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFloydActionPerformed(evt);
            }
        });

        txaBellman.setEditable(false);
        txaBellman.setColumns(20);
        txaBellman.setRows(5);
        txaBellman.setEnabled(false);
        jScrollPane2.setViewportView(txaBellman);

        btnBellman.setBackground(new java.awt.Color(102, 102, 102));
        btnBellman.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBellman.setText("Metodo bellman-Ford");
        btnBellman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBellmanActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(102, 102, 102));
        jButton1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton1.setText("Regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane5)
                            .addComponent(btnProfundidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4)
                            .addComponent(btnAnchura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3)
                            .addComponent(btnFloyd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(btnBellman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnProfundidad)
                        .addComponent(btnAnchura))
                    .addComponent(btnFloyd)
                    .addComponent(btnBellman))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(64, 64, 64))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMostrarMapa)
                        .addGap(18, 18, 18)
                        .addComponent(btnMostrarGrafo)
                        .addGap(16, 16, 16))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMostrarGrafo)
                    .addComponent(btnMostrarMapa)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBellmanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBellmanActionPerformed

        try {
            long tiempoInicio = System.currentTimeMillis();
            String resultado = bellmanFord(pozoControlDao.getGrafo(), 1);
            mostrarResultadoEnTextArea(resultado, txaBellman);
            long tiempoFin = System.currentTimeMillis();
            long tiempoEjecucion = tiempoFin - tiempoInicio;
            JOptionPane.showMessageDialog(null, "Tiempo de ejecución: " + tiempoEjecucion + " ms");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar el algoritmo de Bellman-Ford: " + ex.getMessage());
        }
        
    }//GEN-LAST:event_btnBellmanActionPerformed

    private void btnFloydActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFloydActionPerformed

        try {
            long tiempoInicio = System.currentTimeMillis();
            String resultado = floydWarshall(pozoControlDao.getGrafo());
            mostrarResultadoEnTextArea(resultado, txaFloyd);
            long tiempoFin = System.currentTimeMillis();
            long tiempoEjecucion = tiempoFin - tiempoInicio;
            JOptionPane.showMessageDialog(null, "Tiempo de ejecución: " + tiempoEjecucion + " ms");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar el algoritmo de Floyd-Warshall: " + ex.getMessage());
        }
        
    }//GEN-LAST:event_btnFloydActionPerformed

    private void btnMostrarMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarMapaActionPerformed

        try {
            mostrarMapa();
        }
        catch (Exception e) {
        }

    }//GEN-LAST:event_btnMostrarMapaActionPerformed

    private void btnMostrarGrafoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarGrafoActionPerformed

        try {
            mostrarGrafo();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No sirve el mostrar grafo", "INFORMACION", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnMostrarGrafoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        RegistroPozoLuz vp = new RegistroPozoLuz();
        vp.setVisible(true);
        this.setVisible(false);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        try {
            generarAdyacencias();
            cargarTabla();
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: ");
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnAnchuraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnchuraActionPerformed
        
        try {
            long tiempoInicio = System.currentTimeMillis();
            ListaDinamica<Integer> recorrido = recorridoAnchura( 1);
            mostrarRecorridoEnTextArea(recorrido, txaAnchura);
            long tiempoFin = System.currentTimeMillis();
            long tiempoEjecucion = tiempoFin - tiempoInicio;
            JOptionPane.showMessageDialog(null, "Tiempo de ejecución: " + tiempoEjecucion + " ms");
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al realizar el recorrido en anchura: " + ex.getMessage());
        }

    }//GEN-LAST:event_btnAnchuraActionPerformed

    private void btnProfundidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfundidadActionPerformed

        try {
            long tiempoInicio = System.currentTimeMillis();
            ListaDinamica<Integer> recorrido = recorridoProfundidad(1);
            mostrarRecorridoEnTextArea(recorrido, txaProfundidad);
            long tiempoFin = System.currentTimeMillis();
            long tiempoEjecucion = tiempoFin - tiempoInicio;
            JOptionPane.showMessageDialog(null, "Tiempo de ejecución: " + tiempoEjecucion + " ms");
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al realizar el recorrido en profundidad: " + ex.getMessage());
        }


    }//GEN-LAST:event_btnProfundidadActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GrafoPozo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GrafoPozo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GrafoPozo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GrafoPozo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GrafoPozo().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(GrafoPozo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnchura;
    private javax.swing.JButton btnBellman;
    private javax.swing.JButton btnFloyd;
    private javax.swing.JButton btnMostrarGrafo;
    private javax.swing.JButton btnMostrarMapa;
    private javax.swing.JButton btnProfundidad;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable tblA;
    private javax.swing.JTextArea txaAnchura;
    private javax.swing.JTextArea txaBellman;
    private javax.swing.JTextArea txaFloyd;
    private javax.swing.JTextArea txaProfundidad;
    // End of variables declaration//GEN-END:variables
}
