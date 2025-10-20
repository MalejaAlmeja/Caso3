package caso3;
import java.util.Random;
public class FiltroSpam extends Thread {
	
	public Random random = new Random();
	
    BuzonEntrada buzonEntrada;
    BuzonCuarentena buzonCuarentena;
    BuzonEntrega buzonEntrega;
    
    int numeroFiltros;
    int numeroClientes = 0;
    int numeroClientesProcesados = 0;
    
    public FiltroSpam(int numeroFiltros, BuzonEntrada buzonEntrada, BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega) {
        this.numeroFiltros = numeroFiltros;
        this.buzonEntrada = buzonEntrada;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
    }

    @Override
    public void run() { 
        System.out.println("Filtro de Spam despertado.");
        while (true) {
            if (buzonEntrada.ocupacion > 0) {
                Correo correo = buzonEntrada.correos.get(0);
                buzonEntrada.eliminarMensaje(correo);
                if (correo.esInicio()) {
                    numeroClientes++;
                    System.out.println("Filtro de Spam ha recibido el correo de inicio del Cliente Emisor " + correo.idCliente + ".");
                } else if (correo.esFin()) {
                    numeroClientesProcesados++;
                    
                    System.out.println("Filtro de Spam ha recibido el correo de fin del Cliente Emisor " + correo.idCliente + ".");
                    break;
                    
                } else if (correo.esSpam()) {
                	int tiempoEsperaSpam = random.nextInt(10000, 20001);
                	correo.setTiempoEsperaEnSpam(tiempoEsperaSpam);
                    buzonCuarentena.recibirMensaje(correo);
                    System.out.println("Filtro de Spam ha detectado un correo spam del Cliente Emisor " + correo.idCliente + " y lo ha enviado a cuarentena.");
                } else {
                	
                	boolean checkSemiActiva = false;
                	while(!checkSemiActiva) {
                		synchronized (buzonEntrega) {
                			checkSemiActiva = buzonEntrega.recibirMensaje(correo);
                		}
                		if(checkSemiActiva == false) {
                			FiltroSpam.yield();
                		}
                		
                	}
                	
                    //buzonEntrega.recibirMensaje(correo);
                    System.out.println("Filtro de Spam ha verificado un correo no spam del Cliente Emisor " + correo.idCliente + " y lo ha enviado al buzón de entrega.");
                }
            }
            
            if(numeroClientesProcesados == numeroClientes) {
            	enviarMensajeFinABuzonEntrega();
            }
            
        }
    }
    
    public void enviarMensajeFinABuzonEntrega() {
    	while(buzonEntrada.ocupacion != 0 && buzonCuarentena.ocupacion != 0) {
    		//el enunciado no indica el tipo de espera conveniente, chat aconseja meterle un sleep de 200ms
    		try {
				FiltroSpam.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	//nuevamente, para mandarle el correo a cuarentena y a entrega, toca que sea semiactiva, osea hay que usar ese yield:
    	Correo correoFinParaAmbos = new Correo(-1, false, false, false);
    	correoFinParaAmbos.setFinDefinitivo();
    	
    	boolean checkSemiActiva = false;
    	while(!checkSemiActiva) {
    		synchronized (buzonEntrega) {
    			checkSemiActiva = buzonEntrega.recibirMensaje(correoFinParaAmbos);
    		}
    		if(checkSemiActiva == false) {
    			FiltroSpam.yield();
    		}
    		
    	}
    	
    	boolean checkSemiActiva2 = false;
    	while(!checkSemiActiva) {
    		synchronized (buzonCuarentena) {
    			checkSemiActiva2 = buzonCuarentena.recibirMensaje(correoFinParaAmbos);
    		}
    		if(checkSemiActiva2 == false) {
    			FiltroSpam.yield();
    		}
    		
    	}
    	

    	
    	
    	
    }

}
