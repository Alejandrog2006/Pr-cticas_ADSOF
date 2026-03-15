/**
 * Clase principal para probar las clases de la red social.
 * Contiene pruebas extensas para las clases Usuario, Enlace y Mensaje.
 */
public class Main {
    /**
     * Método main que ejecuta pruebas extensas en las clases de la red social.
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("PRUEBAS EXTENSAS DE LAS CLASES\n");
        
        // Pruebas de usuario
        System.out.println("PRUEBAS DE USUARIO");
        
        // Prueba constructores
        Usuario u1 = new Usuario("maria");
        Usuario u2 = new Usuario("pedro", 3);
        Usuario u3 = new Usuario("sofia", 1);
        Usuario u4 = new Usuario("juan", 5);
        Usuario u5 = new Usuario("laura");
        
        System.out.println("1. Constructores:");
        System.out.println("   Usuario con capacidad por defecto: " + u1);
        System.out.println("   Usuario con capacidad 3: " + u2);
        System.out.println("   Usuario con capacidad 1: " + u3);
        
        // Prueba getNombre y getCapacAmpl
        System.out.println("\n2. getNombre() y getCapacAmpl():");
        System.out.println("   Nombre de u1: " + u1.getNombre());
        System.out.println("   Capacidad amplificación u1: " + u1.getCapacAmpl());
        System.out.println("   Capacidad amplificación u2: " + u2.getCapacAmpl());
        
        // Prueba addEnlace con objeto Enlace
        System.out.println("\n3. addEnlace(Enlace):");
        Enlace e1 = new Enlace(u1, u2, 10);
        boolean r1 = u1.addEnlace(e1);
        System.out.println("   Agregar enlace u1->u2 (coste 10): " + r1 + " | " + u1);
        
        // Intentar agregar enlace duplicado
        Enlace e2 = new Enlace(u1, u2, 15);
        boolean r2 = u1.addEnlace(e2);
        System.out.println("   Agregar enlace duplicado u1->u2: " + r2 + " (debe ser false)");
        
        // Intentar agregar enlace a sí mismo
        Enlace e3 = new Enlace(u1, u1, 5);
        boolean r3 = u1.addEnlace(e3);
        System.out.println("   Agregar enlace u1->u1: " + r3 + " (debe ser false)");
        
        // Prueba addEnlace con parámetros
        System.out.println("\n4. addEnlace(Usuario, int):");
        boolean r4 = u1.addEnlace(u3, 20);
        System.out.println("   Agregar enlace u1->u3 (coste 20): " + r4 + " | " + u1);
        
        boolean r5 = u1.addEnlace(u4, 8);
        System.out.println("   Agregar enlace u1->u4 (coste 8): " + r5 + " | " + u1);
        
        // Agregar enlaces a otros usuarios
        u2.addEnlace(u3, 12);
        u2.addEnlace(u4, 6);
        u3.addEnlace(u4, 15);
        u3.addEnlace(u5, 25);
        
        System.out.println("\n5. Todos los usuarios con enlaces:");
        System.out.println("   " + u1);
        System.out.println("   " + u2);
        System.out.println("   " + u3);
        
        // Prueba getNumEnlaces
        System.out.println("\n6. getNumEnlaces():");
        System.out.println("   u1 tiene " + u1.getNumEnlaces() + " enlaces");
        System.out.println("   u2 tiene " + u2.getNumEnlaces() + " enlaces");
        System.out.println("   u3 tiene " + u3.getNumEnlaces() + " enlaces");
        
        // Prueba getEnlace por índice
        System.out.println("\n7. getEnlace(int):");
        System.out.println("   Enlace 0 de u1: " + u1.getEnlace(0));
        System.out.println("   Enlace 1 de u1: " + u1.getEnlace(1));
        System.out.println("   Enlace 2 de u1: " + u1.getEnlace(2));
        
        // Prueba getEnlace por usuario
        System.out.println("\n8. getEnlace(Usuario):");
        Enlace enlaceU1aU2 = u1.getEnlace(u2);
        System.out.println("   Enlace de u1 a u2: " + enlaceU1aU2);
        Enlace enlaceU1aU5 = u1.getEnlace(u5);
        System.out.println("   Enlace de u1 a u5: " + enlaceU1aU5 + " (debe ser null)");
        
        // Pruebas enlaces
        System.out.println("\nPRUEBAS DE ENLACE");
        
        Enlace enlPrueba = new Enlace(u4, u5, 30);
        System.out.println("\n9. Constructor y getters:");
        System.out.println("   Enlace creado: " + enlPrueba);
        System.out.println("   Usuario origen: " + enlPrueba.getUsuarioO().getNombre());
        System.out.println("   Usuario destino: " + enlPrueba.getUsuarioD().getNombre());
        System.out.println("   Coste: " + enlPrueba.getCoste());
        
        // Prueba coste mínimo
        System.out.println("\n10. Coste mínimo (debe ser 1):");
        Enlace enlCosteMin = new Enlace(u1, u5, 0);
        System.out.println("   Enlace con coste 0 -> coste real: " + enlCosteMin.getCoste());
        Enlace enlCosteNeg = new Enlace(u1, u5, -5);
        System.out.println("   Enlace con coste -5 -> coste real: " + enlCosteNeg.getCoste());
        
        // Prueba cambiarDestino
        System.out.println("\n11. cambiarDestino():");
        System.out.println("   Enlace antes: " + enlPrueba);
        boolean cambio1 = enlPrueba.cambiarDestino(u1, 40);
        System.out.println("   Cambiar destino a u1 y coste 40: " + cambio1);
        System.out.println("   Enlace después: " + enlPrueba);
        
        boolean cambio2 = enlPrueba.cambiarDestino(null, 10);
        System.out.println("   Cambiar destino a null: " + cambio2 + " (debe ser false)");
        
        boolean cambio3 = enlPrueba.cambiarDestino(u2, 0);
        System.out.println("   Cambiar con coste 0: " + cambio3 + " (debe ser false)");
        
        // Prueba costeReal y costeEspecial
        System.out.println("\n12. costeReal() y costeEspecial():");
        System.out.println("   Coste especial: " + enlPrueba.costeEspecial());
        System.out.println("   Coste real: " + enlPrueba.costeReal());
        
        // Pruebas mensaje
        System.out.println("\nPRUEBAS DE MENSAJE");
        
        // Crear red de usuarios
        Usuario alice = new Usuario("alice", 2);
        Usuario bob = new Usuario("bob", 3);
        Usuario charlie = new Usuario("charlie", 1);
        Usuario diana = new Usuario("diana", 4);
        
        alice.addEnlace(bob, 15);
        alice.addEnlace(charlie, 10);
        alice.addEnlace(diana, 25);
        bob.addEnlace(charlie, 5);
        bob.addEnlace(diana, 8);
        charlie.addEnlace(diana, 12);
        
        System.out.println("\n13. Red de usuarios:");
        System.out.println("   " + alice);
        System.out.println("   " + bob);
        System.out.println("   " + charlie);
        
        // Crear mensaje
        Mensaje msg1 = new Mensaje("Hola a todos", 50, alice);
        System.out.println("\n14. Constructor y getters:");
        System.out.println("   Mensaje: " + msg1);
        System.out.println("   Autor: " + msg1.getAutor().getNombre());
        System.out.println("   Capacidad: " + msg1.getCapac());
        System.out.println("   Usuario actual: " + msg1.getUsarioActual().getNombre());
        
        // Prueba puedeDifundirPor
        System.out.println("\n15. puedeDifundirPor():");
        Enlace enlAliceBob = alice.getEnlace(bob);
        System.out.println("   ¿Puede difundir por alice->bob (coste 15)? " + msg1.puedeDifundirPor(enlAliceBob));
        
        Enlace enlAliceDiana = alice.getEnlace(diana);
        System.out.println("   ¿Puede difundir por alice->diana (coste 25)? " + msg1.puedeDifundirPor(enlAliceDiana));
        
        // Prueba aceptadoPor
        System.out.println("\n16. aceptadoPor():");
        System.out.println("   ¿Bob acepta el mensaje? " + msg1.aceptadoPor(bob));
        System.out.println("   ¿Charlie acepta el mensaje? " + msg1.aceptadoPor(charlie));
        
        // Prueba difunde(Enlace) - caso exitoso
        System.out.println("\n17. difunde(Enlace) - caso exitoso:");
        System.out.println("   Antes: " + msg1);
        boolean dif1 = msg1.difunde(enlAliceBob);
        System.out.println("   Difundir alice->bob: " + dif1);
        System.out.println("   Después: " + msg1);
        System.out.println("   Capacidad esperada: 50 - 15 + 3 = 38");
        
        // Prueba difunde(Enlace) - falla por usuario incorrecto
        System.out.println("\n18. difunde(Enlace) - falla por usuario incorrecto:");
        Enlace enlAliceCharlie = alice.getEnlace(charlie);
        boolean dif2 = msg1.difunde(enlAliceCharlie);
        System.out.println("   Intentar difundir alice->charlie (msg está en bob): " + dif2 + " (debe ser false)");
        System.out.println("   Mensaje sigue en: " + msg1.getUsarioActual().getNombre());
        
        // Prueba difunde(Enlace) - falla por alcance insuficiente
        System.out.println("\n19. difunde(Enlace) - falla por alcance insuficiente:");
        Mensaje msg2 = new Mensaje("Test", 10, alice);
        System.out.println("   Mensaje con alcance 10: " + msg2);
        boolean dif3 = msg2.difunde(enlAliceDiana);
        System.out.println("   Intentar difundir alice->diana (coste 25): " + dif3 + " (debe ser false)");
        
        // Prueba difunde(List<Usuario>) - múltiples usuarios
        System.out.println("\n20. difunde(List<Usuario>) - múltiples usuarios:");
        Mensaje msg3 = new Mensaje("Broadcast", 100, alice);
        System.out.println("   Inicio: " + msg3);
        boolean dif4 = msg3.difunde(java.util.Arrays.asList(bob, diana)); // método para crear una lista rápida
        System.out.println("   Difundir a [bob, diana]: " + dif4);
        System.out.println("   Estado actual: " + msg3);
        
        // Prueba difunde(List<Usuario>) - con enlace inexistente
        System.out.println("\n21. difunde(List<Usuario>) - con enlace inexistente:");
        Mensaje msg4 = new Mensaje("Test2", 50, charlie);
        charlie.addEnlace(alice, 20);
        System.out.println("   Inicio: " + msg4);
        Usuario noExiste = new Usuario("nadie");
        boolean dif5 = msg4.difunde(java.util.Arrays.asList(diana, noExiste, alice));
        System.out.println("   Difundir a [diana, noExiste, alice]: " + dif5);
        System.out.println("   Final: " + msg4);
        
        // Prueba difunde(List<Usuario>) - falla por alcance
        System.out.println("\n22. difunde(List<Usuario>) - falla por alcance:");
        Mensaje msg5 = new Mensaje("Test3", 15, alice);
        System.out.println("   Inicio: " + msg5);
        boolean dif6 = msg5.difunde(java.util.Arrays.asList(bob, diana));
        System.out.println("   Difundir a [bob, diana]: " + dif6);
        System.out.println("   Final: " + msg5);
        System.out.println("   (Debe llegar a bob pero fallar con diana)");

        // Prueba con un solo usuario
        System.out.println("\n23. difunde(List<Usuario>) - un solo usuario:");
        Mensaje msg6 = new Mensaje("Solo uno", 30, alice);
        System.out.println("   Inicio: " + msg6);
        boolean dif7 = msg6.difunde(java.util.Arrays.asList(charlie));
        System.out.println("   Difundir solo a charlie: " + dif7);
        System.out.println("   Final: " + msg6);
        
        // Prueba sin usuarios
        System.out.println("\n24. difunde(List<Usuario>) - sin usuarios:");
        Mensaje msg7 = new Mensaje("Ninguno", 30, alice);
        boolean dif8 = msg7.difunde(java.util.Collections.emptyList()); // método para hacer una lista vacía
        System.out.println("   Difundir lista vacia: " + dif8);
        System.out.println("   Mensaje no cambia: " + msg7);
        
        System.out.println("\nPRUEBAS COMPLETADAS");
    }
}
