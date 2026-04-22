package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.model.*;
import fr.univrouen.sepa26.repository.DirectDebitTransactionRepository;
import fr.univrouen.sepa26.dto.ResumeTransactionDTO;
import fr.univrouen.sepa26.dto.ResumeTransactionDTO.TransactionSimple;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class GetController {

	@Autowired
	private DirectDebitTransactionRepository transactionRepository;

	// ====================================================================
	// I.3.1 - Format XML
	// ====================================================================
	@GetMapping(value = "/sepa26/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
	public ResumeTransactionDTO getResumeXml() {
		List<DirectDebitTransaction> last10 = transactionRepository.findTop10Transactions();
		List<TransactionSimple> listeSimplifiee = new ArrayList<>();

		for (DirectDebitTransaction tx : last10) {
			// Valeurs par défaut si les parents sont manquants
			String date = "Date inconnue";
			String somme = "Montant inconnu";
			// Sécurité : On vérifie que le Lot parent existe
			if (tx.getPaymentInformation() != null) {
				somme = tx.getPaymentInformation().getCtrlSum();


				// on vérifie que le Fichier grand-parent existe
				if (tx.getPaymentInformation().getGroupHeader() != null) {
					date = tx.getPaymentInformation().getGroupHeader().getCreDtTm();
				}
			}

			listeSimplifiee.add(new TransactionSimple(tx.getId(), date, tx.getPmtId(), somme));
		}

		return new ResumeTransactionDTO(listeSimplifiee);
	}

	// ====================================================================
	// I.3.2 - Format HTML
	// ====================================================================
	@GetMapping(value = "/sepa26/resume/html", produces = MediaType.TEXT_HTML_VALUE)
	public String getResumeHtml() {
		List<DirectDebitTransaction> last10 = transactionRepository.findTop10Transactions();

		StringBuilder html = new StringBuilder();


		html.append("<!DOCTYPE html><html lang='fr'><head><meta charset='UTF-8'><title>Résumé des transactions</title>")
				.append("<link rel='stylesheet' href='/css/style.css'>")
				.append("</head><body>")


				.append("<main><div class='container'>")
				.append("<h1>Liste des <span class='highlight'>10 dernières</span> transactions</h1>")


				.append("<table>")
				.append("<thead><tr><th>ID interne</th><th>Date (CreDtTm)</th><th>ID Transaction (PmtId)</th><th>Montant (CtrlSum)</th><th>Action</th></tr></thead>")
				.append("<tbody>");

		for (DirectDebitTransaction tx : last10) {
			String date = "Date inconnue";
			String somme = "Montant inconnu";

			if (tx.getPaymentInformation() != null) {
				somme = tx.getPaymentInformation().getCtrlSum();
				if (tx.getPaymentInformation().getGroupHeader() != null) {
					date = tx.getPaymentInformation().getGroupHeader().getCreDtTm();
				}
			}

			html.append("<tr>")
					.append("<td>").append(tx.getId()).append("</td>")
					.append("<td>").append(date).append("</td>")
					.append("<td>").append(tx.getPmtId()).append("</td>")
					.append("<td>").append(somme).append("</td>")
					.append("<td><a class='btn btn-primary' style='padding: 8px 15px; font-size: 13px;' href='/sepa26/html/").append(tx.getId()).append("'>Détails</a></td>")
					.append("</tr>");
		}

		html.append("</tbody></table>")
				.append("<div class='back-link'>")
				.append("<a href='/' class='btn btn-secondary btn-back'>")
				.append("<span class='btn-icon'>‹</span><span class='btn-label'>Retour à l'accueil</span>")
				.append("</a></div>")
				.append("</div></main></body></html>");

		return html.toString();
	}

	// ====================================================================
	//  Détail de la transaction au format XML
	// ====================================================================
	@GetMapping(value = "/sepa26/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> getTransactionXml(@PathVariable("id") Long id) {


		Optional<DirectDebitTransaction> txOpt = transactionRepository.findById(id);

		if (txOpt.isPresent()) {
			DirectDebitTransaction tx = txOpt.get();

			// on crée l'enveloppe conforme au XSD
			Document wrapper = new Document();

			CustomerDirectDebitInitiation sepa = new CustomerDirectDebitInitiation();

			// on récupère le Header original (ou on en crée un vide)
			if (tx.getPaymentInformation() != null && tx.getPaymentInformation().getGroupHeader() != null) {
				sepa.setGrpHdr(tx.getPaymentInformation().getGroupHeader());
			}else {
				// si la transaction est orpheline, on crée un faux Header pour satisfaire le XSD
				GroupHeader fauxHeader = new fr.univrouen.sepa26.model.GroupHeader();
				fauxHeader.setMsgId("ORPHELIN-" + tx.getId());
				fauxHeader.setCreDtTm("1970-01-01T00:00:00");
				fauxHeader.setNbOfTxs("1");
				fauxHeader.setCtrlSum("0.00");
				sepa.setGrpHdr(fauxHeader);
			}

			// on crée un lot de paiement (PmtInf) qui contiendra notre unique transaction
			PaymentInformation pmtInf = tx.getPaymentInformation();
			if (pmtInf == null) {
				// AU LIEU DE FAIRE JUSTE un new PaymentInformation() vide :
				// On utilise votre constructeur pour remplir PmtInfId et ReqdColltnDt !
				pmtInf = new PaymentInformation("LOT-ORPHELIN", "1970-01-01");
			}
			pmtInf.setDrctDbtTxInf(java.util.Collections.singletonList(tx));

			// on assemble
			sepa.setPmtInf(java.util.Collections.singletonList(pmtInf));
			wrapper.setCustomerDirectDebitInitiation(sepa);

			return ResponseEntity.ok(wrapper);
		} else {
			// Erreur : L'ID n'existe pas, on construit le XML d'erreur demandé
			String errorXml = "<Error>\n" +
					"    <id>" + id + "</id>\n" +
					"    <status>ERROR</status>\n" +
					"</Error>";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorXml);
		}
	}

	// ====================================================================
	// Détail de la transaction au format HTML (via XSLT)
	// ====================================================================
	@GetMapping(value = "/sepa26/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> getTransactionHtml(@PathVariable("id") Long id) {

		Optional<DirectDebitTransaction> txOpt = transactionRepository.findById(id);

		if (!txOpt.isPresent()) {
			// Erreur : L'ID n'existe pas, on renvoie du HTML simple
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction introuvable");
		}

		try {

			// 1. On transforme l'objet Java en texte XML brut (Marshalling)
			JAXBContext context = JAXBContext.newInstance(DirectDebitTransaction.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter xmlWriter = new StringWriter();
			marshaller.marshal(txOpt.get(), xmlWriter);
			String xmlContent = xmlWriter.toString();

			// 2. On charge le fichier XSLT depuis le dossier 'resources'
			InputStream xsltStream = getClass().getResourceAsStream("/sepa26.xslt");
			if (xsltStream == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Erreur : Le fichier sepa26.xslt est introuvable dans src/main/resources/");
			}

			// 3. On fusionne le XML et le XSLT pour générer le HTML
			Source xmlSource = new StreamSource(new StringReader(xmlContent));
			Source xsltSource = new StreamSource(xsltStream);

			//  forcer l'utilisation de Saxon au lieu du vieux Xalan etant donné qu'on veut xslt 2.0:
			TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
			Transformer transformer = factory.newTransformer(xsltSource);

			StringWriter htmlWriter = new StringWriter();
			transformer.transform(xmlSource, new StreamResult(htmlWriter));

			return ResponseEntity.ok(htmlWriter.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la transformation XSLT : " + e.getMessage());
		}
	}

	@GetMapping(value = "/sepa26/search", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> searchTransactions(
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "sum", required = false) Double sum) {

		try {
			// 1. Recherche en base avec nos paramètres (qui peuvent être null)
			List<DirectDebitTransaction> transactions = transactionRepository.searchTransactions(date, sum);

			StringBuilder xmlBuilder = new StringBuilder();
			xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			xmlBuilder.append("<Transactions>\n");

			if (transactions.isEmpty()) {
				xmlBuilder.append("  <status>NONE</status>\n");
			} else {
				for (DirectDebitTransaction tx : transactions) {
					xmlBuilder.append("  <Transaction>\n");
					xmlBuilder.append("    <id>").append(tx.getId()).append("</id>\n");

					String creDtTm = tx.getPaymentInformation() != null && tx.getPaymentInformation().getGroupHeader() != null
							? tx.getPaymentInformation().getGroupHeader().getCreDtTm() : "NC";
					xmlBuilder.append("    <CreDtTm>").append(creDtTm).append("</CreDtTm>\n");
					xmlBuilder.append("    <PmtId>").append(tx.getPmtId()).append("</PmtId>\n");

					String amount = tx.getInstdAmt() != null ? String.valueOf(tx.getInstdAmt().getValue()) : "0.0";
					xmlBuilder.append("    <CtrlSum>").append(amount).append("</CtrlSum>\n");

					xmlBuilder.append("  </Transaction>\n");
				}
			}
			xmlBuilder.append("</Transactions>");

			return ResponseEntity.ok(xmlBuilder.toString());

		} catch (Exception e) {
			return ResponseEntity.ok("<Response><status>ERROR</status><description>Erreur de recherche</description></Response>");
		}
	}
}