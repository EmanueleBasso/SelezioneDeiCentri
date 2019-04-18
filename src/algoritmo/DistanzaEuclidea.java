package algoritmo;

public class DistanzaEuclidea {
	
	public static double calcolaDistanza(Luogo p1, Luogo p2){
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		
		double distX = Math.pow(Math.abs(x1 - x2), 2);
		double distY = Math.pow(Math.abs(y1 - y2), 2);
		
		return Math.sqrt(distX + distY);
	}
}
