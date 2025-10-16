import java.util.ArrayList;

public class BuzonEntrada {
    public int capacidad = 0;
    public int ocupacion = 0;
    public static ArrayList<Correo> correos;

    public BuzonEntrada(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void recibirMensaje(Correo correo){
        while (ocupacion == capacidad) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();
        ocupacion++;
        correos.add(correo); 
    }

    public synchronized void eliminarMensaje(Correo correo){
        while (ocupacion == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();
        ocupacion--;
        correos.remove(correo);
    }


    
    
    
    
}
