package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import algoritmo.Citta;

public class PannelloSuperiore extends JPanel {

	private Finestra finestra;
	private JTextField numeroCenteriTextField;
	private JComboBox<String> datasetComboBox;
	
	private boolean disabilitaTemporaneamente;
	
	public PannelloSuperiore(Finestra fin){
		super();
		this.finestra = fin;
		this.disabilitaTemporaneamente = false;
		
		((FlowLayout) getLayout()).setVgap(18);
		
		add(new JLabel("Dataset:"));
		
		datasetComboBox = new JComboBox<>();
		datasetComboBox.setPreferredSize(new Dimension(150, datasetComboBox.getPreferredSize().height));
		caricaNomiDataset();
		datasetComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				if(disabilitaTemporaneamente)
					return;
					
				String nomeFileSelezionato = (String) datasetComboBox.getSelectedItem();
				
				try{
					InputStream fileInput = new FileInputStream(Starter.NOME_CARTELLA_INPUT + "/" + nomeFileSelezionato);
					JsonReader jsonReader = Json.createReader(fileInput);
					
					JsonArray jsonArray = jsonReader.readArray();
					
					ArrayList<Citta> listaCitta = new ArrayList<>();
					for(int i=0; i < jsonArray.size(); i++){
						JsonObject jsonObject = jsonArray.getJsonObject(i);
						
						double x = jsonObject.getJsonNumber("x").doubleValue();
						double y = jsonObject.getJsonNumber("y").doubleValue();
						
						listaCitta.add(new Citta(x, y));
					}
					
					finestra.setListaCitta(listaCitta);
					
					jsonReader.close();
					fileInput.close();
					System.out.println("Caricato le città dal dataset " + nomeFileSelezionato);
				}catch (Exception exception) {
					System.err.println("File non trovato!");
				}
			}
		});
		add(datasetComboBox);
			
		JLabel gap = new JLabel();
		gap.setPreferredSize(new Dimension(30, gap.getPreferredSize().height));
		add(gap);
		
		add(new JLabel("Numero di centri:"));
		
		numeroCenteriTextField = new JTextField();
		numeroCenteriTextField.setPreferredSize(new Dimension(150, numeroCenteriTextField.getPreferredSize().height));
		add(numeroCenteriTextField);
	}
	
	public void caricaNomiDataset(){
		disabilitaTemporaneamente = true;
		
		datasetComboBox.removeAllItems();
		
		File cartella = new File(Starter.NOME_CARTELLA_INPUT);
				
		for(File file: cartella.listFiles()){
			String nomeFile = file.getName();
			
			datasetComboBox.addItem(nomeFile);
		}		
		
		disabilitaTemporaneamente = false;
	}
	
	public void selezionaPrimoDataset(){
		datasetComboBox.setSelectedIndex(0);
	}
	
	public String getNumeroCentri(){
		return numeroCenteriTextField.getText();
	}
	
}
