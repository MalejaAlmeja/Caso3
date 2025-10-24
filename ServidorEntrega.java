import java.util.Random;

public class ServidorEntrega extends Thread {
    public BuzonEntrega buzonEntrega;
    public Random random = new Random();
    public static volatile boolean llegoMensajeFin = false;
	public String nombre;

    public ServidorEntrega(String nombre, BuzonEntrega bzEntrega) {
		this.nombre = nombre;
        this.buzonEntrega = bzEntrega;
    }

    @Override
    public void run() {
        while(!llegoMensajeFin) {
            Correo correo = buzonEntrega.eliminarMensaje();
            if(correo == null) {
                //Thread.yield();//Migue, seguro este yield iría aquí? No sería espera activa aquí nomás con continue?
                //Funciona bien si se quita el yield, pero lo dejo a tu decición :)
                continue;
            }
            if(correo.finDefinitivo) {
                llegoMensajeFin = true;
                break;
            } else {
                int tiempoLectura = random.nextInt(1, 1001);
                try { Thread.sleep(tiempoLectura); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
    }
}
