
public class ClienteEmisor extends Thread {
    int idCliente;
    int numeroCorreos;
    static BuzonEntrada buzonEntrada;
    
    
    public ClienteEmisor(int idCliente, int numeroCorreos, int tamanioBuzonEntrada, BuzonEntrada buzonEntrada) {
        this.idCliente = idCliente;
        this.numeroCorreos = numeroCorreos;
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < numeroCorreos; i++) {
            boolean esSpam;
            esSpam = Math.random() < 0.5; 
            Correo correo = new Correo(idCliente, esSpam);

            System.out.println("Cliente Emisor " + idCliente + " enviando correo " + (i + 1) + "/" + numeroCorreos + (esSpam ? " (SPAM)" : ""));
            while (buzonEntrada.capacidad <= buzonEntrada.ocupacion) {
                try {
                    wait(); //CREO QUE ACÁ NO VA
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            buzonEntrada.correos.add(correo);
            buzonEntrada.ocupacion++;
        }
        System.out.println("Cliente Emisor " + idCliente + " ha terminado de enviar correos.");
    }
    
}
