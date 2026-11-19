package pruebas;

import java.util.Locale;
import negocio.RedSocial;

public class PruebasRedSocial {
    private int total;
    private int aciertos;

    public static void main(String[] args) {
        new PruebasRedSocial().ejecutar();
    }

    private void ejecutar() {
        RedSocial red = new RedSocial();

        boolean ana = red.registrarPersona("ana", "Ana");
        boolean beto = red.registrarPersona("beto", "Beto");
        boolean carlos = red.registrarPersona("carlos", "Carlos");
        boolean dani = red.registrarPersona("dani", "Dani");
        verificar("Registrar a Ana, Beto, Carlos y Dani", ana && beto && carlos && dani);

        verificar("Rechazar a Ana repetida", !red.registrarPersona("ana", "Ana"));
        verificar("Rechazar amistad consigo mismo", !red.agregarAmistad("ana", "ana"));
        verificar("Rechazar seguimiento consigo mismo", !red.seguir("ana", "ana"));

        boolean anaBeto = red.agregarAmistad("ana", "beto");
        boolean anaCarlos = red.agregarAmistad("ana", "carlos");
        boolean betoCarlos = red.agregarAmistad("beto", "carlos");
        boolean carlosDani = red.agregarAmistad("carlos", "dani");
        boolean daniSigueAna = red.seguir("dani", "ana");
        boolean betoSigueDani = red.seguir("beto", "dani");
        boolean carlosSigueAna = red.seguir("carlos", "ana");

        verificar("Crear las siete relaciones de la red inicial",
                anaBeto && anaCarlos && betoCarlos && carlosDani
                        && daniSigueAna && betoSigueDani && carlosSigueAna);

        verificar("La amistad Ana-Beto aparece desde los dos lados",
                contiene(red.listarAmigos("ana"), "beto")
                        && contiene(red.listarAmigos("beto"), "ana"));

        verificar("Dani sigue a Ana en un solo sentido",
                contiene(red.listarSeguidos("dani"), "ana")
                        && contiene(red.listarSeguidores("ana"), "dani")
                        && !contiene(red.listarSeguidos("ana"), "dani"));

        verificar("Rechazar amistad repetida en orden inverso",
                !red.agregarAmistad("beto", "ana"));

        verificar("Quitar una amistad de los dos lados",
                red.eliminarAmistad("ana", "beto")
                        && !contiene(red.listarAmigos("ana"), "beto")
                        && !contiene(red.listarAmigos("beto"), "ana"));

        verificar("Preparar un seguimiento entrante a Carlos",
                red.seguir("beto", "carlos")
                        && contiene(red.listarSeguidos("beto"), "carlos"));

        verificar("Eliminar a Carlos y comprobar que ya no existe",
                red.eliminarPersona("carlos") && !red.eliminarPersona("carlos"));

        boolean quedanReferencias = false;
        for (String usuario : new String[]{"ana", "beto", "dani"}) {
            if (contiene(red.listarAmigos(usuario), "carlos")
                    || contiene(red.listarSeguidos(usuario), "carlos")
                    || contiene(red.listarSeguidores(usuario), "carlos")) {
                quedanReferencias = true;
            }
        }
        verificar("Carlos desaparece de todas las conexiones", !quedanReferencias);

        System.out.printf("%n%d/%d pruebas OK%n", aciertos, total);
    }

    private void verificar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            aciertos++;
        }
        System.out.println((condicion ? "[OK] " : "[FALLA] ") + nombre);
    }

    private boolean contiene(Object listado, String usuario) {
        return listado != null
                && listado.toString().toLowerCase(Locale.ROOT).contains(usuario);
    }
}