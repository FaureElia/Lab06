package it.polito.tdp.meteo.model;

import java.util.*;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class RicorsioneSequenza {
	
	
	
	//LIVELLO --> giorno che stiamo analizzando (parte da 1)
	//List sequenza--> sequenza ottimale finora 
	
	/**
	 * condizioni:
	 * il tecnico deve stare dai 3 ai 6 giorni in una citta
	 * tutte le citta devono essere visitate almeno una volta
	 * 
	 * 
	 * metodo sequenzaOttimale riceve:
	 * mappaConValoriCitta
	 * listaCitta
	 * sequenza trovata finora,
	 * livello,
	 * costo_migliore_trovato
	 * 
	 * 
	 */
	private List<Rilevamento> miglioreSoluzione=new ArrayList<Rilevamento>();
	private int migliorValore=-1;
	private int cont=0;
	
	
	
	public List<Rilevamento> trovaSequenza(int mese) {
		MeteoDAO meteoDAO=new MeteoDAO();
		
		
		List<Rilevamento> sequenzaParziale=new ArrayList<Rilevamento>();
		//stringa in cui ho tutte le citta
		List<Citta> listaCitta=meteoDAO.getAllCities(mese);
		int costo=0;
		for (Citta c: listaCitta) {
			Rilevamento r=c.cercaRilevamento(1);
			if(r!=null) {
				sequenzaParziale.add(c.cercaRilevamento(1));
				this.cont=1;
				c.rilevamentiFatti.add(c.cercaRilevamento(1));
				costo+=c.cercaRilevamento(1).getUmidita();
				c.increaseCounter();
				sequenzaOttimale(sequenzaParziale,1,listaCitta,costo);
				c.decreaseCounter();
				this.cont=0;
				c.rilevamentiFatti.remove(c.cercaRilevamento(1));
				costo-=c.cercaRilevamento(1).getUmidita();
				sequenzaParziale.remove(c.cercaRilevamento(1));
			}
		}
		
		return this.miglioreSoluzione;
		
		
		
	}
	
	public void sequenzaOttimale(List<Rilevamento> parziale,int livello, List<Citta> listaCitta,int costo) {
		if(livello==15) {
			if (costo<this.migliorValore || this.migliorValore==-1) {
				this.migliorValore=costo;
				this.miglioreSoluzione=new ArrayList<Rilevamento>(parziale);
				System.out.println("trovata soluzione migliore"+costo+parziale);
			}
			return;
		}
		
		Rilevamento ultimo=parziale.get(parziale.size()-1);// trovo ultimo rilevamento
		
		
		for (Citta citta:listaCitta) {
			if(citta.getNome().equals(ultimo.getLocalita())) {
				//caso in cui sto nella stessa citta:posso solo se ci sono stato meno di 6 giorni in totale
				if(citta.getCounter()<6) {
					Rilevamento riv=citta.cercaRilevamento(livello+1);
					if (riv!=null) {
							citta.increaseCounter();
							cont++;
							citta.rilevamentiFatti.add(citta.cercaRilevamento (livello+1));
							parziale.add(citta.cercaRilevamento(livello+1));
							costo+=citta.cercaRilevamento(livello+1).getUmidita();
							sequenzaOttimale(parziale,livello+1,listaCitta,costo);
							//backtracking
							citta.decreaseCounter();
							this.cont--;
							parziale.remove(citta.cercaRilevamento(livello+1));
							costo-=citta.cercaRilevamento(livello+1).getUmidita();
					}else {System.out.println("giorno non rilevato");}
				}
			}
			else{//caso in cui voglio provare a cambiare localita
				if(cont>=3 && citta.getCounter()<6) {
					Rilevamento riv=citta.cercaRilevamento(livello+1);
					if (riv!=null) {
					
						int contVecchio=cont;
						this.cont=1;
						citta.increaseCounter();
						citta.rilevamentiFatti.add(citta.cercaRilevamento(livello+1));
						parziale.add(citta.cercaRilevamento(livello+1));
						costo+=100+citta.cercaRilevamento(livello+1).getUmidita();
						sequenzaOttimale(parziale,livello+1,listaCitta,costo);
						this.cont=contVecchio;
						citta.decreaseCounter();
						parziale.remove(citta.cercaRilevamento(livello+1));
						costo-=(100+citta.cercaRilevamento(livello+1).getUmidita());
					}else {System.out.println("giorno non rilevato");}
					
				}
				
			}
			
			
		}
		
	}
	
	
	

}
