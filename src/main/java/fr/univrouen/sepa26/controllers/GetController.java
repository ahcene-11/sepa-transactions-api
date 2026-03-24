package fr.univrouen.sepa26.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetController {
	@GetMapping("/resume")
	public String getListSepa26InXML() {
		return "Envoi de la liste des flux SEPA enregistrés";
	}
	
	@GetMapping("/guid")
	public String getSepa26InXML(@RequestParam(value = "guid") String texte) {
		return ("Détail de la transaction SEPA demandée" +texte );
	}
	
	@GetMapping("/test")
	public String getTestSepa26InXML(@RequestParam(value = "nb") String nombre, @RequestParam(value = "search") String search) {
		return ("Test : <br/> guid = " + nombre + "<br/>" +"titre = "+search);
	}
	
	@org.springframework.web.bind.annotation.RequestMapping(value = "/xml", produces = org.springframework.http.MediaType.APPLICATION_XML_VALUE)
    @org.springframework.web.bind.annotation.ResponseBody
    public fr.univrouen.sepa26.model.Sepa26 getXML() {
        
        // 1. Création de la racine
        fr.univrouen.sepa26.model.Sepa26 sepa = new fr.univrouen.sepa26.model.Sepa26();
        
        // 2. Création et ajout de l'en-tête (GroupHeader)
        fr.univrouen.sepa26.model.GroupHeader header = new fr.univrouen.sepa26.model.GroupHeader("MSG-2026", "2026-03-24T14:26:41", "1", "1100.07");
        sepa.setGrpHdr(header);
        
        // 3. Création et ajout d'un lot de paiement (PaymentInformation)
        fr.univrouen.sepa26.model.PaymentInformation pmtInf = new fr.univrouen.sepa26.model.PaymentInformation("REM-001", "2026-04-01");
        
        // 4. Création et ajout d'une transaction dans ce lot
        fr.univrouen.sepa26.model.DirectDebitTransaction tx = new fr.univrouen.sepa26.model.DirectDebitTransaction("TX-999");
        pmtInf.addTransaction(tx);
        
        sepa.addPmtInf(pmtInf);
        
        return sepa;
    }
}