
package Vista.ModeloTabla;

import Controlador.TDA.ListaDinamica.ListaDinamica;
import Modelo.Pozo;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Elias
 */
public class ModeloTablaPozo extends AbstractTableModel {

    private ListaDinamica<Pozo> pozoTabla;

    public ListaDinamica<Pozo> getPozoTabla() {
        return pozoTabla;
    }

    public void setPozoTabla(ListaDinamica<Pozo> pozoTabla) {
        this.pozoTabla = pozoTabla;
    }

    @Override
    public int getRowCount() {
        return pozoTabla.getLongitud();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Pozo alumno = pozoTabla.getInfo(rowIndex);
            switch (columnIndex) {
                case 0:
                    return (alumno != null) ? alumno.getId(): "";
                case 1:
                    return (alumno != null) ? alumno.getNombre() : " ";
                case 2:
                    return (alumno != null) ? alumno.getFoto(): "";
                case 3:
                    return (alumno != null) ? alumno.getUbicacionPozo().getLongitud() : "";
                case 4:
                    return (alumno != null) ? alumno.getUbicacionPozo().getLatitud(): "";
                default:
                    return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: 
                return "Nro";
            case 1: 
                return "Nombre";
            case 2:
                return "Imagen";
            case 3:
                return "Longitud";
            case 4:
                return "Latitud";
                
            default:
                return null;

        }
    }
}
