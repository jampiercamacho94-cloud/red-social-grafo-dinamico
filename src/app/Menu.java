package app;

import java.util.Scanner;
import negocio.RedSocial;

/**
 * Menu de consola para usar la red social.
 * Responsable: Luis Silva
 */
public class Menu {

    private final RedSocial red = new RedSocial();
    private final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new Menu().iniciar();
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    registrar();
                    break;
                case 2:
                    resultado(red.agregarAmistad(leer("Usuario 1: "), leer("Usuario 2: ")),
                            "Amistad creada.", "No se pudo crear la amistad (lazo, repetida o usuario inexistente).");
                    break;
                case 3:
                    resultado(red.seguir(leer("Quien sigue: "), leer("A quien sigue: ")),
                            "Seguimiento creado.", "No se pudo seguir (lazo, repetido o usuario inexistente).");
                    break;
                case 4:
                    resultado(red.eliminarAmistad(leer("Usuario 1: "), leer("Usuario 2: ")),
                            "Amistad eliminada de los dos lados.", "Esa amistad no existe.");
                    break;
                case 5:
                    resultado(red.dejarDeSeguir(leer("Quien deja de seguir: "), leer("A quien: ")),
                            "Seguimiento eliminado.", "Ese seguimiento no existe.");
                    break;
                case 6:
                    resultado(red.eliminarPersona(leer("Usuario a eliminar: ")),
                            "Usuario y todas sus conexiones eliminados.", "El usuario no existe.");
                    break;
                case 7:
                    verVecinos();
                    break;
                case 8:
                    System.out.println(red.mostrarRed());
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("===== RED SOCIAL =====");
        System.out.println("1. Registrar usuario");
        System.out.println("2. Agregar amistad");
        System.out.println("3. Seguir");
        System.out.println("4. Quitar amistad");
        System.out.println("5. Dejar de seguir");
        System.out.println("6. Eliminar usuario");
        System.out.println("7. Ver vecinos de un usuario");
        System.out.println("8. Mostrar red");
        System.out.println("0. Salir");
    }

    private void registrar() {
        String usuario = leer("Usuario (ID, ej. A): ");
        String nombre = leer("Nombre: ");
        resultado(red.registrarPersona(usuario, nombre),
                "Usuario registrado.", "No se pudo registrar (usuario repetido o datos vacios).");
    }

    private void verVecinos() {
        String u = leer("Usuario: ");
        if (red.listarAmigos(u) == null) {
            System.out.println("El usuario no existe.");
            return;
        }
        System.out.println("Amigos:     " + red.listarAmigos(u));
        System.out.println("Sigue a:    " + red.listarSeguidos(u));
        System.out.println("Lo siguen:  " + red.listarSeguidores(u));
    }

    private void resultado(boolean ok, String siOk, String siFalla) {
        System.out.println(ok ? siOk : siFalla);
    }

    private String leer(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    /** Pide un numero hasta que el usuario escriba uno valido. */
    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leer(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero.");
            }
        }
    }
}
