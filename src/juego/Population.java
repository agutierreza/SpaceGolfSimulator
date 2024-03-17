package juego;

import general.Simulacion;
import general.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBException;


public class Population{
	
	final static int POP_SIZE = 100;
	final static int MAX_ITER = 2000;
	final static double MUTATION_RATE = 0.7;
	final static double MUTATION_PARTICLES_RATE = 0.2;
	final static double CROSSOVER_RATE = 0.9;
	final static int N_PLANETAS = 6;
	private static boolean crossOverFail=false;
	private static Random rand = new Random();  // random-number generator
    //private static Nivel[] population;
    private double totalFitness;
    private static  ArrayList<Nivel> populationList;
    private static int contador=0;
    private static int crucesMalos=0;
	public static void main(String [] args) throws JAXBException, IOException{
		
		
		///*
		//Juego juego = new Juego(900,600,NivelAleatorio(N_PLANETAS));
		Nivel niveldesdefichero = new Nivel("nivel66.xml");
		niveldesdefichero.evaluate();
		
		Nivel nivel1 = new Nivel("nivel75.xml");
		Nivel nivel2 = new Nivel("nivel76.xml");
		//System.out.println("es igual " + nivel1.getPlaneta()[0].esIgual(nivel2.getPlaneta()));

		
		System.out.println("POP Distncia particula-objetivo: "+ Util.distancia(niveldesdefichero.getParticle(), niveldesdefichero.getTarget()));
		System.out.println("POP Distancia intersecciones part-obj: "+niveldesdefichero.intersecciones());
	
		System.out.println("POP accTPlanet: " + niveldesdefichero.accTPlanet());
		System.out.println("POP Aceleraci�n en superficie: "+niveldesdefichero.accAroundTargetPlanet(0,50));
		
		
		Simulacion simula = new Simulacion(niveldesdefichero);
		Juego juego = new Juego(900,600,niveldesdefichero);
		//Juego juego = new Juego(900,600,NivelAleatorio(1));
		
		//*/
		
		
		//Para jugar un nivel aleatorio
		Population pop = new Population();
		Collections.sort(populationList);


		for (int i=0;i<POP_SIZE;i++){
			System.out.println("Nivel "+i+" fitness "+populationList.get(i).getFitnessValue());
		}
		//Nivel[] newPop = new Nivel[POP_SIZE];
		Nivel[] nivelSel = new Nivel[2];
		
		contador = 0;

		for (int iter = 0; iter < MAX_ITER; iter++) {
			//contador = 0;
			//while (contador < POP_SIZE) {
				///System.out.println("Iteracion "+iter+". Total nuevos individuos: "+contador);
				nivelSel[0]=new Nivel(pop.binaryTournament());
				//nivelSel[0]=new Nivel(pop.binaryTournamentMinim());
				do{
					nivelSel[1]=new Nivel(pop.binaryTournament());
					//nivelSel[1]=new Nivel(pop.binaryTournamentMinim());
				}while(nivelSel[1].esIgual(nivelSel[0]));
				
				//gili, haciendo eso est�s apuntando a un objeto de la poblacion y modificandolo en la mutacion!
				//nivelSel[0]=pop.binaryTournament();
				//nivelSel[1]=pop.binaryTournament();
				//Cruzo
				int intentos = 0;
                if ( rand.nextDouble() < CROSSOVER_RATE ) {
                	
                	do{
                		nivelSel=crossOver(nivelSel[0],nivelSel[1]);
                		intentos++;
                	}while(crossOverFail && (intentos < 100));
                	if (intentos==100){
                		crucesMalos++;
                	}
                	//}while(intentos < 100);
                	///System.out.println("Cruzando individuos. Intentos hasta cruce correcto"+intentos);
                }
                
                //Muto
                mutar(nivelSel[0]);
                mutar(nivelSel[1]);
                //pop.evaluate();
                //a�ado a la nueva poblacion
                if(nivelSel[0].inNiveles(populationList)==false){
                	insertarIndividuoEstructurada(nivelSel[0]);
                	//insertarIndividuoEstructuradaMinim(nivelSel[0]);
                	/*
                	for(int j=0;j<N_PLANETAS;j++){
                		System.out.print("Nivel sel "+" Planeta "+j+" pos: "+nivelSel[0].getPlaneta()[j].getPos()[0]+", "+nivelSel[0].getPlaneta()[j].getPos()[0]
                		                   +" masa "+nivelSel[0].getPlaneta()[j].getMasa());
                	}
                	*/
                }
                if(nivelSel[1].inNiveles(populationList)==false){
                	insertarIndividuoEstructurada(nivelSel[1]);
                	//insertarIndividuoEstructuradaMinim(nivelSel[1]);
                }

            pop.evaluate();
            ///System.out.println("Total Fitness iteracion "+iter+"= " + pop.totalFitness);
            System.out.println("" + pop.totalFitness);
            //System.out.println(populationList.get(0).getFitnessValue());
		}
		
		
    	for (int i = 0; i < POP_SIZE; i++) {
    		//population[i].save("nivel"+i+".xml");
    		populationList.get(i).save("nivel"+i+".xml");
    		//System.out.println("nivel"+i+".xml fitness: "+population[i].getFitnessValue());
    		System.out.println("nivel"+i+".xml fitness: "+populationList.get(i).getFitnessValue());    		
    	}
		
    	System.out.println("Cruces malos "+crucesMalos);
    	System.out.println("Total nuevos individuos: "+contador);
		
		
		//de aqu� para abajo son pruebas
		
		
		//Juego juego = new Juego(900,600,pop.NivelAleatorio(7));
		
		//Para jugar desde un fichero
		//Nivel niveldesdefichero = new Nivel("src/logs/nivel.xml");
		//Nivel niveldesdefichero = new Nivel("nivel0.xml");
		//Nivel niveldesdefichero = new Nivel("nivel1.xml");
		//Nivel niveldesdefichero = new Nivel("hijo1.xml");
		//Nivel niveldesdefichero = new Nivel("hijo2.xml");
		//Juego juego = new Juego(900,600,niveldesdefichero);

		//Para jugar antiguamente hacia
		//CreaNivelAleatorio unNivel = new CreaNivelAleatorio(900,600,5);
		//CreaNivelAleatorio unNivel = new CreaNivelAleatorio(7);
    	
    	/////////////////////////////////////////////////////////////
    	/*
for (int iter = 0; iter < MAX_ITER; iter++) {

		nivelSel[0]=new Nivel(pop.binaryTournament());
		do{
			nivelSel[1]=new Nivel(pop.binaryTournament());
		}while(nivelSel[1].esIgual(nivelSel[0]));
	
		int intentos = 0;
	    if ( rand.nextDouble() < CROSSOVER_RATE ) {
	    	
	    	do{
	    		nivelSel=crossOver(nivelSel[0],nivelSel[1]);
	    		intentos++;
	    	}while(crossOverFail && (intentos < 100));
	    	if (intentos==100){
	    		crucesMalos++;
	    	}
	    }
	    mutar(nivelSel[0]);
	    mutar(nivelSel[1]);
	    pop.evaluate();
	
	    if(nivelSel[0].inNiveles(populationList)==false){
	    	insertarIndividuoEstructurada(nivelSel[0]);
	    }
	    if(nivelSel[1].inNiveles(populationList)==false){
	    	insertarIndividuoEstructurada(nivelSel[1]);
	    }
	
	pop.evaluate();
	System.out.println("" + pop.totalFitness);
}

for (int i = 0; i < POP_SIZE; i++) {
	populationList.get(i).save("nivel"+i+".xml");
	System.out.println("nivel"+i+".xml fitness: "+populationList.get(i).getFitnessValue());    		
}
    	
		*/
		//////////////////////

    	
    	
	}


	public Population() throws JAXBException, IOException {
    	//population= new Nivel[POP_SIZE];
    	populationList = new ArrayList<Nivel>();
    	for (int i = 0; i < POP_SIZE; i++) {
    		//population[i]= NivelAleatorio(N_PLANETAS);
    		//population[i].save("nivel"+i+".xml");
    		populationList.add(NivelAleatorio(N_PLANETAS));
    		//populationList.add(new Nivel("nivel"+i+".xml"));
    		//populationList.set(i,NivelAleatorio(N_PLANETAS)) ;
    		populationList.get(i).evaluate();
    		populationList.get(i).save("nivel"+i+".xml");
    		
    	}
    	
    	this.evaluate();
    	
    }
    


    public double evaluate() {
        this.totalFitness = 0.0;
        //System.out.println("evaluando");
        for (int i = 0; i < POP_SIZE; i++) {
        	//System.out.println("Evaluando individuo "+i);
        	
        	for(int j=0;j<N_PLANETAS;j++){
        		//System.out.print("Nivel"+i+" Planeta "+j+" pos: "+populationList.get(i).getPlaneta()[j].getPos()[0]+", "+populationList.get(i).getPlaneta()[j].getPos()[0]
        		//                   +" masa "+populationList.get(i).getPlaneta()[j].getMasa());
        	}
        	
        	//this.totalFitness+=populationList.get(i).evaluate();
        	this.totalFitness+=populationList.get(i).getFitnessValue();
        }

        return this.totalFitness;
    }
    
    public Nivel binaryTournament(){
    	int i = rand.nextInt(POP_SIZE);
    	int j = rand.nextInt(POP_SIZE);
    	while (i==j){
    		j = rand.nextInt(POP_SIZE);
    	}
    	//if (population[i].getFitnessValue()<population[j].getFitnessValue()){
    	if (populationList.get(i).getFitnessValue()<populationList.get(j).getFitnessValue()){
    		return populationList.get(j);
    	}else{
    		return populationList.get(i);
    	}
    }
    public Nivel binaryTournamentMinim(){
    	int i = rand.nextInt(POP_SIZE);
    	int j = rand.nextInt(POP_SIZE);
    	while (i==j){
    		j = rand.nextInt(POP_SIZE);
    	}
    	//if (population[i].getFitnessValue()<population[j].getFitnessValue()){
    	if (populationList.get(i).getFitnessValue()>populationList.get(j).getFitnessValue()){
    		return populationList.get(j);
    	}else{
    		return populationList.get(i);
    	}
    }
    public Nivel rouletteSelection(){
    	double randNum = rand.nextDouble() * this.totalFitness;
    	
    	double acum=0;
    	int ind=0;
    	while (acum<randNum){
    		//acum+=population[ind].getFitnessValue();
    		acum+=populationList.get(ind).getFitnessValue();
    		ind++;
    	}
    	//return population[ind-1];
    	return populationList.get(ind-1);
    }
    

    
	public void setPopulation(Nivel[] newPop) {
		//this.population = newPop;
		//System.arraycopy(newPop, 0, this.population, 0, POP_SIZE);
		for(int i=0;i<POP_SIZE;i++){
			//population[i] = new Nivel(newPop[i]);
			populationList.add(new Nivel(newPop[i]));
    		//System.out.println("Population "+i+" fitness: "+population[i].getFitnessValue());

		}
	}
	//public Nivel[] getPopulation() {
	public ArrayList<Nivel> getPopulation() {
		//return population;
		return populationList;
	}
	
	
	public static void insertarIndividuoEstructurada(Nivel nivel){
        boolean insertado = false;
        if (nivel.evaluate() > populationList.get(32).getFitnessValue()){
        	//borrar el 32
        	///System.out.println("Nivel insertado en grupo 1 Diferencia: "+ (nivel.evaluate()-populationList.get(32).getFitnessValue()));
			populationList.remove(32);
        	populationList.add(new Nivel(nivel));
        	Collections.sort(populationList);
        	insertado = true;
        	contador++;
        }
    	if ((insertado==false)&&nivel.evaluate() > populationList.get(65).getFitnessValue()){
    		//borrar el 65
    		///System.out.println("Nivel insertado en grupo 2 Diferencia: "+ (nivel.evaluate()-populationList.get(65).getFitnessValue()));
			populationList.remove(65);
        	populationList.add(new Nivel(nivel));
        	Collections.sort(populationList);
    		insertado=true;
    		contador++;
    	}
        
		if ((insertado==false)&&nivel.evaluate() > populationList.get(POP_SIZE-1).getFitnessValue()){
	    	//borrar el ultimo
			///System.out.println("Nivel insertado en grupo 3 Diferencia: "+ (nivel.evaluate()-populationList.get(POP_SIZE-1).getFitnessValue()));
			populationList.remove(POP_SIZE-1);
	    	populationList.add(new Nivel(nivel));
	    	Collections.sort(populationList);
	    	contador++;
		}

        
	}

	public static void insertarIndividuoEstructuradaMinim(Nivel nivel){
        boolean insertado = false;
        if (nivel.evaluate() < populationList.get(32).getFitnessValue()){
        	//borrar el 32
        	///System.out.println("Nivel insertado en grupo 1 Diferencia: "+ (nivel.evaluate()-populationList.get(32).getFitnessValue()));
			populationList.remove(32);
        	populationList.add(new Nivel(nivel));
        	Collections.sort(populationList);
        	insertado = true;
        	contador++;
        }
    	if ((insertado==false)&&nivel.evaluate() < populationList.get(65).getFitnessValue()){
    		//borrar el 65
    		///System.out.println("Nivel insertado en grupo 2 Diferencia: "+ (nivel.evaluate()-populationList.get(65).getFitnessValue()));
			populationList.remove(65);
        	populationList.add(new Nivel(nivel));
        	Collections.sort(populationList);
    		insertado=true;
    		contador++;
    	}
        
		if ((insertado==false)&&nivel.evaluate() < populationList.get(POP_SIZE-1).getFitnessValue()){
	    	//borrar el ultimo
			///System.out.println("Nivel insertado en grupo 3 Diferencia: "+ (nivel.evaluate()-populationList.get(POP_SIZE-1).getFitnessValue()));
			populationList.remove(POP_SIZE-1);
	    	populationList.add(new Nivel(nivel));
	    	Collections.sort(populationList);
	    	contador++;
		}

        
	}
    
    
    
	public static void mutar(Nivel nivel){
		int indx,gen;
		indx = rand.nextInt(nivel.getPlaneta().length);
		
		int valor;

		double valX, valY;
		int nGenesMutadosPlaneta=0;
		int nGenesMutados=0;
		for (indx=0;indx<N_PLANETAS;indx++){
			
			if(nivel.planetaBienDefinido(indx)==false){
				System.out.println("PLANETA NO BIEN DEFINIDO");
			}
			
			double[] posOrig = nivel.getPlaneta()[indx].getPos();
			int  masaOrig = nivel.getPlaneta()[indx].getMasa();
			
			do{
				nivel.getPlaneta()[indx].setPos(posOrig);
				nivel.getPlaneta()[indx].setMasa(masaOrig);
				nGenesMutadosPlaneta = 0;
				for (gen=1;gen<4;gen++){
					if (gen == 1){
						if(rand.nextDouble()<(1/(N_PLANETAS*3))){
							valor = (int) (rand.nextGaussian()*50);
							valX = nivel.getPlaneta()[indx].getPos()[0]+valor;
							valY = nivel.getPlaneta()[indx].getPos()[1];
							double[] newPos = {valX,valY};
							nivel.getPlaneta()[indx].setPos(newPos);
							nGenesMutadosPlaneta++;
						}

					}
					if (gen == 2){
						if(rand.nextDouble()<(1/(N_PLANETAS*3))){
							valor = (int) (rand.nextGaussian()*50);
							valY = nivel.getPlaneta()[indx].getPos()[1]+valor;
							valX = nivel.getPlaneta()[indx].getPos()[0];
							double[] newPos = {valX,valY};
							nivel.getPlaneta()[indx].setPos(newPos);
							nGenesMutadosPlaneta++;
						}
					}
					if (gen == 3){
						if(rand.nextDouble()<(1/(N_PLANETAS*3))){
							do{
								valor = (int) (rand.nextGaussian()*50);
								valor += nivel.getPlaneta()[indx].getMasa();
								nivel.getPlaneta()[indx].setMasa((int) valor);
							}while(nivel.getPlaneta()[indx].getMasa()<100);
							nGenesMutadosPlaneta++;
						}
					}
				}
			}while(nivel.planetaBienDefinido(indx)==false);
			nGenesMutados+=nGenesMutadosPlaneta;
		}
		
		if(rand.nextDouble()<MUTATION_PARTICLES_RATE){
			nivel.setiPlanetParticle(rand.nextInt(nivel.getPlaneta().length));
			//nivel.setArgPlanetParticle(2*Math.PI*rand.nextDouble()*nivel.getArgPlanetParticle());
			
			nGenesMutados++;
		}
		if(rand.nextDouble()<MUTATION_PARTICLES_RATE){
			nivel.setArgPlanetParticle(2*Math.PI*rand.nextDouble());
		}
		if(rand.nextDouble()<MUTATION_PARTICLES_RATE){
			do{
				nivel.setiPlanetTarget(rand.nextInt(nivel.getPlaneta().length));
				//nivel.setArgPlanetTarget(2*Math.PI*rand.nextDouble()*nivel.getArgPlanetTarget());
				
			}while(nivel.getiPlanetParticle()==nivel.getiPlanetTarget());

			nGenesMutados++;
		}
		if(rand.nextDouble()<MUTATION_PARTICLES_RATE){
			nivel.setArgPlanetTarget(2*Math.PI*rand.nextDouble());
		}
		nivel.getParticle().setOnTop(nivel.getPlaneta()[nivel.getiPlanetParticle()], nivel.getArgPlanetParticle());
		nivel.getTarget().setOnTop(nivel.getPlaneta()[nivel.getiPlanetTarget()], nivel.getArgPlanetTarget());
		///System.out.println("Genes mutados: "+nGenesMutados);
	}
public static  Nivel[] crossOver2(Nivel parent1, Nivel parent2) throws JAXBException, IOException{
	boolean error = false;
	int [][] divParent1;
	int [][] divParent2;
	Nivel offSpring1 = NivelAleatorio(N_PLANETAS);
	Nivel offSpring2 = NivelAleatorio(N_PLANETAS);
	Nivel [] offSpring = new Nivel[2];
	double m,n;
	
	do{
		int x1,y1,x2,y2;
    	x1 = rand.nextInt(900);
    	y1 = rand.nextInt(600);
    	do{//evitemos divisiones por cero
    		x2=rand.nextInt(900);
    	}while (x1==x2);
    	y2 = rand.nextInt(600);
		m = (float)(y2-y1)/(float)(x2-x1);
    	n = -m*x1+y1;
    	divParent1 = division(m,n, parent1);
    	divParent2 = division(m,n, parent2);
	}while(divParent1[0].length!=divParent2[0].length || divParent1[0].length==0 || divParent1[0].length==N_PLANETAS);
	

	for (int i=0;i<divParent1[0].length;i++){         //divParent1[0]:array de indices de padre1,parte sup
		offSpring1.getPlaneta()[i]=new Planet(parent1.getPlaneta()[divParent1[0][i]]);
	}
	for (int i=divParent1[0].length;i<N_PLANETAS;i++){//divParent2[1]:array de indices de padre2,parte inf
		offSpring1.getPlaneta()[i]=new Planet(parent2.getPlaneta()[divParent2[1][i-divParent1[0].length]]);
	}
	for (int i=0;i<divParent2[0].length;i++){         //divParent2[0]:array de indices de padre2,parte sup
		offSpring2.getPlaneta()[i]= new Planet(parent2.getPlaneta()[divParent2[0][i]]);
	}
	for (int i=divParent2[0].length;i<N_PLANETAS;i++){//divParent1[1]:array de indices de padre1,parte inf
		offSpring2.getPlaneta()[i]=new Planet(parent1.getPlaneta()[divParent1[1][i-divParent2[0].length]]);
	}

	if ((offSpring1.nivelBienDefinido() == false)|| (offSpring2.nivelBienDefinido() == false)){
		error = true;
	}
	
	if(error==false){
		if(rand.nextBoolean()){
			rePosParticleTarget(parent1,offSpring1);
		}else{
			rePosParticleTarget(parent2,offSpring1);
		}
		
		if(rand.nextBoolean()){
			rePosParticleTarget(parent1,offSpring2);
		}else{
			rePosParticleTarget(parent2,offSpring2);
		}
		
		if(offSpring1.getiPlanetParticle()==offSpring1.getiPlanetTarget()||
				offSpring2.getiPlanetParticle()==offSpring2.getiPlanetTarget())
		{
			error = true;
		}
	}
	if (error){
		crossOverFail = true;
		Nivel[] fail = {parent1,parent2};
		return fail;
	}else{
		crossOverFail = false;
		offSpring1.save("hijo1.xml");
		offSpring2.save("hijo2.xml");
		offSpring[0]=offSpring1;
		offSpring[1]=offSpring2;
		return offSpring;
	}
}
	public static  Nivel[] crossOver(Nivel parent1, Nivel parent2) throws JAXBException, IOException{
		boolean error = false;
		int [][] divParent1;
    	int [][] divParent2;
    	Nivel offSpring1 = NivelAleatorio(N_PLANETAS);
    	Nivel offSpring2 = NivelAleatorio(N_PLANETAS);
    	Nivel [] offSpring = new Nivel[2];
		double m,n;
    	
		//Corto la pantalla por una recta, haciendo que haya el mismo n�mero de planetas en cada parte
		do{
    		int x1,y1,x2,y2;
        	x1 = rand.nextInt(900);
        	y1 = rand.nextInt(600);
        	do{//evitemos divisiones por cero
        		x2=rand.nextInt(900);
        	}while (x1==x2);
        	y2 = rand.nextInt(600);
    		
    		m = (float)(y2-y1)/(float)(x2-x1);
        	n = -m*x1+y1;
        	//m=0.8123980164527893;
        	//n=-189.1598624587059;
        	//System.out.println("m:"+m);
        	//System.out.println("n:"+n);
        	divParent1 = division(m,n, parent1);
        	divParent2 = division(m,n, parent2);
        //Mientras no haya el mismo n� de planetas en cada cara de los dos padres, o queden todos en una sola, se repite el corte
    	}while(divParent1[0].length!=divParent2[0].length || divParent1[0].length==0 || divParent1[0].length==N_PLANETAS);
		
		
		
		//Creamos los hijos
		
		//OFFSPRING1
		//partesup
		for (int i=0;i<divParent1[0].length;i++){         //divParent1[0]:array de indices de padre1,parte sup
			//offSpring1.getPlaneta()[i]=parent1.getPlaneta()[divParent1[0][i]].clone();
			offSpring1.getPlaneta()[i]=new Planet(parent1.getPlaneta()[divParent1[0][i]]);
			//System.out.println("hijo 1, planeta "+i+ " copiado de padre 1, planeta "+divParent1[0][i]);
		}
		//parte inf: lo que quede por cubrir(grande o chico)
		for (int i=divParent1[0].length;i<N_PLANETAS;i++){//divParent2[1]:array de indices de padre2,parte inf
			//offSpring1.getPlaneta()[i]=parent2.getPlaneta()[divParent2[1][i-divParent1[0].length]].clone();
			offSpring1.getPlaneta()[i]=new Planet(parent2.getPlaneta()[divParent2[1][i-divParent1[0].length]]);
			//System.out.println("hijo 1, planeta "+i+ " copiado de padre 2, planeta "+divParent2[1][i-divParent1[0].length]);
		}
		
		//OFFSPRING2
		//partesup
		for (int i=0;i<divParent2[0].length;i++){         //divParent2[0]:array de indices de padre2,parte sup
			//offSpring2.getPlaneta()[i]=parent2.getPlaneta()[divParent2[0][i]].clone();
			offSpring2.getPlaneta()[i]= new Planet(parent2.getPlaneta()[divParent2[0][i]]);
			//System.out.println("hijo 2, planeta "+i+ " copiado de padre 2, planeta "+divParent2[0][i]);
		}
		//parte inf: lo que quede por cubrir(grande o chico)
		for (int i=divParent2[0].length;i<N_PLANETAS;i++){//divParent1[1]:array de indices de padre1,parte inf
			//offSpring2.getPlaneta()[i]=parent1.getPlaneta()[divParent1[1][i-divParent2[0].length]].clone();
			offSpring2.getPlaneta()[i]=new Planet(parent1.getPlaneta()[divParent1[1][i-divParent2[0].length]]);
			//System.out.println("hijo 2, planeta "+i+ " copiado de padre 1, planeta "+divParent1[1][i-divParent1[0].length]);
		}
		
		
		//check malformaciones (planetas intersecados)
		/*
		for(int i=0;i<N_PLANETAS;i++){
			for(int j=i+1;j<N_PLANETAS;j++){
				//System.out.println("Compruebo el "+i+" con el "+j);
				//if (Util.distancia(offSpring1.getPlaneta()[i], offSpring1.getPlaneta()[j])<2*offSpring1.getParticle().getRadio()){
				if (Util.distancia(offSpring1.getPlaneta()[i], offSpring1.getPlaneta()[j])<Math.min(offSpring1.getPlaneta()[i].getRadio(), offSpring1.getPlaneta()[j].getRadio())){
					//System.out.println("Planetas "+i+" y "+j+" en hijo 1 demasiado cerca!");
					error=true;
					break;
				}
			}
		}
		
		for(int i=0;i<N_PLANETAS;i++){
			for(int j=i+1;j<N_PLANETAS;j++){
				//if (Util.distancia(offSpring2.getPlaneta()[i], offSpring2.getPlaneta()[j])<2*offSpring2.getParticle().getRadio()){
				if (Util.distancia(offSpring2.getPlaneta()[i], offSpring2.getPlaneta()[j])<Math.min(offSpring2.getPlaneta()[i].getRadio(), offSpring2.getPlaneta()[j].getRadio())){
						//System.out.println("Planetas "+i+" y "+j+" en hijo 2 demasiado cerca!");
						error=true;
						break;
				}
			}
		}
		*/
		if ((offSpring1.nivelBienDefinido() == false)|| (offSpring2.nivelBienDefinido() == false)){
			error = true;
		}
		
		//Posiciono particle y target en la descendencia, a partir de los padres.
		if(error==false){
			
			if(rand.nextBoolean()){
				rePosParticleTarget(parent1,offSpring1);
			}else{
				rePosParticleTarget(parent2,offSpring1);
			}
			
			if(rand.nextBoolean()){
				rePosParticleTarget(parent1,offSpring2);
			}else{
				rePosParticleTarget(parent2,offSpring2);
			}
			
			if(offSpring1.getiPlanetParticle()==offSpring1.getiPlanetTarget()||
					offSpring2.getiPlanetParticle()==offSpring2.getiPlanetTarget())
			{
				error = true;
			}
			

		}

		//Damos un veredicto
		if (error){
			crossOverFail = true;
			Nivel[] fail = {parent1,parent2};
			return fail;
		}else{
			crossOverFail = false;
			offSpring1.save("hijo1.xml");
			offSpring2.save("hijo2.xml");
			offSpring[0]=offSpring1;
			offSpring[1]=offSpring2;
			return offSpring;
		}
	}
	
	
	public void bestPosParticleTarget(Nivel padre1, Nivel padre2, Nivel hijo){
		double argParticle1,argTarget1,argParticle2,argTarget2;
		argParticle1 = padre1.getArgPlanetParticle();
		argTarget1 = padre1.getArgPlanetTarget();
		argParticle2 = padre2.getArgPlanetParticle();
		argTarget2 = padre2.getArgPlanetTarget();
		for (int i=0;i<Population.N_PLANETAS;i++){
			for(int j=0;i<Population.N_PLANETAS;j++){
				hijo.getParticle().setOnTop(hijo.getPlaneta()[i], argParticle1);
				hijo.getTarget().setOnTop(hijo.getPlaneta()[j], argTarget1);
			}
		}
	}
	/**
	 * Reposiciona particle y target en el nivel hijo en los planetas m�s cercanos del hijo a la posici�n que ocupaban en el nivel padre 
	 * @param padre
	 * @param hijo
	 */
	public static void rePosParticleTarget(Nivel padre, Nivel hijo){
		double[] distancesParticle = new double[N_PLANETAS];
		double[] distancesTarget = new double[N_PLANETAS];
		double argParticle,argTarget;
		argParticle = padre.getArgPlanetParticle();
		argTarget = padre.getArgPlanetTarget();
		for (int i=0;i<N_PLANETAS;i++){
			distancesParticle[i]=Util.distancia(padre.getParticle(), hijo.getPlaneta()[i]);
			distancesTarget[i]=Util.distancia(padre.getTarget(), hijo.getPlaneta()[i]);
		}
		hijo.setiPlanetParticle(Util.getIndexMin(distancesParticle));
		hijo.setiPlanetTarget(Util.getIndexMin(distancesTarget));
		hijo.setArgPlanetParticle(argParticle);
		hijo.setArgPlanetTarget(argTarget);
		hijo.getParticle().setOnTop(hijo.getPlaneta()[hijo.getiPlanetParticle()], hijo.getiPlanetParticle());
		//como siempre, muy importante al crear un nivel:
		hijo.getParticle().setCollidedPlanet(hijo.getPlaneta()[hijo.getiPlanetParticle()]);
		hijo.getTarget().setOnTop(hijo.getPlaneta()[hijo.getiPlanetTarget()], hijo.getiPlanetTarget());
	}
	
    public static int[][] division(double m, double n, Nivel nivel){

    	//Compruebo donde cae cada planeta
    	int [][] separa = new int[2][];
    	ArrayList<Integer> sup= new ArrayList<Integer>();
    	ArrayList<Integer> inf= new ArrayList<Integer>();
    	
    	for (int i=0;i<nivel.getPlaneta().length;i++){
    		if (nivel.getPlaneta()[i].getPos()[1] <= (m*nivel.getPlaneta()[i].getPos()[0]+n)){
    			sup.add(i);
    			//System.out.println("Planeta "+i+" en caso A");
    		}else{
    			inf.add(i);
    			//System.out.println("Planeta "+i+" en caso B");
    		}	
    	}
    	//me guardo los valores en un array bidimensional, 
    	separa[0]=new int[sup.size()];
    	separa[1]=new int[inf.size()];
    	for (int i=0;i<sup.size();i++){
    		separa[0][i]=sup.get(i);
    	}
    	for (int i=0;i<inf.size();i++){
    		separa[1][i]=inf.get(i);
    	}
    	//System.out.println("N caso A coinciden: "+ separa[0].length);
    	//este mismo bucle se repite con el otro nivel padre. Si los arrays miden lo mismo se acepta, en caso contrario se repite todo esto hasta que por narices ocurra (puedes hacer tb una expecci�n, y si en un n� determinado de veces no se consige al carajo y los hijos son clones de los padres)
    	return separa;
    }
	
    public static Nivel NivelAleatorio(int nPlanetas) throws JAXBException, IOException{
    	Planet particula ,objetivo ;
    	Planet[] planeta= new Planet[nPlanetas];
    	
		double [] posParticula = {0,0};
		double [] velParticula = {0,0};
		double [] accParticula = {0,0};
		double [] posObjetivo = {0,0};
		double [] velObjetivo = {0,0};
		double [] accObjetivo = {0,0};


    	int [] masa = new int[nPlanetas];
    	
    	//creamos y posicionamos los planetas
		for (int i=0;i<nPlanetas;i++){
			planeta[i]= new Planet(masa[i]);
			planeta[i].setSprite("imagenes/planeta3.png");
			//planeta[i].setSprite("imagenes/bolaroja2.gif");
			do{
	    		randomMass(planeta[i],(int)(100),(int)(900),500,200);
	    		planeta[i].setRadio();
	    		//this.randomPos(planeta[i], gWidth, gHeight);
	    		randomPos(planeta[i], 900, 600);
			}while(bienDef(planeta[i],i,planeta) == false);
			//}while(bienDefinido(i) == false);
		}
    	particula = new Planet(4,posParticula,velParticula,accParticula);
    	int indexPlanet;
    	indexPlanet = rand.nextInt(nPlanetas);
    	Planet.setiPlanetParticle(indexPlanet);
    	double arg = (double) (rand.nextDouble()*2*Math.PI);
    	Planet.setArgPlanetParticle(arg);
    	particula.setOnTop(planeta[indexPlanet], arg);
    	particula.setCollidedPlanet(planeta[indexPlanet]);
        //particula.setSprite("imagenes/planeta2.png");
        particula.setSprite("imagenes/bolaroja2.gif");
		
		
        //y ahora lo(s) objetivo(s) / obstaculo(s)       
        objetivo = new Planet(5,posObjetivo,velObjetivo,accObjetivo);
        int indexPlanet2;
    	do{
    		//Si solo hay un planeta dejo q el objetivo est� donde la part�cula, en caso contrario hago q siempre est� en otro
        	if (nPlanetas > 1){
        		indexPlanet2 = rand.nextInt(nPlanetas);
        	}else{
        		indexPlanet2 = 0;
        	}
    	}while (nPlanetas > 1 && indexPlanet == indexPlanet2);
    	Planet.setiPlanetTarget(indexPlanet2);
    	double arg2 =  rand.nextDouble()*2*Math.PI;
    	Planet.setArgPlanetTarget(arg2);
    	objetivo.setOnTop(planeta[indexPlanet2], arg2);
    	objetivo.setSprite("imagenes/planeta2.png");
    	Nivel nivelAleatorio= new Nivel(particula,planeta,objetivo);
    	
    	nivelAleatorio.setiPlanetParticle(indexPlanet);
    	nivelAleatorio.setiPlanetTarget(indexPlanet2);
    	nivelAleatorio.setArgPlanetParticle(arg);
    	nivelAleatorio.setArgPlanetTarget(arg2);
    	
    	return nivelAleatorio;
    }
    
    public static void randomPos(Planet p,int width, int height){
    	double valX,valY;
    	valX=rand.nextInt(900-2 *(int)(p.getRadio())-16) +8+p.getRadio();
    	valY=rand.nextInt(600-2 *(int)(p.getRadio())-16) +8+p.getRadio();
    	double [] pos ={valX,valY};
    	p.setPos(pos);
    }

    public static void randomMass(Planet p, int min, int max, int media, int varianza){
		//int media, varianza;
		//media = (int) (500);
		//varianza = (int) (200);
    	double valMasa;
		do{
			valMasa = (double) (rand.nextGaussian() * media + varianza);
			p.setMasa((int) Math.round(valMasa));
			//System.out.println("valMasa: " + valMasa);
			//System.out.println("Masa   : " + p.getMasa());
		}while(valMasa <min || valMasa > max);
    }
    


	public static boolean bienDef(Planet p, int index,Planet [] planeta){
		boolean sinIntersecciones =true;
		int i = 0;
		while (sinIntersecciones && i<index){
			//if (Util.distancia(planeta[i], p)<2*p.getRadio()){
			if (Util.distancia(planeta[i], p)<Math.min(p.getRadio(), planeta[i].getRadio())){
				sinIntersecciones=false;
			}
			i++;
		}

		return sinIntersecciones;
	}




}
