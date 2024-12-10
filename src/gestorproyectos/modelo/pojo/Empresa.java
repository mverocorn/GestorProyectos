package gestorproyectos.modelo.pojo;

public class Empresa {
    private int idEmpresa;
    private String nombreEmpresa;
    private String correoEmpresa;
    private String telefonoEmpresa;
    private String calleEmpresa;
    private String colonia;
    private String numExt;
    private String codigoPostal;

    public Empresa(int idEmpresa, String nombreEmpresa, String correoEmpresa, 
        String telefonoEmpresa, String calleEmpresa, String colonia, String numExt, String codigoPostal) {
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.correoEmpresa = correoEmpresa;
        this.telefonoEmpresa = telefonoEmpresa;
        this.calleEmpresa = calleEmpresa;
        this.colonia = colonia;
        this.numExt = numExt;
        this.codigoPostal = codigoPostal;
    }

    public Empresa() {
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getCorreoEmpresa() {
        return correoEmpresa;
    }

    public void setCorreoEmpresa(String correoEmpresa) {
        this.correoEmpresa = correoEmpresa;
    }

    public String getTelefonoEmpresa() {
        return telefonoEmpresa;
    }

    public void setTelefonoEmpresa(String telefonoEmpresa) {
        this.telefonoEmpresa = telefonoEmpresa;
    }

    public String getCalleEmpresa() {
        return calleEmpresa;
    }

    public void setCalleEmpresa(String calleEmpresa) {
        this.calleEmpresa = calleEmpresa;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}

