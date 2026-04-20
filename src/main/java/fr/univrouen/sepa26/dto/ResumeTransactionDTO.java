package fr.univrouen.sepa26.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

// 1. L'objet principal qui va englober la liste
@XmlRootElement(name = "Resume")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResumeTransactionDTO {

    @XmlElement(name = "Transaction")
    private List<TransactionSimple> transactions;

    public ResumeTransactionDTO() {}
    public ResumeTransactionDTO(List<TransactionSimple> transactions) { this.transactions = transactions; }

    // ==========================================
    // 2. La classe interne qui représente 1 transaction résumée
    // ==========================================
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TransactionSimple {
        @XmlElement(name = "Id")
        private Long id;
        @XmlElement(name = "CreDtTm")
        private String creDtTm;
        @XmlElement(name = "PmtId")
        private String pmtId;
        @XmlElement(name = "CtrlSum")
        private String ctrlSum;

        public TransactionSimple(Long id, String creDtTm, String pmtId, String ctrlSum) {
            this.id = id;
            this.creDtTm = creDtTm;
            this.pmtId = pmtId;
            this.ctrlSum = ctrlSum;
        }
    }
}