package caso3;
public class Correo {
    private static int secuencial = 0;
    private int id;
    private boolean spam;
    private boolean esInicio;
    private boolean esFin;
    public int idCliente;
    public int tiempoSpam = -1;
    
    public boolean finDefinitivo;
    
    public Correo(int idCliente, boolean spam, boolean esInicio, boolean esFin) {
        int nuevoId = Integer.parseInt("" + idCliente + secuencial);
        this.id = nuevoId;
        this.spam = spam;
        this.esInicio = esInicio;
        this.esFin = esFin;
        secuencial++;
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


}