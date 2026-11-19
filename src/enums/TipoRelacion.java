package enums;

/**
 * Tipos de relacion entre usuarios de la red.
 * Responsable: Luis Silva
 */
public enum TipoRelacion {
    AMISTAD, // no dirigida: se guarda en los dos usuarios (Ana - Beto)
    SEGUIR   // dirigida: solo en el que sigue (Dani -> Ana)
}
