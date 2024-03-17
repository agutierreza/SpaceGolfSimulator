package juego;
import general.Util;
import java.awt.Graphics;

import javax.swing.ImageIcon;



public class Planet implements Cloneable {
	
	//private float escala = (float) 900/900;
	
	protected int masa;
	private int radio;
	private double[] pos;
	private double[] vel;
	private double[] acel;
	private String sprite;
	private Planet collidedPlanet;
	private boolean collided = false;
	
	
	private static int iPlanetParticle;
	private static int iPlanetTarget;
	private static double argPlanetParticle;
	private static double argPlanetTarget;
	private int timesCollided=0;
	private int timesRecollided=0;
	
	
	public Planet (int masa, double[] pos, double[] vel, double acel[]){
		this.masa=masa;
		this.pos = pos;
		this.vel = vel;
		this.acel = acel;
		this.radio= ((int) (4*Math.sqrt(masa)));
		//this.radio = ((int)masa/4);
	}
	public Planet (){

	}
	
	public Planet (int masa){
		this.masa=masa;
		this.radio= ((int) (4*Math.sqrt(masa)));

	}
	public Planet (int masa, double[] pos){
		this.masa = masa;
		this.pos = pos;
	}
	
	public Planet(Planet otroPlaneta){
		double [] Pos = {otroPlaneta.getPos()[0],otroPlaneta.getPos()[1]};
		this.masa = otroPlaneta.getMasa();
		this.radio=otroPlaneta.getRadio();
		this.setPos(Pos) ;
		this.vel = otroPlaneta.vel;
		this.acel = otroPlaneta.acel;
		this.sprite=this.sprite;

		
		this.collided = otroPlaneta.collided;
		this.iPlanetParticle=otroPlaneta.iPlanetParticle;
		this.iPlanetTarget=otroPlaneta.iPlanetTarget;
		this.argPlanetParticle=otroPlaneta.argPlanetParticle;
		this.argPlanetTarget=otroPlaneta.argPlanetTarget;
		this.timesCollided=otroPlaneta.timesCollided;
		this.timesRecollided=otroPlaneta.timesRecollided;
	}
	
	@Override
	public Planet clone() {
		try
		{
			Planet clon = (Planet) super.clone();
			//si no pongo esto me copia la variable miembro Pos del clon al original!!
			double [] Pos = {clon.getPos()[0],clon.getPos()[1]};
			int masa = clon.getMasa();
			int radio = clon.getRadio();
			clon.setPos(Pos);
			clon.setMasa(masa);
			clon.setRadio(radio);
		return (Planet) clon;
		}
		catch(Exception e){ return null; }
	}
	
	public void setMasa(int masa) {
		this.masa = masa;
	}
	public void setMasaRadio(int masa) {
		this.masa = masa;
		this.radio = ((int) (4*Math.sqrt(masa)));
	}
	public int getMasa() {
		return masa;
	}
    public void setRadio(){
		double dMasa = (double)(this.getMasa());
		int radio = (int) (4*Math.sqrt(dMasa));
		this.setRadio(radio);
    }
	public void setRadio(int radio) {
		this.radio = radio;
	}
	public int getRadio() {
		return radio;
	}
	public void setPos(double[] pos) {
		this.pos = pos;
	}
	public double[] getPos() {
		return pos;
	}
	public void setVel(double [] vel) {
		this.vel = vel;
	}
	public double [] getVel() {
		return vel;
	}
	public double [] getAcc() {
		return acel;
	}
	public double getModvel(){
		return (double) (Math.pow(vel[0], 2) + Math.pow(vel[1], 2));
	}
	public void setAcelToCero(){
		this.acel[0]=0;
		this.acel[1]=0;
	}
	public void setAcel(double[] acel){
		this.acel = acel;
	}
	public void setTimesRecollided(int timesRecollided) {
		this.timesRecollided = timesRecollided;
	}
	public int getTimesRecollided() {
		return timesRecollided;
	}
	
	
	public void setTimesCollided(int timesCollided) {
		this.timesCollided = timesCollided;
	}
	public int getTimesCollided() {
		return timesCollided;
	}
	public void setOnTop(Planet p,double arg){

		double modulo = p.getRadio()+this.getRadio();
		double posX,posY;
		posX = (double) (modulo*Math.cos(arg));
		//Como tenemos el eje Y invertido y lo he respetado, tengo que poner esto para representar correctamente el ángulo
		posY = (double) (modulo*Math.sin(Math.PI+arg));
		
		this.pos[0]=p.pos[0]+posX;
		this.pos[1]=p.pos[1]+posY;
	}
	
	/**
	 * Distancia del planeta objeto al planeta parámetro p
	 * @param p
	 * @return distancia del centro de un planeta al otro
	 */
	public double distanceTo (Planet p){
		//modulo
		return Util.modulo(this.pos, p.pos);
		//return (double)  Math.sqrt(Math.pow(this.pos[0]-p.pos[0],2) + Math.pow(this.pos[1]-p.pos[1],2));
	}
	


	public void addAcelBy (Planet p, int TimeDelta){

		double cubeDistance= (double)  Math.pow(this.distanceTo(p), 3);
		this.acel[0]+=(p.pos[0]-pos[0])* p.masa / (cubeDistance*TimeDelta*TimeDelta);
		this.acel[1]+=(p.pos[1]-pos[1])* p.masa / (cubeDistance*TimeDelta*TimeDelta);
		
	}
	public void addAcelBy (Planet p){
		double cubeDistance= (double)  Math.pow(this.distanceTo(p), 3);
		this.acel[0]+=(p.pos[0]-pos[0])* p.masa / (cubeDistance);
		this.acel[1]+=(p.pos[1]-pos[1])* p.masa / (cubeDistance);
	}
	public void addAcelBy (Planet p, double d){

		double cubeDistance= (double)  Math.pow(d, 3);
		this.acel[0]+=(p.pos[0]-pos[0])* p.masa / (cubeDistance);
		this.acel[1]+=(p.pos[1]-pos[1])* p.masa / (cubeDistance);
	}
	
	private void updateVel (int TimeDelta){
		this.vel[0]+=this.acel[0]/TimeDelta;
		this.vel[1]+=this.acel[1]/TimeDelta;
	}
	private void updateVel (){
		this.vel[0]+=this.acel[0];
		this.vel[1]+=this.acel[1];
	}
	
	
	private void updatePos (int TimeDelta){
		
		if (this.isCollided() == true){
			//actualización en caso de colisión
			double[] P = {0,0};
			double[] Portog = {0,0};
			double[] Ux={0,0};
			double[] Uy={0,0};
			double modulo = collidedPlanet.distanceTo(this);
			double proyX,proyY;

			P[0]=collidedPlanet.pos[0]-this.pos[0];
			P[1]=collidedPlanet.pos[1]-this.pos[1];
			Portog[0]=-P[1];
			Portog[1]=P[0];
			proyX=(this.vel[0]*P[0]+this.vel[1]*P[1])/modulo;
			proyY=(this.vel[0]*Portog[0]+this.vel[1]*Portog[1])/modulo;
			
			Ux[0]=proyX*P[0]/modulo;
			Ux[1]=proyX*P[1]/modulo;
			
			Uy[0]=proyY*Portog[0]/modulo;
			Uy[1]=proyY*Portog[1]/modulo;
			
			this.vel[0]=(double) ((-Ux[0]+Uy[0])/1.2);
			this.vel[1]=(double) ((-Ux[1]+Uy[1])/1.2);
			
			pos[0]+=vel[0]/TimeDelta;
			pos[1]+=vel[1]/TimeDelta;
			
		} else{
			//actualización normal por la gravedad
			this.pos[0]+=this.vel[0]/TimeDelta;
			this.pos[1]+=this.vel[1]/TimeDelta;
		}
		//this.acel[0]=0;
		//this.acel[1]=0;
		
	}
	
	private void updatePos(){
		
		if (this.isCollided() == true){
			//actualización en caso de colisión
			double[] P = {0,0};
			double[] Portog = {0,0};
			double[] Ux={0,0};
			double[] Uy={0,0};
			double modulo = collidedPlanet.distanceTo(this);
			double proyX,proyY;

			P[0]=collidedPlanet.pos[0]-this.pos[0];
			P[1]=collidedPlanet.pos[1]-this.pos[1];
			Portog[0]=-P[1];
			Portog[1]=P[0];
			proyX=(this.vel[0]*P[0]+this.vel[1]*P[1])/modulo;
			proyY=(this.vel[0]*Portog[0]+this.vel[1]*Portog[1])/modulo;
			
			Ux[0]=proyX*P[0]/modulo;
			Ux[1]=proyX*P[1]/modulo;
			
			Uy[0]=proyY*Portog[0]/modulo;
			Uy[1]=proyY*Portog[1]/modulo;
			
			this.vel[0]=(double) ((-Ux[0]+Uy[0])/1.2);
			this.vel[1]=(double) ((-Ux[1]+Uy[1])/1.2);
			
			pos[0]+=vel[0];
			pos[1]+=vel[1];
			
		} else{
			//actualización normal por la gravedad
			this.pos[0]+=this.vel[0];
			this.pos[1]+=this.vel[1];
		}
		//this.acel[0]=0;
		//this.acel[1]=0;
		
	}
	
	public void updatePlanet (int TimeDelta){
		this.updateVel(TimeDelta*TimeDelta);
		this.updatePos(TimeDelta);
		this.acel[0]=0;
		this.acel[1]=0;
	}
	public void updatePlanet (){
		this.updateVel();
		this.updatePos();
		this.acel[0]=0;
		this.acel[1]=0;
	}	
	public void setCollidedPlanet(Planet collidedPlanet) {
		if (collidedPlanet != this.collidedPlanet){
			setTimesCollided(getTimesCollided() + 1);
			//System.out.println("Número de colisiones tipo1 en "+this.getSprite()+ ": "+timesCollided);
			}
		else {
			setTimesRecollided(getTimesRecollided() + 1);
			//System.out.println("Número de colisiones tipo2 en "+this.getSprite()+ ": "+timesRecollided);
		}

		this.collidedPlanet = collidedPlanet;
	}


	public Planet getCollidedPlanet() {
		return collidedPlanet;
	}
	public void setCollided(boolean collided) {
		this.collided = collided;
	}
	public boolean isCollided() {
		return collided;
	}
	
	public static void setiPlanetParticle(int iPlanetParticle) {
		Planet.iPlanetParticle = iPlanetParticle;
	}
	public static int getiPlanetParticle() {
		return iPlanetParticle;
	}
	public static void setiPlanetTarget(int iPlanetTarget) {
		Planet.iPlanetTarget = iPlanetTarget;
	}
	public static int getiPlanetTarget() {
		return iPlanetTarget;
	}
	public static void setArgPlanetParticle(double argPlanetParticle) {
		Planet.argPlanetParticle = argPlanetParticle;
	}
	public static double getArgPlanetParticle() {
		return argPlanetParticle;
	}
	public static void setArgPlanetTarget(double argPlanetTarget) {
		Planet.argPlanetTarget = argPlanetTarget;
	}
	public static double getArgPlanetTarget() {
		return argPlanetTarget;
	}
	

	public boolean esIgual(Planet p){
		if(this.pos[0]==p.pos[0]&&this.pos[1]==p.pos[1]&&this.masa==p.masa){
			return true;
		}else{
			return false;
		}
	}
    public boolean inPlanetas(Planet[] planetas){
    	boolean iguales = false;
    	int i=0;
    	while((iguales==false)&&i<planetas.length){
    		if (this.esIgual(planetas[i])){
    			iguales = true;
    		}
    		
    		i++;
    	}
    	return iguales;
    }
    
	
	/**
	 * 
	 * @param a: coordenada x
	 * @param b: coordenada y
	 * @return boolean: contiene el planeta objeto al punto (a,b)?
	 */
	public boolean contains(float escala,int a, int b){
		double modulo;
		//System.out.println("Posicion en ventana: ("+ getWindowPos(escala)[0]+", "+getWindowPos(escala)[1]+")");
		modulo =  (double) Math.sqrt(Math.pow(a-this.getWindowPos(escala)[0], 2) + Math.pow(b-this.getWindowPos(escala)[1], 2));
		
		if (modulo<this.radio){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	 * @param p
	 * @return double la velocidad de escape del planeta objeto desde la superficie del planeta parámetro p
	 */
	public double velEscapeFrom(Planet p){
		double velEscape;
		velEscape = (double) Math.sqrt(2*p.masa/this.distanceTo(p));
		
		return velEscape;
	}
	
	
	
	
	
	
	
	
	
	
	//Opciones gráficas
	
    public int getWidth() // Ancho del Sprite
    {
        //return new ImageIcon(getClass().getResource(sprite)).getImage().getWidth(null);
    	return (int) (2.388*this.getRadio());
    	
    }
 
    public int getHeight() // Alto del Sprite
    {
        //return new ImageIcon(getClass().getResource(sprite)).getImage().getHeight(null);
    	return (int) (2.388*this.getRadio()); 
    }
 
    
    
    public void setSprite(String nombre) // Asignamos el fichero imagen al Sprite
    {
        sprite=nombre;
    }
 
    public String getSprite() // Nos devuelve el fichero imagen del Sprite
    {
        return sprite;
    }
    

    
    public  double[] getWindowPos(float escala){
    	double[] windowPos = {0,0};
    	windowPos[0]=escala*getPos()[0];
    	windowPos[1]=escala*getPos()[1];
    	return windowPos;
    }

    public void putSprite(Graphics grafico,float escala, int coordenadaHorizontal,int coordenadaVertical) // Pegamos el Sprite en la pantalla
    {
    	int tamImg = (int)(escala*getHeight());
    	//grafico.drawImage(new ImageIcon(getClass().getResource(p.getSprite())).getImage(), (int)((coordenadaHorizontal-p.getWidth()/2)), (int)((coordenadaVertical-p.getHeight()/2)),tamImg,tamImg,null);

    	grafico.drawImage(new ImageIcon(getClass().getResource(sprite)).getImage(), (int)(escala*(coordenadaHorizontal-getWidth()/2)), (int)(escala*(coordenadaVertical-getHeight()/2)),tamImg,tamImg,null);
    	//grafico.drawImage(new ImageIcon(getClass().getResource(sprite)).getImage(), coordenadaHorizontal, coordenadaVertical,(int)(2.388*this.radio),(int)(2.388*this.radio), null);
    }







}
	

