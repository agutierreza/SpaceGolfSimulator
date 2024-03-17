package general;
import juego.Juego;
import juego.Planet;

public class Util {
	/**
	 * 	Calcula (sin asignar) un vector de velocidad para planeta, con modulo máximo maxVel, y a partir de la posición del ratón
	 * A cambiar, ya que maxVel debería depender del tamaño de la pantalla...
	 * Cambialo para que el módulo máximo se defina a partir de la escala
	 * @param planeta
	 * @param maxVel 
	 * @param x
	 * @param y
	 * @return Vector velocidad bidimensional de doubles
	 */
	public static double [] newVel(float escala, Planet p,int x, int y){
		double [] newVel = {0,0};
		double modulo;
		//cambiado 08/06/2013
		float maxVel=3f;

		//escala 
		
		//Importante que el punto inicial del vector de velocidad sea la posición del planeta EN LA VENTANA
		newVel[0] = x - p.getWindowPos(escala)[0];
		newVel[1] = y - p.getWindowPos(escala)[1];
		
		modulo = (double) (Math.sqrt(Math.pow(newVel[0],2) + Math.pow(newVel[1],2)));
		
		if (modulo>=150){
			newVel[0]= maxVel * newVel[0]/modulo;
			newVel[1]= maxVel * newVel[1]/modulo;
		}else{
			double escalar;
			 
			escalar = maxVel/150;
			newVel[0] = escalar * newVel[0];
			newVel[1] = escalar * newVel[1];
		}
		
		
		return newVel;
	}

    public static double distanciaInterseccion(double[] p1, double[] p2, Planet p){
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
    	
    	double discr = R2*m2+R2-a2*m2+2*a*b*m-2*a*m*n-b2+2*b*n-n2;
    	if (discr <= 0){
    		return 0;
    	}else{
    		double [] corte1 = new double[2];
    		double [] corte2 = new double[2];
    		corte1[0]=(-Math.sqrt(discr)/(m2+1)) + ((a+b*m-m*n)/(m2+1));
    		corte2[0]=( Math.sqrt(discr)/(m2+1)) + ((a+b*m-m*n)/(m2+1));
    		corte1[1]= m*corte1[0]+n;
    		corte2[1]= m*corte2[0]+n;
    		
    		return Util.modulo(corte1, corte2);
    	}
    	
    }

    public static int getIndexMin(double[] vector){
    	double valorMin = vector[0];
    	int index = 0;
    	for(int i=1;i<vector.length;i++){
    		if (vector[i]<valorMin){
    			valorMin=vector[i];
    			index=i;
    		}
    	}
    	return index;
    }
    
    
	/**
	 * 
	 * @param planeta1
	 * @param planeta2
	 * @return Devuelve un double con la mínima distancia de los planetas parámetro, considerandolos como círculos.
	 */
	public static double distancia(Planet planeta1,Planet planeta2){
		double distancia;
		distancia = planeta1.distanceTo(planeta2)-planeta1.getRadio()-planeta2.getRadio();
		return distancia;
		
	}
	
	public static double modulo(double[]punto){
		double parcial,total = 0;
		for (int i=0;i<punto.length;i++){
			parcial = Math.pow(punto[i], 2);
			total += parcial;
		}
		return Math.sqrt(total);
	}
	public static double modulo(double[] p1, double[] p2){
		if (p1.length == p2.length){
			double parcial,total = 0;
			for (int i=0;i<p1.length;i++){
				parcial = Math.pow(p1[i]-p2[i], 2);
				total += parcial;
			}
			return Math.sqrt(total);
		}else{
			return 0;	
		}
	}
	public static double modulo(int[] p1, int[] p2){
		if (p1.length == p2.length){
			double parcial,total = 0;
			for (int i=0;i<p1.length;i++){
				parcial = Math.pow(p1[i]-p2[i], 2);
				total += parcial;
			}
			return Math.sqrt(total);
		}else{
			return 0;	
		}
	}
	
	/*
	 * Podría estar más generalizado pero para lo que lo necesito está estupendo
	 */
	public static double pathDistance (int [][] camino){
		double distancia = 0;
		for (int i=0;i<camino[0].length-1;i++){
			double [] p1 = new double[2];
			double [] p2 = new double[2];
			//double[] p1 = {0,0},p2={0,0};
			p1[0]=camino[0][i];p1[1]=camino[1][i];
			p2[0]=camino[0][i+1];p2[1]=camino[1][i+1];
			distancia+=Util.modulo(p1, p2);
		}
		return distancia;
	}
	
	public static double [] setOn(Planet p,double modulo,double arg){

		double [] newPos = {0,0};
		newPos[0] = (double) (modulo*Math.cos(arg))+p.getPos()[0];
		newPos[1] = (double) (modulo*Math.sin(Math.PI+arg))+p.getPos()[1];
		return newPos;
	}
	
	public static double [] acelBy (Planet p, double [] pos){
		
		double d = Math.sqrt(Math.pow(pos[0]-p.getPos()[0],2) + Math.pow(pos[1]-p.getPos()[1],2));
		double cubeDistance= (double)  Math.pow(d, 3);
		double[] acel={0,0};
		acel[0]=(p.getPos()[0]-pos[0])* p.getMasa() / (cubeDistance);
		acel[1]=(p.getPos()[1]-pos[1])* p.getMasa() / (cubeDistance);
		return acel;
	}
}
