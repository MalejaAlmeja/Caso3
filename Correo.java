public class Correo {
    private static int secuencial = 0;
    private int id;
    private boolean spam;
    
    public Correo(int idCliente, boolean spam) {
        int nuevoId = Integer.parseInt("" + idCliente + secuencial);
        this.id = nuevoId;
        this.spam = spam;
        secuencial++;
    }
    public int getId() {
        return id;
    }
    public boolean isSpam() {
        return spam;
    }


}