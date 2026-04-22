package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.GroupHeader;
import fr.univrouen.sepa26.model.PaymentInformation;
import fr.univrouen.sepa26.model.DirectDebitTransaction;
// Importez votre GroupHeaderRepository
import fr.univrouen.sepa26.repository.GroupHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;
import java.util.List;

@RestController
public class PostController {

	@Autowired
	private GroupHeaderRepository groupHeaderRepository;

	@PostMapping(value = "/sepa26/insert", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> insertTransaction(@RequestBody String xmlPayload) {
		try {
			// ==========================================
			// ÉTAPE 1 : LE BOUCLIER XSD
			// ==========================================
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Source schemaFile = new StreamSource(getClass().getResourceAsStream("/sepa26.xsd"));
			Schema schema = factory.newSchema(schemaFile);
			Validator validator = schema.newValidator();

			// Si le XML est invalide, ça plante ici et on saute au "catch (SAXException)" !
			validator.validate(new StreamSource(new StringReader(xmlPayload)));

			// ==========================================
			// ÉTAPE 2 : LA LECTURE DU XML (JAXB)
			// ==========================================
			JAXBContext context = JAXBContext.newInstance(Document.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Document doc = (Document) unmarshaller.unmarshal(new StringReader(xmlPayload));

			// ==========================================
			// ÉTAPE 3 : VÉRIFICATION DES DOUBLONS
			// ==========================================
			GroupHeader header = doc.getCustomerDirectDebitInitiation().getGrpHdr();
			String msgId = header.getMsgId();

			// On vérifie si ce fichier a déjà été inséré
			if (groupHeaderRepository.existsByMsgId(msgId)) {
				return ResponseEntity.ok(
						"<Response><status>ERROR</status><detail>Fichier en doublon : Le MsgId '" + msgId + "' existe déjà en base.</detail></Response>"
				);
			}

			// ==========================================
			// ÉTAPE 4 : RE-LIER LES PARENTS ET ENFANTS
			// ==========================================
			List<PaymentInformation> lots = doc.getCustomerDirectDebitInitiation().getPmtInfList();
			if (lots != null) {
				for (PaymentInformation lot : lots) {
					// On attache le Header au Lot
					lot.setGroupHeader(header);

					if (lot.getTransactions() != null) {
						for (DirectDebitTransaction tx : lot.getTransactions()) {
							// On attache le Lot à la Transaction
							tx.setPaymentInformation(lot);
						}
					}
				}
				// On donne la liste complète des lots au Header
				header.setPaymentInformationList(lots);
			}

			// ==========================================
			// ÉTAPE 5 : SAUVEGARDE EN CASCADE
			// ==========================================
			// Si votre GroupHeader a bien @OneToMany(cascade = CascadeType.ALL), ça va tout sauvegarder d'un coup !
			groupHeaderRepository.save(header);

			return ResponseEntity.ok("<Response><id>" + header.getId() + "</id><status>INSERTED</status></Response>");

		} catch (SAXException e) {
			// ERREUR XSD (Faux IBAN, balises dans le désordre, etc.)
			return ResponseEntity.ok(
					"<Response><status>ERROR</status><detail>Flux XML non valide par rapport au schéma : " + e.getMessage() + "</detail></Response>"
			);
		} catch (Exception e) {
			// ERREUR GÉNÉRALE (Problème BDD, etc.)
			return ResponseEntity.ok(
					"<Response><status>ERROR</status><detail>Erreur interne du serveur : " + e.getMessage() + "</detail></Response>"
			);
		}
	}
}