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
        boolean seguirCorriendoCuarentena = true;
        while(seguirCorriendoCuarentena) {
            List<Correo> copia = buzonCuarentena.getCorreos();
            for(Correo correo : copia) {

                if(correo.esFinDefinitivo()){
                    System.out.println("[Manejador Cuarentena]: Recibió el mensaje de fin que le permite finalizar. ");
                    seguirCorriendoCuarentena = false;
                    break;
                }

                int numeroRechazo = random.nextInt(1, 22);
                if(numeroRechazo % 7 == 0){
                    if(buzonCuarentena.eliminarMensaje(correo)) {
                            System.out.println("[Manejador Cuarentena]: Eliminó el correo " + correo.getId() + " por malicioso. ");
                        }
                }
                else{
                    synchronized (correo) {
                        if (correo.tiempoSpam > 0) {
                            correo.setTiempoEsperaEnSpam(correo.tiempoSpam - 1);
                        }
                    }
                }

                if(correo.tiempoSpam <= 0 && !correo.finDefinitivo) {
                    System.out.println("[Manejador Cuarentena]: Redujo el tiempo de espera en Spam a 0. ");

                    boolean eliminado = buzonCuarentena.eliminarMensaje(correo);
                    boolean checkSemiActiva = false;
                    while(!checkSemiActiva) {
                        synchronized (buzonEntrega) {
                            checkSemiActiva = buzonEntrega.recibirMensaje(correo);
                            if (checkSemiActiva) {
                                System.out.println("[Manejador Cuarentena]: Envió el correo " + correo.getId() + " al buzon de entrega después de la revisión. ");
                            }
                        }
                        if(!checkSemiActiva){
                            Thread.yield();
                        } 
                    }
                }
            }
            try { 
                Thread.sleep(1000); 
            } catch (InterruptedException e) 
            { 
                Thread.currentThread().interrupt(); 
            }
        }
        System.out.println("[Manejador Cuarentena]: Se apaga");

    }
}


