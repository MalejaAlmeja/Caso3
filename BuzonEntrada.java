import java.util.ArrayList;

public class BuzonEntrada {
    public int capacidad = 0;
    public int ocupacion = 0;
    public ArrayList<Correo> correos;

    public BuzonEntrada(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void entraCorreo(Correo correo) {
        correos.add(correo);
        ocupacion++;
        //CREO QUE AQUÍ PONGO LO DEL WAIT Y TAL
    }

    
    
    
    
}
