package general;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import juego.CreaNivelAleatorio;
import juego.Juego;
import juego.Nivel;
import juego.Planet;

public class Simulacion extends Nivel{
//public class Simulacion extends Juego{

	private double [] vTest = {0,0};
	private int nTiros = 0;
	private int nAciertos = 0;
	private float dificultadAciertos = 0;
	
	
	private float escala = 1;
	private JFrame ventana;
	private BufferedImage pantalla;

	public Simulacion(Nivel nivel){
		

		
    	super.setParticle(nivel.getParticle());
    	super.setPlaneta(nivel.getPlaneta());
    	super.setTarget(nivel.getTarget());
		super.setiPlanetParticle(nivel.getiPlanetParticle());
		super.setiPlanetTarget(nivel.getiPlanetTarget());
		
        ventana=new JFrame();
        this.pantalla = new BufferedImage(900,600, BufferedImage.TYPE_INT_RGB );
        ventana.setSize(900 + 16, 600 + 38);
        ventana.add(this);
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        escala = 900/900f;
        
        
        
        
        //Empiezan las cosas de la simulacion
        
        trickyBug();
		
		int ghostMasa = getParticle().getMasa();
    	double [] ghostPos = {getParticle().getPos()[0],getParticle().getPos()[1]};
    	double [] ghostVel = vTest;
    	double [] ghostAcel = {0,0};
    	Planet ghostPlanet = new Planet(ghostMasa,ghostPos,ghostVel,ghostAcel);
		

    	/*
    	vTest = Util.newVel(1, ghostPlanet, 1, 1);
		int [][] puntos= simula(ghostPlanet,2000,vTest);
		dibujaTraza(this.getGraphics(), puntos, Color.red);
    	*/
    	
		long t1=System.currentTimeMillis();
		
		//mapGravity(ghostPlanet);
		
		/*
		for(int i=0;i<900;i=i+50){
			for (int j=0;j<600;j=j+50){
				System.out.println("i: "+i+" j: "+j);
				vTest = Util.newVel(1, getParticle(), i, j);
				int [][] puntos= simula(5000,vTest);
				dibujaTraza(this.getGraphics(), puntos, Color.red);
				nTiros++;
			}
		}
		*/
		
		for(int i=(int) (getParticle().getPos()[0]-150);i<(int) (getParticle().getPos()[0]+150);i++){
			for (int j=(int) (getParticle().getPos()[1]-150);j<(int) (getParticle().getPos()[1]+150);j++){
				//System.out.println("i: "+i+" j: "+j);
				vTest = Util.newVel(1, getParticle(), i, j);
				System.out.println(Util.modulo(vTest));
				
				int [][] puntos= simula(5000,vTest);
				//dibujaTraza(this.getGraphics(), puntos, Color.red);
				nTiros++;
			}
		}

		
		float prob = (float)(nAciertos)/(float)(nTiros);
		float dificultad = dificultadAciertos/(float)(nTiros);
		System.out.println("Estadistica: " + prob);
		System.out.println("Dificultad: " + dificultad);

		
    	System.out.println("Tiempo total: " + String.valueOf(System.currentTimeMillis()- t1 + " milisegundos"));
		
	}
	
	
	

	
	public void mapGravity(Planet p){

		double [] [] map =  new double[901][601];
		double []  pos = {0,0};
        FileWriter fichero = null;
        PrintWriter pw = null;
        try{
        	fichero = new FileWriter("src/logs/mapa.txt");
        	pw = new PrintWriter(fichero);
        	
    		for(int i=0;i<=900;i++){
    			for (int j=0;j<=600;j++){
    				
    				//Actualizo posicion y reseteo
    				pos[0]=(double)i;
    				pos[1]=(double)j;
    				p.setPos(pos);
    				p.setAcelToCero();
    				p.setCollidedPlanet(null);
    				
    				//compruebo si el punto i,j actual no está en el interior de un planeta
    				for (int k=0;k<getPlaneta().length;k++){
    	    			if (Util.distancia(p,getPlaneta()[k])<0){
    	            		p.setCollidedPlanet(getPlaneta()[k]);
    	    			}
    				}
    				//dependiendo de si el punto es legal o no, escribo el módulo de la aceleración o 0 en map, segun corresponda
    				if(p.getCollidedPlanet()==null){
    					for (int k=0;k<getPlaneta().length;k++){
    						p.addAcelBy(getPlaneta()[k]);
    					}

    					map[i][j]=-Math.sqrt(Math.pow(p.getAcc()[0], 2)+Math.pow(p.getAcc()[1], 2));

    				}else{
    					map[i][j]=-0.1;
    				}
    				
    				pw.print(map[i][j]+";");
    			}//endj
    			pw.println();
    		}//endi
        	
        	
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
	
	public void escribeMapa(double [] [] map){
		for (int i=0;i<map.length;i++){
			
		}
	}

	

	@Override
	public int[][] simula(int nVeces,double[] vel){
		long t0=System.currentTimeMillis();

		Planet ghostPlanet = getParticle().clone();
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
    	
    	//if (ghostPlanet.getCollidedPlanet() == getPlaneta()[Planet.getiPlanetTarget()]){
    	if (ghostPlanet.getCollidedPlanet() == getPlaneta()[this.getiPlanetTarget()]){
    		
    		nAciertos++;
    		dificultadAciertos+=1000/(Math.sqrt(ghostPlanet.getTimesCollided())*Util.pathDistance(puntos));
    		System.out.println("Dificultad por distancia: " + 1000/(Math.sqrt(ghostPlanet.getTimesCollided())*Util.pathDistance(puntos)));
    		System.out.println("Distancia camino: " + Util.pathDistance(puntos));
    		System.out.println("Golpes: "+ghostPlanet.getTimesCollided());
    	}
    	
    	System.out.println("Tiempo en simular " + nVeces + " iteraciones: " + String.valueOf(System.currentTimeMillis()- t0 + " milisegundos. Iteraciones reales: " + it));
    	System.out.println("Distancia camino: " + Util.pathDistance(puntos));
    	System.out.println("Golpes: "+ghostPlanet.getTimesCollided());
		return puntos;
	}
    
    public void dibujaTraza(Graphics grafico,int [][] puntos,Color colorTraza){
    	Graphics2D grafico2d = (Graphics2D) grafico;
        paint(grafico);

        for (int i=0;i<2000-1;i++){
        	grafico2d.setColor(colorTraza);
        	grafico2d.drawLine(puntos[0][i], puntos[1][i], puntos[0][i+1],puntos[1][i+1]);
        }
        
    }
}
