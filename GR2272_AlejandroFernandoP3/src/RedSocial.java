import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * Representa una red social con usuarios, enlaces y mensajes.
 * Puede cargar datos desde archivos y gestionar la red.
 */
public class RedSocial {
    private List<Mensaje> mensajes;
    private List<Usuario> usuarios;
    private List<Enlace> enlaces;

    /**
     * Construye una red social cargando datos desde archivos.
     * @param nombre1 el nombre del archivo de usuarios
     * @param nombre2 el nombre del archivo de enlaces
     * @param nombre3 el nombre del archivo de mensajes
     * @throws IOException si falla la lectura
     */
    public RedSocial(String nombre1, String nombre2, String nombre3) throws IOException {
        this.usuarios = new ArrayList<>();
        this.enlaces = new ArrayList<>();
        this.mensajes = new ArrayList<>();
        
        leerUsuarios(nombre1);
        leerEnlaces(nombre2);
        leerMensaje(nombre3);
    }

    /* Constructor que inicializa vacio */
    /**
     * Construye una red social vacía.
     * @throws IOException si falla la inicialización
     */
    public RedSocial() throws IOException {
        this.usuarios = new ArrayList<>();
        this.enlaces = new ArrayList<>();
        this.mensajes = new ArrayList<>();
    }

    /**
     * Lee usuarios desde un archivo.
     * @param nombreFichero el nombre del archivo
     * @throws IOException si falla la lectura
     */
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

    /**
     * Lee enlaces desde un archivo.
     * @param nombreFichero el nombre del archivo
     * @throws IOException si falla la lectura
     */
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

    /**
     * Lee mensajes desde un archivo.
     * @param nombreFichero el nombre del archivo
     * @throws IOException si falla la lectura
     */
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
    /**
     * Busca un usuario por nombre en la lista.
     * @param usuarios la lista de usuarios
     * @param nombre el nombre a buscar
     * @return el usuario si se encuentra, null en caso contrario
     */
    private Usuario encontrarUsuario(List<Usuario> usuarios, String nombre) {
        for (Usuario u : usuarios) {
            if (u.getNom().equals(nombre)) {
                return u;
            }
        }
        return null; // no encontrado
    }

    /* IMPLEMENTACIÓN DE MÉTODOS DE RED SOCIAL FACHADA */

    /**
     * Crea un nuevo usuario con capacidad por defecto.
     * @param nombre el nombre del usuario
     * @return el usuario creado, o null si es inválido o ya existe
     */
    public Usuario crearUsuario(String nombre) {
        if (nombre == null || nombre.isBlank() || encontrarUsuario(this.usuarios, nombre) != null) {
            return null;
        }
        Usuario usuario = new Usuario(nombre);
        this.usuarios.add(usuario);
        return usuario;
    }

    /**
     * Crea un nuevo usuario con capacidad especificada.
     * @param nombre el nombre del usuario
     * @param capacidadAmplificacion la capacidad de amplificación
     * @return el usuario creado, o null si es inválido o ya existe
     */
    public Usuario crearUsuario(String nombre, int capacidadAmplificacion) {
        if (nombre == null || nombre.isBlank() || encontrarUsuario(this.usuarios, nombre) != null) {
            return null;
        }

        Usuario usuario = new Usuario(nombre, capacidadAmplificacion);
        this.usuarios.add(usuario);
        return usuario;
    }

    /**
     * Crea un nuevo enlace entre usuarios.
     * @param nombreOrigen el nombre del usuario origen
     * @param nombreDestino el nombre del usuario destino
     * @param coste el costo del enlace
     * @return el enlace creado, o null si los usuarios no se encuentran o el enlace es inválido
     */
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

    /**
     * Crea un nuevo enlace entre usuarios usando objetos de usuario.
     * @param uOrigen el usuario origen
     * @param uDestino el usuario destino
     * @param coste el costo del enlace
     * @return el enlace creado, o null si es inválido
     */
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

    /**
     * Crea un nuevo mensaje.
     * @param texto el texto del mensaje
     * @param alcance el alcance del mensaje
     * @param nombreUsuarioInicial el nombre del usuario inicial
     * @return el mensaje creado, o null si el usuario no se encuentra
     */
    public Mensaje crearMensaje(String texto, int alcance, String nombreUsuarioInicial) {
        Usuario inicial = encontrarUsuario(this.usuarios, nombreUsuarioInicial);
        if (inicial == null) {
            return null;
        }

        Mensaje mensaje = new Mensaje(texto, alcance, inicial);
        this.mensajes.add(mensaje);
        return mensaje;
    }

    /**
     * Escribe usuarios a un archivo.
     * @param nombreFichero el nombre del archivo
     * @throws IOException si falla la escritura
     */
    public void escribirUsuarios(String nombreFichero) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
        for (Usuario u : this.usuarios) {
            bw.write(u.getNom() + " " + u.getCapacAmpl());
            bw.newLine();
        }
        bw.close();
    }

    /**
     * Escribe enlaces a un archivo.
     * @param nombreFichero el nombre del archivo
     * @throws IOException si falla la escritura
     */
    public void escribirEnlaces(String nombreFichero) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
        for (Enlace e : this.enlaces) {
            bw.write(e.getUsuarioO().getNom() + " " + e.getUsuarioD().getNom() + " " + e.getCoste());
            bw.newLine();
        }
        bw.close();
    }

    /**
     * Escribe mensajes a un archivo.
     * @param nombreFichero el nombre del archivo
     * @throws IOException si falla la escritura
     */
    public void escribirMensajes(String nombreFichero) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
        for (Mensaje m : this.mensajes) {
            bw.write("\"" + m.getTexto() + "\" " + m.getCapac() + " " + m.getAutor().getNom());
            bw.newLine();
            bw.newLine();
        }
        bw.close();
    }

    /**
     * Escribe todos los datos (usuarios, enlaces, mensajes) a archivos.
     * @param archivoUsuarios el nombre del archivo de usuarios
     * @param archivoEnlaces el nombre del archivo de enlaces
     * @param archivoMensajes el nombre del archivo de mensajes
     * @throws IOException si falla la escritura
     */
    public void escribirTodo(String archivoUsuarios, String archivoEnlaces, String archivoMensajes) throws IOException {
        escribirUsuarios(archivoUsuarios);
        escribirEnlaces(archivoEnlaces);
        escribirMensajes(archivoMensajes);
    }

    
}
