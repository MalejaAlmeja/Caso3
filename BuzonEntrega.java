import java.util.ArrayList;

public class BuzonEntrega {
    private int capacidad;
    private int ocupacion = 0;
    private final ArrayList<Correo> correos;

    public BuzonEntrega(int capacidad) {
        this.capacidad = capacidad;
        this.correos = new ArrayList<>();
    }   

    public synchronized boolean recibirMensaje(Correo correo){
        if (ocupacion == capacidad) {
            return false;
        }
        correos.add(correo);
        ocupacion = correos.size();
        notifyAll();
        return true;
    }

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
