public class EjemploUsoMensajesBasicos {
    public static void main(String[] args) {
        Usuario ana = new Usuario("ana", 1);        // capacidad de amplificación 1
        Usuario luis = new Usuario("luis", 5);
        Usuario carmen = new Usuario("carmen");     // por defecto capacidad 2
        
        Mensaje m = new Mensaje("¡Hola!", 50, ana);    // texto (¡Hola!) 50 unid. alcance inicial, msj en ana
        ana.addEnlace(new Enlace(ana, luis, 68));
        ana.addEnlace(carmen, 33);
        System.out.println(m);
        
        m.difunde(luis, carmen);    // irá directamente a @carmen
        System.out.println(m);      // alcance 19 = 50 - 33 + 2
        
        carmen.addEnlace(new Enlace(carmen, luis, 11));
        m.difunde(carmen.getEnlace(luis));
        System.out.println(m);      // en @luis con alcance 13 = 19 - 11 + 5
    }
}
