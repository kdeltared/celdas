package ab.demo;
import ab.demo.Chancho;
import ab.demo.Sensor;
import ab.vision.ABType;
import java.util.*;


public class Estado {
	#private List<Chancho> chanchos;
	#private int cantidad_inicial;
	public float propIgualdad = 0.01;
	private int boundsCentrado = 10;
	private List<ABObject> maderas;
	private List<ABObject> hielos;
	private List<ABObject> piedras;
	private ABType birdType;

	public Estado(Sensor sensor) {
		#this.chanchos = sensor.getChanchos();
		this.maderas = sensor.getCantidadMadera();
		this.hielos = sensor.getCantidadHielo();
		this.piedras = sensor.getCantidadPiedra();
		this.birdType = sensor.getBird();
		
	}
	#public Estado(List<Chancho> chanchos, int cantidad_inicial, ABType birdType) {
	#	this.chanchos = chanchos;
	#	this.cantidad_inicial = cantidad_inicial;
	#	this.birdType = birdType;
	#}

	//Comparar si los estados iniciales son iguales
	public boolean Igual(Teoria teoria, Chancho chancho) {
		
		if (this.birdType !=. teoria.getEstado().getBird()) {
			return false;
		}
		propIzqMadera = this.getProporcionIzquierdaDeChancho(chancho, this.maderas);
		propArribaMadera = this.getProporcionArribaDeChancho(chancho, this.maderas);
		propIzqPiedra = this.getProporcionIzquierdaDeChancho(chancho, this.piedras);
		propArribaPiedra = this.getProporcionArribaDeChancho(chancho, this.piedras);
		propIzqHielo = this.getProporcionIzquierdaDeChancho(chancho, this.hielos);
		propArribaHielo = this.getProporcionArribaDeChancho(chancho, this.hielos);
		
		return (abs(propIzqMadera -teoria.getCantidadIzquierdaMadera()) <= this.propIgualdad && 
				abs(propArribaMadera -teoria.getCantidadArribaMadera()) <= this.propIgualdad &&
				abs(propIzqHielo -teoria.getCantidadIzquierdaHielo()) <= this.propIgualdad && 
				abs(propArribaHielo -teoria.getCantidadArribaHielo()) <= this.propIgualdad &&
				abs(propIzqPiedra -teoria.getCantidadIzquierdaPiedra()) <= this.propIgualdad && 
				abs(propArribaPiedra -teoria.getCantidadArribaPiedra()) <= this.propIgualdad)
	}
	
	private float getProporcionIzquierdaDeChancho(Chancho chancho, int obstaculos){
		//cuento la cantidad de obstaculos que estan efectivamente a la izquierda del chancho..
		int posX = chancho.getX(); //?? NOSE como guarda su lugar en la escena!
		int izquierda = 0;
		for obs in obstaculos:
			//comparo si los centros estan a la izquierda del chancho..
			if (obs.getCenterX() < posX){
				izquierda+=1;
			}
		return izquierda/this.getTotalObstaculos();
	}
	//FIX!! Posiciones de objetos
	private float getProporcionIzquierdaDeChancho(Chancho chancho, int obstaculos){
		//cuento la cantidad de obstaculos que estan efectivamente a la izquierda del chancho..
		int posY = chancho.getY(); //?? NOSE como guarda su lugar en la escena!
		int posX = chancho.getX(); //centrar el "arriba"
		int arriba = 0;
		for obs in obstaculos:
			//comparo si los centros estan a la izquierda del chancho..
			if (obs.getCenterY() > posY && obs.getCenterX() > posX-this.boundsCentrado && obs.getCenterX() < posX+this.boundsCentrado ){
				arriba+=1;
			}
		return arriba/this.getTotalObstaculos();
	}
		
	public getTotalObstaculos();
		return this.cantidadHielo+this.cantidadMadera+this.cantidadPiedra;
	}
	public ABType getBird() {
		return this.birdType;
	}

}
