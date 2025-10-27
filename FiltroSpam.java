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
        System.out.println("[" + this.getName() + "]: Despertó. ");

        boolean finalizado = false; // bandera para evitar múltiples mensajes de cierre

        while (true) {
            if (ServidorEntrega.llegoMensajeFin) break;

            Correo correo = buzonEntrada.sacarCorreo(this);
            if (correo == null) continue;

            if (correo.esInicio()) {
                synchronized (FiltroSpam.class) {
                    numeroClientes++;
                    if (!servidoresIniciados) {
                        servidoresIniciados = true;
                        System.out.println("====== Se detectó el primer correo de inicio, por lo que se inician los servidores de entrega. ======");
                        Simulador.iniciarServidoresEntrega();
                    }
                }

                System.out.println("[" + this.getName() + "]: Inició a recibir los correos de un nuevo cliente con id: " + correo.idCliente);
            }

            else if (correo.esFin()) {
                synchronized (FiltroSpam.class) {
                    numeroClientesProcesados++;
                    System.out.println("[" + this.getName() + "]: Recibió todos los correos del cliete " + correo.idCliente);

                    if (numeroClientesProcesados >= numClienteTot && !ServidorEntrega.llegoMensajeFin) {
                        ServidorEntrega.llegoMensajeFin = true;

                        System.out.println("======= Todos los correos de los clientes ya fueron recibidos ======");
                        System.out.println("[" + this.getName() + "]: Envía el correo de fin a todos los servidores de entrega y al buzón de cuarentena.");
                        enviarMensajeFinABuzonEntrega();
                        finalizado = true; // marcar que este filtro ya cerró
                        break;
                    }
                }
            }

            else if (correo.esSpam()) {
                int tiempoEsperaSpam = random.nextInt(10000, 20001);
                correo.setTiempoEsperaEnSpam(tiempoEsperaSpam);
                buzonCuarentena.recibirMensaje(correo);
                System.out.println("[" + this.getName() + "]: Recibió el correo " + correo.getId()+ " que es de tipo Spam del cliente " + correo.idCliente+ " y lo envió a cuarentena. ");
            }

            else {
                boolean insertado = false;
                while (!insertado) {
                    synchronized (buzonEntrega) {
                        insertado = buzonEntrega.recibirMensaje(correo);
                    }
                    if (!insertado) Thread.yield();
                }
                System.out.println("[" + this.getName() + "]: Recibió el correo " + correo.getId()+ " sin spam y lo envió al buzón de entrega. ");
            }
        }

        // 🔹 Mensaje único de finalización
        if (finalizado || ServidorEntrega.llegoMensajeFin) {
            System.out.println("[" + this.getName() + "]: Terminó su ejecución al quedar los buzones necesarios vacíos.");
        }
    }


    private void enviarMensajeFinABuzonEntrega() {
        Correo correoFin = new Correo(-1, false, false, false);
        correoFin.setFinDefinitivo();

        // Esperar a que no queden correos por procesar
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
        System.out.println("===== Correo de fin enviado correctamente a los buzones de entrega y cuarentena. ======");
    }
}
