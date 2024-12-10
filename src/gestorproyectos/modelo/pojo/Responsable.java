package gestorproyectos.modelo.pojo;

public class Responsable {
    private int idResponsable;
    private String nombreResponsable;
    private String apellidoResponsable;
    private String correoResponsable;
    private String cargoResponsable;

    public Responsable(int idResponsable, String nombreResponsable, String apellidoResponsable, 
        String correoResponsable, String cargoResponsable) {
        this.idResponsable = idResponsable;
        this.nombreResponsable = nombreResponsable;
        this.apellidoResponsable = apellidoResponsable;
        this.correoResponsable = correoResponsable;
        this.cargoResponsable = cargoResponsable;
    }

    public Responsable() {
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getApellidoResponsable() {
        return apellidoResponsable;
    }

    public void setApellidoResponsable(String apellidoResponsable) {
        this.apellidoResponsable = apellidoResponsable;
    }

    public String getCorreoResponsable() {
        return correoResponsable;
    }

    public void setCorreoResponsable(String correoResponsable) {
        this.correoResponsable = correoResponsable;
    }

    public String getCargoResponsable() {
        return cargoResponsable;
    }

    public void setCargoResponsable(String cargoResponsable) {
        this.cargoResponsable = cargoResponsable;
    }
}
