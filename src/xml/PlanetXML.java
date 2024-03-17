package xml;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "planet")
// If you want you can define the order in which the fields are written
// Optional
@XmlType(propOrder = { "posX", "posY", "masa"})
public class PlanetXML {

  private int id;
  private int posX;
  private int posY;
  private int masa;
  
  public PlanetXML(){
	  this.posX=0;
	  this.posY = 0;
	  this.masa=0;
  }
  
  //private int id;
  //private double arg;


  // If you like the variable name, e.g. "name", you can easily change this
  // name for your XML-Output:
  /*
  @XmlElement(name = "type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
  */
  @XmlAttribute
  public int getId() {
		return id;
	  }
			  
  public void setId(int id) {
	  this.id = id;
	  }
  public int getPosX() {
    return posX;
  }

  public void setPosX(int pos) {
    this.posX = pos;
  }
  
  public int getPosY() {
	return posY;
  }
  
  public void setPosY(int pos) {
    this.posY = pos;
  }
  public int getMasa() {
	return masa;
  }
	  
  public void setMasa(int masa) {
	this.masa = masa;
  }
  

  /*


  public void setArg(double arg) {
	this.arg = arg;
  }

  public double getArg() {
	return arg;
  }
  */



} 