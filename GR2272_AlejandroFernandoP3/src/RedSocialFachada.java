/* clase de prueba para lo de fachada (no lo he hecho yo) */
import java.io.*;
import java.util.*;
import java.util.regex.*;

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

    public RedSocialFachada() {
        this.usuarios = new ArrayList<>();
        this.enlaces = new ArrayList<>();
        this.mensajes = new ArrayList<>();
        this.secuenciaDifusion = new ArrayList<>();
    }

    public RedSocialFachada(String archivoUsuarios, String archivoEnlaces, String archivoMensaje) throws IOException {
        this();
        cargarDesdeFicheros(archivoUsuarios, archivoEnlaces, archivoMensaje);
    }

    public Usuario crearUsuario(String nombre) {
        return crearUsuario(nombre, 2);
    }

    public Usuario crearUsuario(String nombre, int capacidadAmplificacion) {
        if (nombre == null || nombre.isBlank() || buscarUsuario(nombre) != null) {
            return null;
        }

        Usuario usuario = new Usuario(nombre, capacidadAmplificacion);
        this.usuarios.add(usuario);
        return usuario;
    }

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

    public boolean ejecutarDifusionPlanificada() {
        if (this.mensajeActual == null) {
            return false;
        }
        return this.mensajeActual.difunde(this.secuenciaDifusion);
    }

    public boolean crearYDifundirMensaje(String texto, int alcance, String nombreUsuarioInicial, List<String> nombresSecuencia) {
        Mensaje creado = crearMensaje(texto, alcance, nombreUsuarioInicial);
        if (creado == null) {
            return false;
        }

        planificarDifusion(nombresSecuencia);
        return ejecutarDifusionPlanificada();
    }

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

    public void guardarEnFicheros(String archivoUsuarios, String archivoEnlaces, String archivoMensaje) throws IOException {
        guardarUsuarios(archivoUsuarios);
        guardarEnlaces(archivoEnlaces);
        guardarMensajeYSecuencia(archivoMensaje);
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(this.usuarios);
    }

    public List<Enlace> getEnlaces() {
        return Collections.unmodifiableList(this.enlaces);
    }

    public List<Mensaje> getMensajes() {
        return Collections.unmodifiableList(this.mensajes);
    }

    public Mensaje getMensajeActual() {
        return this.mensajeActual;
    }

    public List<Usuario> getSecuenciaDifusion() {
        return Collections.unmodifiableList(this.secuenciaDifusion);
    }

    private void cargarUsuarios(String archivoUsuarios) throws IOException {
        try (Scanner scanner = new Scanner(new FileReader(archivoUsuarios))) {
            while (scanner.hasNext()) {
                String nombre = scanner.next();
                int capacidad = scanner.nextInt();
                crearUsuario(nombre, capacidad);
            }
        }
    }

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

    private void guardarUsuarios(String archivoUsuarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoUsuarios))) {
            for (Usuario usuario : this.usuarios) {
                bw.write(usuario.getNom() + " " + usuario.getCapacAmpl());
                bw.newLine();
            }
        }
    }

    private void guardarEnlaces(String archivoEnlaces) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoEnlaces))) {
            for (Enlace enlace : this.enlaces) {
                bw.write(enlace.getUsuarioO().getNom() + " " + enlace.getUsuarioD().getNom() + " " + enlace.getCoste());
                bw.newLine();
            }
        }
    }

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

    private Usuario buscarUsuario(String nombre) {
        for (Usuario usuario : this.usuarios) {
            if (usuario.getNom().equals(nombre)) {
                return usuario;
            }
        }
        return null;
    }

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

    private static final class ParsedMensaje {
        private final String texto;
        private final int alcance;
        private final String usuarioInicial;

        private ParsedMensaje(String texto, int alcance, String usuarioInicial) {
            this.texto = texto;
            this.alcance = alcance;
            this.usuarioInicial = usuarioInicial;
        }
    }
}
