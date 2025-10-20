package caso3;

import java.util.Random;

public class ServidorEntrega extends Thread{
    public BuzonEntrega buzonEntrega;
    public Random random = new Random();
    
    public ServidorEntrega(BuzonEntrega bzEntrega) {
    	this.buzonEntrega = bzEntrega;
    }
    
    @Override
    public void run() {
    	while(true) {
    		//se me queda en activa esperando hasta que pueda
    		Correo correoPorFinalizar = buzonEntrega.eliminarMensaje();
    		
    		if(correoPorFinalizar == null) {
    			ServidorEntrega.yield();
    		}
    		
    		if(correoPorFinalizar.esFin()) {
    			break;
    		}else {
    			int tiempoLecturaFicticia = random.nextInt(1, 1001);
    			try {
					ServidorEntrega.sleep(tiempoLecturaFicticia);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    		
    		
    	}
    }
}
