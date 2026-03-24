package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DirectDebitTransaction {

    @XmlElement(name = "PmtId")
    private String pmtId;

    public DirectDebitTransaction() {}

    public DirectDebitTransaction(String pmtId) {
        this.pmtId = pmtId;
    }
}