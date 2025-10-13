import java.util.ArrayList;

public class ClienteEmisor extends Thread {
    int idCliente;
    int numeroCorreos;
    int tamanioBuzonEntrada;
    static ArrayList<Correo> buzonEntrada;
    
    public ClienteEmisor(int idCliente, int numeroCorreos, int tamanioBuzonEntrada) {
        this.idCliente = idCliente;
        this.numeroCorreos = numeroCorreos;
        this.buzonEntrada = new ArrayList<>(tamanioBuzonEntrada);
        System.out.println("Cliente Emisor " + idCliente + " creado, creando " + numeroCorreos + " correos.");
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < numeroCorreos; i++) {
            boolean esSpam;
            esSpam = Math.random() < 0.5; 

            Correo correo = new Correo(idCliente, esSpam);
            synchronized (buzonEntrada) {
                buzonEntrada.add(correo); // añadir caso donde el buzon esta lleno, no sé si es acá
                System.out.println("Cliente Emisor " + idCliente + " ha enviado el correo " + correo.getId());
                buzonEntrada.notifyAll();
            }
            try {
                Thread.sleep((long) (Math.random() * 1000)); // resolver
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Cliente Emisor " + idCliente + " ha terminado de enviar correos.");
    }
    
}
