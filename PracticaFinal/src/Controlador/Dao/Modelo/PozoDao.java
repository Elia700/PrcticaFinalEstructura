
package Controlador.Dao.Modelo;

import static Controlador.Dao.Bridge.getConection;
import Controlador.Dao.DaoImplement;
import Controlador.TDA.Grafos.GrafoNoDirigidoEtiquetado;
import Controlador.TDA.ListaDinamica.ListaDinamica;
import Modelo.Pozo;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Elias
 */
public class PozoDao extends DaoImplement<Pozo>{
    
    public PozoDao(Class<Pozo> clazz) {
        super(Pozo.class);
    }

    public PozoDao() {
        super(Pozo.class);
    }
    
    private ListaDinamica<Pozo> ListaPozo = new ListaDinamica<>();
    private GrafoNoDirigidoEtiquetado<Pozo> grafoPozo;
    private Pozo pozo;

    public ListaDinamica<Pozo> getListaPozo() {
        if(ListaPozo.EstaVacio()){
            ListaPozo = all();
        }
        return ListaPozo;
    }

    public void setListaPozo(ListaDinamica<Pozo> ListaPozo) {
        this.ListaPozo = ListaPozo;
    }

    public GrafoNoDirigidoEtiquetado<Pozo> getGrafoPozo() {
        return grafoPozo;
    }

    public void setGrafoPozo(GrafoNoDirigidoEtiquetado<Pozo> grafoPozo) {
        this.grafoPozo = grafoPozo;
    }

    public Pozo getPozo() {
        if (pozo == null) {
            pozo = new Pozo();
        }
        return pozo;
    }

    public void setPozo(Pozo pozo) {
        this.pozo = pozo;
    }
    
    

    public GrafoNoDirigidoEtiquetado<Pozo> getGrafo () throws Exception {
        if (grafoPozo == null) {
            ListaDinamica<Pozo> list = getListaPozo();
            System.out.println(list);
            if (!list.EstaVacio()) {
                grafoPozo = new GrafoNoDirigidoEtiquetado<>(list.getLongitud(), Pozo.class);
                for (int i = 0; i < list.getLongitud(); i++) {
                    grafoPozo.etiquetarVertice((i + 1), list.getInfo(i));
                }
            }
        }
        
        return grafoPozo;
    }

    public Boolean Persist() {
        pozo.setId(all().getLongitud()+ 1);
        return Persist(pozo);
    }
    
    public void guardarGrafo() throws Exception{
        getConection().toXML(grafoPozo, new FileWriter("files/grafo.json"));
    }
    
    public void loadGrapg() throws Exception{
        
        grafoPozo = (GrafoNoDirigidoEtiquetado<Pozo>)getConection().fromXML(new FileReader("files/grafo.json"));
        ListaPozo.Limpiar();
        for(int i = 1; i <= grafoPozo.num_vertice(); i++){
            ListaPozo.Agregar(grafoPozo.getEtiquetaE(i));
        }
    }

    @Override
    public String toString() {
        return pozo.getNombre();
    }
}
