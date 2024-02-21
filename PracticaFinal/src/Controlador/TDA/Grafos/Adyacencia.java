
package Controlador.TDA.Grafos;

/**
 *
 * @author Elias
 */
public class Adyacencia {
    private Integer Origen;
    private Integer Destino;
    private Double Peso;

    public Adyacencia() {
        
    }

    public Adyacencia(Integer Destino, Double Peso) {
        this.Destino = Destino;
        this.Peso = Peso;
    }    

    public Adyacencia(Integer Origen, Integer Destino, Double Peso) {
        this.Origen = Origen;
        this.Destino = Destino;
        this.Peso = Peso;
    }

    public Integer getOrigen() {
        return Origen;
    }

    public void setOrigen(Integer Origen) {
        this.Origen = Origen;
    }
    

    public Integer getDestino() {
        return Destino;
    }

    public void setDestino(Integer Destino) {
        this.Destino = Destino;
    }

    public Double getPeso() {
        return Peso;
    }

    public void setPeso(Double Peso) {
        this.Peso = Peso;
    }
    
}
