package modelo;

/**
 * Nodo de la lista de personas (vertice del grafo).
 * Cada vertice tiene su propia lista de relaciones (aristas).
 * Responsable: Luis Silva
 */
public class Vertice {

    private final Persona persona;
    private Arista aristas; // primera relacion; null si no tiene ninguna
    private Vertice sig;    // siguiente persona de la red

    public Vertice(Persona persona) {
        this.persona = persona;
        this.aristas = null;
        this.sig = null;
    }

    public Persona getPersona() {
        return persona;
    }

    public Arista getAristas() {
        return aristas;
    }

    public void setAristas(Arista aristas) {
        this.aristas = aristas;
    }

    public Vertice getSig() {
        return sig;
    }

    public void setSig(Vertice sig) {
        this.sig = sig;
    }

    @Override
    public String toString() {
        return persona.toString();
    }
}
