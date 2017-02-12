package ab.demo;
import ab.demo.Chancho;
import ab.demo.Sensor;
import ab.vision.ABType;
import java.util.*;


public class Estado {
	private List<Chancho> chanchos;
	private int cantidad_inicial;
	public static int limite = 3;
	private ABType birdType;

	public Estado(Sensor sensor) {
		this.chanchos = sensor.getChanchos();
		this.maderas = sensor.getCantidadMadera();
		this.hielos = sensor.getCantidadHielo();
		this.piedras = sensor.getCantidadPiedra();
		this.birdType = sensor.getBird();
	}
	public Estado(List<Chancho> chanchos, int cantidad_inicial, ABType birdType) {
		this.chanchos = chanchos;
		this.cantidad_inicial = cantidad_inicial;
		this.birdType = birdType;
	}

	//Comparar si los estados iniciales son iguales
	public boolean Igual(Estado estadoTeoria, Chancho chancho) {
		
		if (this.birdType != estadoToria.getBird()) {
			return false;
		}
		propIzq = this.getProporcionIzquierdaChancho(chancho);
		propIzqTeoria = estado.getProporcionIzquierdaChancho(chancho);
		propArriba = this.getProporcionIzquierdaChancho(chancho);
		propArribaTeoria = estado.getProporcionArribaChancho(chancho);

		if (estado.getCantidadHielo()/ estadoTeoria.getTotalObstaculos() == this.cantidadHielo/this.getTotalObstaculos() &&
			estado.getCantidadMadera()/ estadoTeoria.getTotalObstaculos() == this.cantidadMadera/ this.getTotalObstaculos() &&
			estado.getCantidadPiedra()/ estadoTeoria.getTotalObstaculos() == this.cantidadPiedra/ this.getTotalObstaculos()){
			return false;
		}
		return true;
	}
	
	public getTotalObstaculos();
		return this.cantidadHielo+this.cantidadMadera+this.cantidadPiedra;
	}
	public ABType getBird() {
		return this.birdType;
	}

}
