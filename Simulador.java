import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Simulador {

    private static BuzonEntrada buzonEntrada;
    private static BuzonCuarentena buzonCuarentena;
    private static BuzonEntrega buzonEntrega;
    private static ManejadorCuarentena manejadorCuarentena;

    private static int numeroClientes;
    private static int numeroFiltrosSpam;
    private static int numeroServidores;
    private static int numeroCorreosPorUsuario;
    private static int limiteBuzonEntrada;
    private static int limiteBuzonEntrega;

    // 🔹 NUEVO: para almacenar referencias a hilos creados
    private static final ArrayList<ClienteEmisor> clientes = new ArrayList<>();
    private static final ArrayList<FiltroSpam> filtros = new ArrayList<>();
    public static final ArrayList<ServidorEntrega> servidores = new ArrayList<>();

    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {
            numeroClientes = Integer.parseInt(br.readLine().split(":")[1].trim());
            numeroFiltrosSpam = Integer.parseInt(br.readLine().split(":")[1].trim());
            numeroServidores = Integer.parseInt(br.readLine().split(":")[1].trim());
            numeroCorreosPorUsuario = Integer.parseInt(br.readLine().split(":")[1].trim());
            limiteBuzonEntrada = Integer.parseInt(br.readLine().split(":")[1].trim());
            limiteBuzonEntrega = Integer.parseInt(br.readLine().split(":")[1].trim());
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("========== INICIO DEL CASO 3 DE TIC ==========");

        buzonEntrada = new BuzonEntrada(limiteBuzonEntrada);
        buzonCuarentena = new BuzonCuarentena();
        buzonEntrega = new BuzonEntrega(10);

        manejadorCuarentena = new ManejadorCuarentena(buzonCuarentena, buzonEntrega);
        manejadorCuarentena.start();

        FiltroSpam.numClienteTot = numeroClientes;
        FiltroSpam.numeroClientes = 0;
        FiltroSpam.numeroClientesProcesados = 0;
        FiltroSpam.servidoresIniciados = false;

        ServidorEntrega.llegoMensajeFin = false;

        // Crear y lanzar clientes
        for (int i = 1; i <= numeroClientes; i++) {
            ClienteEmisor cliente = new ClienteEmisor(i, buzonEntrada, numeroCorreosPorUsuario);
            clientes.add(cliente);
            cliente.start();
        }

        // Crear y lanzar filtros
        for (int i = 1; i <= numeroFiltrosSpam; i++) {
            FiltroSpam filtro = new FiltroSpam(buzonEntrada, buzonCuarentena, buzonEntrega);
            filtro.setName("Filtro " + i);
            filtros.add(filtro);
            filtro.start();
        }

        // Esperar a que llegue el mensaje de fin global
        while (!ServidorEntrega.llegoMensajeFin) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 🔹 Esperar que los servidores, filtros y manejador terminen antes de finalizar el programa
        try {
            for (ClienteEmisor c : clientes) c.join();
            System.out.println("A1");

            if (manejadorCuarentena != null) manejadorCuarentena.join();
            System.out.println("A3");

            // Esperar servidores (se crean más tarde, así que usamos la lista estática)
            for (ServidorEntrega s : servidores) s.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ✅ Ahora sí imprimimos el fin REAL
        System.out.println("========== FINALIZA EL CASO 3 DE TIC ==========");
    }

    public static void iniciarServidoresEntrega() {
        System.out.println("Iniciando servidores de entrega");
        //ServidorEntrega[] serv = new ServidorEntrega[numeroServidores];

        for (int i = 1; i <= numeroServidores; i++) {
            ServidorEntrega servidor = new ServidorEntrega("Servidor Entrega " + i, buzonEntrega);
            servidores.add(servidor); // 🔹 almacenar referencia para hacer join luego
            System.out.println("[" + servidor.nombre + "]: Se creó un nuevo servidor de entrega. ");
            servidor.start();
        }

        System.out.println(numeroServidores + " servidores de entrega iniciados correctamente.");
    }

    public static void filtrosMensaje(){
        for(FiltroSpam fl: filtros){
            System.out.println("[" + fl.getName() + "]: Terminó su ejecución al quedar los buzones necesarios vacíos. ");
        }
    }

}
