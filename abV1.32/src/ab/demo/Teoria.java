package ab.demo;
import ab.vision.ABType;
import ab.demo.Estado;

public class Teoria {
	private Estado estado;
	#private int cantidad_final;
	private float cantidadIzquierdaMadera;
	private float cantidadArribaMadera;
	private float cantidadIzquierdaHielo;
	private float cantidadArribaHielo;
	private float cantidadIzquierdaPiedra;
	private float cantidadArribaPiedra;
	
	private int tiroParabola;
	private int exitos;
	private int usos;

	public Teoria(Estado estado, int cantMaderaIzquierda, int cantMaderaArriba, int cantHieloIzquierda, int cantHieloArriba,
				int cantPiedraIzquierda, int cantPiedraArriba) {
		this.estado = estado;
		this.cantidadIzquierdaMadera=cantMaderaIzquierda;
		this.cantidadArribaMadera=cantMaderaArriba;
		this.cantidadIzquierdaHielo=cantHieloIzquierda;
		this.cantidadArribaHielo=cantHieloArriba;
		this.cantidadIzquierdaPiedra=cantPiedraIzquierda;
		this.cantidadArribaPiedra=cantPiedraArriba;
	}
	//Levantar desde archivo
	public Teoria() {
	}
	
	public Estado getEstado() {
		return this.estado;
	}

	//Retorna true si la teoria es mejor a la pasada
	// 
	public boolean mejorQue(Teoria teoria) {
		if (teoria == null) {
			return true;
		}
		return this.puntaje/this.usos > teoria.getPuntaje()/teoria.getUsos();
	}
	
	public boolean esIgual(Teoria teoria) {
		return (teoria.getEstado().Igual(this.getEstado()) )
	}
	
	public int getCantidadIzquierdaMadera(){
		return this.cantidadIzquierdaMadera;
	}
	public int getCantidadArribaMadera(){
		return this.cantidadArribaMadera;
	}
	public int getCantidadIzquierdaHielo(){
		return this.cantidadIzquierdaHielo;
	}
	public int getCantidadArribaHielo(){
		return this.cantidadArribaHielo;
	}
	public int getCantidadIzquierdaPiedra(){
		return this.cantidadIzquierdaPiedra;
	}
	public int getCantidadArribaPiedra(){
		return this.cantidadArribaPiedra;
	}
	public void setPuntaje(int newPuntaje)  {
		this.puntaje = newPuntaje;
	}

	public void setUsos(int newUsos) {
		this.usos = newUsos;
	}

	public void setTiroParabola(int newTiroParabola) {
		this.tiroParabola = newTiroParabola;
	}

	public int getTiroParabola() {
		return this.tiroParabola;
	}
	

	//public int getCantidadFinal() {
	//	return this.cantidad_final;
	//}

	//public int getCantidadInicial() {
	//	return this.estado_inicial.getCantidadInicial();
	//}
	
	public int getPuntaje()  {
		return this.puntaje;
	}

	public int getUsos() {
		return this.usos;
	}
	public ABType getBird() {
		return this.estado.getBird();
	}
}

