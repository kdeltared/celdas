package ab.demo;
import ab.demo.Sensor;
import ab.vision.ABType;
import java.util.*;
import ab.vision.ABObject;


public class Estado {
	
    private double propIgualdad = 0.01;
	private int boundsCentrado = 20;
    private double propSimil = 0.10;
    
    public ABType birdType;
	public double maderaIzqNormalizada;
    public double maderaArribaNormalizada;
	public double hieloIzqNormalizada;
    public double hieloArribaNormalizada;
	public double piedraIzqNormalizada;
    public double piedraArribaNormalizada;
	    
    private ABObject pig;
    
    public Estado(Sensor sensor,ABObject pig) {
		
        this.pig = pig;
		this.birdType = sensor.getBird();
        
        int cantTotal = sensor.getCantidadTotalDeBloques();
        
        //System.out.println("Calculando madera");
        this.maderaIzqNormalizada = this.normalizar(this.getCantidadIzquierda(pig,sensor.getMadera()),cantTotal);
        this.maderaArribaNormalizada = this.normalizar(this.getCantidadArriba(pig,sensor.getMadera()),cantTotal);
        //System.out.println("Calculando hielo");
        this.hieloIzqNormalizada = this.normalizar(this.getCantidadIzquierda(pig,sensor.getHielo()),cantTotal);
        this.hieloArribaNormalizada = this.normalizar(this.getCantidadArriba(pig,sensor.getHielo()),cantTotal);
        //System.out.println("Calculando piedra");
        this.piedraIzqNormalizada = this.normalizar(this.getCantidadIzquierda(pig,sensor.getPiedra()),cantTotal);
        this.piedraArribaNormalizada = this.normalizar(this.getCantidadArriba(pig,sensor.getPiedra()),cantTotal);
        
        //System.out.println("maderaIzqNormalizada :"+ this.maderaIzqNormalizada);
        //System.out.println("maderaArribaNormalizada :"+ this.maderaArribaNormalizada);
        //System.out.println("hieloIzqNormalizada :"+ this.hieloIzqNormalizada);
        //System.out.println("hieloArribaNormalizada :"+ this.hieloArribaNormalizada);
        //System.out.println("piedraIzqNormalizada :"+ this.piedraIzqNormalizada);
        //System.out.println("piedraArribaNormalizada :"+ this.piedraArribaNormalizada);
        
    }
    public Estado(ABType birdType,double maderaIzqNormalizada,double maderaArribaNormalizada,
                double hieloIzqNormalizada, double hieloArribaNormalizada, double piedraIzqNormalizada,
                double piedraArribaNormalizada ){
        this.birdType=birdType;
        this.maderaIzqNormalizada=maderaIzqNormalizada;
        this.maderaArribaNormalizada=maderaArribaNormalizada;
        this.hieloIzqNormalizada=hieloIzqNormalizada;
        this.hieloArribaNormalizada=hieloArribaNormalizada;
        this.piedraIzqNormalizada=piedraIzqNormalizada;
        this.piedraArribaNormalizada=piedraArribaNormalizada;
    }
    
   
	//Comparar si los estados iniciales son iguales
	public boolean Igual(Estado estado) {
	    if (this.birdType != estado.getBird()) {return false;}
		if (this.maderaIzqNormalizada != estado.maderaIzqNormalizada){return false;}
        if (this.maderaArribaNormalizada != estado.maderaArribaNormalizada){return false;}
        if (this.hieloIzqNormalizada != estado.hieloIzqNormalizada){return false;}
        if (this.hieloArribaNormalizada != estado.hieloArribaNormalizada){return false;}
        if (this.piedraIzqNormalizada != estado.piedraIzqNormalizada){return false;}
        if (this.piedraArribaNormalizada != estado.piedraArribaNormalizada){return false;}
        return true;
	}
    
    public double comparar(Estado estado){
        if (this.birdType != estado.birdType){ 
            return 999.9;
        }
        double sum =  Math.sqrt(Math.pow((this.maderaIzqNormalizada - estado.maderaIzqNormalizada),2) +
                      Math.pow((this.maderaArribaNormalizada - estado.maderaArribaNormalizada),2) +
                      Math.pow((this.hieloIzqNormalizada - estado.hieloIzqNormalizada),2) +
                      Math.pow((this.hieloArribaNormalizada - estado.hieloArribaNormalizada),2) +
                      Math.pow((this.piedraIzqNormalizada - estado.piedraIzqNormalizada),2) +
                      Math.pow((this.piedraArribaNormalizada - estado.piedraArribaNormalizada),2));
        return sum;
	}
   
	public ABType getBird() {
		return this.birdType;
	}
    
    private int getCantidadIzquierda(ABObject pig, List<ABObject> bloques){
        double pigX = pig.getCenter().getX();
        double pigY = pig.getCenter().getY();
        //System.out.println("getCantidadIzquierda de un cerdo");
        //System.out.println("pigX :"+ pigX);
        //System.out.println("pigY :"+ pigY);
		
        int cantDeBloques = 0;
		for (ABObject bloque : bloques){
			//comparo si los centros estan a la izquierda del chancho..
            //System.out.println("Bloque X :"+ bloque.getCenter().getX());
            //System.out.println("Bloque Y :"+ bloque.getCenter().getY());
			if (bloque.getCenter().getX() < pigX){
				if (bloque.getCenter().getY() < (pigY + this.boundsCentrado) &&
                    bloque.getCenter().getY() > (pigY - this.boundsCentrado)){
                        cantDeBloques += 1;
                        //System.out.println("Bloque encontrado");
                }
			}
		}
		return cantDeBloques;
    }
    
    private int getCantidadArriba(ABObject pig, List<ABObject> bloques){
        double pigX = pig.getCenter().getX();
        double pigY = pig.getCenter().getY();
        //System.out.println("getCantidadArriba de un cerdo");
        //System.out.println("pigX :"+ pigX);
        //System.out.println("pigY :"+ pigY);
		
        int cantDeBloques = 0;
		for (ABObject bloque : bloques){
			//comparo si los centros estan a la izquierda del chancho..
            //System.out.println("Bloque X :"+ bloque.getCenter().getX());
            //System.out.println("Bloque Y :"+ bloque.getCenter().getY());
			if (bloque.getCenter().getY() < pigY){
				if (bloque.getCenter().getX() < (pigX + this.boundsCentrado) &&
                    bloque.getCenter().getX() > (pigX - this.boundsCentrado)){
                        cantDeBloques += 1;
                        //System.out.println("Bloque encontrado");
                }
			}
		}
		return cantDeBloques;
    }
    
    private double normalizar(int valor,int total){
        double normalizado = (double)valor / total;
        return normalizado;
    }
    
    public void setPig(ABObject pig){
        this.pig = pig;
    }
    
    public ABObject getPig(){
        return this.pig;
    }
    
}
