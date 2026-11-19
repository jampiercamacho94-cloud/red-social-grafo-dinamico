package modelo;

/**
 * Datos de un usuario de la red social.
 * Responsable: Luis Silva
 */
public class Persona {

    private final String usuario; // ID corto: A, B, C, D
    private final String nombre;  // Ana, Beto, Carlos, Dani

    public Persona(String usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre + " (" + usuario + ")";
    }
}
