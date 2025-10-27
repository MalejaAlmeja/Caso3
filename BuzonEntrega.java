import java.util.ArrayList;

public class BuzonEntrega {
    private int capacidad;
    private int ocupacion = 0;
    private final ArrayList<Correo> correos;

    public BuzonEntrega(int capacidad) {
        this.capacidad = capacidad;
        this.correos = new ArrayList<>();
    }   

    // Semiactiva: devuelve false si está lleno
    public synchronized boolean recibirMensaje(Correo correo){
        // si está lleno, devolvemos false (semiactivo)
        if (ocupacion == capacidad) {
            return false;
        }

        // ✅ no debemos alterar llegoMensajeFin aquí (eso lo hace el servidor al leer)
        correos.add(correo);
        ocupacion = correos.size();
        notifyAll();
        return true;
    }

    // Espera activa en servidores: devuelve null si vacío
    public synchronized Correo eliminarMensaje(ServidorEntrega serv) {
        if (correos.isEmpty()) {
            return null; 
        }

        Correo c = correos.remove(0);
        System.out.println("[" + serv.nombre + "]: El servidor de entrega obtuvo el correo " + c.getId());
        ocupacion = correos.size();
        notifyAll(); 
        return c;
    }
}
