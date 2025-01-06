
package gestorproyectos.modelo.pojo;

/**
 *
 * @author jonathan
 */
public class Documento {
    
    private int idDocumento;
    private String nombreDocumento;
    private byte[] documento;
    private String fechaEntrega;
    private String validado;
    private int idExpediente;

    public Documento() {
    }

    public Documento(int idDocumento,String nombreDocumento, byte[] documento, String fechaEntrega, String validado, int idExpediente) {
        this.idDocumento = idDocumento;
        this.nombreDocumento =  nombreDocumento;
        this.documento = documento;
        this.fechaEntrega = fechaEntrega;
        this.validado = validado;
        this.idExpediente = idExpediente;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getValidado() {
        return validado;
    }

    public void setValidado(String validado) {
        this.validado = validado;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }
    
    
}
