import java.util.Random;

public class FiltroSpam extends Thread {
    private final Random random = new Random();

    private final BuzonEntrada buzonEntrada;
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;

    public static int numeroClientes = 0;
    public static int numClienteTot = 0;
    public static int numeroClientesProcesados = 0;
    public static boolean servidoresIniciados = false;

    public FiltroSpam(BuzonEntrada buzonEntrada, BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega) {
        this.buzonEntrada = buzonEntrada;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
    }

    @Override
    public void run() {
        System.out.println("Filtro de Spam despertado.");

        while (true) {
            if (ServidorEntrega.llegoMensajeFin) break;

            Correo correo = buzonEntrada.sacarCorreo(this);
            if (correo == null) continue;


            if (correo.esInicio()) {
                synchronized (FiltroSpam.class) {
                    numeroClientes++;
                    if (!servidoresIniciados) {
                        servidoresIniciados = true;
                        System.out.println("Primer cliente detectado. Iniciando servidores de entrega...");
                        Simulador.iniciarServidoresEntrega();
                    }
                }

                System.out.println("El filtro de spam " + this.getName() +
                        " recibió correos de un nuevo cliente.");
            }

            else if (correo.esFin()) {
                synchronized (FiltroSpam.class) {
                    numeroClientesProcesados++;
                    System.out.println("El filtro de spam " + this.getName() +
                            " recibió todos los correos del usuario.");

                    if (numeroClientesProcesados >= numClienteTot && !ServidorEntrega.llegoMensajeFin) {
                        ServidorEntrega.llegoMensajeFin = true;
                        System.out.println("Todos los clientes procesados. " + this.getName() +
                                " envía el correo de fin.");
                        enviarMensajeFinABuzonEntrega();
                    }
                }
            }

            else if (correo.esSpam()) {
                int tiempoEsperaSpam = random.nextInt(10000, 20001);
                correo.setTiempoEsperaEnSpam(tiempoEsperaSpam);
                buzonCuarentena.recibirMensaje(correo);
                System.out.println("Filtro de Spam ha detectado un correo spam del Cliente Emisor " +
                        correo.idCliente + " y lo ha enviado a cuarentena.");
            }

            else {
                boolean insertado = false;
                while (!insertado) {
                    synchronized (buzonEntrega) {
                        insertado = buzonEntrega.recibirMensaje(correo);
                    }
                    if (!insertado) Thread.yield(); 
                }
                System.out.println("Filtro de Spam ha verificado un correo no spam del Cliente Emisor " +
                        correo.idCliente + " y lo ha enviado al buzón de entrega.");
            }
        }

        System.out.println(this.getName() + " ha finalizado su ejecución.");
    }


    private void enviarMensajeFinABuzonEntrega() {
        Correo correoFin = new Correo(-1, false, false, false);
        correoFin.setFinDefinitivo();


        while (buzonEntrada.ocupacion != 0 || buzonCuarentena.ocupacion != 0) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        boolean entregado = false;
        while (!entregado) {
            synchronized (buzonEntrega) {
                entregado = buzonEntrega.recibirMensaje(correoFin);
            }
            if (!entregado) Thread.yield();
        }

        buzonCuarentena.recibirMensaje(correoFin);
        System.out.println("Correo de fin enviado correctamente a entrega y cuarentena.");
    }
}
