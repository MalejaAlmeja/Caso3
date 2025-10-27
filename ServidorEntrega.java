import java.util.Random;

public class ServidorEntrega extends Thread {
    public BuzonEntrega buzonEntrega;
    public Random random = new Random();
    public static volatile boolean llegoMensajeFin = false; // volatile para sincronización visible
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
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                if (llegoMensajeFin && buzonEntregaVacio()) break;
                continue;
            }

            if (correo.finDefinitivo) {
                llegoMensajeFin = true;
                break;
            } else {
                int tiempoLectura = random.nextInt(1, 1001);
                try { Thread.sleep(tiempoLectura); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }

        System.out.println("[" + this.nombre + "]: Finaliza al haber recibido el mensaje de fin. ");
    }

    private boolean buzonEntregaVacio() {
        synchronized (buzonEntrega) {
            try {
                var field = BuzonEntrega.class.getDeclaredField("ocupacion");
                field.setAccessible(true);
                return field.getInt(buzonEntrega) == 0;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
