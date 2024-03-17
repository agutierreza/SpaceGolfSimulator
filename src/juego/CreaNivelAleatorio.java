
package juego;

import java.io.IOException;
import java.util.Random;

import javax.xml.bind.JAXBException;

import general.Simulacion;
import general.Util;

public class CreaNivelAleatorio {
	
	int nPlanetas;

    private Random r = new Random();
    Juego nivelAleatorio;
	
	
    public  CreaNivelAleatorio(int nPlanetas) throws JAXBException, IOException{

    	this.nPlanetas = nPlanetas;
    	
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
        		this.randomMass(planeta[i],(int)(100),(int)(900));
        		planeta[i].setRadio();
        		//this.randomPos(planeta[i], gWidth, gHeight);
        		this.randomPos(planeta[i], 900, 600);
    		}while(bienDef(planeta[i],i,planeta) == false);
    	}
    	
    	//y ahora la partícula
    	particula = new Planet(4,posParticula,velParticula,accParticula);
    	int indexPlanet;
    	indexPlanet = r.nextInt(nPlanetas);
    	Planet.setiPlanetParticle(indexPlanet);
    	double arg = (double) (r.nextDouble()*2*Math.PI);
    	Planet.setArgPlanetParticle(arg);
    	particula.setOnTop(planeta[indexPlanet], arg);
    	particula.setCollidedPlanet(planeta[indexPlanet]);
        //particula.setSprite("imagenes/planeta2.png");
        particula.setSprite("imagenes/bolaroja2.gif");
		
		
        //y ahora lo(s) objetivo(s) / obstaculo(s)       
        objetivo = new Planet(5,posObjetivo,velObjetivo,accObjetivo);
        int indexPlanet2;
    	do{
    		//Si solo hay un planeta dejo q el objetivo esté donde la partícula, en caso contrario hago q siempre esté en otro
        	if (nPlanetas > 1){
        		indexPlanet2 = r.nextInt(nPlanetas);
        	}else{
        		indexPlanet2 = 0;
        	}
    	}while (nPlanetas > 1 && indexPlanet == indexPlanet2);
    	Planet.setiPlanetTarget(indexPlanet2);
    	double arg2 =  r.nextDouble()*2*Math.PI;
    	Planet.setArgPlanetTarget(arg2);
    	objetivo.setOnTop(planeta[indexPlanet2], arg2);
    	objetivo.setSprite("imagenes/planeta2.png");
        
    	
    	
    	
    	
    	//JUGANDO CON VARIOS EJEMPLOS CURIOSOS 
        /*
        double [] posPlanetaTest = {200,300};
        double [] posPlanetaTest2 = {600,300};
        double [] posParticulaTest = {400,300};
        double [] velTest = {0,0}; //equilibrio
        //double [] velTest = {0,1}; //oscilador armónico
        //double [] velTest = {1.07772, -1.42413}; //lazo
        planeta[0].setMasaRadio(400);
        planeta[1].setMasaRadio(400);
        planeta[0].setPos(posPlanetaTest);
        planeta[1].setPos(posPlanetaTest2);
        particula.setPos(posParticulaTest);
        particula.setVel(velTest);
        */
        
        //Velocidad de escape
        /*
        double [] posPlanetaTest = {000,300};
        double [] posParticulaTest = {400,300};
        double [] velTest = {0.707,0};
        planeta[0].setMasaRadio(100);
        planeta[0].setPos(posPlanetaTest);
        particula.setPos(posParticulaTest);
        particula.setVel(velTest);
        */
        

        /*
        double [] posPlanetaTest = {450,300};
        double [] posParticulaTest = {450,300};
        double [] velTest = {0,0};
        planeta[0].setMasa(400);
        //planeta[0].setRadio(40);
        particula.setRadio(1);
        //planeta[0].setRadio(20);
        
        planeta[0].setPos(posPlanetaTest);
        particula.setPos(posParticulaTest);
        particula.setVel(velTest);
		*/
      
        
        //Esta clase debería ser un método que devolviese un objeto Nivel...
    	Nivel nivelAleatorio= new Nivel(particula,planeta,objetivo);
    	
    	nivelAleatorio.setiPlanetParticle(indexPlanet);
    	nivelAleatorio.setiPlanetTarget(indexPlanet2);
    	nivelAleatorio.setArgPlanetParticle(arg);
    	nivelAleatorio.setArgPlanetTarget(arg2);
        
        //Juego juego = new Juego(750,500,nivelAleatorio);
        //Juego juego = new Juego(900,600,nivelAleatorio);
        //Juego juego = new Juego(1350,900,nivelAleatorio);
        Simulacion simula = new Simulacion(nivelAleatorio);
        Juego juego = new Juego(360,240,nivelAleatorio);
        //Juego juego = new Juego(900,600,nivelAleatorio);


        //Juego juego = new Juego(750,500,nivelAleatorio);
    }
    
    

    
    public Juego getNivel(){
		return nivelAleatorio;
    	
    }
    
    
    
    
    
    
   
    
    public void randomPos(Planet p,int width, int height){
    	double valX,valY;
    	//valX=r.nextInt(width-2*(int)(p.getRadio()))+p.getRadio();
    	//valY=r.nextInt(height-2*(int)(p.getRadio()))+p.getRadio();
    	valX=r.nextInt(900-2*(int)(p.getRadio()))+p.getRadio();
    	valY=r.nextInt(600-2*(int)(p.getRadio()))+p.getRadio();
    	double [] pos ={valX,valY};
    	p.setPos(pos);
    }

    public void randomMass(Planet p, int min, int max){
		int media, varianza;
		media = (int) (500);
		varianza = (int) (200);
    	double valMasa;
		do{
			valMasa = (double) (r.nextGaussian() * media + varianza);
			p.setMasa((int) Math.round(valMasa));
			//System.out.println("valMasa: " + valMasa);
			//System.out.println("Masa   : " + p.getMasa());
		}while(valMasa <min || valMasa > max);
    }
    

	public boolean bienDef(Planet p, int index,Planet [] planeta){
		boolean sinIntersecciones =true;
		int i = 0;
		while (sinIntersecciones && i<index){
			if (Util.distancia(planeta[i], p)<2*p.getRadio()){
				sinIntersecciones=false;
			}
			i++;
		}

		return sinIntersecciones;
	}
	
	
}
