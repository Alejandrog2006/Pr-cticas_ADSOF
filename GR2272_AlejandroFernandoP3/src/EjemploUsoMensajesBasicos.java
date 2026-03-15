import java.io.*;

/**
 * Clase de ejemplo para el uso básico de mensajes en la red social.
 * Carga usuarios, enlaces y mensajes desde archivos y los procesa.
 */
public class EjemploUsoMensajesBasicos {
    /**
     * Método main que crea redes sociales desde archivos.
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            RedSocial s;
            s = new RedSocial("USUARIOS.txt", "ENLACES.txt", "MENSAJE.txt");
            s = new RedSocial("USUARIOS.txt", "ENLACES.txt", "MENSAJE2.txt");
        } catch (IOException e) { 
            System.out.println("Error en archivos");
        }
    }
}
