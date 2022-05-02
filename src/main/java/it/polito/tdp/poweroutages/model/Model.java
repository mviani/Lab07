package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	
	private List<PowerOutages> poByNerc;
	private double oreMigliori;
    private int numeroMigliore;
    private List<PowerOutages> migliorSequenza;
    private int x;
    private int y;
	
	public Model() {
		podao = new PowerOutageDAO();
		poByNerc = new ArrayList<PowerOutages>();
		numeroMigliore = Integer.MIN_VALUE;
		migliorSequenza = new ArrayList<PowerOutages>();
		oreMigliori=0.0;
		x=0;
		y=0;
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public List<PowerOutages> getPowerOutagesByNerc(Nerc n) {
		return podao.getPowerOutagesByNerc(n);
	}
	

	public List<PowerOutages> getWorstCaseAnalysis(Nerc n, int anniMax, int oreMax) {
		x=anniMax;
		y=oreMax;
		poByNerc.clear();
		poByNerc = getPowerOutagesByNerc(n);
		List<PowerOutages> parziale = new ArrayList<PowerOutages>();
		analysisRicorsiva(parziale,0,0.0,0);
		return migliorSequenza;
	}
    
	public void analysisRicorsiva(List<PowerOutages> parziale, int livello,double oreAttuali,int numeroAttuale) {
		if(livello==poByNerc.size())
			return;
		
		if(oreAttuali>y)
			return;
		
		if(parziale.size()>1 && !checkAnni(parziale))
			return;
		else {
			if(numeroAttuale>numeroMigliore) {
				migliorSequenza = new ArrayList<PowerOutages>(parziale);
				oreMigliori = oreAttuali;
				numeroMigliore = numeroAttuale;
				
			}
			
		}
		
		parziale.add(poByNerc.get(livello));
		oreAttuali = oreAttuali + getOreModifica(poByNerc.get(livello));
		numeroAttuale = numeroAttuale + poByNerc.get(livello).getCustomerAffected();
		analysisRicorsiva(parziale,livello + 1,oreAttuali,numeroAttuale);
		parziale.remove(parziale.size()-1);
		oreAttuali = oreAttuali - getOreModifica(poByNerc.get(livello));
		numeroAttuale = numeroAttuale - poByNerc.get(livello).getCustomerAffected();
		analysisRicorsiva(parziale,livello + 1,oreAttuali,numeroAttuale);
		
	}	
	
    private boolean checkAnni(List<PowerOutages> parziale) {
    	LocalDateTime d1 = parziale.get(0).getDateEventBegan();
    	LocalDateTime d2 = parziale.get(parziale.size()-1).getDateEventBegan();
		double ns = Duration.between(d1, d2).toDays();
		double years = ns/365;
		if(years>x)
		return false;
		else return true;
	}

	public double getOreModifica(PowerOutages p) {
    	LocalDateTime d1 = p.getDateEventBegan();
		LocalDateTime d2 = p.getDateEventFinished();
		double ns = Duration.between(d1, d2).toMinutes();
		double hours = ns/60;
    	return hours;
    }
	
	public double getOreMigliori() {
		return oreMigliori;
	}

	public int getNumeroMigliore() {
		return numeroMigliore;
	}
	
}
