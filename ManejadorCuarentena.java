import java.util.List;
import java.util.Random;

public class ManejadorCuarentena extends Thread{
    public Random random = new Random();
    private BuzonCuarentena buzonCuarentena;
    private BuzonEntrega buzonEntrega;

    public ManejadorCuarentena(BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega) {
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
    }

    @Override
    public void run() {
        while(true) {
            List<Correo> copia = buzonCuarentena.getSnapshot();
            for(Correo correo : copia) {
                synchronized (correo) {
                    if (correo.tiempoSpam > 0) {
                        correo.setTiempoEsperaEnSpam(correo.tiempoSpam - 1);
                    }
                }
                if(correo.tiempoSpam <= 0 && !correo.finDefinitivo) {
                    int numeroRechazo = random.nextInt(1,22);
                    if(numeroRechazo % 7 == 0) {
                        if(buzonCuarentena.eliminarMensaje(correo)) {
                            System.out.println("Se eliminó mensaje malicioso");
                        }
                    } else {
                        boolean checkSemiActiva = false;
                        while(!checkSemiActiva) {
                            synchronized (buzonEntrega) {
                                checkSemiActiva = buzonEntrega.recibirMensaje(correo);
                            }
                            if(!checkSemiActiva) Thread.yield();
                        }
                        if(buzonCuarentena.eliminarMensaje(correo)) {
                            System.out.println("Se puso el mensaje de cuarentena a buzon de entrega");
                        }
                    }
                }
            }
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
}
