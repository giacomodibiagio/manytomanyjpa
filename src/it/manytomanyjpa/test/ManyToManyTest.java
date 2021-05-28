package it.manytomanyjpa.test;

import java.util.Date;

import it.manytomanyjpa.dao.EntityManagerUtil;
import it.manytomanyjpa.model.Ruolo;
import it.manytomanyjpa.model.StatoUtente;
import it.manytomanyjpa.model.Utente;
import it.manytomanyjpa.service.MyServiceFactory;
import it.manytomanyjpa.service.RuoloService;
import it.manytomanyjpa.service.UtenteService;

public class ManyToManyTest {

	public static void main(String[] args) {
		UtenteService utenteServiceInstance = MyServiceFactory.getUtenteServiceInstance();
		RuoloService ruoloServiceInstance = MyServiceFactory.getRuoloServiceInstance();

		// ora passo alle operazioni CRUD
		try {

			// inizializzo i ruoli sul db
			initRuoli(ruoloServiceInstance);

			System.out.println("Elenca utenti: ");
			for (Utente utenteItem : utenteServiceInstance.listAll()) {
				System.out.println(utenteItem);
			}

			Utente utenteNuovo = new Utente("pippo.rossi", "xxx", "pippo", "rossi", new Date());
			utenteServiceInstance.inserisciNuovo(utenteNuovo);

			Ruolo ruoloDaDb = ruoloServiceInstance.caricaSingoloElemento(1L);

			Utente utenteDaDb = utenteServiceInstance.listAll().stream().findFirst().orElse(null);
			if (utenteDaDb != null) {
				utenteServiceInstance.aggiungiRuolo(utenteDaDb, ruoloDaDb);
			}

			// proviamo a passarlo nello stato ATTIVO
			Utente utenteDaDb2 = utenteServiceInstance.listAll().stream().findFirst().orElse(null);
			if (utenteDaDb2 != null) {
				System.out.println(
						"stato attuale dell'utente :" + utenteDaDb2.getUsername() + " " + utenteDaDb2.getStato());
				utenteDaDb2.setStato(StatoUtente.ATTIVO);
				utenteServiceInstance.aggiorna(utenteDaDb2);
				System.out.println(
						"stato nuovo dell'utente :" + utenteDaDb2.getUsername() + " " + utenteDaDb2.getStato());
			}
			
			System.out.println("######## TEST LIST #######");
			
			for(Ruolo ruoloItem: ruoloServiceInstance.listAll())
				System.out.println(ruoloItem);
			
			for(Utente utenteItem: utenteServiceInstance.listAll())
				System.out.println(utenteItem);
			
			
			System.out.println(ruoloDaDb);
			
			utenteDaDb.setCognome("nuovo cognome");
			utenteServiceInstance.aggiorna(utenteDaDb);
			
			ruoloDaDb.setDescrizione("aggiornamento descrizione");
			ruoloServiceInstance.aggiorna(ruoloDaDb);
			
			Ruolo nuovoRuolo = new Ruolo("mio ruolo","ROLE_MIO_USER");
			ruoloServiceInstance.inserisciNuovo(nuovoRuolo);
			

			
			ruoloServiceInstance.rimuovi(nuovoRuolo);
			
			System.out.println("##### TEST DELETE ########");
			//utenteServiceInstance.rimuovi(utenteDaDb);
			//ruoloServiceInstance.rimuovi(ruoloDaDb);
			
			utenteServiceInstance.disaccoppiaRuoloUtente(utenteDaDb, nuovoRuolo);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			EntityManagerUtil.shutdown();
		}

	}

	private static void initRuoli(RuoloService ruoloServiceInstance) throws Exception {
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN") == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Administrator", "ROLE_ADMIN"));
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", "ROLE_CLASSIC_USER") == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Classic User", "ROLE_CLASSIC_USER"));
		}
	}

}
