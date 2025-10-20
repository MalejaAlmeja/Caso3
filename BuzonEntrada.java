package caso3;
import java.util.ArrayList;

public class BuzonEntrada {
	
    public int capacidad;
    public int ocupacion = 0;
    public static ArrayList<Correo> correos;

    public BuzonEntrada(int capacidad) {
        this.capacidad = capacidad;
        this.correos = new ArrayList<Correo>();
    }

    //voy a ir almacenando los mensajes en el buzon de entrada
    public synchronized void recibirMensaje(Correo correo){
    	//en caso que la cantidad de correos que metí es igual a la capacidad del buffer me toca esperar
        while (ocupacion == capacidad) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //meto el correo al buzon
        correos.add(correo); 
        notifyAll();
        ocupacion++;
        
    }

    //cuando ya vaya sacando los mensajes del buzón de entrada
    public synchronized void eliminarMensaje(Correo correo){
        while (ocupacion == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        correos.remove(correo);
        notifyAll();
        ocupacion--;
        
    }


    
    
    
    
}
