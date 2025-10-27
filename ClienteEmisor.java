import java.util.Random;

public class ClienteEmisor extends Thread {
    public Random random = new Random();
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
        System.out.println("[Cliente " + idCliente + "]: Empieza a enviar correos. ");
        while(true){
            Correo correo = generarCorreo();
            if(correo == null) break;
            buzonEntrada.recibirMensaje(this, correo);
        }
        System.out.println("[Cliente " + idCliente + "]: Terminó de enviar correos. ");
    }

    public Correo generarCorreo(){
        synchronized(ClienteEmisor.class){
            if(cantidadCorreoGenerados >= (numeroCorreos + 2)){
                return null;
            }
            cantidadCorreoGenerados++;
            if(cantidadCorreoGenerados == 1){
                Correo c = new Correo(idCliente, false, true, false);
                System.out.println("[Cliente " + this.idCliente + "]: envió el correo de Inicio con id: " + c.getId());
                
                return c;
            } else if (cantidadCorreoGenerados == numeroCorreos + 2){
                Correo c = new Correo(idCliente, false, false, true);
                System.out.println("[Cliente " + this.idCliente + "]: envió el correo de Fin con id: " + c.getId());
                return c;
            } else {
                double numero = random.nextDouble();
                boolean esSpam = true;
                if(numero < 0.5){
                    esSpam = false;
                }
                Correo c = new Correo(idCliente, esSpam, false, false);
                System.out.println("[Cliente " + this.idCliente + "]: envió un correo normal con id: " + c.getId());
                return c;
            }
        }
    }
}
