package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import algoritmo.ApproxCentriSenzaR;
import algoritmo.Centro;
import algoritmo.Citta;
import algoritmo.Luogo;

public class Finestra extends JFrame {

	private PannelloSuperiore pannelloSuperiore;
	private JPanel pannelloCentrale;
	private PannelloDestra pannelloDestra;
	
	private XYChart grafico;
	
	private ArrayList<Citta> listaCitta;
	private int k;
	private ArrayList<Centro> listaCentri;
	
	private double maxX;
	private double maxY;
	
	public Finestra(){
		super();
		listaCitta = new ArrayList<>();
		k = 0;
		listaCentri = new ArrayList<>();
		
		creaLayout();
			
		setTitle("Problema della selezione dei centri");
		setSize(new Dimension(700, 600));
		setLocationRelativeTo(null);
	}
	
	private void creaLayout(){					
		grafico = new XYChart(new XYChartBuilder());
		grafico.getStyler().setPlotGridLinesVisible(false);
	    grafico.getStyler().setLegendVisible(false); 
		grafico.getStyler().setXAxisMin(0.0);
		grafico.getStyler().setYAxisMin(0.0);
				
	    pannelloCentrale = new XChartPanel<>(grafico);
		add(pannelloCentrale, BorderLayout.CENTER);	
		
		pannelloSuperiore = new PannelloSuperiore(this);
		pannelloSuperiore.setPreferredSize(new Dimension(pannelloSuperiore.getPreferredSize().width, 60));
		add(pannelloSuperiore, BorderLayout.NORTH);
		
		pannelloDestra = new PannelloDestra(this);
		pannelloDestra.setPreferredSize(new Dimension(160,pannelloSuperiore.getPreferredSize().height));
		add(pannelloDestra, BorderLayout.EAST);
		
		pannelloSuperiore.selezionaPrimoDataset();
	}
			
	public void mostra(){
		setVisible(true);
	}
	
	public void setListaCitta(ArrayList<Citta> listaCitta){
		grafico.removeSeries("Citta");
		rimuoviCentri();
		
		this.listaCitta = listaCitta;
		k = 0;
		this.listaCentri = null;
				
		double[] xData = new double[listaCitta.size()];
	    double[] yData = new double[listaCitta.size()];
		for(int i = 0; i < listaCitta.size(); i++){
			Citta c = listaCitta.get(i);
			
			xData[i] = c.getX();
			yData[i] = c.getY();
		}
				
		XYSeries serie = grafico.addSeries("Citta", xData, yData);
		serie.setLineStyle(SeriesLines.NONE);
				
		pannelloDestra.setRaggioCopertura(0);
		pannelloDestra.setIterazione(k, listaCitta.size());
		pannelloDestra.setUltimoCentro(null);
		pannelloDestra.setDistanza(0);
		
		double maxValoreX = 0.0, maxValoreY = 0.0;
		for(int i = 0; i < listaCitta.size(); i++){
			if(listaCitta.get(i).getX() > maxValoreX)
				maxValoreX = listaCitta.get(i).getX();
			if(listaCitta.get(i).getY() > maxValoreY)
				maxValoreY = listaCitta.get(i).getY();
		}
		
		grafico.getStyler().setXAxisMax(maxValoreX);
		grafico.getStyler().setYAxisMax(maxValoreY);
		
		if(pannelloCentrale.getMouseListeners().length == 2)
			pannelloCentrale.removeMouseListener(pannelloCentrale.getMouseListeners()[1]);
		
		pannelloDestra.disabilitaBottoneSalvaDataset();
		
		pannelloCentrale.repaint();
	}
	
	public void mostraDatasetLibero(){
		grafico.removeSeries("Citta");
		rimuoviCentri();
		
		this.listaCitta = new ArrayList<>();
		k = 0;
		this.listaCentri = null;
		
		JTextField xField = new JTextField(5);
		JTextField yField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("X:"));
		myPanel.add(xField);
		myPanel.add(Box.createHorizontalStrut(15));
		myPanel.add(new JLabel("Y:"));
		myPanel.add(yField);

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Inserisci il range dei valori", JOptionPane.CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {			
			try{
				double x = Double.parseDouble(xField.getText());
				double y = Double.parseDouble(yField.getText());
				
				maxX = x;
				maxY = y;
				
				grafico.getStyler().setXAxisMax(x);
				grafico.getStyler().setYAxisMax(y);
			}catch (NumberFormatException e) {
				maxX = 1.0;
				maxY = 1.0;
				
				grafico.getStyler().setXAxisMax(1.0);
				grafico.getStyler().setYAxisMax(1.0);
			}
		}
					
		pannelloCentrale.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {						
				int x = e.getX();
			    int y = e.getY();
			    					    
			    if(x > 65 && y > 25 && x < (grafico.getWidth() - 50) && y < (grafico.getHeight() - 60)){   		
			    	x = x - 65;
			    	y = y - 25;
			    	
			    	double x1 = ((x) * maxX) / (grafico.getWidth() - 104);
			    	double y1 = ((grafico.getHeight() - 75 - y) * maxY) / (grafico.getHeight() - 75);
			    	
			    	listaCitta.add(new Citta(x1,y1));
			    	
			    	grafico.removeSeries("Citta");
												
					double[] xData = new double[listaCitta.size()];
				    double[] yData = new double[listaCitta.size()];
					for(int i = 0; i < listaCitta.size(); i++){
						Citta c = listaCitta.get(i);
						
						xData[i] = c.getX();
						yData[i] = c.getY();
					}
					
					XYSeries serie = grafico.addSeries("Citta", xData, yData);
					serie.setLineStyle(SeriesLines.NONE);
										
					pannelloCentrale.repaint();
			    }	    
			}
		});
		
		pannelloDestra.setRaggioCopertura(0);
		pannelloDestra.setIterazione(k, 0);
		pannelloDestra.setUltimoCentro(null);
		pannelloDestra.setDistanza(0);
		
		pannelloDestra.abilitaBottoneSalvaDataset();
		
		pannelloCentrale.repaint();
	}
	
	public ArrayList<Centro> getListaCentri(){
		return listaCentri;
	}
	
	public ArrayList<Citta> getListaCitta(){
		return listaCitta;
	}
	
	public void calcola(){
		try{
			k = Integer.parseInt(pannelloSuperiore.getNumeroCentri());
			
			if(k <= 0)
				throw new NumberFormatException();			
		}catch (NumberFormatException e){
			System.out.println("Non è stato inserito il numero di centri o è errato.");
			return;
		}
		
		if(listaCitta.size() == 0){
			System.out.println("Non è stato selezionato un dataset.");
			return;
		}
		
		ApproxCentriSenzaR approxCentriSenzaR = new ApproxCentriSenzaR(listaCitta, k);
		listaCentri = approxCentriSenzaR.esegui();
		
		mostraCentri();
						
		pannelloDestra.setRaggioCopertura(approxCentriSenzaR.getRaggioCopertura());
		pannelloDestra.setIterazione(k,listaCitta.size());
		pannelloDestra.setUltimoCentro(listaCentri.get(listaCentri.size() - 1));
		pannelloDestra.setDistanza(approxCentriSenzaR.getDistanzaCentro());
		
		k = 0;
		
		if(pannelloCentrale.getMouseListeners().length == 2)
			pannelloCentrale.removeMouseListener(pannelloCentrale.getMouseListeners()[1]);
	}
	
	public void calcolaSingolaIterazione(){
		int iterazioneMassima;
		
		try{
			iterazioneMassima = Integer.parseInt(pannelloSuperiore.getNumeroCentri());
			
			if(iterazioneMassima <= 0)
				throw new NumberFormatException();			
		}catch (NumberFormatException e){
			System.out.println("Non è stato inserito il numero di centri o è errato.");
			return;
		}
		
		if(k >= iterazioneMassima){
			System.out.println("Raggiunta l'iterazione massima!");
			return;
		}
			
		if(k == 0){
			ArrayList<Citta> listaCittaNuove = new ArrayList<>();
			for(Citta c: listaCitta)
				listaCittaNuove.add(new Citta(c.getX(), c.getY()));
			listaCitta = listaCittaNuove;
						
			listaCentri = new ArrayList<>();
		}
		
		ApproxCentriSenzaR approxCentriSenzaR = new ApproxCentriSenzaR(listaCitta, listaCentri, k);
		
		try{
			listaCentri = approxCentriSenzaR.eseguiSingolaIterazione();
		}catch (NullPointerException e) {
			return;
		}
		
		if(null == listaCentri)
			return;
		
		mostraCentri();	
		
		pannelloDestra.setRaggioCopertura(approxCentriSenzaR.getRaggioCopertura());
		pannelloDestra.setIterazione(k + 1, listaCitta.size());
		pannelloDestra.setUltimoCentro(listaCentri.get(listaCentri.size() - 1));
		pannelloDestra.setDistanza(approxCentriSenzaR.getDistanzaCentro());
				
		k++;
		
		if(pannelloCentrale.getMouseListeners().length == 2)
			pannelloCentrale.removeMouseListener(pannelloCentrale.getMouseListeners()[1]);
	}
	
	private void mostraCentri(){
		rimuoviCentri();
		
		if(listaCentri.size() > 1){
			double[] xData = new double[listaCentri.size() - 1];
		    double[] yData = new double[listaCentri.size() - 1];
			for(int i = 0; i < (listaCentri.size() - 1); i++){
				Centro c = listaCentri.get(i);
				
				xData[i] = c.getX();
				yData[i] = c.getY();
			}
					
			XYSeries serie = grafico.addSeries("Centri", xData, yData);
			serie.setLineStyle(SeriesLines.NONE);
			serie.setMarkerColor(Color.GREEN);
		}
		
		double[] xData = new double[1];
		double[] yData = new double[1];
			
		xData[0] = listaCentri.get(listaCentri.size() - 1).getX();
		yData[0] = listaCentri.get(listaCentri.size() - 1).getY();

		XYSeries serie = grafico.addSeries("UltimoCentro", xData, yData);
		serie.setLineStyle(SeriesLines.NONE);
		serie.setMarkerColor(Color.RED);
				
		mostraCentriPiuVicini();
				
		pannelloCentrale.repaint();
	}
	
	private void rimuoviCentri(){
		grafico.removeSeries("Centri");
		grafico.removeSeries("UltimoCentro");

		for(int i = 0; i < listaCitta.size(); i++)
			grafico.removeSeries("Linea" + i);
	}

	private void mostraCentriPiuVicini(){
		ArrayList<Luogo> listaCentroPiuVicino = ApproxCentriSenzaR.centroPiuVicino(listaCitta, listaCentri);
				
		for(int i = 0; i < listaCentroPiuVicino.size(); i++){
			double[] xData = new double[2];
		    double[] yData = new double[2];
			
		    xData[0] = listaCitta.get(i).getX();
		    yData[0] = listaCitta.get(i).getY();
		    xData[1] = listaCentroPiuVicino.get(i).getX();
		    yData[1] = listaCentroPiuVicino.get(i).getY();
		    
			XYSeries serie = grafico.addSeries("Linea" + i, xData, yData);
			
			serie.setMarker(SeriesMarkers.NONE);
			serie.setLineWidth(0.1f);
			serie.setLineColor(Color.DARK_GRAY);
		}
	}
	
	public void aggiornaDataset(){
		pannelloSuperiore.caricaNomiDataset();
	}
	
}
