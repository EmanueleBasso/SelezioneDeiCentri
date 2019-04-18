package algoritmo;

public class Citta extends Luogo {

	private double distanza;
	
	public Citta(double x, double y){
		super(x, y);
		distanza = Double.MAX_VALUE;
	}
	
	public double getDistanza(){
		return distanza;
	}
	
	public void setDistanza(double distanza){
		this.distanza = distanza;
	}
		
}
