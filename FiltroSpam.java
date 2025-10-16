public class FiltroSpam extends Thread {
    BuzonEntrada buzonEntrada;
    BuzonCuarentena buzonCuarentena;
    BuzonEntrega buzonEntrega;
    int numeroFiltros;
    int numeroClientes = 0;
    int numeroClientesProcesados = 0;
    
    public FiltroSpam(int numeroFiltros, BuzonEntrada buzonEntrada, BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega) {
        this.numeroFiltros = numeroFiltros;
        this.buzonEntrada = buzonEntrada;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
    }

    @Override
    public void run() { 
        System.out.println("Filtro de Spam despertado.");
        while (true) {
            if (buzonEntrada.ocupacion > 0) {
                Correo correo = buzonEntrada.correos.get(0);
                buzonEntrada.eliminarMensaje(correo);
                if (correo.esInicio()) {
                    numeroClientes++;
                    System.out.println("Filtro de Spam ha recibido el correo de inicio del Cliente Emisor " + correo.idCliente + ".");
                } else if (correo.esFin()) {
                    numeroClientesProcesados++;
                    System.out.println("Filtro de Spam ha recibido el correo de fin del Cliente Emisor " + correo.idCliente + ".");
                    if (numeroClientesProcesados == numeroClientes) {
                        System.out.println("Filtro de Spam ha procesado todos los clientes emisores. Terminando.");
                        //TODO
                        //Los filtros de spam finalizan cuando se han recibido tantos mensajes de 
                        //FIN como número de clientes y se ha entregado un mensaje de FIN en el 
                        //buzón de entrega y un mensaje de fin en el buzón de cuarentena. 
                        break;
                    }
                } else if (correo.esSpam()) {
                    buzonCuarentena.recibirMensaje(correo);
                    System.out.println("Filtro de Spam ha detectado un correo spam del Cliente Emisor " + correo.idCliente + " y lo ha enviado a cuarentena.");
                } else {
                    buzonEntrega.recibirMensaje(correo);
                    System.out.println("Filtro de Spam ha verificado un correo no spam del Cliente Emisor " + correo.idCliente + " y lo ha enviado al buzón de entrega.");
                }
            }
        }
    }

}
