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
        if (ocupacion == capacidad) {
            return false;
        }
        correos.add(correo);
        ocupacion = correos.size();
        notifyAll();
        return true;
    }

    // Espera activa en servidores: devuelve null si vacío
    public synchronized Correo eliminarMensaje() {
        if (correos.isEmpty()) {
            return null; 
        }
        Correo c = correos.remove(0);
        ocupacion = correos.size();
        notifyAll(); 
        return c;
    }
}
