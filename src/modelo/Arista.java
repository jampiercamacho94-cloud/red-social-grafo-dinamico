package modelo;

import enums.TipoRelacion;

/**
 * Nodo de la lista de relaciones de un vertice.
 * Responsable: Luis Silva
 */
public class Arista {

    private final Vertice destino;   // persona con la que se relaciona
    private final TipoRelacion tipo; // AMISTAD o SEGUIR
    private Arista sig;              // siguiente relacion del mismo vertice

    public Arista(Vertice destino, TipoRelacion tipo) {
        this.destino = destino;
        this.tipo = tipo;
        this.sig = null;
    }

    public Vertice getDestino() {
        return destino;
    }

    public TipoRelacion getTipo() {
        return tipo;
    }

    public Arista getSig() {
        return sig;
    }

    public void setSig(Arista sig) {
        this.sig = sig;
    }

    @Override
    public String toString() {
        return tipo + " -> " + destino.getPersona().getNombre();
    }
}
