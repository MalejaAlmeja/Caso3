package caso3;
import java.util.ArrayList;

public class BuzonCuarentena {
    public int ocupacion = 0;
    public static ArrayList<Correo> correos;

    public BuzonCuarentena(){}

    //ESPERA SEMIACTIVA - CORREGIR
    public synchronized boolean recibirMensaje(Correo correo){
        ocupacion++;
        correos.add(correo); 
        return true;
    }

    //ESPERA SEMIACTIVA - CORREGIR
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
