import java.io.IOException;

public class PruebaRedSocial {
	public static void main(String[] args) {
		try {
			RedSocial red = new RedSocial("txt/USUARIOS.txt", "txt/ENLACES.txt", "txt/MENSAJES.txt");
			System.out.println("PRUEBA DE RED SOCIAL");
			System.out.println();

			System.out.println("1. Carga inicial de archivos");
			System.out.println("   Red social cargada correctamente desde la carpeta txt");
			System.out.println();

			System.out.println("2. Creacion de usuarios");
			Usuario lucia = red.crearUsuario("lucia");
			Usuario pablo = red.crearUsuario("pablo", 4);
			Usuario repetido = red.crearUsuario("ana");
			System.out.println("   Crear usuario lucia: " + (lucia != null));
			System.out.println("   Crear usuario pablo con capacidad 4: " + (pablo != null));
			System.out.println("   Crear usuario repetido ana: " + (repetido == null));
			System.out.println();

			System.out.println("3. Creacion de enlaces");
			Enlace enlace1 = red.crearEnlace("lucia", "ana", 3);
			Enlace enlace2 = red.crearEnlace("pablo", "lucia", 5);
			Enlace enlaceInvalido = red.crearEnlace("lucia", "nadie", 2);
			System.out.println("   Crear enlace lucia -> ana: " + enlace1);
			System.out.println("   Crear enlace pablo -> lucia: " + enlace2);
			System.out.println("   Crear enlace a usuario inexistente: " + (enlaceInvalido == null));
			System.out.println();

			System.out.println("4. Creacion de mensajes");
			Mensaje mensaje1 = red.crearMensaje("Hola desde lucia", 12, "lucia");
			Mensaje mensaje2 = red.crearMensaje("Mensaje de prueba", 20, "pablo");
			Mensaje mensajeInvalido = red.crearMensaje("No debe crearse", 10, "fantasma");
			System.out.println("   Crear mensaje desde lucia: " + mensaje1);
			System.out.println("   Crear mensaje desde pablo: " + mensaje2);
			System.out.println("   Crear mensaje con autor inexistente: " + (mensajeInvalido == null));
			System.out.println();

			System.out.println("5. Escritura de datos");
			red.escribirTodo("txt/SALIDA_USUARIOS.txt", "txt/SALIDA_ENLACES.txt", "txt/SALIDA_MENSAJES.txt");
			System.out.println("   Archivos de salida generados correctamente");
			System.out.println();

			System.out.println("PRUEBA FINALIZADA");
		} catch (IOException e) {
			System.out.println("Error al cargar la red social");
		}
	}
}
