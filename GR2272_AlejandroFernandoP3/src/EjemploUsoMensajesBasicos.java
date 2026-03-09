import java.io.*;

public class EjemploUsoMensajesBasicos {
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
