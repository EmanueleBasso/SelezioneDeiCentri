package algoritmo;

import java.util.ArrayList;
import java.util.Random;

public class ApproxCentriSenzaR {

	private ArrayList<Citta> listaCitta;
	private ArrayList<Centro> listaCentri;
	private int k;
	private double distanzaCentro;
	
	public ApproxCentriSenzaR(ArrayList<Citta> listaCitta, int k) {
		this.listaCitta = new ArrayList<>();
		for(Citta citta: listaCitta){
			this.listaCitta.add(new Citta(citta.getX(), citta.getY()));
		}
		
		this.k = k;
		this.distanzaCentro = 0.0;
	}
	
	public ApproxCentriSenzaR(ArrayList<Citta> listaCitta, ArrayList<Centro> listaCentri, int k) {
		this.listaCitta = listaCitta;
		
		this.listaCentri = listaCentri;
		
		this.k = k;
		this.distanzaCentro = 0.0f;
	}
	
	public ArrayList<Centro> esegui() {
		// ALGORITMO
		ArrayList<Centro> listaCentri = new ArrayList<>();
		
		if(k >= listaCitta.size()){
			for(Citta citta: listaCitta)
				listaCentri.add(new Centro(citta.getX(), citta.getY()));
		}else{
			Citta cittaScelta;
			
			Random random = new Random();
			int sceltaRandom = random.nextInt(listaCitta.size());
		
			cittaScelta = listaCitta.get(sceltaRandom);
						
			listaCentri.add(new Centro(cittaScelta.getX(), cittaScelta.getY()));
			aggiornaDistanze(cittaScelta);
			
			while(listaCentri.size() < k){
				cittaScelta = getCittaPiuDistante();
				
				listaCentri.add(new Centro(cittaScelta.getX(), cittaScelta.getY()));
				aggiornaDistanze(cittaScelta);
			}
		}
		
		return listaCentri;
	}
	
	public ArrayList<Centro> eseguiSingolaIterazione() {
		// ALGORITMO
		if(k >= listaCitta.size()){
			return null;
		}else if(k == 0){					
			Random random = new Random();
			int sceltaRandom = random.nextInt(listaCitta.size());
		
			Citta cittaScelta = listaCitta.get(sceltaRandom);
						
			listaCentri.add(new Centro(cittaScelta.getX(), cittaScelta.getY()));
			aggiornaDistanze(cittaScelta);
			
			return listaCentri;
		}else{
			Citta cittaScelta = getCittaPiuDistante();
			
			listaCentri.add(new Centro(cittaScelta.getX(), cittaScelta.getY()));
			aggiornaDistanze(cittaScelta);
			
			return listaCentri;
		}		
	}
	
	private void aggiornaDistanze(Citta cittaScelta){
		cittaScelta.setDistanza(0.0);
		
		for(Citta c: listaCitta){
			if(c.getDistanza() > 0.0){
				double nuovaDistanza = DistanzaEuclidea.calcolaDistanza(cittaScelta, c);

				if(nuovaDistanza < c.getDistanza()){
					c.setDistanza(nuovaDistanza);
				}
			}
		}
	}
	
	private Citta getCittaPiuDistante(){
		Citta cittaPiuDistante = null;
		double distanza = 0.0;
		
		for(Citta c: listaCitta){
			if(c.getDistanza() > 0.0){
				if(c.getDistanza() > distanza){
					cittaPiuDistante = c;
					distanza = c.getDistanza();
				}
			}
		}
		
		distanzaCentro = distanza;
		
		return cittaPiuDistante;
	}
	
	public double getDistanzaCentro(){
		return distanzaCentro;
	}
	
	public double getRaggioCopertura(){
		if(k >= listaCitta.size())
			return 0.0;
		
		double raggioCopertura = 0.0;
		
		for(Citta c: listaCitta){
			if(c.getDistanza() > 0.0){
				if(c.getDistanza() > raggioCopertura){
					raggioCopertura = c.getDistanza();
				}
			}
		}
		
		return raggioCopertura;
	}
	
	public static ArrayList<Luogo> centroPiuVicino(ArrayList<Citta> listaCitta, ArrayList<Centro> listaCentri){
		ArrayList<Luogo> listaCentroPiuVicino = new ArrayList<>();
		
		for(Citta citta: listaCitta){
			double distanza = Double.MAX_VALUE;
			Luogo centroPiuVicino = null;
			
			for(Centro centro: listaCentri){
				double nuovaDistanza = DistanzaEuclidea.calcolaDistanza(citta, centro);
				
				if(nuovaDistanza == 0){
					centroPiuVicino = new Luogo(centro.getX(), centro.getY());
					break;
				}else if(nuovaDistanza < distanza){
					distanza = nuovaDistanza;
					centroPiuVicino = new Luogo(centro.getX(), centro.getY());
				}
			}
			
			listaCentroPiuVicino.add(centroPiuVicino);
		}
		
		return listaCentroPiuVicino;
	}
	
}
