import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Simulador {

    // Buzones compartidos
    private static BuzonEntrada buzonEntrada;
    private static BuzonCuarentena buzonCuarentena;
    private static BuzonEntrega buzonEntrega;

    // Parámetros configurables
    private static int NUM_CLIENTES;
    private static int NUM_FILTROS;
    private static int NUM_SERVIDORES;
    private static int NUM_CORREOS;
    private static int CAPACIDAD_ENTRADA;
    private static int CAPACIDAD_ENTREGA;

    public static void main(String[] args) {
        //Lectura del archivo

       try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {
            NUM_CLIENTES = Integer.parseInt(br.readLine().split(":")[1].trim());
            NUM_FILTROS = Integer.parseInt(br.readLine().split(":")[1].trim());
            NUM_SERVIDORES = Integer.parseInt(br.readLine().split(":")[1].trim());
            NUM_CORREOS = Integer.parseInt(br.readLine().split(":")[1].trim());
            CAPACIDAD_ENTRADA = Integer.parseInt(br.readLine().split(":")[1].trim());
            CAPACIDAD_ENTREGA = Integer.parseInt(br.readLine().split(":")[1].trim());
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
            System.exit(1);
        }

        //Finalizar de leer el archivo

        System.out.println("========== INICIO DE LA SIMULACIÓN ==========");

        buzonEntrada = new BuzonEntrada(CAPACIDAD_ENTRADA);
        buzonCuarentena = new BuzonCuarentena();
        buzonEntrega = new BuzonEntrega(CAPACIDAD_ENTREGA);

        FiltroSpam.numClienteTot = NUM_CLIENTES;
        FiltroSpam.numeroClientes = 0;
        FiltroSpam.numeroClientesProcesados = 0;
        FiltroSpam.servidoresIniciados = false;

        ServidorEntrega.llegoMensajeFin = false; 


        for (int i = 1; i <= NUM_CLIENTES; i++) {
            ClienteEmisor cliente = new ClienteEmisor(i, buzonEntrada, NUM_CORREOS);
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
