package it.manytomanyjpa.dao;

import it.manytomanyjpa.model.Ruolo;

public interface RuoloDAO extends IBaseDAO<Ruolo> {
	
	public Ruolo findByDescrizioneAndCodice(String descrizione, String codice) throws Exception;

}
