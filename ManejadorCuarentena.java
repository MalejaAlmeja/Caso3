package caso3;

import java.util.List;
import java.util.Random;

public class ManejadorCuarentena extends Thread{
	public Random random = new Random();
	private BuzonCuarentena buzonCuarentena;
	private BuzonEntrega buzonEntrega;
	
	private List<Correo> copiaCorreos;
	
	public ManejadorCuarentena( BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega) {
		this.buzonCuarentena = buzonCuarentena;
		this.buzonEntrega = buzonEntrega;
		this.copiaCorreos = buzonCuarentena.correos;
	}
	
	@Override
	public void run() {
		while(true) {
			for(Correo correosIterando: copiaCorreos) {
				synchronized(ManejadorCuarentena.class) {
					correosIterando.setTiempoEsperaEnSpam(correosIterando.tiempoSpam - 1);
				}
				
				
				if(correosIterando.tiempoSpam == 0){
					int numeroRechazo = random.nextInt(1,22);
					if(numeroRechazo % 7 == 0) {
						buzonCuarentena.eliminarMensaje(correosIterando);
						System.out.println("Se eliminó mensaje malicioso");
					}
					else {
						boolean checkSemiActiva = false;
				    	while(!checkSemiActiva) {
				    		synchronized (buzonEntrega) {
				    			checkSemiActiva = buzonEntrega.recibirMensaje(correosIterando);
				    		}
				    		if(checkSemiActiva == false) {
				    			FiltroSpam.yield();
				    		}
				    		
				    	}
				    	System.out.println("Se puso el mensaje de cuarentena a buzon de entrega");
					}
				}
			}
			try {
				ManejadorCuarentena.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}