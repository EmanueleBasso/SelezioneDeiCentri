package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import algoritmo.Centro;
import algoritmo.Citta;

public class PannelloDestra extends JPanel {

	private Finestra finestra;
	private JLabel raggioLabel;
	private JLabel iterazioneLabel;
	private JLabel ultimoCentroLabel;
	private JLabel distanzaLabel;
	private JButton datasetSalva;
	
	public PannelloDestra(Finestra fin){
		super();
		this.finestra = fin;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel pannelloTop = new JPanel();
		((FlowLayout) pannelloTop.getLayout()).setVgap(20);
		
		JButton calcolaButton = new JButton("Calcola");
		calcolaButton.setPreferredSize(new Dimension(130, calcolaButton.getPreferredSize().height));
		calcolaButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				finestra.calcola();
			}
		});
		pannelloTop.add(calcolaButton);
		
		JButton singolaIterazione = new JButton("Iterazione");
		singolaIterazione.setPreferredSize(new Dimension(130, singolaIterazione.getPreferredSize().height));
		singolaIterazione.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				finestra.calcolaSingolaIterazione();
			}
		});
		pannelloTop.add(singolaIterazione);
		
		JButton datasetLibero = new JButton("Dataset libero");
		datasetLibero.setPreferredSize(new Dimension(130, datasetLibero.getPreferredSize().height));
		datasetLibero.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				finestra.mostraDatasetLibero();
			}
		});
		pannelloTop.add(datasetLibero);
		
		datasetSalva = new JButton("Salva dataset");
		datasetSalva.setPreferredSize(new Dimension(130, datasetSalva.getPreferredSize().height));
		datasetSalva.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String nomeFile = JOptionPane.showInputDialog(null,"Inserisci il nome del file");
				
				if(null == nomeFile || nomeFile.isEmpty())
					return;
				else{
					try{
						OutputStream fileOutput = new FileOutputStream(Starter.NOME_CARTELLA_INPUT + "/" + nomeFile + ".json");
						JsonWriter jsonWriter = Json.createWriter(fileOutput);
						
						JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
						
						for(Citta citta: finestra.getListaCitta()){
							JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
							jsonObjectBuilder.add("x", citta.getX()).add("y", citta.getY());
							
							jsonArrayBuilder.add(jsonObjectBuilder.build());
						}
						
						jsonWriter.writeArray(jsonArrayBuilder.build());
						
						jsonWriter.close();
						fileOutput.close();
						System.out.println("File output salvato.");
						
						finestra.aggiornaDataset();
					}catch (Exception exception) {
						System.err.println("Problemi nel scrivere il file output!");
					}
				}
			}
		});
		pannelloTop.add(datasetSalva);
		
		JButton salvaButton = new JButton("Salva output");
		salvaButton.setPreferredSize(new Dimension(130, salvaButton.getPreferredSize().height));
		salvaButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {			
				try{
					OutputStream fileOutput = new FileOutputStream(Starter.NOME_FILE_OUTPUT);
					JsonWriter jsonWriter = Json.createWriter(fileOutput);
					
					JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
					
					for(Centro centro: finestra.getListaCentri()){
						JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
						jsonObjectBuilder.add("x", centro.getX()).add("y", centro.getY());
						
						jsonArrayBuilder.add(jsonObjectBuilder.build());
					}
					
					jsonWriter.writeArray(jsonArrayBuilder.build());
					
					jsonWriter.close();
					fileOutput.close();
					System.out.println("File output salvato.");
				}catch (Exception exception) {
					System.err.println("Problemi nel scrivere il file output!");
				}				
			}
		});
		pannelloTop.add(salvaButton);
		
		JPanel pannelloBottom = new JPanel();

		pannelloBottom.add(new JLabel("Raggio di copertura:"));
		
		raggioLabel = new JLabel("0");
		raggioLabel.setHorizontalAlignment(SwingConstants.CENTER);
		raggioLabel.setPreferredSize(new Dimension(130, raggioLabel.getPreferredSize().height));
		pannelloBottom.add(raggioLabel);
		
		pannelloBottom.add(new JLabel("Iterazione:"));
		
		iterazioneLabel = new JLabel("0");
		iterazioneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iterazioneLabel.setPreferredSize(new Dimension(130, iterazioneLabel.getPreferredSize().height));
		pannelloBottom.add(iterazioneLabel);
		
		pannelloBottom.add(new JLabel("Ultimo centro:"));
		
		ultimoCentroLabel = new JLabel("x: , y: ");
		ultimoCentroLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ultimoCentroLabel.setPreferredSize(new Dimension(130, ultimoCentroLabel.getPreferredSize().height));
		pannelloBottom.add(ultimoCentroLabel);
		
		pannelloBottom.add(new JLabel("Ultima distanza:"));
		
		distanzaLabel = new JLabel("0");
		distanzaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		distanzaLabel.setPreferredSize(new Dimension(130, distanzaLabel.getPreferredSize().height));
		pannelloBottom.add(distanzaLabel);
		
		add(pannelloTop);
		add(pannelloBottom);
	}
	
	public void setRaggioCopertura(double raggio){
		DecimalFormat df = new DecimalFormat("#.##");
		raggioLabel.setText(df.format(raggio));
	}
	
	public void setIterazione(int iterazione,int n){
		iterazioneLabel.setText("" + iterazione + "/" + n);
	}
	
	public void setUltimoCentro(Centro c){
		DecimalFormat df = new DecimalFormat("#.##");

		if(null == c)
			ultimoCentroLabel.setText("x: , y: ");
		else
			ultimoCentroLabel.setText("x: " + df.format(c.getX()) + ", y: " + df.format(c.getY()));
	}
	
	public void setDistanza(double distanza){
		DecimalFormat df = new DecimalFormat("#.##");
		distanzaLabel.setText(df.format(distanza));
	}
	
	public void abilitaBottoneSalvaDataset(){
		datasetSalva.setEnabled(true);
	}
	
	public void disabilitaBottoneSalvaDataset(){
		datasetSalva.setEnabled(false);
	}
}
