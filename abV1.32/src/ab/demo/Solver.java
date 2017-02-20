package ab.demo;
import java.util.*;
import java.util.Random;
import ab.demo.Teoria;
import ab.demo.Estado;
import ab.demo.Chancho;
import ab.demo.JSON;

public class Solver {
	private List<Teoria> teorias;
    double factorDeAjuste = 5.0;
    
    private double propIgualdad = 0.01;
	private double propSimil = 0.10;
	
	public Solver() {
		this.teorias = JSON.read();
	}
	public void grabar() {
		JSON.write(this.teorias);
	}

	//Genera una teoria navie y para un determinado estado.
	public void generarTeoriaNaive(Estado estado_inicial) {
		Teoria teoria_inicial = new Teoria(estado_inicial);
		teoria_inicial.setCantidadFinal(estado_inicial.getCantidadInicial() - 1);
		teoria_inicial.setExitos(0);
		teoria_inicial.setUsos(1);
		//Setear maximo en lista
		int limite = 0;
		if ( estado_inicial.getCantidadInicial() > Estado.limite) {		
			limite = Estado.limite;
		} else {
			limite = estado_inicial.getCantidadInicial();
		}
		System.out.println("LIMITE :"+limite);
		boolean chancho_dummie = true;
		int random=0;
		List<Chancho> chanchosList = estado_inicial.getChanchos();
		while (chancho_dummie) {
			Random randomGenerator = new Random();
			random = randomGenerator.nextInt(limite);
			if (chanchosList.get(random).getValue() != Chancho.dummie) {
				chancho_dummie = false;
			}
		}
		teoria_inicial.setAccion(random);
		this.agregarTeoria(teoria_inicial);
	}
	//Aca hay q hacer la magia de combinar teorias
	public Teoria agregarTeoria(Teoria teoria_nueva) {
		Iterator<Teoria> iTeoria = this.teorias.iterator();
		Teoria teoria;
		while (iTeoria.hasNext()) {
			teoria = iTeoria.next();
			if (teoria.esIgual(teoria_nueva)) {
				System.out.println("Ya existia esa teoria");
				return teoria;
			}
		}
		this.teorias.add(teoria_nueva);
        return teoria_nueva;
	}
    
	public Teoria getTeoria(Estado estado) {
		//Generar teoria Naive para este estado
		System.out.println("----TEORIAS: "+ this.teorias.size());
		this.generarTeoriaNaive(estado);
		//Con este estado buscar la mejor teoria
		Iterator<Teoria> iTeoria = this.teorias.iterator();
		Teoria teoria;
		Teoria teoria_elegida = null;
		while (iTeoria.hasNext()) {
			teoria = iTeoria.next();
			if (teoria.getEstado().Igual(estado)) {
				if (teoria.mejorQue(teoria_elegida)) {
					teoria_elegida = teoria;
				}
			}
		}
		System.out.println("TEORIA :  ACCION:"+ teoria_elegida.getAccion() + " EXITOS:"+teoria_elegida.getExitos() +" USOS:"+teoria_elegida.getUsos());
		return teoria_elegida;
	}
    
    //Esta funcion devuelve la teoria a utilizar en el disparo
    //Para ello obtiene una teoria para cada estado de chancho y luego elije de manera estocástica la mejor.
    public Teoria getTeoriaParaAplicar(List<Estado> estados){
        //conseguir una teoria por cada estado
        System.out.println("Calculando teorias para aplicar");
        List <Teoria> posiblesTeorias = new ArrayList<Teoria>();
        Iterator<Estado> iEstado = estados.iterator();
		while (iEstado.hasNext()) {
			Estado estado = iEstado.next();
			posiblesTeorias.add(this.getTeoriaParaUnEstado(estado));
        }
        Teoria teoriaSeleccionada = seleccionarTeoria(posiblesTeorias);
        return this.agregarTeoria(teoriaSeleccionada);
                
    }
    
    //A partir de un estado elije una teoria
    public Teoria getTeoriaParaUnEstado(Estado estado){
        //Buscar teorias iguales y similares
        List <Teoria> teoriasIguales = new ArrayList<Teoria>();
        List <Teoria> teoriasSimilares = new ArrayList<Teoria>();
        
        Iterator<Teoria> iTeoria = this.teorias.iterator();
		while (iTeoria.hasNext()) {
			Teoria teoria = iTeoria.next();
            double grado = estado.comparar(teoria.getEstado());
            if (grado <= this.propIgualdad){
                teoriasIguales.add(teoria);
            }else{
                if (grado <= this.propSimil){
                    teoriasSimilares.add(teoria);
                }
            }
		}
        //generar teoria random
        //tel empanadas 
        Teoria teoriaRandom = new Teoria(estado);
        teoriaRandom.setAccion((int) (Math.random()+0.5));
        teoriaRandom.setUsos(0);
        teoriaRandom.setPuntaje(0);
        
        Teoria teoriaNueva = null;
        //Si existen teorias iguales las utilizo
        if(teoriasIguales.size() > 0){
            teoriasIguales.add(teoriaRandom);
            teoriaNueva = seleccionarTeoria(teoriasIguales);
        }else{
            //si existen teoria similares las utilizo
            if(teoriasSimilares.size() > 0){
                //Usar la accion de la teoria similar y generar una nueva teoria
                teoriasSimilares.add(teoriaRandom);
                Teoria teoriaSimilar = seleccionarTeoria(teoriasSimilares);
                teoriaNueva = new Teoria(estado);
                teoriaNueva.setAccion(teoriaSimilar.getAccion());
                                
            }else{
                //Si no existe teoria similar generar una nueva teoria con una accion random
                //no se encontraron teorias similares
                teoriaNueva = teoriaRandom;
            }
        }
        teoriaNueva.setPig(estado.getPig());
        return teoriaNueva;
    }
    
    //seleccionar teroria de manera estocastica
    public Teoria seleccionarTeoria(List<Teoria> posiblesTeorias){
        //chequeo
        if(posiblesTeorias.size()==0){return null;}
        //calcular valor de exito de cada teoria
        System.out.println("Calculando valor de exito para cada teoria");
        List <Double> valoresDeExito = new ArrayList<Double>();
        double sumatoria = 0.0;
        Iterator<Teoria> iTeoria = posiblesTeorias.iterator();
		while (iTeoria.hasNext()) {
			Teoria teoria = iTeoria.next();
            double valor = ( (double)teoria.getPuntaje() / (teoria.getUsos()+1) ) + this.factorDeAjuste ;
            System.out.println("Valor de teoria:" +valor);
            sumatoria += valor;
			valoresDeExito.add(valor);
        }
        
        //seleccionar una teoria de forma estocástica en base a su valor de exito
        System.out.println("Seleccionando teoria");
        double ran = Math.random() * sumatoria;
        System.out.println("Valor random: "+ran);
        double acumulado = 0.0;
        int indice = 0;
        Iterator<Double> iValoresDeExito = valoresDeExito.iterator();
        while(iValoresDeExito.hasNext()){
            double valor = iValoresDeExito.next();
            acumulado += valor;
            System.out.println("Valor acumulado: "+acumulado);
            if (ran <= acumulado){
                break;
                }
            indice += 1;
        }
        System.out.println("Seleccionando teoria con indice " + indice);
        Teoria teoriaElejida = posiblesTeorias.get(indice);
        return teoriaElejida;    
        
    }
    
}