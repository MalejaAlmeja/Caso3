import java.util.concurrent.atomic.AtomicInteger;

public class Correo {
    //se le pregunto al profe sobre el uso del secuencial con AtomicInteger
    //y nos menciono que el enunciado indicaba de poner un secuencial
    
    private static final AtomicInteger secuencial = new AtomicInteger(0);
    private int id;
    private boolean spam;
    private boolean esInicio;
    private boolean esFin;
    public int idCliente;
    public int tiempoSpam = -1;
    public boolean finDefinitivo = false;

    public Correo(int idCliente, boolean spam, boolean esInicio, boolean esFin) {
        this.idCliente = idCliente;
        this.spam = spam;
        this.esInicio = esInicio;
        this.esFin = esFin;
        this.id = idCliente * 100000 + secuencial.getAndIncrement();
    }
    public int getId() { 
        return id; 
    }

    public boolean esSpam() { 
        return spam; 
    }

    public boolean esInicio() { 
        return esInicio; 
    }

    public boolean esFin() { 
        return esFin; 
    }

    public void setTiempoEsperaEnSpam(int tiempo) { 
        this.tiempoSpam = tiempo; 
    }


    public void setFinDefinitivo() { 
        this.finDefinitivo = true; 
    }

    public boolean esFinDefinitivo(){
        return this.finDefinitivo; 
    }

}
