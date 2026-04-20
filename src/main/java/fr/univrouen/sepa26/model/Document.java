package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

// On déclare que c'est la racine, et on précise l'espace de nom défini dans le TP
@XmlRootElement(name = "Document", namespace = "http://univ.fr/sepa26")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {

    // Document contient un et un seul élément <CstmrDrctDbtInitn>
    @XmlElement(name = "CstmrDrctDbtInitn")

    private CustomerDirectDebitInitiation customerDirectDebitInitiation;

    public Document() {}

    public CustomerDirectDebitInitiation getCustomerDirectDebitInitiation() {
        return customerDirectDebitInitiation;
    }

    public void setCustomerDirectDebitInitiation(CustomerDirectDebitInitiation customerDirectDebitInitiation) {
        this.customerDirectDebitInitiation = customerDirectDebitInitiation;
    }
}