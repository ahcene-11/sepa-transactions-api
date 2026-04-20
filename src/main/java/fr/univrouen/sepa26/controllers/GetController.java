package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.model.DirectDebitTransaction;
import fr.univrouen.sepa26.repository.DirectDebitTransactionRepository;
import fr.univrouen.sepa26.dto.ResumeTransactionDTO;
import fr.univrouen.sepa26.dto.ResumeTransactionDTO.TransactionSimple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

			listeSimplifiee.add(new TransactionSimple(tx.getId_db(), date, tx.getPmtId(), somme));
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
					.append("<td>").append(tx.getId_db()).append("</td>")
					.append("<td>").append(date).append("</td>")
					.append("<td>").append(tx.getPmtId()).append("</td>")
					.append("<td>").append(somme).append("</td>")
					.append("<td><a class='btn btn-primary' style='padding: 8px 15px; font-size: 13px;' href='/sepa26/html/").append(tx.getId_db()).append("'>Détails</a></td>")
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
}