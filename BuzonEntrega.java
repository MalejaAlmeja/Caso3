package caso3;
import java.util.ArrayList;

public class BuzonEntrega {
 private int capacidad;
 private int ocupacion = 0;
 public static ArrayList<Correo> correos;

    public BuzonEntrega(int capacidad) {
        this.capacidad = capacidad;
    }   

    public synchronized boolean recibirMensaje(Correo correo){
        while (ocupacion == capacidad) {
            return false;
        }
        notifyAll();
        ocupacion++;
        correos.add(correo); 
        return true;
    }

    public synchronized Correo eliminarMensaje() {
        if (correos.isEmpty()) {
            return null; 
        }
        Correo c = correos.remove(0);
        notifyAll(); 
        return c;
    }
    
}
