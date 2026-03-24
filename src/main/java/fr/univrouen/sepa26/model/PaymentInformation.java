package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentInformation {

    @XmlElement(name = "PmtInfId")
    private String pmtInfId;

    @XmlElement(name = "ReqdColltnDt")
    private String reqdColltnDt;

    // C'est ici qu'on stockerait les transactions (DrctDbtTxInf)
    @XmlElement(name = "DrctDbtTxInf")
    private List<DirectDebitTransaction> transactions;

    public PaymentInformation() {
        this.transactions = new ArrayList<>();
    }

    public PaymentInformation(String pmtInfId, String reqdColltnDt) {
        this.pmtInfId = pmtInfId;
        this.reqdColltnDt = reqdColltnDt;
        this.transactions = new ArrayList<>();
    }
    
    public void addTransaction(DirectDebitTransaction tx) {
        this.transactions.add(tx);
    }
}