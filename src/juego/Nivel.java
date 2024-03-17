package juego;






import general.Util;
import xml.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Nivel extends JPanel implements Comparable{
	protected static final int MAX_COLLISIONS_COLLIDED = 25;
    private Planet[] planeta;
	private Planet particle, target;
	private int iPlanetParticle;
	private int iPlanetTarget;
	private double argPlanetParticle;
	private double argPlanetTarget;
	
	private double fitnessValue;
    
    
	//Constructor que copia un nivel a partir de otro.
	public Nivel (Nivel otroNivel){

		Planet [] newPlaneta = new Planet[otroNivel.getPlaneta().length];
		for(int i=0;i<otroNivel.getPlaneta().length;i++){
			//newPlaneta[i]=otroNivel.getPlaneta()[i].clone();
			newPlaneta[i] = new Planet(otroNivel.getPlaneta()[i]);
		}
		this.planeta=newPlaneta;
		//Planet newParticle = otroNivel.particle.clone();
		
		//this.particle= otroNivel.particle.clone();
		this.particle= new Planet(otroNivel.particle);
		//this.target= otroNivel.target;
		this.target= new Planet(otroNivel.target);
		this.iPlanetParticle=otroNivel.iPlanetParticle;
		this.iPlanetTarget=otroNivel.iPlanetTarget;
		this.argPlanetParticle=otroNivel.argPlanetParticle;
		this.argPlanetTarget=otroNivel.argPlanetTarget;
		this.fitnessValue=otroNivel.fitnessValue;
	}
	
	
	public Nivel (Planet particle,Planet[] planeta,Planet target){
		
		this.setParticle(particle);
		this.setPlaneta(planeta);
		this.setTarget(target);
		this.setiPlanetParticle(iPlanetParticle);
		this.setiPlanetTarget(iPlanetTarget);
		this.setArgPlanetParticle(argPlanetParticle);
		this.setArgPlanetTarget(argPlanetTarget);
		/*
		
		System.out.println("Particula Pos: ("+particle.getPos()[0]+", "+particle.getPos()[1]+")");
    	System.out.println("Particula on: " + Planet.getiPlanetParticle());
		System.out.println("Objetivo Pos: ("+target.getPos()[0]+", "+target.getPos()[1]+")");
		System.out.println("Objetivo on: " + Planet.getiPlanetTarget());
		System.out.println("Distncia particula-objetivo: "+ Util.distancia(particle, target));
		}
		
		
		accTPlanet();
		System.out.println("Aceleración a distancia xxx: "+accAroundTargetPlanet(200,50));
		*/
		
		for (int i=0;i<planeta.length;i++){
			//System.out.println(planeta[i].toString());
    		//System.out.println("Planeta["+i+"] Pos: ("+planeta[i].getPos()[0]+", "+planeta[i].getPos()[1]+"). Masa: "+ planeta[i].getMasa());
		}

	}
	
	public Nivel (){
		
	}


	/**
	 * Constructor de nivel a partir de un fichero xml. 
	 * En un futuro puedo meterle un esquema XSD y lanzar una expección si el fichero no la satisface
	 * @param ruta
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public Nivel(String ruta) throws JAXBException, FileNotFoundException {
		// TODO Auto-generated constructor stub
		
		JAXBContext context = JAXBContext.newInstance(NivelXML.class);
		Unmarshaller um = context.createUnmarshaller();
		NivelXML nivelXML = (NivelXML) um.unmarshal(new FileReader(ruta));
		ArrayList<PlanetXML> list = nivelXML.getPlanetsList();
		
		planeta= new Planet[list.size()];
		double [] posParticula = {0,0};
		double [] velParticula = {0,0};
		double [] accParticula = {0,0};
		double [] posObjetivo = {0,0};
		double [] velObjetivo = {0,0};
		double [] accObjetivo = {0,0};
		
		for (PlanetXML planet : list) {
			System.out.println(planet.getId());
			System.out.println(planet.getMasa());
			double [] pos ={planet.getPosX(),planet.getPosY()};
			planeta[planet.getId()]= new Planet(planet.getMasa());
			planeta[planet.getId()].setPos(pos);
			planeta[planet.getId()].setSprite("imagenes/planeta3.png");
			
		}
		particle = new Planet(4,posParticula,velParticula,accParticula);
		particle.setOnTop(planeta[nivelXML.getIdParticle()], nivelXML.getArgParticle());
		particle.setSprite("imagenes/bolaroja2.gif");
		particle.setCollidedPlanet(planeta[nivelXML.getIdParticle()]);
		
		target = new Planet(5,posObjetivo,velObjetivo,accObjetivo);
		target.setOnTop(planeta[nivelXML.getIdTarget()], nivelXML.getArgTarget());
		target.setSprite("imagenes/planeta2.png");
		
    	this.setiPlanetParticle(nivelXML.getIdParticle());
    	this.setiPlanetTarget(nivelXML.getIdTarget());
    	this.setArgPlanetParticle(nivelXML.getArgParticle());
    	this.setArgPlanetTarget(nivelXML.getArgTarget());
	}



	/**
	 * Guarda el nivel en un fichero xml
	 * @param ruta
	 * @throws JAXBException
	 * @throws IOException
	 */
	public void save(String ruta) throws JAXBException, IOException{
		//guarda en un XML la info del nivel
		PlanetXML[] planetaXML = new PlanetXML[planeta.length];
		ArrayList<PlanetXML> planetList = new ArrayList<PlanetXML>();
		
		for (int i=0;i<planeta.length;i++){
			planetaXML[i]= new PlanetXML();
			planetaXML[i].setId(i);
			planetaXML[i].setMasa(planeta[i].getMasa());
			planetaXML[i].setPosX((int) planeta[i].getPos()[0]);
			planetaXML[i].setPosY((int) planeta[i].getPos()[1]);
			planetList.add(planetaXML[i]);
		}
		

		
		NivelXML nivelXML = new NivelXML();
		
		nivelXML.setIdParticle(this.getiPlanetParticle());
		nivelXML.setIdTarget(this.getiPlanetTarget());
		nivelXML.setArgParticle(this.getArgPlanetParticle());
		nivelXML.setArgTarget(this.getArgPlanetTarget());
		nivelXML.setPlanetList(planetList);


	    JAXBContext context = JAXBContext.newInstance(NivelXML.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    //m.marshal(nivelXML, System.out);
	    m.marshal(nivelXML, new File(ruta));
	}
	
	
	
	
	
	
	public void setFitnessValue(double fitnessValue) {
		this.fitnessValue = fitnessValue;
	}

	public double getFitnessValue() {
		return fitnessValue;
	}

	public void setPlaneta(Planet[] planeta) {
		this.planeta = planeta;
	}

	public Planet[] getPlaneta() {
		return planeta;
	}

	public void setParticle(Planet particle) {
		this.particle = particle;
	}

	public Planet getParticle() {
		return particle;
	}

	public void setTarget(Planet target) {
		this.target = target;
	}

	public Planet getTarget() {
		return target;
	}
	public void setiPlanetParticle(int iPlanetParticle) {
		this.iPlanetParticle = iPlanetParticle;
	}
	public int getiPlanetParticle() {
		return iPlanetParticle;
	}
	public void setiPlanetTarget(int iPlanetTarget) {
		this.iPlanetTarget = iPlanetTarget;
	}
	public int getiPlanetTarget() {
		return iPlanetTarget;
	}
	public void setArgPlanetParticle(double argPlanetParticle) {
		this.argPlanetParticle = argPlanetParticle;
	}
	public double getArgPlanetParticle() {
		return argPlanetParticle;
	}
	public void setArgPlanetTarget(double argPlanetTarget) {
		this.argPlanetTarget = argPlanetTarget;
	}
	public double getArgPlanetTarget() {
		return argPlanetTarget;
	}
	
	
	
	
    public void trickyBug(){
    	//extends nivel
    	//double d = Util.distancia(getParticle(),getParticle().getCollidedPlanet());
    	double d = Util.distancia(getParticle(),getParticle().getCollidedPlanet());
    	if(d<=0.01){
    		System.out.println("Partícula incrustada: " + d);
    		double [] vector = new double[2];
    		double [] newPos = new double[2];
    		vector[0]= getParticle().getPos()[0]- getParticle().getCollidedPlanet().getPos()[0];
    		vector[1]= getParticle().getPos()[1]- getParticle().getCollidedPlanet().getPos()[1];
    		double modulovector = (double) Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
    		double distanciacorrecta = getParticle().getRadio()+getParticle().getCollidedPlanet().getRadio();
    		double eRelativo= (Math.abs(d)/getParticle().getCollidedPlanet().getRadio());
    		if (eRelativo<0.01){
    			eRelativo = 0.01f;
    		}
    		double escalar = (1+eRelativo)*distanciacorrecta/modulovector;
    		newPos[0]=  getParticle().getCollidedPlanet().getPos()[0]+ escalar*vector[0];
    		newPos[1]=  getParticle().getCollidedPlanet().getPos()[1]+ escalar*vector[1];
    		getParticle().setPos(newPos);
    		//displayGame();
    		System.out.println("escalar "+eRelativo);
    		System.out.println("Arreglado. distancia: "+Util.distancia(getParticle(),getParticle().getCollidedPlanet()));
    	}
    	
    }
    

    
    public void planetsInteraction(Planet p){
		for (int j=0;j<planeta.length;j++){//primero miro si hay colision con cada planeta
			if (p.isCollided()==false && (Util.distancia(p,planeta[j])<0)){
        		p.setCollided(true);
        		p.setCollidedPlanet(planeta[j]);
    		}else{
    			if (p.getCollidedPlanet() == planeta[j])
    				p.setCollided(false); //si he vuelto a alejarme lo suficiente DEL ULTIMO PLANETA COLISIONADO, digo que la particula no está colisionada
    		}
			//masrapido
    		p.addAcelBy(planeta[j]);//añadimos las aceleraciones de los planetas a la partícula
    		//getParticula().addAcelBy(planeta[j],d);//añadimos las aceleraciones de los planetas a la partícula

		}
    }
    public void objetiveInteraction(){
    	if (Util.distancia(getParticle(),getTarget())<0){
    		System.out.println("Objetivo alcanzado!");
    		
    	}
    }
    
    
	public int[][]  simula(int nVeces,double[] vel){
		long t0=System.currentTimeMillis();

		Planet ghostPlanet = new Planet(getParticle());
		//Planet ghostPlanet = getParticle().clone();
		
		
		
		ghostPlanet.setVel(vel);
    	//Aquí lo importante
    	int [][] puntos= new int[2][nVeces];
    	
    	int it=0;

    	while (it<nVeces && ghostPlanet.getTimesRecollided()< MAX_COLLISIONS_COLLIDED){
 
    		planetsInteraction(ghostPlanet);
			ghostPlanet.updatePlanet();
			puntos[0][it]=(int)ghostPlanet.getPos()[0];
			puntos[1][it]=(int)ghostPlanet.getPos()[1];
    		it++;	
    	}
    	
    	//Completamos los puntos si fuera necesario
    	for (int i=it;i<nVeces;i++){
			puntos[0][i]=(int)ghostPlanet.getPos()[0];
			puntos[1][i]=(int)ghostPlanet.getPos()[1];
    	}
    	
    	System.out.println("Tiempo en simular " + nVeces + " iteraciones: " + String.valueOf(System.currentTimeMillis()- t0 + " milisegundos. Iteraciones reales: " + it));
    	System.out.println("Distancia camino: " + Util.pathDistance(puntos));
    	System.out.println("Golpes: "+ghostPlanet.getTimesCollided());
		return puntos;

    }
	
	
	//Métodos para evaluar los niveles


	/**
	 * Calcula el módulo de la aceleración que habría en el centro del planeta objetivo sin tenerse en cuenta así mismo.
	 * Así recoge en un solo valor información sobre la masa y la posición del resto de los planetas. Naturalmente,
	 * a mayor módulo de aceleración, debería ser más difícil alcanzar el objetivo (aunque la masa del planeta objetivo
	 * jugaría un papel mucho más importante.
	 * 
	 */
	public double accTPlanet(){
		double modulo = 0;
		double acel[];
		double total[] = {0,0};
		for (int i=0;i<planeta.length;i++){
			if (i != this.getiPlanetTarget()){
			//if (i != Planet.getiPlanetTarget()){
				//quitar de Planet parametros
				acel=Util.acelBy(planeta[i],planeta[this.getiPlanetTarget()].getPos());
				//acel=Util.acelBy(planeta[i],planeta[Planet.getiPlanetTarget()].getPos());
				total[0]+=acel[0];
				total[1]+=acel[1];
			}
		}
		modulo = Math.sqrt(Math.pow(total[0],2) + Math.pow(total[1],2));
		System.out.println("Acel en objetivo: "+modulo);
		return modulo;
	}
	
	public double intersecciones(){
		double parcial = 0,total=0;
		//int nIntersecciones=0;
		for (int i=0;i<planeta.length;i++){
			//
			//int nIntersecciones=0;
			parcial=0;
			if (target.getPos()[0]-particle.getPos()[0]>=0){
				//caso1
				if (planeta[i].getPos()[0]<planeta[this.getiPlanetTarget()].getPos()[0] && planeta[i].getPos()[0]>particle.getPos()[0]){
					//compruebo
					if (i!=this.getiPlanetTarget()){
						if(i==this.getiPlanetParticle()){
							//parcial=2*Util.distanciaInterseccion(particle.getPos(), planeta[this.getiPlanetTarget()].getPos(), planeta[i]);
							parcial=Util.distanciaInterseccion(particle.getPos(), planeta[this.getiPlanetTarget()].getPos(), planeta[i]);
							//nIntersecciones++;
							//System.out.println("Planeta particle "+i+ " aporta: "+parcial);
						}else{
							parcial=Util.distanciaInterseccion(particle.getPos(), planeta[this.getiPlanetTarget()].getPos(), planeta[i]);
							//nIntersecciones++;
						}
						//System.out.println("Planeta "+i+" aporta: "+parcial);
					}
				}
			}else{
				//caso2
				if (planeta[i].getPos()[0]<particle.getPos()[0] && planeta[i].getPos()[0]>planeta[this.getiPlanetTarget()].getPos()[0]){
					//compruebo
					if (i!=this.getiPlanetTarget()){
						if(i==this.getiPlanetParticle()){
							//parcial=2*Util.distanciaInterseccion(particle.getPos(), planeta[this.getiPlanetTarget()].getPos(), planeta[i]);
							parcial=Util.distanciaInterseccion(particle.getPos(), planeta[this.getiPlanetTarget()].getPos(), planeta[i]);
							//nIntersecciones++;
							//System.out.println("Planeta particle "+i+ " aporta: "+parcial);
						}else{
							parcial=Util.distanciaInterseccion(particle.getPos(), planeta[this.getiPlanetTarget()].getPos(), planeta[i]);
							//nIntersecciones++;
						}
						
						//System.out.println("Planeta "+i+" aporta: "+parcial);
					}
				}
			}
			total+=parcial;
			//total*=nIntersecciones;
		}
		//double d = Util.distancia(getParticle(), getTarget());
		//System.out.println("Total: "+total);
		//return (total/d);
		return (total);
	}
	
	//cuidado con la división por cero en m!!!
    public  double distanciaInterseccion(double[] p1, double[] p2, Planet p){
    	
		Graphics2D grafico2d = (Graphics2D) this.getGraphics();
		grafico2d.setColor(Color.red);
		
    	double m = (p2[1]-p2[0])/(p1[1]-p1[0]);
    	double n = -m*p1[0]+p1[1];
    	double a = p.getPos()[0];
    	double b = p.getPos()[1];
    	double R = p.getRadio();
    	double m2 = Math.pow(m, 2);
    	double n2 = Math.pow(n, 2);
    	double a2 = Math.pow(a, 2);
    	double b2 = Math.pow(b, 2);
    	double R2 = Math.pow(R, 2);
    	
    	grafico2d.drawLine((int)p1[0],(int) p1[1],(int)p2[0],(int) p2[1]);
    	
    	double discr = R2*m2+R2-a2*m2+2*a*b*m-2*a*m*n-b2+2*b*n-n2;
    	if (discr <= 0){
    		return 0;
    	}else{
    		double [] corte1 = new double[2];
    		double [] corte2 = new double[2];
    		corte1[0]=- Math.sqrt(discr)/(m2+1) + (a+b-m*n)/(m2+1);
    		corte2[0]=  Math.sqrt(discr)/(m2+1) + (a+b-m*n)/(m2+1);
    		corte1[1]= m*corte1[0]+n;
    		corte2[1]= m*corte2[0]+n;
    		
    		return Util.modulo(corte1, corte2);
    	}
    	
    }
    
    public double relativeMassTargetPlanet(){
    	double mTotal = 0;
    	for (int i=0; i<this.planeta.length;i++){
    		mTotal+=planeta[i].getMasa();
    	}
    	return planeta[this.getiPlanetTarget()].getMasa()/mTotal;
    }
	
	/**
	 * Posiciona la partícula a la distancia d nArgs veces, con ángulos equiespaciados. Calcula la aceleración en estos
	 * puntos causada por todos los planetas y con esto
	 * @param distancia
	 * @param accuracy
	 * @return 
	 */
	public double accAroundTargetPlanet(double d,int nArgs){
		double acelMedia=0;
		double arg = 2*Math.PI/nArgs;
		//me cargo la distancia
		//d = planeta[Planet.getiPlanetTarget()].getRadio()+4;
		d = planeta[this.getiPlanetTarget()].getRadio()+4;
		
		double [] newPos = {0,0};
		double [] newAcel = {0,0};
		
		for(int i=0;i<nArgs;i++){
			
			//Obtenemos posición dando coordenadas relativas al planeta objetivo
			
			
			newPos=Util.setOn(planeta[this.getiPlanetTarget()], d, arg);
			//System.out.println("newPos: "+newPos[0]+","+newPos[1]);
			//Calculo el vector dado por el punto anterior y el centro del planeta;
			double [] B = {0,0};
			//B[0]= planeta[Planet.getiPlanetTarget()].getPos()[0]-newPos[0];
			//B[1]= planeta[Planet.getiPlanetTarget()].getPos()[1]-newPos[1];
			B[0]= planeta[this.getiPlanetTarget()].getPos()[0]-newPos[0];
			B[1]= planeta[this.getiPlanetTarget()].getPos()[1]-newPos[1];
			//Sumamos la acel de todos los planetas desde newPos
			
			newAcel[0]=0;
			newAcel[1]=0;
			for (int j=0;j<planeta.length;j++){
				newAcel[0]+= Util.acelBy(planeta[j], newPos)[0];
				newAcel[1]+= Util.acelBy(planeta[j], newPos)[1];
			}
			/*Probando solo con el propio target planet para ver q la proy se calcula bien
			newAcel[0]=Util.acelBy(planeta[Planet.getiPlanetTarget()], newPos)[0];
			newAcel[1]=Util.acelBy(planeta[Planet.getiPlanetTarget()], newPos)[1];
			System.out.println(newAcel[0]+", "+newAcel[1]);
			*/
			double[] acelTargetPlanet = new double[2];
			acelTargetPlanet = Util.acelBy(planeta[this.getiPlanetTarget()], newPos);
			//Hacemos el calculo de la proyección horizontal con el vector de acc final
			//La proyeccion es el producto vectorial de newPos*B / |B|, y |B| es precisamente la distancia.
			double proy = (B[0]*newAcel[0]+B[1]*newAcel[1])/d;
			//System.out.println("angulo: "+arg+" Proy: "+proy);
			double proyTarget = (B[0]*acelTargetPlanet[0]+B[1]*acelTargetPlanet[1])/d;
			//IMPORTANTE: Si hay una atraccion muy fuerte hacia el TargetPlanet causada por otros planetas no la tengo en cuenta
			if(proy>proyTarget){
				proy=proyTarget;
			}
			//proy=Util.modulo(acelTargetPlanet);
			
			acelMedia+=proy;
			//Guardar el valor en algún sitio para acceder fuera
			arg+=2*Math.PI/nArgs;
		}
		
		//Calcular la media de todos los valores anteriores para dar el resultado final.
		return acelMedia;
	}
	
    public double evaluate() {
        
    	double fitness = 0;
        

        fitness = this.intersecciones();

    	//fitness = Math.abs(this.accAroundTargetPlanet(0,50));
    	
    	
    	
    	//fitness=1/(Math.abs(this.accTPlanet())+0.01);
    	//for (int i=0; i<this.planeta.length;i++){
    	//	fitness+=planeta[i].getMasa();
    	//}
        
    	//fitness = Util.distancia(this.getParticle(), this.getTarget());
        //System.out.println("Total: "+fitness);
        this.setFitnessValue(fitness);

        return fitness;
    }
	public boolean nivelBienDefinido(){
		boolean bienDef=true;
		for(int i=0;i<this.planeta.length;i++){
			if (planetaBienDefinido(i)==false){
				bienDef=false;
			}
		}
		return bienDef;
	}
    public boolean planetaBienDefinido(int indx){
    	
    	boolean sinIntersecciones =true;
    	boolean planetaInterior = true;
		for(int i=0;i<this.getPlaneta().length;i++){
			if (Util.distancia(this.getPlaneta()[indx], this.getPlaneta()[i])<Math.min(this.getPlaneta()[indx].getRadio(), this.getPlaneta()[i].getRadio()) && (indx !=i)){
				sinIntersecciones=false;
				//System.out.println("Planetas "+i+" y "+indx+" demasiado cerca");
			}
		}
		
		int limiteSuperior,limiteInferior,limiteIzquierda,limiteDerecha;
		limiteIzquierda = (int) (this.getPlaneta()[indx].getPos()[0]-this.getPlaneta()[indx].getRadio()-this.getParticle().getRadio());
		limiteDerecha   = (int) (this.getPlaneta()[indx].getPos()[0]+this.getPlaneta()[indx].getRadio()+this.getParticle().getRadio());
		limiteSuperior  = (int) (this.getPlaneta()[indx].getPos()[1]-this.getPlaneta()[indx].getRadio()-this.getParticle().getRadio());
		limiteInferior  = (int) (this.getPlaneta()[indx].getPos()[1]+this.getPlaneta()[indx].getRadio()+this.getParticle().getRadio());
		
		if((limiteIzquierda<0)|| (limiteDerecha>900)|| (limiteSuperior<0)||(limiteInferior>600)){
			planetaInterior = false;
			//System.out.println("Planeta"+indx+" en mala posicion. Izquierda: "+limiteIzquierda+" derecha: "+limiteDerecha+" superior: "+limiteSuperior+" inferior: "+limiteInferior);
		}
			
    	return (sinIntersecciones && planetaInterior);
    	
    }
    
    public boolean inNiveles(ArrayList<Nivel> population){
    	boolean iguales = false;
    	int i=0;
    	while((iguales==false)&&i<population.size()){
    		if (this.esIgual(population.get(i))){
    			iguales = true;
    			/*
            	for(int j=0;j<this.planeta.length;j++){
            		System.out.print("Nivel list"+" Planeta "+j+" pos: "+population.get(i).getPlaneta()[j].getPos()[0]+", "+population.get(i).getPlaneta()[j].getPos()[0]
            		                   +" masa "+population.get(i).getPlaneta()[j].getMasa());
            	}
            	*/
    		}
    		
    		i++;
    	}
    	return iguales;
    }

    public boolean esIgual (Nivel nivel){
    	
    	
    	boolean iguales = true;
    	int i=0;
    	//filtro más facil de comprobar
    	if (this.planeta.length!=nivel.planeta.length){
    		iguales = false;
    	}
    	//segundo filtro
    	
    	/*
    	if (iguales && this.particle.esIgual(nivel.particle)==false || this.target.esIgual(nivel.target)==false){
    		iguales = false;
    	}
    	*/
    	
    	//ultimo filtro y más chungo (este se podría programar mejor, pero estoy hasta los WWW!!
    	while(iguales==true&&i<this.planeta.length){
    		if (this.planeta[i].inPlanetas(nivel.planeta)==false){
    			iguales = false;
    		}
    		i++;
    	}
    	
    	return iguales;
    }

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		double compareQuantity = ((Nivel) arg0).getFitnessValue();

		//el primero el mas grande
		
		if (this.getFitnessValue()>compareQuantity){
			return -1;
		}else{
			if (this.getFitnessValue()<compareQuantity){
				return 1;
			}else{
				return 0;
			}
			
		}
		
		//el primero el mas chico
		/*
		if (this.getFitnessValue()>compareQuantity){
			return 1;
		}else{
			if (this.getFitnessValue()<compareQuantity){
				return -1;
			}else{
				return 0;
			}
			
		}
		*/
	}
	/*
	@Override
	public boolean equals(Object obj){
		Nivel nivel = (Nivel)obj;
		if(this.planeta.equals(nivel)){
			return true;
		}else{
			return false;
		}

	}
	*/




	
}
