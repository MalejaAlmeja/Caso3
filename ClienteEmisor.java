public class ClienteEmisor extends Thread {
    public int idCliente;
    public int numeroCorreos = 0;
    public int cantidadCorreoGenerados = 0;
    public BuzonEntrada buzonEntrada;

    public ClienteEmisor(int idCliente, BuzonEntrada buzonEntrada, int numeroCorreos) {
        this.idCliente = idCliente;
        this.buzonEntrada = buzonEntrada;
        this.numeroCorreos = numeroCorreos;
    }

    @Override
    public void run() {
        System.out.println("Cliente Emisor " + idCliente + " ha empezado a enviar correos.");
        while(true){
            Correo correo = generarCorreo();
            if(correo == null) break;
            buzonEntrada.recibirMensaje(this, correo);
        }
        System.out.println("Cliente Emisor " + idCliente + " ha terminado de enviar correos.");
    }

    public Correo generarCorreo(){
        synchronized(ClienteEmisor.class){
            if(cantidadCorreoGenerados >= (numeroCorreos + 2)){
                return null;
            }
            cantidadCorreoGenerados++;
            if(cantidadCorreoGenerados == 1){
                Correo c = new Correo(idCliente, false, true, false);
                System.out.println("El cliente " + this.idCliente + " generó el correo (INICIO) " + c.getId());
                return c;
            } else if (cantidadCorreoGenerados == numeroCorreos + 2){
                Correo c = new Correo(idCliente, false, false, true);
                System.out.println("El cliente " + this.idCliente + " generó el correo (FIN) " + c.getId());
                return c;
            } else {
                boolean esSpam = Math.random() < 0.5; 
                Correo c = new Correo(idCliente, esSpam, false, false);
                System.out.println("El cliente " + this.idCliente + " generó el correo " + c.getId());
                return c;
            }
        }
    }
}
