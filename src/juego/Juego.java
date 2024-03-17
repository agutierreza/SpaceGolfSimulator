package juego;

 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.bind.JAXBException;

import xml.PlanetXML;


import general.Util;

public class Juego extends Nivel implements MouseListener, MouseMotionListener{
	private static final long serialVersionUID = 1L;
	private static final int TIME_DELTA = 1;
	private static final int TICKS_PER_SECOND = 200;
    private static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    private static final int MAX_FRAMESKIP = 10;
    
    private int fWidth,fHeight;
    private JFrame ventana;
    private JPanel panelTotal,panelPrueba;
    private float escala;

	private BufferedImage pantalla;

    private boolean pause=false;
    private boolean settingVel = false;
    private int gameUpdates = 0;
    private int frameUpdates = 0 ;
    long next_update;

    
    public Juego(int frameWidth, int frameHeight, Nivel nivel) throws JAXBException, IOException{

    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	int screenWidth = gd.getDisplayMode().getWidth();
    	int screenHeight = gd.getDisplayMode().getHeight();
    	
    	this.fWidth = screenWidth/2;
    	this.fHeight = fWidth*2/3; 
    	
    	nivel.save("src/logs/nivel.xml");
    	nivel.accTPlanet();
        nivel.intersecciones();
        System.out.println("Aceleraci�n en superficie: "+nivel.accAroundTargetPlanet(0,50));
    	
    	escala = fWidth/900f;
    	
    	super.setParticle(nivel.getParticle());
    	super.setPlaneta(nivel.getPlaneta());
    	super.setTarget(nivel.getTarget());
    	super.setiPlanetParticle(nivel.getiPlanetParticle());
    	super.setiPlanetTarget(nivel.getiPlanetTarget());
    	super.setArgPlanetParticle(nivel.getArgPlanetParticle());
    	super.setArgPlanetTarget(nivel.getArgPlanetTarget());
    	
    	
        this.setSize(fWidth, fHeight);
        this.pantalla = new BufferedImage(fWidth,fHeight, BufferedImage.TYPE_INT_RGB );
        JLabel label = new JLabel();
        label.setName("hello");
        label.setText("hello");
        label.setSize(20, 50);
        
        JPanel panelBar = new JPanel();
        panelBar.setBackground(Color.green);
        
        JPanel panelTotal = new JPanel();


        panelTotal.setBackground(Color.red);
        
        ventana=new JFrame();
        //ventana.setSize(fWidth + 6, fHeight + 28);
        ventana.setSize(fWidth, (int)(fHeight*1.08));
        //ventana.setSize(width, height);
        //ventana.setSize(this.getSize());
        ventana.setUndecorated(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //this.addMouseListener(this);
        ventana.addMouseListener(this);
        addMouseListener(this);
        //this.addMouseMotionListener(this);
        ventana.addMouseMotionListener(this);
        addMouseMotionListener(this);
        

        panelTotal.setLayout(null);
        ventana.add(panelTotal);
        panelTotal.add(this);
        
        panelTotal.add(panelBar);
        panelBar.setBounds(0, fHeight, fWidth, (int)(fHeight*0.08));
        panelBar.add(label);

        
        ventana.setVisible(true);

        paint(this.getGraphics());

        int loops;
        next_update = System.currentTimeMillis();
       
        getParticle().setTimesRecollided(MAX_COLLISIONS_COLLIDED);
        

        //bucle del juego
        while(true)
        {   
        	loops = 0;

        	if (getParticle().getTimesRecollided()>=MAX_COLLISIONS_COLLIDED){
    			trickyBug();
    			pause();
    		}
        	while ((System.currentTimeMillis() > next_update && loops < MAX_FRAMESKIP) && pause==false){
        		planetsInteraction(getParticle());
        		objetiveInteraction();

            	getParticle().updatePlanet(TIME_DELTA);
            	gameUpdates++;
            	//System.out.println("Game updates so far : "+ gameUpdates);
            	//var del bucle
        		next_update += SKIP_TICKS;
                loops++;
        	}
            
        	
        	displayGame();
        	
        }
        
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public int[][] probando(int x1,int y1, int x2, int y2){
		Graphics2D grafico2d = (Graphics2D) this.getGraphics();
		grafico2d.setColor(Color.red);
		paint(this.getGraphics());
		//paint(panelTotal.getGraphics());
    	//defino la recta con los puntos elegidos
    	double m = (float)(y2-y1)/(float)(x2-x1);
    	System.out.println(m);
    	double n = -m*x1+y1;
    	
    	m=-0.2857142984867096;
    	n=205.14286515116692;
    	
    	
    	//grafico2d.drawLine(x1, y1, x2, y2);
    	grafico2d.drawLine(0, (int)n, 900, (int)(900*m+n));
    	
    	//Compruebo donde cae cada planeta
    	int [][] separa = new int[2][];
    	ArrayList<Integer> sup= new ArrayList<Integer>();
    	ArrayList<Integer> inf= new ArrayList<Integer>();
    	for (int i=0;i<getPlaneta().length;i++){
    		if (getPlaneta()[i].getPos()[1] <= (m*getPlaneta()[i].getPos()[0]+n)){
    			//separa[0][nSup]=i;
    			sup.add(i);
    			System.out.println("Planeta "+i+" en caso A");
    			grafico2d.fillOval((int) getPlaneta()[i].getPos()[0], (int) getPlaneta()[i].getPos()[1], 20, 20);
    		}else{
    			inf.add(i);
    			System.out.println("Planeta "+i+" en caso B");
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
    	System.out.println("N caso A: "+ separa[0].length);
    	return separa;
    }
    

    public  double distanciaInterseccion(Graphics grafico, double[] p1, double[] p2, Planet p){
    	
		Graphics2D grafico2d = (Graphics2D) grafico;
		grafico2d.setColor(Color.red);
		
    	double m = (p2[1]-p1[1])/(p2[0]-p1[0]);
    	double n = (-m*p1[0]+p1[1]);
    	double a = p.getPos()[0];
    	double b = p.getPos()[1];
    	double R = p.getRadio();
    	double m2 = Math.pow(m, 2);
    	double n2 = Math.pow(n, 2);
    	double a2 = Math.pow(a, 2);
    	double b2 = Math.pow(b, 2);
    	double R2 = Math.pow(R, 2);
    	
    	double [] pprueba = new double[2];
    	pprueba[0]=800;
    	pprueba[1]=m*pprueba[0]+n;
    	grafico2d.drawLine((int)p1[0],(int) p1[1],(int)p2[0],(int) p2[1]);
    	//grafico2d.drawLine((int)p1[0],(int) p1[1],(int)pprueba[0],(int) pprueba[1]);

    	double discr = R2*m2+R2-a2*m2+2*a*b*m-2*a*m*n-b2+2*b*n-n2;
    	if (discr <= 0){
    		return 0;
    	}else{
    		double [] corte1 = new double[2];
    		double [] corte2 = new double[2];
    		corte1[0]=(- Math.sqrt(discr)/(m2+1)) + ((a+b*m-m*n)/(m2+1));
    		corte2[0]=( Math.sqrt(discr)/(m2+1)) + ((a+b*m-m*n)/(m2+1));
    		corte1[1]= m*corte1[0]+n;
    		corte2[1]= m*corte2[0]+n;
    		grafico2d.fillOval((int) corte1[0], (int) corte1[1], 5, 5);
    		grafico2d.fillOval((int) corte2[0], (int) corte2[1], 5, 5);
    		return Util.modulo(corte1, corte2);
    	}
    	
    }
    
    public void pause() throws FileNotFoundException, JAXBException{
    	pause = true;
    }
    public void resume(){
    	pause = false;
    }

    public void nextLevel() throws FileNotFoundException, JAXBException{
    	Nivel nivel = new Nivel("nivel1.xml");
    	super.setParticle(nivel.getParticle());
    	super.setPlaneta(nivel.getPlaneta());
    	super.setTarget(nivel.getTarget());
    	super.setiPlanetParticle(nivel.getiPlanetParticle());
    	super.setiPlanetTarget(nivel.getiPlanetTarget());
    	super.setArgPlanetParticle(nivel.getArgPlanetParticle());
    	super.setArgPlanetTarget(nivel.getArgPlanetTarget());
    	paint(this.getGraphics());
    	//paint(panelTotal.getGraphics());
    }
    public void displayGame(){
        if (pause == false){
        	paint(this.getGraphics());
        	//paint(panelTotal.getGraphics());
        	//paint(pantalla.getGraphics());
        	frameUpdates++;
        	//dibujacontraza(this.getGraphics());
        }
    }
    public int getFPS(){
    	int fps = 0;
    	if (gameUpdates != 0){
    		fps =TICKS_PER_SECOND* frameUpdates/gameUpdates;
    	}
    	return fps;
    }


    @Override
    public void paint (Graphics grafico){
    	Graphics2D grafico2d = (Graphics2D) grafico;
    	putBackGround(pantalla.getGraphics());
    	getParticle().putSprite(pantalla.getGraphics(),escala, (int) getParticle().getPos()[0], (int) getParticle().getPos()[1]);
    	getTarget().putSprite(pantalla.getGraphics(),escala, (int) getTarget().getPos()[0], (int) getTarget().getPos()[1]);

        for (int j=0;j<getPlaneta().length;j++){
            getPlaneta()[j].putSprite(pantalla.getGraphics(),escala, (int) getPlaneta()[j].getPos()[0], (int) getPlaneta()[j].getPos()[1]);
    	}
        
    	grafico2d.drawImage(pantalla, 0, 0, this);
    	//grafico2d.drawImage(pantalla, 0, 0, ventana);
    	
    }
    
    
    public void dibujacontraza(Graphics grafico)
    {
    	Graphics2D grafico2d = (Graphics2D) grafico;
    	getParticle().putSprite(pantalla.getGraphics(),escala, (int) getParticle().getPos()[0], (int) getParticle().getPos()[1]);
    	getTarget().putSprite(pantalla.getGraphics(),escala, (int) getTarget().getPos()[0], (int) getTarget().getPos()[1]);
    	grafico2d.drawImage(pantalla, 0, 0, this);
    	//grafico2d.drawImage(pantalla, 0, 0, ventana);
    }

    public void putBackGround(Graphics grafico)
    {
    	Graphics2D grafico2d = (Graphics2D) grafico;
        String ruta = "imagenes/space2.jpg";
    	grafico2d.drawImage(new ImageIcon(getClass().getResource(ruta)).getImage(), 0, 0, null);
    	//grafico2d.drawImage(new ImageIcon(getClass().getResource(ruta)).getImage(), 0, 0, ventana);
    }
    
 // La simulaci�n que se hace con el rat�n
    public void dibujaOraculo(Graphics grafico,int x1,int y1, int x2, int y2,Color colorRecta,Color colorTraza){
    	Graphics2D grafico2d = (Graphics2D) grafico;

        paint(grafico);
    	
        double [] newVel;
        newVel= Util.newVel(escala,getParticle(), x2, y2);
    	grafico2d.setColor(colorRecta);
    	
    	//Esto que hace asi??? Cambialo!!
        grafico2d.drawLine(x1, y1, x1+(int)((150/3)*newVel[0]),y1+(int)((150/3)*newVel[1]));
        //
        
        int nSimulaciones = 300;
        int [][] puntos= simula(nSimulaciones,newVel);
        for (int i=0;i<nSimulaciones-1;i++){
        	grafico2d.setColor(colorTraza);
        	grafico2d.drawLine(puntos[0][i], puntos[1][i], puntos[0][i+1],puntos[1][i+1]);
        }    
    }
    

    
    public int[][]  simula(int nVeces,double[] vel){
    	int [][] puntos= new int[2][nVeces];
    	//puntos=super.simula(nVeces, vel);
    	int it=0;
		long t0=System.currentTimeMillis();

		Planet ghostPlanet = getParticle().clone();
		ghostPlanet.setVel(vel);
		ghostPlanet.setTimesCollided(0);
		//while (it<nVeces && ghostPlanet.getTimesRecollided()< MAX_COLLISIONS_COLLIDED){
    	while (it<nVeces && ghostPlanet.getTimesRecollided()< 3 && ghostPlanet.getTimesCollided()<1){
 
    		planetsInteraction(ghostPlanet);
			ghostPlanet.updatePlanet();
			puntos[0][it]=(int)ghostPlanet.getPos()[0];
			puntos[1][it]=(int)ghostPlanet.getPos()[1];
    		it++;	
    	}
    	
    	//Completamos los puntos si fuera necesario
    	//for (int i=it;i<nVeces;i++){
    	for (int i=it;i<nVeces;i++){
			puntos[0][i]=(int)ghostPlanet.getPos()[0];
			puntos[1][i]=(int)ghostPlanet.getPos()[1];
    	}
    	System.out.println("Tiempo en simular " + nVeces + " iteraciones: " + String.valueOf(System.currentTimeMillis()- t0 + " milisegundos. Iteraciones reales: " + it));
    	System.out.println("Distancia camino: " + Util.pathDistance(puntos));
    	System.out.println("Golpes: "+ghostPlanet.getTimesCollided());
    	
    	
    	for (int i=0;i<2;i++){
    		for(int j=0;j<nVeces;j++){
    			puntos[i][j]*=escala;
    		}
    	}
    	return puntos;
    	
    }

    //RATON EVENTS

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//resume();
		//settingVel = false;
		System.out.println("fuera");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("collided" + getParticula().isCollided());
    	System.out.println("Particula juego:" + getParticle().getPos()[0]+","+getParticle().getPos()[1]);

		System.out.println("Pulsado: ("+e.getX()+", "+e.getY()+")");
		System.out.println("Pause: "+pause);
		// Cuando pulso el rat�n compruebo que estoy dentro de la zona de la part�cula Y que el juego est� parado
		if (getParticle().contains(escala,e.getX(), e.getY())&&pause==true){
			//pause();
			System.out.println("jp�a");
			settingVel=true;
			//Inicializo todo lo importante a CERO
			getParticle().setTimesRecollided(0);
			double [] cero = {0,0};
			getParticle().setVel(cero);
			getParticle().setAcelToCero();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

		if (settingVel == true){
			gameUpdates= 0 ;
			frameUpdates = 0;
			double [] newVel=Util.newVel(escala,getParticle(), e.getX(), e.getY());
			getParticle().setVel(newVel);
			System.out.println("El vector ser�a: ("+newVel[0]+", "+newVel[1]+")");
			resume();
			settingVel = false;
			next_update = System.currentTimeMillis();
		}
	
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
    	//System.out.println("Particula juego:" + getParticle().getPos()[0]+","+getParticle().getPos()[1]);

		if (settingVel==true){
			//escala
			//dibujaOraculo(this.getGraphics(), (int)particula.getPos()[0], (int)particula.getPos()[1], e.getX(), e.getY(),Color.blue, Color.red);
			System.out.println("Pos: ("+e.getX()+", "+e.getY()+")");
			dibujaOraculo(this.getGraphics(), (int)getParticle().getWindowPos(escala)[0], (int)getParticle().getWindowPos(escala)[1], e.getX(), e.getY(),Color.blue, Color.red);
			//dibujaOraculo(pantalla.getGraphics(), (int)getParticle().getWindowPos(escala)[0], (int)getParticle().getWindowPos(escala)[1], e.getX(), e.getY(),Color.blue, Color.red);

			//System.out.println("collided" + getParticula().isCollided()+" "+ (Util.distancia(getParticula(),getParticula().getCollidedPlanet())));
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		/*
    	Random ran = new Random();
    	int x1,y1,x2,y2;
    	x1 = ran.nextInt(900);
    	y1 = ran.nextInt(600);
    	do{
    		x2=ran.nextInt(900);
    	}while (x1==x2);
    	y2 = ran.nextInt(600);
		probando(x1,y1,x2,y2);
		*/
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseMoved(MouseEvent e){
		System.out.println("Movido: ("+e.getX()+", "+e.getY()+")");
	}


	/*
	public void setParticula(Planet particula) {
		this.particula = particula;
	}

	public Planet getParticula() {
		return particula;
	}
	*/
	
}





/*

        	if (System.currentTimeMillis()-tiempo>5 && settingVel==false && particula.getTimesRecollided()<MAX_COLLISIONS_COLLIDED) { // actualizamos cada tantos milisegundos y si no estamos definiendo vector velocidad con raton
            	
        		
            	for (int i=0;i<TIMEDELTA;i++){      		
            		for (int j=0;j<planeta.length;j++){
                		//primero miro si hay colision con cada planeta
                		if (particula.collided==false && (Util.distancia(particula,planeta[j])<0)){
                    		particula.collided=true;
                    		particula.setCollidedPlanet(planeta[j]);
                		}else{
                			if (particula.getCollidedPlanet() == planeta[j])
                				particula.collided=false; //si he vuelto a alejarme lo suficiente DEL ULTIMO PLANETA COLISIONADO, digo que la particula no est� colisionada
                		}
            			//a�adimos las aceleraciones de los planetas a la part�cula
            			particula.addAcelBy(planeta[j], TIMEDELTA);
            			
            		}
        			particula.updatePlanet(TIMEDELTA);
            	}
            	
            	nActualiz++;
            	System.out.println("Updates so far: "+ nActualiz);
            	
            	//si tard� mucho en pintar la �ltima vez, actualizo la posicion pero me salto un frame
            	//if (settingVel == false && System.currentTimeMillis()-tiempo<10){
                if (settingVel == false){
                	paint(this.getGraphics());
                	//dibujacontraza(this.getGraphics());
                }
            	
                tiempo=System.currentTimeMillis();
                System.out.println(System.currentTimeMillis()-tiempo2);
            	//System.out.println("Posici�n particula: " + planeta[0].getPos()[0] + ", " + planeta[0].getPos()[1]);
            	//System.out.println("modulo velocidad: " + planeta[0].getModvel());
            	//System.out.println("N�mero de colisiones tipo1: " + planeta[0].collisions);
            	//System.out.println("N�mero de colisiones tipo2: " + planeta[0].collisions2);
            	
            }

*/