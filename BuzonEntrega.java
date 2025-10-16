import java.util.ArrayList;

public class BuzonEntrega {
 private int capacidad = 0;
 private int ocupacion = 0;
 public static ArrayList<Correo> correos;

    public BuzonEntrega(int capacidad) {
        this.capacidad = capacidad;
    }   

    //ESPERA SEMIACTIVA - CAMBIAR CREO - NO SÉ DÓNDE DE PONE EL YIELD
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

    //ESPERA ACTIVA - CAMBIAR
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
