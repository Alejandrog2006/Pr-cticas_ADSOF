import java.util.*;
import java.io.*;
import java.nio.file.*;

public class RedSocial {
    private List<Mensaje> mensajes;
    private List<Usuario> usuarios;
    private List<Enlace> enlaces;

    public RedSocial(String nombre1, String nombre2, String nombre3) throws IOException {
        this.usuarios = new ArrayList<>();
        this.enlaces = new ArrayList<>();
        this.mensajes = new ArrayList<>();
        
        leerUsuarios(nombre1);
        leerEnlaces(nombre2);
        leerMensaje(nombre3);

    }

    private void leerUsuarios(String nombreFichero) throws IOException {
        String contenido = Files.readString(Path.of(nombreFichero));
        Scanner scanner = new Scanner(contenido);

        while (scanner.hasNext()) {
            Usuario u = new Usuario(scanner.next(), scanner.nextInt());
            usuarios.add(u);
            /*System.out.println("Usuario añadido: " + u + "\n");*/
        }

        scanner.close();
    }

    private void leerEnlaces(String nombreFichero) throws IOException {
        String contenido = Files.readString(Path.of(nombreFichero));
        Scanner scanner = new Scanner(contenido);

        while (scanner.hasNext()) {
            Usuario uO = encontrarUsuario(usuarios, scanner.next());
            Usuario uD = encontrarUsuario(usuarios, scanner.next());

            Enlace e = new Enlace(uO, uD, scanner.nextInt());
            enlaces.add(e);
            uO.addEnlace(e);
        }

        scanner.close();
    }

    private void leerMensaje(String nombreFichero) throws IOException {
        String contenido = Files.readString(Path.of(nombreFichero));
        Scanner scanner = new Scanner(contenido);

        String mensaje = scanner.next();
        int capac = scanner.nextInt();
        Usuario u = encontrarUsuario(this.usuarios, scanner.next());
        List<Usuario> usuariosDifusion = new ArrayList<>();

        this.mensaje = new Mensaje(mensaje, capac, u);
        System.out.println("Mensaje añadido: " + mensaje + "\n");

        while (scanner.hasNext()) {
            String aux = scanner.next();
            if (aux.equals("+")) 
            usuariosDifusion.add(encontrarUsuario(this.usuarios,aux));
        }

        this.mensaje.difunde(usuariosDifusion);
        scanner.close();
    }

    // método para buscar un usuario por nombre
    private Usuario encontrarUsuario(List<Usuario> usuarios, String nombre) {
        for (Usuario u : usuarios) {
            if (u.getNom().equals(nombre)) {
                return u;
            }
        }
        return null; // no encontrado
    }
}
