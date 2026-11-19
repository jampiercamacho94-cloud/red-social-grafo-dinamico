package negocio;

import enums.TipoRelacion;
import modelo.Arista;
import modelo.Persona;
import modelo.Vertice;

/**
 * Grafo dinamico de la red social: lista enlazada de vertices (usuarios) y,
 * colgada de cada uno, su lista enlazada de relaciones (aristas).
 * AMISTAD es no dirigida: se guarda en la lista de los dos usuarios.
 * SEGUIR es dirigida: se guarda solo en la lista de quien sigue.
 * Sin HashMap, HashSet, ArrayList ni LinkedList.
 *
 * Responsable: Damian Cunalata
 * Etapa 1: altas de usuarios y relaciones (amistad y seguir).
 */
public class RedSocial {

    private Vertice cabeza;   // primer usuario; null = red vacia

    // altas de usuarios

    /** Registra un usuario al final de la lista. Rechaza datos vacios y usuarios repetidos. */
    public boolean registrarPersona(String usuario, String nombre) {
        if (vacio(usuario) || vacio(nombre) || buscar(usuario) != null) {
            return false;
        }
        Vertice nuevo = new Vertice(new Persona(usuario.trim(), nombre.trim()));
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Vertice ultimo = cabeza;
            while (ultimo.getSig() != null) {
                ultimo = ultimo.getSig();
            }
            ultimo.setSig(nuevo);
        }
        return true;
    }

    public boolean existePersona(String usuario) {
        return buscar(usuario) != null;
    }

    public int totalPersonas() {
        int total = 0;
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            total++;
        }
        return total;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    // relaciones

    /** Crea la amistad en los dos sentidos. Rechaza lazo, usuario inexistente y amistad repetida. */
    public boolean agregarAmistad(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || buscarArista(va, vb, TipoRelacion.AMISTAD) != null) {
            return false;
        }
        insertarArista(va, vb, TipoRelacion.AMISTAD);
        insertarArista(vb, va, TipoRelacion.AMISTAD);
        return true;
    }

    /** Quita la amistad de los dos lados. */
    public boolean eliminarAmistad(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || buscarArista(va, vb, TipoRelacion.AMISTAD) == null) {
            return false;
        }
        quitarArista(va, vb, TipoRelacion.AMISTAD);
        quitarArista(vb, va, TipoRelacion.AMISTAD);
        return true;
    }

    /** a empieza a seguir a b (un solo sentido). Rechaza lazo, usuario inexistente y seguimiento repetido. */
    public boolean seguir(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || buscarArista(va, vb, TipoRelacion.SEGUIR) != null) {
            return false;
        }
        insertarArista(va, vb, TipoRelacion.SEGUIR);
        return true;
    }

    /** a deja de seguir a b. */
    public boolean dejarDeSeguir(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        return va != null && vb != null && quitarArista(va, vb, TipoRelacion.SEGUIR);
    }

    public boolean sonAmigos(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        return va != null && vb != null && buscarArista(va, vb, TipoRelacion.AMISTAD) != null;
    }

    public boolean sigueA(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        return va != null && vb != null && buscarArista(va, vb, TipoRelacion.SEGUIR) != null;
    }

    // auxiliares

    private static boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }

    /** Busca sin importar mayusculas ni espacios: "Ana" y " ana " son el mismo usuario. */
    private Vertice buscar(String usuario) {
        if (vacio(usuario)) {
            return null;
        }
        String clave = usuario.trim();
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            if (v.getPersona().getUsuario().equalsIgnoreCase(clave)) {
                return v;
            }
        }
        return null;
    }

    private Arista buscarArista(Vertice origen, Vertice destino, TipoRelacion tipo) {
        for (Arista e = origen.getAristas(); e != null; e = e.getSig()) {
            if (e.getDestino() == destino && e.getTipo() == tipo) {
                return e;
            }
        }
        return null;
    }

    /** Inserta al final para que los listados salgan en el orden en que se crearon. */
    private void insertarArista(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista nueva = new Arista(destino, tipo);
        if (origen.getAristas() == null) {
            origen.setAristas(nueva);
            return;
        }
        Arista ultima = origen.getAristas();
        while (ultima.getSig() != null) {
            ultima = ultima.getSig();
        }
        ultima.setSig(nueva);
    }

    private boolean quitarArista(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista ant = null;
        for (Arista e = origen.getAristas(); e != null; ant = e, e = e.getSig()) {
            if (e.getDestino() == destino && e.getTipo() == tipo) {
                if (ant == null) {
                    origen.setAristas(e.getSig());
                } else {
                    ant.setSig(e.getSig());
                }
                return true;
            }
        }
        return false;
    }
}
