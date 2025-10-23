public class Simulador {

    // Buzones compartidos
    private static BuzonEntrada buzonEntrada;
    private static BuzonCuarentena buzonCuarentena;
    private static BuzonEntrega buzonEntrega;

    // Parámetros configurables
    private static final int NUM_CLIENTES = 5;
    private static final int NUM_FILTROS = 5;
    private static final int NUM_SERVIDORES = 3;

    private static final int cantidadCorreosPorClientes = 5;

    public static void main(String[] args) {
        System.out.println("========== INICIO DE LA SIMULACIÓN ==========");

  
        buzonEntrada = new BuzonEntrada(10);
        buzonCuarentena = new BuzonCuarentena();
        buzonEntrega = new BuzonEntrega(10);


        FiltroSpam.numClienteTot = NUM_CLIENTES;
        FiltroSpam.numeroClientes = 0;
        FiltroSpam.numeroClientesProcesados = 0;
        FiltroSpam.servidoresIniciados = false;

        ServidorEntrega.llegoMensajeFin = false; 


        for (int i = 1; i <= NUM_CLIENTES; i++) {
            ClienteEmisor cliente = new ClienteEmisor(i, buzonEntrada, cantidadCorreosPorClientes);
            cliente.start();
        }

        for (int i = 1; i <= NUM_FILTROS; i++) {
            FiltroSpam filtro = new FiltroSpam(buzonEntrada, buzonCuarentena, buzonEntrega);
            filtro.setName("Filtro-" + i);
            filtro.start();
        }

        while (!ServidorEntrega.llegoMensajeFin) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("========== SIMULACIÓN FINALIZADA ==========");
    }

    public static void iniciarServidoresEntrega() {
        System.out.println("Iniciando servidores de entrega...");

        for (int i = 1; i <= NUM_SERVIDORES; i++) {
            ServidorEntrega servidor = new ServidorEntrega("ServidorEntrega-" + i, buzonEntrega);
            servidor.start();
        }

        System.out.println(NUM_SERVIDORES + " servidores de entrega iniciados correctamente.");
    }
}
