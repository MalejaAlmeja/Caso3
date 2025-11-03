import java.util.Random;

public class ServidorEntrega extends Thread {
    public BuzonEntrega buzonEntrega;
    public Random random = new Random();
    public static boolean llegoMensajeFin = false;
	public String nombre;

    public ServidorEntrega(String nombre, BuzonEntrega bzEntrega) {
		this.nombre = nombre;
        this.buzonEntrega = bzEntrega;
    }

    @Override
    public void run() {
        while (true) {
            Correo correo = buzonEntrega.eliminarMensaje(this);

            if (correo == null) {
                
                if (llegoMensajeFin && buzonEntregaVacio()) {
                    break;
                }
                
            }
            else{
                if (correo.finDefinitivo) {
                    llegoMensajeFin = true;
                    break;
                } else {
                int tiempoLectura = random.nextInt(1, 1001);
                try { 
                    Thread.sleep(tiempoLectura); 
                } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt(); 
                }
              }
            }
        }

        System.out.println("[" + this.nombre + "]: Finaliza al haber recibido el mensaje de fin. ");
    }

    private boolean buzonEntregaVacio() {
        synchronized (buzonEntrega) {
                int valorOcupacion = BuzonEntrega.ocupacion;
                if(valorOcupacion == 0){
                    return true;
                }
                else return false;

        }
    }
}
