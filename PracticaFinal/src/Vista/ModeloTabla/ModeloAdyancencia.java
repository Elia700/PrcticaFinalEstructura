
package Vista.ModeloTabla;


import Controlador.TDA.Grafos.GrafoDirigidoEtiquetado;
import Controlador.Utiles.UtilesPozo;
import Modelo.Pozo;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Elias
 */
public class ModeloAdyancencia  extends AbstractTableModel {
    private GrafoDirigidoEtiquetado<Pozo> grafo;
    
    @Override
    public int getRowCount() {
        return grafo.num_vertice();
    }

    @Override
    public int getColumnCount() {
        return grafo.num_vertice()+1;
    }
    
    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "POZO";
        } 
        else {
            try {
                return grafo.getEtiquetaE(column).toString();
            } 
            catch (Exception e) {
                return "";
            }
        }
    }

    public GrafoDirigidoEtiquetado<Pozo> getGrafo() {
        return grafo;
    }

    public void setGrafo(GrafoDirigidoEtiquetado<Pozo> grafo) {
        this.grafo = grafo;
    }
    
    @Override
    public Object getValueAt(int i, int i1) {
        try {
            if (i1 == 0) {
                return grafo.getEtiquetaE(i + 1).toString();
            }
            else {
                Pozo o = grafo.getEtiquetaE(i + 1);
                Pozo d = grafo.getEtiquetaE(i1);
                if (grafo.isEdge(o, d)) {
                    return UtilesPozo.redondear(grafo.peso_arista(i + 1, i1)).toString();
                }
                else {
                    return "--";
                }
            }
        } 
        catch (Exception e) {
            return "";
        }
    }

    
}
//        if(column == 0){
//            return "Escuelas";
//        }
//        else{
//            try {
//                return grafo.getetiqeuta(column.toString);
//            } catch (Exception e) {
//                return "";
//            }
//        }
//        switch (column) {
//            case 0: 
//                return "#";
//            case 1: 
//                return "Nombre";
//            case 2:
//                return "Ruta escudo";
//            case 3:
//                return "Ruta portada";
//            case 4:
//                return "Longitud";
//            case 5:
//                return "Latitud";
//                
//            default:
//                return null;
//
//        }
//    }
//
