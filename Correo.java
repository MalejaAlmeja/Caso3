public class Correo {
    private static int secuencial = 0;
    private int id;
    private boolean spam;
    private boolean esInicio;
    private boolean esFin;
    public int idCliente;
    
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


}