import java.util.ArrayList;
import java.util.List;

public class BuzonCuarentena {
    public int ocupacion = 0;
    private final ArrayList<Correo> correos;

    public BuzonCuarentena(){
        this.correos = new ArrayList<>();
    }

    public synchronized boolean recibirMensaje(Correo correo){
        correos.add(correo);
        ocupacion = correos.size();
        notifyAll();
        return true;
    }

    public synchronized boolean eliminarMensaje(Correo correo){
        boolean removed = correos.remove(correo);
        if (removed) {
            ocupacion = correos.size();
            notifyAll();
        }
        return removed;
    }

    public synchronized List<Correo> getCorreos(){
        return new ArrayList<>(this.correos);
    }
}

