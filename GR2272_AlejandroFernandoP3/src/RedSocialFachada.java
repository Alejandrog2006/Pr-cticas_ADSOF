/* clase de prueba para lo de fachada (no lo he hecho yo) */
import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Clase fachada para la red social, proporcionando una interfaz simplificada.
 * Gestiona usuarios, enlaces, mensajes y planificación de difusión.
 */
public class RedSocialFachada {
    private final List<Usuario> usuarios;
    private final List<Enlace> enlaces;
    private final List<Mensaje> mensajes;

    // Estado de la simulacion planificada/ejecutada.
    private Mensaje mensajeActual;
    private String textoInicial;
    private int alcanceInicial;
    private Usuario usuarioInicial;
    private final List<Usuario> secuenciaDifusion;

    /**
     * Construye una red social fachada vacía.
     */
    public RedSocialFachada() {
        this.usuarios = new ArrayList<>();
        this.enlaces = new ArrayList<>();
        this.mensajes = new ArrayList<>();
        this.secuenciaDifusion = new ArrayList<>();
    }

    /**
     * Construye una red social fachada cargando datos desde archivos.
     * @param archivoUsuarios el archivo de usuarios
     * @param archivoEnlaces el archivo de enlaces
     * @param archivoMensaje el archivo de mensajes
     * @throws IOException si falla la carga
     */
    public RedSocialFachada(String archivoUsuarios, String archivoEnlaces, String archivoMensaje) throws IOException {
        this();
        cargarDesdeFicheros(archivoUsuarios, archivoEnlaces, archivoMensaje);
    }

    /**
     * Crea un usuario con capacidad por defecto.
     * @param nombre el nombre del usuario
     * @return el usuario creado, o null si es inválido
     */
    public Usuario crearUsuario(String nombre) {
        return crearUsuario(nombre, 2);
    }

    /**
     * Crea un usuario con capacidad especificada.
     * @param nombre el nombre del usuario
     * @param capacidadAmplificacion la capacidad de amplificación
     * @return el usuario creado, o null si es inválido
     */
    public Usuario crearUsuario(String nombre, int capacidadAmplificacion) {
        if (nombre == null || nombre.isBlank() || buscarUsuario(nombre) != null) {
            return null;
        }

        Usuario usuario = new Usuario(nombre, capacidadAmplificacion);
        this.usuarios.add(usuario);
        return usuario;
    }

    /**
     * Crea un enlace entre usuarios por nombre.
     * @param nombreOrigen el nombre del usuario origen
     * @param nombreDestino el nombre del usuario destino
     * @param coste el costo del enlace
     * @return el enlace creado, o null si es inválido
     */
    public Enlace crearEnlace(String nombreOrigen, String nombreDestino, int coste) {
        Usuario origen = buscarUsuario(nombreOrigen);
        Usuario destino = buscarUsuario(nombreDestino);
        if (origen == null || destino == null) {
            return null;
        }

        Enlace enlace = new Enlace(origen, destino, coste);
        if (!origen.addEnlace(enlace)) {
            return null;
        }

        this.enlaces.add(enlace);
        return enlace;
    }

    /**
     * Crea un mensaje y lo establece como actual.
     * @param texto el texto del mensaje
     * @param alcance el alcance del mensaje
     * @param nombreUsuarioInicial el nombre del usuario inicial
     * @return el mensaje creado, o null si es inválido
     */
    public Mensaje crearMensaje(String texto, int alcance, String nombreUsuarioInicial) {
        Usuario inicial = buscarUsuario(nombreUsuarioInicial);
        if (inicial == null) {
            return null;
        }

        this.textoInicial = texto;
        this.alcanceInicial = alcance;
        this.usuarioInicial = inicial;

        this.mensajeActual = new Mensaje(texto, alcance, inicial);
        this.mensajes.add(this.mensajeActual);
        this.secuenciaDifusion.clear();

        return this.mensajeActual;
    }

    /**
     * Planifica la secuencia de difusión para el mensaje actual.
     * @param nombresUsuarios la lista de nombres de usuarios para difusión
     * @return true si la planificación fue exitosa, false en caso contrario
     */
    public boolean planificarDifusion(List<String> nombresUsuarios) {
        this.secuenciaDifusion.clear();
        for (String nombre : nombresUsuarios) {
            Usuario usuario = buscarUsuario(nombre);
            if (usuario != null) {
                this.secuenciaDifusion.add(usuario);
            }
        }
        return !this.secuenciaDifusion.isEmpty() || nombresUsuarios.isEmpty();
    }

    /**
     * Ejecuta la difusión planificada para el mensaje actual.
     * @return true si la ejecución fue exitosa, false en caso contrario
     */
    public boolean ejecutarDifusionPlanificada() {
        if (this.mensajeActual == null) {
            return false;
        }
        return this.mensajeActual.difunde(this.secuenciaDifusion);
    }

    /**
     * Crea un mensaje y lo difunde a los usuarios especificados.
     * @param texto el texto del mensaje
     * @param alcance el alcance del mensaje
     * @param nombreUsuarioInicial el nombre del usuario inicial
     * @param nombresSecuencia la lista de nombres de usuarios para difusión
     * @return true si la creación y difusión fueron exitosas, false en caso contrario
     */
    public boolean crearYDifundirMensaje(String texto, int alcance, String nombreUsuarioInicial, List<String> nombresSecuencia) {
        Mensaje creado = crearMensaje(texto, alcance, nombreUsuarioInicial);
        if (creado == null) {
            return false;
        }

        planificarDifusion(nombresSecuencia);
        return ejecutarDifusionPlanificada();
    }

    /**
     * Carga datos desde archivos.
     * @param archivoUsuarios el archivo de usuarios
     * @param archivoEnlaces el archivo de enlaces
     * @param archivoMensaje el archivo de mensajes
     * @throws IOException si falla la carga
     */
    public void cargarDesdeFicheros(String archivoUsuarios, String archivoEnlaces, String archivoMensaje) throws IOException {
        this.usuarios.clear();
        this.enlaces.clear();
        this.mensajes.clear();
        this.secuenciaDifusion.clear();
        this.mensajeActual = null;
        this.textoInicial = null;
        this.alcanceInicial = 0;
        this.usuarioInicial = null;

        cargarUsuarios(archivoUsuarios);
        cargarEnlaces(archivoEnlaces);
        cargarMensajeYSecuencia(archivoMensaje);

        if (this.mensajeActual != null) {
            this.mensajeActual.difunde(this.secuenciaDifusion);
        }
    }

    /**
     * Guarda datos en archivos.
     * @param archivoUsuarios el archivo de usuarios
     * @param archivoEnlaces el archivo de enlaces
     * @param archivoMensaje el archivo de mensajes
     * @throws IOException si falla el guardado
     */
    public void guardarEnFicheros(String archivoUsuarios, String archivoEnlaces, String archivoMensaje) throws IOException {
        guardarUsuarios(archivoUsuarios);
        guardarEnlaces(archivoEnlaces);
        guardarMensajeYSecuencia(archivoMensaje);
    }

    /**
     * Obtiene la lista de usuarios.
     * @return lista inmodificable de usuarios
     */
    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(this.usuarios);
    }

    /**
     * Obtiene la lista de enlaces.
     * @return lista inmodificable de enlaces
     */
    public List<Enlace> getEnlaces() {
        return Collections.unmodifiableList(this.enlaces);
    }

    /**
     * Obtiene la lista de mensajes.
     * @return lista inmodificable de mensajes
     */
    public List<Mensaje> getMensajes() {
        return Collections.unmodifiableList(this.mensajes);
    }

    /**
     * Obtiene el mensaje actual.
     * @return el mensaje actual
     */
    public Mensaje getMensajeActual() {
        return this.mensajeActual;
    }

    /**
     * Obtiene la secuencia de difusión.
     * @return lista inmodificable de usuarios en secuencia de difusión
     */
    public List<Usuario> getSecuenciaDifusion() {
        return Collections.unmodifiableList(this.secuenciaDifusion);
    }

    /**
     * Carga usuarios desde un archivo.
     * @param archivoUsuarios el archivo de usuarios
     * @throws IOException si falla la carga
     */
    private void cargarUsuarios(String archivoUsuarios) throws IOException {
        try (Scanner scanner = new Scanner(new FileReader(archivoUsuarios))) {
            while (scanner.hasNext()) {
                String nombre = scanner.next();
                int capacidad = scanner.nextInt();
                crearUsuario(nombre, capacidad);
            }
        }
    }

    /**
     * Carga enlaces desde un archivo.
     * @param archivoEnlaces el archivo de enlaces
     * @throws IOException si falla la carga
     */
    private void cargarEnlaces(String archivoEnlaces) throws IOException {
        try (Scanner scanner = new Scanner(new FileReader(archivoEnlaces))) {
            while (scanner.hasNext()) {
                String origen = scanner.next();
                String destino = scanner.next();
                int coste = scanner.nextInt();
                crearEnlace(origen, destino, coste);
            }
        }
    }

    /**
     * Carga mensaje y secuencia de difusión desde un archivo.
     * @param archivoMensaje el archivo de mensaje
     * @throws IOException si falla la carga
     */
    private void cargarMensajeYSecuencia(String archivoMensaje) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoMensaje))) {
            String primeraLinea = br.readLine();
            if (primeraLinea == null || primeraLinea.isBlank()) {
                return;
            }

            ParsedMensaje parsed = parsearPrimeraLineaMensaje(primeraLinea);
            if (crearMensaje(parsed.texto, parsed.alcance, parsed.usuarioInicial) == null) {
                return;
            }

            String linea;
            while ((linea = br.readLine()) != null) {
                String nombre = linea.trim();
                if (nombre.isEmpty()) {
                    continue;
                }

                Usuario usuario = buscarUsuario(nombre);
                if (usuario != null) {
                    this.secuenciaDifusion.add(usuario);
                }
            }
        }
    }
    /**
     * Guarda usuarios en un archivo.
     * @param archivoUsuarios el archivo de usuarios
     * @throws IOException si falla el guardado
     */    private void guardarUsuarios(String archivoUsuarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoUsuarios))) {
            for (Usuario usuario : this.usuarios) {
                bw.write(usuario.getNom() + " " + usuario.getCapacAmpl());
                bw.newLine();
            }
        }
    }

    /**
     * Guarda enlaces en un archivo.
     * @param archivoEnlaces el archivo de enlaces
     * @throws IOException si falla el guardado
     */
    private void guardarEnlaces(String archivoEnlaces) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoEnlaces))) {
            for (Enlace enlace : this.enlaces) {
                bw.write(enlace.getUsuarioO().getNom() + " " + enlace.getUsuarioD().getNom() + " " + enlace.getCoste());
                bw.newLine();
            }
        }
    }

    /**
     * Guarda mensaje y secuencia de difusión en un archivo.
     * @param archivoMensaje el archivo de mensaje
     * @throws IOException si falla el guardado
     */
    private void guardarMensajeYSecuencia(String archivoMensaje) throws IOException {
        if (this.mensajeActual == null || this.usuarioInicial == null || this.textoInicial == null) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoMensaje))) {
            bw.write("\"" + this.textoInicial + "\" " + this.alcanceInicial + " " + this.usuarioInicial.getNom());
            bw.newLine();

            for (Usuario usuario : this.secuenciaDifusion) {
                bw.write(usuario.getNom());
                bw.newLine();
            }
        }
    }

    /**
     * Busca un usuario por nombre.
     * @param nombre el nombre a buscar
     * @return el usuario si se encuentra, null en caso contrario
     */
    private Usuario buscarUsuario(String nombre) {
        for (Usuario usuario : this.usuarios) {
            if (usuario.getNom().equals(nombre)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Parsea la primera línea de un mensaje.
     * @param linea la línea a parsear
     * @return el mensaje parseado
     */
    private ParsedMensaje parsearPrimeraLineaMensaje(String linea) {
        Pattern pattern = Pattern.compile("^\"(.*)\"\\s+(-?\\d+)\\s+(\\S+)$");
        Matcher matcher = pattern.matcher(linea.trim());

        if (matcher.matches()) {
            return new ParsedMensaje(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3));
        }

        String[] partes = linea.trim().split("\\s+");
        if (partes.length < 3) {
            throw new IllegalArgumentException("Formato invalido en primera linea de mensaje: " + linea);
        }

        return new ParsedMensaje(partes[0], Integer.parseInt(partes[1]), partes[2]);
    }

    /**
     * Clase auxiliar para representar un mensaje parseado.
     */
    private static final class ParsedMensaje {
        private final String texto;
        private final int alcance;
        private final String usuarioInicial;

        /**
         * Constructor de ParsedMensaje.
         * @param texto el texto del mensaje
         * @param alcance el alcance del mensaje
         * @param usuarioInicial el nombre del usuario inicial
         */
        private ParsedMensaje(String texto, int alcance, String usuarioInicial) {
            this.texto = texto;
            this.alcance = alcance;
            this.usuarioInicial = usuarioInicial;
        }
    }
}
