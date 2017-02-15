package ab.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ab.vision.ABType;
import ab.demo.Teoria;
/import ab.demo.Chancho;
import ab.demo.Estado;
import java.util.*;



public class JSON {
	private static final String FILE_NAME = "teorias.json";
	//	RedBird(4), 
	//	YellowBird(5), 
	//	BlueBird(6), 
	//	BlackBird(7), 
	//	WhiteBird(8)
	public static int getID(ABType bird) {
		int retorno=0;
		switch (bird) {
			case RedBird: 
				retorno= 4;break;
			case YellowBird:
				retorno= 5;break;
			case BlackBird:
				retorno= 7;break;
			case BlueBird:
				retorno= 6;break;
			case WhiteBird:
				retorno= 8;break;
			default:
				retorno =  0;
		}
		return retorno;
	}
	public static ABType getABType(int id) {
		ABType retorno=null;
		switch (id) {
			case 4: 
				retorno= ABType.RedBird;break;
			case 5:
				retorno= ABType.YellowBird;break;
			case 7:
				retorno= ABType.BlackBird;break;
			case 6:
				retorno= ABType.BlueBird;break;
			case 8:
				retorno= ABType.WhiteBird;break;
			default:
				retorno = ABType.Unknown;
		}
		return retorno;
	}
	public static void write(List<Teoria> teorias){
		try {

			JSONArray jsonArrayTeorias = new JSONArray();

			for (Teoria unaTeoria : teorias) {
			

				JSONObject unJSONObj = new JSONObject();

				int i =0;
				//for (Chancho unChancho : unaTeoria.getEstado().getChanchos()){
				//	unJSONObj.put("chancho "+i,unChancho.getValue());
				//	i++;
				//}
				unJSONObj.put("tipoPajaro", JSON.getID(unaTeoria.getBird()));
				unJSONObj.put("cantMaderaIzquierda", unaTeoria.getCantidadIzquierdaMadera());
				unJSONObj.put("cantMaderaArriba", unaTeoria.getCantidadArribaMadera());
				unJSONObj.put("cantHieloIzquierda", unaTeoria.getCantidadIzquierdaHielo());
				unJSONObj.put("cantHieloArriba", unaTeoria.getCantidadArribaHielo());
				unJSONObj.put("cantPiedraIzquierda", unaTeoria.getCantidadIzquierdaPiedra());
				unJSONObj.put("cantPiedraArriba", unaTeoria.getCantidadArribaPiedra());
				unJSONObj.put("tiroParabola", unaTeoria.getTiroParabola());
				unJSONObj.put("puntaje", unaTeoria.getPuntaje());
				unJSONObj.put("usos", unaTeoria.getUsos());

				//agregar jsonobject al jsonarray
				jsonArrayTeorias.add(unJSONObj);
			
			}

			JSONObject mainJSONObj = new JSONObject();
			mainJSONObj.put("teorias",jsonArrayTeorias);
			//escribir en el archivo el json array
			FileWriter file = new FileWriter(FILE_NAME);
			file.write(mainJSONObj.toJSONString());
			file.flush();
			file.close();
			} catch (IOException e) {
				e.printStackTrace();
				
			}
	}

	public static List<Teoria> read(){
		JSONParser parser = new JSONParser();
		List<Teoria> teorias = new ArrayList<Teoria>();
		try {
			File teo = new File(FILE_NAME);
			if (teo.exists()) System.out.println("===> Se reconoce el archivo de teorias <" + teo.getAbsolutePath() + ">");
			Object unObjeto = parser.parse(new FileReader(FILE_NAME));
			JSONObject mainJSONObj = (JSONObject) unObjeto;
			JSONArray teoriasJSON = (JSONArray) mainJSONObj.get("teorias");
			for (int i = 0, size = teoriasJSON.size(); i < size; i++){
				JSONObject unaTeoria = (JSONObject) teoriasJSON.get(i); //No estoy seguro de esta linea. O es .get(i) o .getJSONObject(i)
				
				//List<Chancho> chanchos = new ArrayList<Chancho>();
							
				//Crear el birdtype
				long birdType = (Long) unaTeoria.get("tipoPajaro");
				long cantPiedraIzquierda = (Long) unaTeoria.get("cantPiedraIzquierda");	
				long cantPiedraArriba = (Long) unaTeoria.get("cantPiedraArriba");
				long cantHieloIzquierda = (Long) unaTeoria.get("cantHieloIzquierda");
				long cantHieloArriba = (Long) unaTeoria.get("cantHieloArriba");
				long cantMaderaIzquierda = (Long) unaTeoria.get("cantMaderaIzquierda");
				long cantMaderaArriba = (Long) unaTeoria.get("cantMaderaArriba");
				long tiroParabola = (Long) unaTeoria.get("tiroParabola");
				long puntaje = (Long) unaTeoria.get("puntaje");	
				long usos = (Long) unaTeoria.get("usos");
				//Long value;		
				//for (int j=0; j < Estado.limite; j++){
				//	value = (Long) (unaTeoria.get("chancho "+ j));					
				//	chanchos.add(new Chancho(0,(int) (long) value));
				//}
				//Collections.sort(chanchos);
				
				Estado estado = new Estado(JSON.getABType((int)birdType));
				Teoria teoria = new Teoria(estado, cantMaderaIzquierda, cantMaderaArriba, cantHieloIzquierda, 
							cantHieloArriba, cantPiedraIzquierda, cantPiedraArriba);
				
				//teoria.setCantidadMaderaIzq((int)cantMaderaIzquierda);
				//teoria.setCantidadMaderaArriba((int)cantMaderaArriba);
				//teoria.setCantidadHieloIzq((int)cantHieloIzquierda);
				//teoria.setCantidadHieloArriba((int)cantHieloArriba);
				//teoria.setCantidadPiedraIzq((int)cantPiedraIzquierda);
				//teoria.setCantidadPiedraArriba((int)cantPiedraArriba);
				teoria.setTiroParabola((int)tiroParabola);
				teoria.setUsos((int)usos);
				teoria.setPuntaje((int)puntaje);

				teorias.add(teoria);
			}	

	
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			} catch (IOException e3) {
				e3.printStackTrace();
			} catch (ParseException e4) {
				e4.printStackTrace();
			}
		
		return teorias;
	} 
}
