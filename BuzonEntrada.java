import java.util.Comparator;
import java.util.PriorityQueue;

public class BuzonEntrada {
    public int capacidad;
    public int ocupacion = 0;
    public PriorityQueue<Correo> correos;

    public BuzonEntrada(int capacidad) {
        this.capacidad = capacidad;
        this.correos = new PriorityQueue<>(new Comparator<Correo>() {
            @Override
            public int compare(Correo correo1, Correo correo2) {
                int p1 = prioridad(correo1);
                int p2 = prioridad(correo2);
                if (p1 != p2)
                {
                    return Integer.compare(p1, p2);
                } 
                else{
                    return Integer.compare(correo1.getId(), correo2.getId());
                }
            }

            private int prioridad(Correo correosPrioridad) {
                if (correosPrioridad.esInicio()) {
                    return 1;
                }
                else if(correosPrioridad.esFin()){
                    return 3;
                }
                else{
                    return 2;
                }
                
            }
        });
    }

    public synchronized void recibirMensaje(ClienteEmisor cliente, Correo correo){
        while (ocupacion == capacidad) {
            try {
                System.out.println("[Cliente " + cliente.idCliente + "]: Dejó de enviar correos (se durmió) porque el buzón de entrada está lleno. ");
                wait();
                System.out.println("[Cliente " + cliente.idCliente + "]: Despertó y vuelve a enviar correos porque el buzón de entrada tiene espacio. ");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        correos.add(correo);
        ocupacion = correos.size();
        notifyAll();
    }

    public synchronized Correo sacarCorreo(Thread filtroSpam){
        while(ocupacion == 0){
            try {
                System.out.println("[" + filtroSpam.getName() + "]: Se duerme porque el buzón de entrada está vacío. ");
                wait();
                System.out.println("[" + filtroSpam.getName() + "]: Se despierta porque hay correos en el buzón de entrada. ");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        

        Correo correoRetirar = this.correos.poll();
        ocupacion = correos.size();
        notifyAll();
        return correoRetirar;
    }
}
