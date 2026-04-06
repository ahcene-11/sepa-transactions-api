package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.repository.GroupHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.sepa26.model.*;
import fr.univrouen.sepa26.repository.PaymentInformationRepository;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;

@RestController
public class PostController {

	@Autowired
	private GroupHeaderRepository headerRepository;

	// Remarque : On n'a même plus besoin d'injecter PaymentInformationRepository ici !

	@PostMapping(value = "/testpost", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public String postTest(@RequestBody String flux) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(flux);
			Document doc = (Document) unmarshaller.unmarshal(reader);

			// 1. On récupère le Header et la liste des lots depuis le XML
			GroupHeader header = doc.getCustomerDirectDebitInitiation().getGrpHdr();
			List<PaymentInformation> lots = doc.getCustomerDirectDebitInitiation().getPmtInfList();

			if (header != null && lots != null) {
				// 2. On attache la liste des lots au Header
				header.setPaymentInformationList(lots);

				for (PaymentInformation paymentInfo : lots) {
					// 3. On attache le Header à chaque lot (pour la clé étrangère)
					paymentInfo.setGroupHeader(header);

					// 4. On s'occupe des transactions (comme avant)
					if (paymentInfo.getTransactions() != null) {
						for (DirectDebitTransaction tx : paymentInfo.getTransactions()) {
							tx.setPaymentInformation(paymentInfo);
						}
					}
				}

				// 5. LE SAUVETAGE EN CASCADE !
				// En sauvegardant le Header, Spring sauvegarde les lots, qui sauvegardent les transactions !
				headerRepository.save(header);
			}

			return "<result><response>Succès : XML sauvegardé !</response></result>";

		} catch (Exception e) {
			e.printStackTrace();
			return "<result><response>Erreur lors du traitement : " + e.getMessage() + "</response></result>";
		}
	}
}