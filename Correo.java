import java.util.concurrent.atomic.AtomicInteger;

public class Correo {
    private static final AtomicInteger SECUENCIAL = new AtomicInteger(0);
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
        this.id = idCliente * 100000 + SECUENCIAL.getAndIncrement();
    }
    public int getId() { return id; }
    public boolean esSpam() { return spam; }
    public boolean esInicio() { return esInicio; }
    public boolean esFin() { return esFin; }
    public void setTiempoEsperaEnSpam(int tiempo) { this.tiempoSpam = tiempo; }
    public void setFinDefinitivo() { this.finDefinitivo = true; }
}
