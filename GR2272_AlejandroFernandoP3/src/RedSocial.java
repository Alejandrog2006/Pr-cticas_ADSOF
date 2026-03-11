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

    /* Constructor que inicializa vacio */
    public RedSocial() throws IOException {
        this.usuarios = new ArrayList<>();
        this.enlaces = new ArrayList<>();
        this.mensajes = new ArrayList<>();
    }

    private void leerUsuarios(String nombreFichero) throws IOException {
        String contenido = Files.readString(Path.of(nombreFichero));
        Scanner scanner = new Scanner(contenido);

        while (scanner.hasNext()) {
            /* En este método y los siguientes de lectura tambien se podria llamar a crearUsuario(para este caso) */
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
        String siguienteCabecera = null;
        boolean terminar = false;

        while (!terminar && (siguienteCabecera != null || scanner.hasNextLine())) {
            String cabecera;
            if (siguienteCabecera != null) {
                cabecera = siguienteCabecera;
                siguienteCabecera = null;
            } else {
                cabecera = scanner.nextLine().trim();
            }

            if (cabecera.isEmpty()) {
                continue;
            }

            int ultimoEspacio = cabecera.lastIndexOf(' ');
            int penultimoEspacio = cabecera.lastIndexOf(' ', ultimoEspacio - 1);

            String texto = cabecera.substring(0, penultimoEspacio);
            if (texto.startsWith("\"") && texto.endsWith("\"")) {
                texto = texto.substring(1, texto.length() - 1);
            }

            int capac = Integer.parseInt(cabecera.substring(penultimoEspacio + 1, ultimoEspacio));
            Usuario autor = encontrarUsuario(this.usuarios, cabecera.substring(ultimoEspacio + 1));
            Mensaje mensaje = new Mensaje(texto, capac, autor);

            List<Usuario> usuariosDifusion = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();
                if (linea.isEmpty()) {
                    if (!scanner.hasNextLine()) {
                        terminar = true;
                        break;
                    }

                    String siguienteLinea = scanner.nextLine().trim();
                    if (siguienteLinea.isEmpty()) {
                        terminar = true;
                        break;
                    }

                    siguienteCabecera = siguienteLinea;
                    break;
                }

                Usuario destino = encontrarUsuario(this.usuarios, linea);
                if (destino != null) {
                    usuariosDifusion.add(destino);
                }
            }

            mensaje.difunde(usuariosDifusion);
            this.mensajes.add(mensaje);
        }

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

    /* IMPLEMENTACIÓN DE MÉTODOS DE RED SOCIAL FACHADA */

    public Usuario crearUsuario(String nombre) {
        if (nombre == null || nombre.isBlank() || encontrarUsuario(this.usuarios, nombre) != null) {
            return null;
        }
        Usuario usuario = new Usuario(nombre);
        this.usuarios.add(usuario);
        return usuario;
    }

    public Usuario crearUsuario(String nombre, int capacidadAmplificacion) {
        if (nombre == null || nombre.isBlank() || encontrarUsuario(this.usuarios, nombre) != null) {
            return null;
        }

        Usuario usuario = new Usuario(nombre, capacidadAmplificacion);
        this.usuarios.add(usuario);
        return usuario;
    }

    public Enlace crearEnlace(String nombreOrigen, String nombreDestino, int coste) {
        Usuario uOrigen = encontrarUsuario(this.usuarios, nombreOrigen);
        Usuario uDestino = encontrarUsuario(this.usuarios, nombreDestino);
        if (uOrigen == null || uDestino == null) {
            return null;
        }

        Enlace enlace = new Enlace(uOrigen, uDestino, coste);
        if (!uOrigen.addEnlace(enlace)) {
            return null;
        }

        this.enlaces.add(enlace);
        return enlace;
    }

    public Enlace crearEnlace(Usuario uOrigen, Usuario uDestino, int coste) {
        if (uOrigen == null || uDestino == null) {
            return null;
        }

        Enlace enlace = new Enlace(uOrigen, uDestino, coste);
        if (!uOrigen.addEnlace(enlace)) {
            return null;
        }

        this.enlaces.add(enlace);
        return enlace;
    }

    public Mensaje crearMensaje(String texto, int alcance, String nombreUsuarioInicial) {
        Usuario inicial = encontrarUsuario(this.usuarios, nombreUsuarioInicial);
        if (inicial == null) {
            return null;
        }

        Mensaje mensaje = new Mensaje(texto, alcance, inicial);
        this.mensajes.add(mensaje);
        return mensaje;
    }

    public void escribirUsuarios(String nombreFichero) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
        for (Usuario u : this.usuarios) {
            bw.write(u.getNom() + " " + u.getCapacAmpl());
            bw.newLine();
        }
        bw.close();
    }

    public void escribirEnlaces(String nombreFichero) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
        for (Enlace e : this.enlaces) {
            bw.write(e.getUsuarioO().getNom() + " " + e.getUsuarioD().getNom() + " " + e.getCoste());
            bw.newLine();
        }
        bw.close();
    }

    public void escribirMensajes(String nombreFichero) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
        for (Mensaje m : this.mensajes) {
            bw.write("\"" + m.getTexto() + "\" " + m.getCapac() + " " + m.getAutor().getNom());
            bw.newLine();
            bw.newLine();
        }
        bw.close();
    }

    public void escribirTodo(String archivoUsuarios, String archivoEnlaces, String archivoMensajes) throws IOException {
        escribirUsuarios(archivoUsuarios);
        escribirEnlaces(archivoEnlaces);
        escribirMensajes(archivoMensajes);
    }

    
}
