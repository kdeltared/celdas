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
	//Comparar si los estados son similares, y devuelve la nueva teoria si es simil.
	public Teoria getTeoriaSimilar(Teoria teoria, Chancho chancho) {
		
		if (this.birdType !=. teoria.getEstado().getBird()) {
			return null;
		}
		float propIzqMadera = this.getProporcionIzquierdaDeChancho(chancho, this.maderas);
		float propArribaMadera = this.getProporcionArribaDeChancho(chancho, this.maderas);
		float propIzqPiedra = this.getProporcionIzquierdaDeChancho(chancho, this.piedras);
		float propArribaPiedra = this.getProporcionArribaDeChancho(chancho, this.piedras);
		float propIzqHielo = this.getProporcionIzquierdaDeChancho(chancho, this.hielos);
		float propArribaHielo = this.getProporcionArribaDeChancho(chancho, this.hielos);
		
		if (abs(propIzqMadera -teoria.getCantidadIzquierdaMadera()) <= this.propSimil && 
				abs(propArribaMadera -teoria.getCantidadArribaMadera()) <= this.propSimil &&
				abs(propIzqHielo -teoria.getCantidadIzquierdaHielo()) <= this.propSimil && 
				abs(propArribaHielo -teoria.getCantidadArribaHielo()) <= this.propSimil &&
				abs(propIzqPiedra -teoria.getCantidadIzquierdaPiedra()) <= this.propSimil && 
				abs(propArribaPiedra -teoria.getCantidadArribaPiedra()) <= this.propSimil){
			return new Teoria(this, propIzqMadera, propArribaMadera, propIzqPiedra, propArribaPiedra, propIzqHielo, propArribaHielo);
		}
		return null;
	}
	
	private int getIzquierda(int posChanchoX, List<ABObject> obs){
		int izquierda = 0;
		for (ABObject ob : obs){
			//comparo si los centros estan a la izquierda del chancho..
			if (ob.getCenterX() < posChanchoX){
				izquierda+=1;
			}
		}
		return izquierda;
	}
	private float getProporcionIzquierdaDeChancho(Chancho chancho, List<ABObject> obstaculos){
		//cuento la cantidad de obstaculos que estan efectivamente a la izquierda del chancho..
		int posX = chancho.getX(); //?? NOSE como guarda su lugar en la escena!
		int izquierda = getIzquierda(posX, obstaculos);
		int madera = getIzquierda(posX, this.maderas);
		int piedra = getIzquierda(posX, this.piedras);
		int hielo = getIzquierda(posX, this.hielos);
		
		return (float)izquierda/(madera+hielo+piedra);
	}
	//FIX!! Posiciones de objetos
	private int getArriba(int posChanchoX, int posChanchoY, List<ABObject> obs){
		int arriba = 0;
		for (ABObject ob : obs){
			//comparo si los centros estan a la izquierda del chancho..
			if (ob.getCenterY() > posChanchoY && ob.getCenterX() > posChanchoX-this.boundsCentrado 
				&& ob.getCenterX() < posChanchoX+this.boundsCentrado ){
				arriba+=1;
			}
		}
		return arriba;
	}
	private float getProporcionIzquierdaDeChancho(Chancho chancho, List<ABObject> obstaculos){
		//cuento la cantidad de obstaculos que estan efectivamente a la izquierda del chancho..
		int posY = chancho.getY(); //?? NOSE como guarda su lugar en la escena!
		int posX = chancho.getX(); //centrar el "arriba"
		int arriba = getArriba(posX, posY, obstaculos);
		int madera = getArriba(posX, posY, this.maderas);
		int hielo = getArriba(posX, posY, this.hielos);
		int piedra = getArriba(posX, posY, this.piedras);
		return (float)arriba/(madera+piedra+hielo);
	}
		
	//public getTotalObstaculos();
		//return this.cantidadHielo+this.cantidadMadera+this.cantidadPiedra;
	//}
	public ABType getBird() {
		return this.birdType;
	}

}
