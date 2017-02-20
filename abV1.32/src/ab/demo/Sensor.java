package ab.demo;
import java.util.*;
import ab.vision.ABObject;
import ab.vision.ABType;
import ab.vision.Vision;
import ab.demo.Chancho;
import java.awt.Point;

public class Sensor{
	private int cantidad_inicial; //legacy
	
    private ABType birdType;	
	private List<ABObject> pigs;
	private List<ABObject> madera = new ArrayList<ABObject>(); 
	private List<ABObject> hielo = new ArrayList<ABObject>();
	private List<ABObject> piedra = new ArrayList<ABObject>();
    
    private int cantidadTotalDeBloques = 0;
    
    //legacy
	public Sensor(List<ABObject> pigs, ABType birdType) {
		this.cantidad_inicial = pigs.size();
		this.birdType = birdType;
		this.pigs = pigs;
	}
    
    public Sensor(List<ABObject> pigs, ABType birdType, List<ABObject> blocks) {
        this.cantidad_inicial = pigs.size();//legacy
		this.birdType = birdType;
		this.pigs = pigs;
		
		Iterator<ABObject> iBlocks = blocks.iterator();
		while (iBlocks.hasNext()) {
			ABObject block = iBlocks.next();
			switch(block.getType()){
				case Ice:
					hielo.add(block);
					break;
				case Wood:
					madera.add(block);
					break;
				case Stone:
					piedra.add(block);
					break;
				}
			}
        this.cantidadTotalDeBloques = this.hielo.size() + this.madera.size() + this.piedra.size();
            
		System.out.println("pigs :"+ this.pigs.size());
		System.out.println("bird :"+ this.birdType);
		System.out.println("hielo :"+ this.hielo.size());
		System.out.println("madera :"+ this.madera.size());
		System.out.println("piedra :"+ this.piedra.size());
		}
    
    public void setPigs(List<ABObject> pigs) {
		this.pigs = pigs;
	}
    
    public List<ABObject> getMadera(){
		return this.madera;
		}
		
	public List<ABObject> getHielo(){
		return this.hielo;
		}
		
	public List<ABObject> getPiedra(){
		return this.piedra;
		}
	
    public List<ABObject> getPigs() {
		return this.pigs;
		}
    
    //legacy
	//Ordenar lista de chanchos
	public List<Chancho> getChanchos() {
		List<Chancho> chanchos = new ArrayList<Chancho>();
		Iterator<ABObject> iPigs = this.pigs.iterator();
		ABObject chancho=null;
		int suma= 0;
		int iterator=0;
		Point _tpt;
		while (iPigs.hasNext()) {
			iterator++;
			chancho = iPigs.next();
			_tpt = chancho.getCenter();
			suma = suma + _tpt.x;
		}
		int centro_x = suma/iterator;
		iPigs = this.pigs.iterator();
		iterator = 0;
		while (iPigs.hasNext()) {
			chancho = iPigs.next();
			_tpt = chancho.getCenter();
			chanchos.add(new Chancho(iterator,(this.abs(_tpt.x - centro_x))/5));
			iterator++;
		}
		//Agrego chanchos falsos para llegar a la cantidad limite
		while(chanchos.size() < Estado.limite) {
			chanchos.add(new Chancho(0,Chancho.dummie));
		}
		//Ordenar por value
		Collections.sort(chanchos);
		//Sacar Chanchos que sobran
		while(chanchos.size() > Estado.limite) {
			chanchos.remove(chanchos.size()-1);
		}
		return chanchos;
	}

	public int getCantidad() {
		return this.pigs.size();
	}
	public ABType getBird() {
		return this.birdType;
	}
	
    public int getCantidadTotalDeBloques(){
        return this.cantidadTotalDeBloques;
    }
    
	private int abs (int numero) {
      return numero > 0 ? numero : -numero;
	}
    
    
    //devuelve la lista con los estados de cada chancho
    public List<Estado> getEstados(){
        List<Estado> estados = new ArrayList<Estado>();
        
        Iterator<ABObject> iPig = this.pigs.iterator();
		while (iPig.hasNext()) {
			ABObject pig = iPig.next();
			estados.add(this.calcularEstado(pig));
			}
            
        return estados;
        }
        
    private Estado calcularEstado(ABObject pig){
        Estado estado = new Estado(this,pig);
        return estado;
        }
}
