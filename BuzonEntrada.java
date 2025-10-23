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
            public int compare(Correo c1, Correo c2) {
                int p1 = prioridad(c1);
                int p2 = prioridad(c2);
                if (p1 != p2) return Integer.compare(p1, p2);
                return Integer.compare(c1.getId(), c2.getId());
            }
            private int prioridad(Correo c) {
                if (c.esInicio()) return 1;
                if (c.esFin()) return 3;
                return 2;
            }
        });
    }

    // Bloqueante: clientes esperan si lleno
    public synchronized void recibirMensaje(ClienteEmisor cliente, Correo correo){
        while (ocupacion == capacidad) {
            try {
                System.out.println("El cliente " + cliente.idCliente + " ha dejado de enviar correos (se durmió) dado que el buzón de entrada está lleno.");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        correos.add(correo);
        ocupacion = correos.size();
        notifyAll();
    }

    // Bloqueante: filtros esperan si vacío
    public synchronized Correo sacarCorreo(Thread filtroSpam){
        while(ocupacion == 0){
            try {
                System.out.println("El buzón de entrada está vacío, por lo que el filtro " + filtroSpam.getName() + " se queda dormido.");
                wait();
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
