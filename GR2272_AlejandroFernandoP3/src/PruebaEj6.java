/*
 * Nombre del fichero: PruebaEj6.java
 * Autor: Alejandro González y Fernando Blanco
 */
public class PruebaEj6 {
    public static void main(String[] args) {
        // Pruebas de usuario
        System.out.println("PRUEBAS DE USUARIO");
        
        Usuario u1 = new Usuario("maria");
        Usuario u2 = new Usuario("pedro", 20);
        Usuario u3 = new Usuario("sofia", 1, Usuario.Exposicion.MEDIA);
        Usuario u4 = new Usuario("juan", 5, Usuario.Exposicion.VIRAL);
        Usuario u5 = new Usuario("laura");

        System.out.println("1. Constructores:");
        System.out.println("   Usuario con capacidad y exposicion por defecto: " + u1);
        System.out.println("   Usuario con capacidad 3: " + u2);
        System.out.println("   Usuario con capacidad 1 y exposicion MEDIA: " + u3);
        System.out.println("   Usuario con capacidad 5 y exposicion VIRAL: " + u4);
        System.out.println("   Usuario con capacidad y exposicion por defecto: " + u5);
   
        u2.addEnlace(u3, 12);
        u2.addEnlace(u4, 6);
        u3.addEnlace(u4, 15);
        u3.addEnlace(u5, 25);
        
        Mensaje msg1 = new Mensaje("Hola a todos", 50, u2);
        msg1.difunde(u2.getEnlace(u3));
        System.out.println("   Usuario con capacidad 1 y nueva exposicion: " + u3);
        msg1.difunde(u2.getEnlace(u4));
        System.out.println("   Usuario con capacidad 5 y nueva exposicion: " + u4);

        //Prueba de usuario interesado
        System.out.println("PRUEBAS DE USUARIO INTERESADO");

        UsuarioInteresado ui1 = new UsuarioInteresado("ana");
        UsuarioInteresado ui2 = new UsuarioInteresado("pepe");
        System.out.println("    Usuario Interesado:" + ui1);
        System.out.println("    Usuario Interesado:" + ui2);

        ui1.addEnlace(ui2, 10);  
        ui1.addEnlace(u4, 5);

        Enlace enlaceInteres = ui1.getEnlace(ui2);
        System.out.println("    Enlace obtenido por usuario interesado al buscar pepe: " + enlaceInteres);

        //Prueba de Mensaje Controlado
        System.out.println("PRUEBAS DE MENSAJE CONTROLADO");

        MensajeControlado msgControlado = new MensajeControlado("Mensaje Controlado", 40, u2, 15);
        System.out.println("    Mensaje Controlado creado: " + msgControlado);
        System.out.println("    Se intenta difundir por enlace de pedro a juan: " + msgControlado.difunde(u2.getEnlace(u4)));
        System.out.println("    Se intenta difundir por enlace de pedro a sofia: " + msgControlado.difunde(u2.getEnlace(u3)));

        //Prueba de Enlace Señuelo
        System.out.println("PRUEBAS DE ENLACE SEÑUELO");

        EnlaceSenuelo enlaceSenuelo = new EnlaceSenuelo(u2, u5, 8, 2, 0.9);
        System.out.println("    Enlace Señuelo creado: " + enlaceSenuelo);
        System.out.println("    Se intenta conseguir el usuario destino del enlace señuelo: " + enlaceSenuelo.getUsuarioD());
        System.out.println("    Se intenta difundir por enlace señuelo: " + msg1.difunde(enlaceSenuelo));
        System.out.println("    Se intenta conseguir el coste especial del enlace señuelo: " + enlaceSenuelo.costeEspecial() + " distinto del coste original: " + enlaceSenuelo.getCoste() + ", siendo el coste real: " + enlaceSenuelo.costeReal());
    }    
}