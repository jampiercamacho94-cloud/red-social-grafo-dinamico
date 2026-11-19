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

    // bajas

    /**
     * Elimina al usuario y TODAS sus conexiones: sus amistades, a quien sigue y quien lo sigue.
     * Las aristas que llegan a el estan en las listas de los demas, por eso se revisan todas.
     */
    public boolean eliminarPersona(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return false;
        }
        for (Vertice w = cabeza; w != null; w = w.getSig()) {
            if (w != v) {
                quitarTodasHacia(w, v);
            }
        }
        v.setAristas(null);
        if (cabeza == v) {
            cabeza = v.getSig();
        } else {
            Vertice ant = cabeza;
            while (ant.getSig() != v) {
                ant = ant.getSig();
            }
            ant.setSig(v.getSig());
        }
        return true;
    }

    // consultas

    /** Amigos del usuario separados por coma ("" si no tiene, null si no existe). */
    public String listarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : listar(v, TipoRelacion.AMISTAD);
    }

    /** A quienes sigue el usuario ("" si a nadie, null si no existe). */
    public String listarSeguidos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : listar(v, TipoRelacion.SEGUIR);
    }

    /** Quienes siguen al usuario ("" si nadie, null si no existe). Hay que revisar a todos. */
    public String listarSeguidores(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Vertice w = cabeza; w != null; w = w.getSig()) {
            if (buscarArista(w, v, TipoRelacion.SEGUIR) != null) {
                agregar(sb, w.getPersona().getUsuario());
            }
        }
        return sb.toString();
    }

    /** Cantidad de amigos (grado de amistad), -1 si no existe. */
    public int contarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return -1;
        }
        int total = 0;
        for (Arista e = v.getAristas(); e != null; e = e.getSig()) {
            if (e.getTipo() == TipoRelacion.AMISTAD) {
                total++;
            }
        }
        return total;
    }

    /** Cada amistad esta guardada dos veces, por eso se divide para 2. */
    public int totalAmistades() {
        return contarAristas(TipoRelacion.AMISTAD) / 2;
    }

    public int totalSeguimientos() {
        return contarAristas(TipoRelacion.SEGUIR);
    }

    /** Una linea por usuario con sus amigos, a quien sigue y quien lo sigue. */
    public String mostrarRed() {
        if (cabeza == null) {
            return "(red vacia)";
        }
        StringBuilder sb = new StringBuilder();
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            String u = v.getPersona().getUsuario();
            sb.append(String.format("%-7s amigos: [%s] | sigue a: [%s] | lo siguen: [%s]%n",
                    u, listar(v, TipoRelacion.AMISTAD), listar(v, TipoRelacion.SEGUIR), listarSeguidores(u)));
        }
        return sb.toString().stripTrailing();
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

    /** Quita de la lista de w todas las aristas (de cualquier tipo) que apuntan a v. */
    private void quitarTodasHacia(Vertice w, Vertice v) {
        while (w.getAristas() != null && w.getAristas().getDestino() == v) {
            w.setAristas(w.getAristas().getSig());
        }
        for (Arista e = w.getAristas(); e != null && e.getSig() != null; ) {
            if (e.getSig().getDestino() == v) {
                e.setSig(e.getSig().getSig());
            } else {
                e = e.getSig();
            }
        }
    }

    private String listar(Vertice v, TipoRelacion tipo) {
        StringBuilder sb = new StringBuilder();
        for (Arista e = v.getAristas(); e != null; e = e.getSig()) {
            if (e.getTipo() == tipo) {
                agregar(sb, e.getDestino().getPersona().getUsuario());
            }
        }
        return sb.toString();
    }

    private static void agregar(StringBuilder sb, String texto) {
        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb.append(texto);
    }

    private int contarAristas(TipoRelacion tipo) {
        int total = 0;
        for (Vertice v = cabeza; v != null; v = v.getSig()) {
            for (Arista e = v.getAristas(); e != null; e = e.getSig()) {
                if (e.getTipo() == tipo) {
                    total++;
                }
            }
        }
        return total;
    }
}
