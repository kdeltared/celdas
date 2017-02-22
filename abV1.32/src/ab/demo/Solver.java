package ab.demo;
import java.util.*;
import java.util.Random;
import ab.demo.Teoria;
import ab.demo.Estado;
import ab.demo.JSON;

public class Solver {
	private List<Teoria> teorias;
    double factorDeAjuste = 0.25;
    
    private double propIgualdad = 0.05;
	private double propSimil = 0.10;
	
	public Solver() {
		this.teorias = JSON.read();
	}
	public void grabar() {
		JSON.write(this.teorias);
	}

    //Aca hay q hacer la magia de combinar teorias
	public Teoria agregarTeoria(Teoria teoria_nueva) {
		Iterator<Teoria> iTeoria = this.teorias.iterator();
		Teoria teoria;
		while (iTeoria.hasNext()) {
			teoria = iTeoria.next();
			if (teoria.esIgual(teoria_nueva)) {
				System.out.println("Ya existia esa teoria");
				teoria.setPig(teoria_nueva.getPig());
                return teoria;
			}
		}
		this.teorias.add(teoria_nueva);
        return teoria_nueva;
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
            System.out.println("Teoria Igual seleccionada: "+teoriaNueva);
        }else{
            //si existen teoria similares las utilizo
            if(teoriasSimilares.size() > 0){
                //Usar la accion de la teoria similar y generar una nueva teoria
                teoriasSimilares.add(teoriaRandom);
                Teoria teoriaSimilar = seleccionarTeoria(teoriasSimilares);
                teoriaNueva = new Teoria(estado);
                teoriaNueva.setAccion(teoriaSimilar.getAccion());
                System.out.println("Teoria Similar seleccionada");
                                
            }else{
                //Si no existe teoria similar generar una nueva teoria con una accion random
                //no se encontraron teorias similares
                teoriaNueva = teoriaRandom;
            }
        }
        System.out.println("Seteando chancho a la teoria: " +  estado.getPig());
        teoriaNueva.setPig(estado.getPig());
        return teoriaNueva;
    }
    
    //seleccionar teroria de manera estocastica
    public Teoria seleccionarTeoria(List<Teoria> posiblesTeorias){
        //chequeo
        if(posiblesTeorias.size()==0){return null;}
        //calcular valor de exito de cada teoria
        //System.out.println("Calculando valor de exito para cada teoria");
        List <Double> valoresDeExito = new ArrayList<Double>();
        double sumatoria = 0.0;
        Iterator<Teoria> iTeoria = posiblesTeorias.iterator();
		while (iTeoria.hasNext()) {
			Teoria teoria = iTeoria.next();
            double valor = ( (double)teoria.getPuntaje() / (teoria.getUsos()+1) ) + this.factorDeAjuste ;
            //System.out.println("Valor de teoria:" +valor);
            sumatoria += valor;
			valoresDeExito.add(valor);
        }
        
        //seleccionar una teoria de forma estocástica en base a su valor de exito
        //System.out.println("Seleccionando teoria");
        double ran = Math.random() * sumatoria;
        //System.out.println("Valor random: "+ran);
        double acumulado = 0.0;
        int indice = 0;
        Iterator<Double> iValoresDeExito = valoresDeExito.iterator();
        while(iValoresDeExito.hasNext()){
            double valor = iValoresDeExito.next();
            acumulado += valor;
            //System.out.println("Valor acumulado: "+acumulado);
            if (ran <= acumulado){
                break;
                }
            indice += 1;
        }
        //System.out.println("Seleccionando teoria con indice " + indice);
        if(indice >= posiblesTeorias.size()){
            indice = posiblesTeorias.size()-1;
        }
        Teoria teoriaElejida = posiblesTeorias.get(indice);
        return teoriaElejida;    
        
    }
    
}