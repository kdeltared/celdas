package ab.demo;
import ab.vision.ABType;
import ab.vision.ABObject;
import ab.demo.Estado;

public class Teoria {
	private Estado estado_inicial;
	private int accion;
    private int puntaje;
    private int usos;
    
	public Teoria(Estado estado) {
		this.estado_inicial = estado;
	}
    
	//Levantar desde archivo
	public Teoria() {
	}

    public void setPuntaje(int puntaje){
        this.puntaje = puntaje;
    }
    
    public int getPuntaje(){
        return this.puntaje;
    }

	public void setUsos(int newUsos) {
		this.usos = newUsos;
	}

	public void setAccion(int newAction) {
		this.accion = newAction;
	}
    
   	public int getAccion() {
		return this.accion;
	}

	public Estado getEstado() {
		return this.estado_inicial;
	}

		
	public boolean esIgual(Teoria teoria) {
		if (teoria.getEstado().Igual(this.getEstado()) && teoria.getAccion() == this.getAccion()) {
			return true;
		} else {
			return false;
		}
	}

	public int getUsos() {
		return this.usos;
	}
	public ABType getBird() {
		return this.estado_inicial.getBird();
	}
    
    public void setPig(ABObject pig){
        this.estado_inicial.setPig(pig);
    }
    
    public ABObject getPig(){
        return this.estado_inicial.getPig();
    }
      
}

