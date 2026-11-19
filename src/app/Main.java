package app;

import negocio.RedSocial;

/**
 * Demo de la actividad en el orden del moderador:
 * 1) crea a Ana, Beto, Carlos y Dani; 2) crea amistades y seguimientos;
 * 3) aplica las modificaciones del moderador; 4) elimina a Carlos; 5) muestra la red final.
 *
 * Responsable: Darwin Tisalema
 */
public class Main {

    /* Demostracion de las cuatro reglas indicadas para Joel en docs/REPARTO.md. */
    private static final CambioModerador[] CAMBIOS_MODERADOR = {
        new CambioModerador("Agregar amistad Ana - Ana", Operacion.AGREGAR_AMISTAD, "A", "A"),
        new CambioModerador("Agregar amistad Ana - Beto", Operacion.AGREGAR_AMISTAD, "A", "B"),
        new CambioModerador("Quitar amistad Carlos - Dani", Operacion.QUITAR_AMISTAD, "C", "D"),
        new CambioModerador("Quitar seguimiento Carlos -> Ana", Operacion.DEJAR_DE_SEGUIR, "C", "A")
    };

    private enum Operacion {
        AGREGAR_AMISTAD,
        QUITAR_AMISTAD,
        SEGUIR,
        DEJAR_DE_SEGUIR
    }

    private record CambioModerador(
            String indicacion,
            Operacion operacion,
            String origen,
            String destino) {
    }

    public static void main(String[] args) {
        RedSocial red = new RedSocial();

        titulo(1, "Registrar usuarios");
        registrar(red, "A", "Ana");
        registrar(red, "B", "Beto");
        registrar(red, "C", "Carlos");
        registrar(red, "D", "Dani");
        mostrarRed(red);

        titulo(2, "Crear la red inicial acordada");
        crearRedInicial(red);
        mostrarRed(red);

        titulo(3, "Aplicar cambios del moderador");
        aplicarCambiosDelModerador(red);
        mostrarRed(red);

        titulo(4, "Eliminar a Carlos y todas sus conexiones");
        mostrarResultado("Eliminar a Carlos", red.eliminarPersona("C"));
        mostrarRed(red);

        titulo(5, "Mostrar la red final y los vecinos de cada usuario");
        mostrarRed(red);
        mostrarVecinos(red, "A", "Ana");
        mostrarVecinos(red, "B", "Beto");
        mostrarVecinos(red, "D", "Dani");
    }

    private static void crearRedInicial(RedSocial red) {
        mostrarResultado("AMISTAD: Ana - Beto", red.agregarAmistad("A", "B"));
        mostrarResultado("AMISTAD: Ana - Carlos", red.agregarAmistad("A", "C"));
        mostrarResultado("AMISTAD: Beto - Carlos", red.agregarAmistad("B", "C"));
        mostrarResultado("AMISTAD: Carlos - Dani", red.agregarAmistad("C", "D"));
        mostrarResultado("SIGUE: Dani -> Ana", red.seguir("D", "A"));
        mostrarResultado("SIGUE: Beto -> Dani", red.seguir("B", "D"));
        mostrarResultado("SIGUE: Carlos -> Ana", red.seguir("C", "A"));
    }

    private static void aplicarCambiosDelModerador(RedSocial red) {
        if (CAMBIOS_MODERADOR.length == 0) {
            System.out.println("PENDIENTE: no hay indicaciones del moderador registradas.");
            System.out.println("La red se conserva sin cambios para no inventar datos de la bitacora.");
            return;
        }

        for (int i = 0; i < CAMBIOS_MODERADOR.length; i++) {
            CambioModerador cambio = CAMBIOS_MODERADOR[i];
            boolean aceptado = aplicar(red, cambio);
            System.out.printf("%d. %s -> %s%n", i + 1, cambio.indicacion(),
                    aceptado ? "ACEPTADA" : "RECHAZADA");
        }
    }

    private static boolean aplicar(RedSocial red, CambioModerador cambio) {
        return switch (cambio.operacion()) {
            case AGREGAR_AMISTAD -> red.agregarAmistad(cambio.origen(), cambio.destino());
            case QUITAR_AMISTAD -> red.eliminarAmistad(cambio.origen(), cambio.destino());
            case SEGUIR -> red.seguir(cambio.origen(), cambio.destino());
            case DEJAR_DE_SEGUIR -> red.dejarDeSeguir(cambio.origen(), cambio.destino());
        };
    }

    private static void registrar(RedSocial red, String usuario, String nombre) {
        mostrarResultado("Registrar " + nombre + " (" + usuario + ")",
                red.registrarPersona(usuario, nombre));
    }

    private static void mostrarVecinos(RedSocial red, String usuario, String nombre) {
        System.out.println("\n" + nombre + ":");
        System.out.println("  Amigos: " + red.listarAmigos(usuario));
        System.out.println("  Seguidos: " + red.listarSeguidos(usuario));
        System.out.println("  Seguidores: " + red.listarSeguidores(usuario));
    }

    private static void mostrarResultado(String accion, boolean aceptada) {
        System.out.printf("[%s] %s%n", aceptada ? "ACEPTADA" : "RECHAZADA", accion);
    }

    private static void mostrarRed(RedSocial red) {
        System.out.println(red.mostrarRed());
    }

    private static void titulo(int paso, String texto) {
        System.out.printf("%n=== PASO %d: %s ===%n", paso, texto);
    }
}
