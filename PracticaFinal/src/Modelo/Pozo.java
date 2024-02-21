
package Modelo;

/**
 *
 * @author Elias
 */
public class Pozo {
    private Integer Id;
    private String Nombre;
    private String Foto;
    private Ubicacion ubicacionPozo;

    public Pozo() {
        
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    public String getFoto() {
        return Foto;
    }

    public void setFoto(String Foto) {
        this.Foto = Foto;
    }

    public Ubicacion getUbicacionPozo() {
        return ubicacionPozo;
    }

    public void setUbicacionPozo(Ubicacion ubicacionPozo) {
        this.ubicacionPozo = ubicacionPozo;
    }

    @Override
    public String toString() {
        return Nombre;
    }
    
    
}
