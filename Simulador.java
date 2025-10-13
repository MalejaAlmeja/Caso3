import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Simulador {
    public static void main(String[] args) {
        ArrayList<Correo> buzonEntrada;
        ArrayList<Correo> buzonEntrega;
        ArrayList<Correo> buzonCuarentena;

        int clientesEmisores = 0;
        int mensajesPorCliente = 0;
        int filtrosSpam = 0;
        int servidoresEntrega = 0;
        int capacidadMaximaBuzonEntrada = 0;
        int capacidadBuzonEntrega = 0;


        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Numero clientes emisores:")) {
                    clientesEmisores = Integer.parseInt(linea.split(": ")[1].trim());
                } else if (linea.startsWith("Numero mensajes por cliente:")) {
                    mensajesPorCliente = Integer.parseInt(linea.split(": ")[1].trim());
                } else if (linea.startsWith("Numero filtros de spam:")) {
                    filtrosSpam = Integer.parseInt(linea.split(": ")[1].trim());
                } else if (linea.startsWith("Numero de servidores de entrega:")) {
                    servidoresEntrega = Integer.parseInt(linea.split(": ")[1].trim());
                } else if (linea.startsWith("Capacidad maxima de buzon de entrada:")) {
                    capacidadMaximaBuzonEntrada = Integer.parseInt(linea.split(":")[1].trim());
                } else if (linea.startsWith("Capacidad del buzon de entrega:")) {
                    capacidadBuzonEntrega = Integer.parseInt(linea.split(": ")[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        buzonEntrada = new ArrayList<>(capacidadMaximaBuzonEntrada);
        buzonEntrega = new ArrayList<>(capacidadBuzonEntrega);
        buzonCuarentena = new ArrayList<>();

        for (int i = 0; i < clientesEmisores; i++) {
            int numeroCorreos = 20 + (int)(Math.random() * 81);
            ClienteEmisor cliente = new ClienteEmisor(i + 1, numeroCorreos, capacidadMaximaBuzonEntrada);
        }
    
    }}
