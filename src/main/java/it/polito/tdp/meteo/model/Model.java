package it.polito.tdp.meteo.model;

import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	MeteoDAO meteoDAO;

	public Model() {
		meteoDAO=new MeteoDAO();

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		Map<String,Double> risultato=meteoDAO.getMEdiaUmidita(mese);
		String ritorno="";
		for (String loc: risultato.keySet()) {
			ritorno+="\n"+loc+": "+risultato.get(loc);
			
		}
		return ritorno;
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
		RicorsioneSequenza ric=new RicorsioneSequenza();
		List<Rilevamento> soluzione=ric.trovaSequenza(mese);
		String sequenza="";
		int costo=0;
		for (int i=0;i<soluzione.size();i++) {
			sequenza+=(i+1)+") "+soluzione.get(i).getLocalita()+":"+soluzione.get(i).getUmidita()+"\n";
			if (i==0) {
				costo+=soluzione.get(i).getUmidita();
			}
			else if(soluzione.get(i).getLocalita().equals(soluzione.get(i-1).getLocalita())) {
				costo+=soluzione.get(i).getUmidita();
			}
			else if(!soluzione.get(i).getLocalita().equals(soluzione.get(i-1).getLocalita())) {
				costo+=100+soluzione.get(i).getUmidita();
			}
			
			
		}
		
		sequenza+="costo totale: "+costo;
		return sequenza;
	}
	

}
