package xml;


import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "xs")
public class NivelXML {
	@XmlElementWrapper(name = "planetList")
	@XmlElement(name = "planet")
	private ArrayList<PlanetXML> planetList;
	private int idParticle;
	private int idTarget;
	private double argParticle;
	private double argTarget;
	

	
	  public void setPlanetList(ArrayList<PlanetXML> bookList) {
		    this.planetList = bookList;
		  }

		  public ArrayList<PlanetXML> getPlanetsList() {
		    return planetList;
		  }

	public void setIdParticle(int idParticle) {
		this.idParticle = idParticle;
	}
	
	public int getIdParticle() {
		return idParticle;
	}
	public void setIdTarget(int idTarget) {
		this.idTarget = idTarget;
	}
	
	public int getIdTarget() {
		return idTarget;
	}
	public void setArgParticle(double argParticle) {
		this.argParticle = argParticle;
	}
	
	public double getArgParticle() {
		return argParticle;
	}
	
	public void setArgTarget(double argTarget) {
		this.argTarget = argTarget;
	}
	public double getArgTarget() {
		return argTarget;
	}


}
