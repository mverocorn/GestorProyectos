
package gestorproyectos.modelo.pojo;

/**
 *
 * @author jonathan
 */
public class Reporte {
 
    private int idReporte;
    private String nombreReporte;
    private byte[] reporte;
    private int horasReportadas;
    private String tipoReporte;
    private String validado;
    private int noReporte;
    private int idExpediente;

    public Reporte() {
    }

    public Reporte(int idReporte, String nombreReporte, byte[] reporte, int horasReportadas, String tipoReporte, String validado, int noReporte, int idExpediente) {
        this.idReporte = idReporte;
        this.nombreReporte = nombreReporte;
        this.reporte = reporte;
        this.horasReportadas = horasReportadas;
        this.tipoReporte = tipoReporte;
        this.validado = validado;
        this.noReporte = noReporte;
        this.idExpediente = idExpediente;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public byte[] getReporte() {
        return reporte;
    }

    public void setReporte(byte[] reporte) {
        this.reporte = reporte;
    }

    public int getHorasReportadas() {
        return horasReportadas;
    }

    public void setHorasReportadas(int horasReportadas) {
        this.horasReportadas = horasReportadas;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getValidado() {
        return validado;
    }

    public void setValidado(String validado) {
        this.validado = validado;
    }

    public int getNoReporte() {
        return noReporte;
    }

    public void setNoReporte(int noReporte) {
        this.noReporte = noReporte;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }
    
        
}
