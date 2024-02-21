
package Vista;

import Controlador.Dao.Modelo.PozoDao;
import Controlador.TDA.ListaDinamica.Excepcion.ListaVacia;
import Controlador.TDA.ListaDinamica.ListaDinamica;
import Modelo.Pozo;
import Modelo.Ubicacion;
import Vista.ModeloTabla.ModeloTablaPozo;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Elias
 */
public class RegistroPozoLuz extends javax.swing.JFrame {
    PozoDao pozoControlDao = new PozoDao(Modelo.Pozo.class);
    ModeloTablaPozo mtp = new ModeloTablaPozo();
    ListaDinamica<Pozo> listaPozo = new ListaDinamica<>();
    private File ImagenPortada;
    private File ImagenLogo;
    private String rutaImagenGuardadaPortada;
    private String rutaImagenGuardadaLogo;

    /**
     * Creates new form VistaRegistroPizzeria
     */
    public RegistroPozoLuz() {
        initComponents();
        this.setLocationRelativeTo(null);
        CargarTabla();
    }
        
    private void CargarTabla() {
        mtp.setPozoTabla(pozoControlDao.getListaPozo());
        tblPozo.setModel(mtp);
        tblPozo.updateUI();
    }
        
    private void Limpiar() throws ListaVacia {
        txtNombre.setText("");
        txtLongitud.setText("");
        txtLatitud.setText("");
        txtImagen.setText("");
        pozoControlDao.setPozo(null);
        CargarTabla();
    }
        
    private void Guardar() throws ListaVacia {

        if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Porfavor ingrese el nombre del pozo");
        }
        else if(txtLongitud.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Porfavor ingrese la longitud del pozo");
        }
        else if(txtLatitud.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Porfavor ingrese la latitud del pozo");
        }
        else if(txtImagen.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Porfavor ingrese la imagen del pozo");
        }
        else{
            Integer idPozo = listaPozo.getLongitud()+1;
            String Nombre = txtNombre.getText();
            String Imagen = txtImagen.getText();

            Double Longitud = Double.valueOf(txtLongitud.getText());
            Double Latitud = Double.valueOf(txtLatitud.getText());

            Pozo nuevoPozo = new Pozo();
            nuevoPozo.setId(idPozo);
            nuevoPozo.setNombre(Nombre);
            nuevoPozo.setFoto(Imagen);

            Ubicacion nuevaUbicacion = new Ubicacion();
            nuevaUbicacion.setIdUbicacion(idPozo);
            nuevaUbicacion.setLongitud(Longitud);
            nuevaUbicacion.setLatitud(Latitud);

            nuevoPozo.setUbicacionPozo(nuevaUbicacion);
            pozoControlDao.setPozo(nuevoPozo);
            
            if (pozoControlDao.Persist()) {
                JOptionPane.showMessageDialog(null, "POZO GUARDADA EXISTOSAMENTE", "INFORMACION", JOptionPane.INFORMATION_MESSAGE);
//                CargarTabla();
//                Limpiar();
                pozoControlDao.setPozo(null);
            } 
            else {
                JOptionPane.showMessageDialog(null, "NO SE PUEDE GUARDAR", "INFORMACION", JOptionPane.INFORMATION_MESSAGE);
            }
            Limpiar();
        }
    }
    
    private void Seleccionar(){
        int fila = tblPozo.getSelectedRow();
        if(fila < 0){
            JOptionPane.showMessageDialog(null, "Escoga un registro");
        }
        else{
            try {
                pozoControlDao.setPozo(mtp.getPozoTabla().getInfo(fila));
                
                txtNombre.setText(pozoControlDao.getPozo().getNombre());
                txtImagen.setText(pozoControlDao.getPozo().getFoto());
                txtLongitud.setText(pozoControlDao.getPozo().getUbicacionPozo().getLongitud().toString());
                txtLatitud.setText(pozoControlDao.getPozo().getUbicacionPozo().getLatitud().toString());
                
            } 
            catch (Exception e) {
                
            }
        }
    }
    
    private File CargarFoto() throws Exception {
        File obj = null;
        JFileChooser choosser = new JFileChooser();

        String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
        choosser.setCurrentDirectory(new File(downloadsPath));

        choosser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagenes", "jpg", "png", "jpeg");
        choosser.addChoosableFileFilter(filter);

        Integer resp = choosser.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            obj = choosser.getSelectedFile();
            System.out.println("Foto cargada correctamente");
        } 
        else {
            System.out.println("No se puede cargar la imagen");
        }
        return obj;
    }
    
    private void mostrarImagenEnVentana(String rutaImagen) {
        try {
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                Imagen f = new Imagen();
                f.mostrarImagen(rutaImagen);
                f.setVisible(true);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al mostrar la imagen", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtLongitud = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtLatitud = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtImagen = new javax.swing.JTextField();
        btnSubirImagen = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPozo = new javax.swing.JTable();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 204));
        jPanel1.setForeground(new java.awt.Color(0, 0, 255));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nombre");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Longitud");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Latitud");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Imagen");

        txtImagen.setEditable(false);
        txtImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtImagenMouseClicked(evt);
            }
        });

        btnSubirImagen.setBackground(new java.awt.Color(0, 0, 0));
        btnSubirImagen.setForeground(new java.awt.Color(255, 255, 255));
        btnSubirImagen.setText("Subir imagen");
        btnSubirImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirImagenActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Registrar pozo de luz");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("LISTA DE POZOS");

        tblPozo.setForeground(new java.awt.Color(0, 0, 0));
        tblPozo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblPozo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPozoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPozo);

        btnModificar.setBackground(new java.awt.Color(0, 0, 0));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("Modificar seleccionado");
        btnModificar.setToolTipText("");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 0, 0));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Registrar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Ver grafo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGuardar))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtLongitud, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtLatitud)
                                        .addComponent(txtImagen)))
                                .addComponent(btnSubirImagen, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnModificar, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtLatitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSubirImagen)
                        .addGap(354, 354, 354))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificar)
                    .addComponent(btnGuardar)
                    .addComponent(jButton1))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubirImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirImagenActionPerformed
        
        try {
            ImagenLogo = CargarFoto();
            if (ImagenLogo != null) {
                txtImagen.setText(ImagenLogo.getAbsolutePath());
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la imagen", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btnSubirImagenActionPerformed

    private void txtImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtImagenMouseClicked
        
        if (evt.getClickCount() == 2) {
            mostrarImagenEnVentana(txtImagen.getText());
        }
        
    }//GEN-LAST:event_txtImagenMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        try {
            Guardar();
        } 
        catch (Exception e) {
        
        }
        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

        int fila = tblPozo.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Escoga un registro");
        } 
        else {
            if (txtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Porfavor ingrese el nombre del pozo");
            } 
            else if (txtLongitud.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Porfavor ingrese la longitud del pozo");
            }
            else if (txtLatitud.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Porfavor ingrese la latitud del pozo");
            } 
            else if (txtImagen.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Porfavor ingrese el logo del pozo");
            } 
            else {
                Integer IdPersona = pozoControlDao.getPozo().getId();
                String Nombre = txtNombre.getText();
                String Logo = txtImagen.getText();

                Double Longitud = Double.valueOf(txtLongitud.getText());
                Double Latitud = Double.valueOf(txtLatitud.getText());

                Pozo pozoModificado = new Pozo();
                pozoModificado.setNombre(Nombre);
                pozoModificado.setFoto(Logo);

                Ubicacion up = new Ubicacion();
                up.setIdUbicacion(IdPersona);
                up.setLatitud(Latitud);
                up.setLongitud(Longitud);
                pozoModificado.setUbicacionPozo(up);

                pozoControlDao.Merge(pozoModificado, IdPersona - 1);

                CargarTabla();

                try {
                    Limpiar();
                } 
                catch (Exception e) {

                }
            }
        }
        
    }//GEN-LAST:event_btnModificarActionPerformed

    private void tblPozoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPozoMouseClicked
        Seleccionar();
    }//GEN-LAST:event_tblPozoMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
        try {
            GrafoPozo vgp = new GrafoPozo();
            vgp.setVisible(true);
            this.setVisible(false);
        } 
        catch (Exception e) {
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(RegistroPozoLuz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistroPozoLuz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistroPozoLuz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistroPozoLuz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistroPozoLuz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSubirImagen;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPozo;
    private javax.swing.JTextField txtImagen;
    private javax.swing.JTextField txtLatitud;
    private javax.swing.JTextField txtLongitud;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
