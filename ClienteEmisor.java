
public class ClienteEmisor extends Thread {
    int idCliente;
    int numeroCorreos;
    static BuzonEntrada buzonEntrada;
    
    
    public ClienteEmisor(int idCliente,int numeroCorreos, BuzonEntrada buzonEntrada) {
        this.idCliente = idCliente;
        this.numeroCorreos = numeroCorreos;
        this.buzonEntrada = buzonEntrada;
    }

    @Override
    public void run() {
        System.out.println("Cliente Emisor " + idCliente + " ha empieza a enviar correos.");
        for (int i = 0; i < numeroCorreos+2; i++) {
            if (i==0){
                Correo correoInicio = new Correo(idCliente, false, true, false);
                buzonEntrada.recibirMensaje(correoInicio);
            }
            else if (i==numeroCorreos+1){
                Correo correoFin = new Correo(idCliente, false, false, true);
                buzonEntrada.recibirMensaje(correoFin);
                break;
            }
            else{
            boolean esSpam;
            esSpam = Math.random() < 0.5; 
            Correo correo = new Correo(idCliente, esSpam, false, false);
            buzonEntrada.recibirMensaje(correo);
            }
        }
        System.out.println("Cliente Emisor " + idCliente + " ha terminado de enviar correos.");
    }
    
}
