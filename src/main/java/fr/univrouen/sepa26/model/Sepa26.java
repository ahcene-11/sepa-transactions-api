package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CstmrDrctDbtInitn")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sepa26 {

    // Élément obligatoire (Group Header)
    @XmlElement(name = "GrpHdr")
    private GroupHeader grpHdr;

    // Élément répétitif (Payment Information), d'où la List
    @XmlElement(name = "PmtInf")
    private List<PaymentInformation> pmtInfList;

    public Sepa26() {
        this.pmtInfList = new ArrayList<>();
    }

    public void setGrpHdr(GroupHeader grpHdr) {
        this.grpHdr = grpHdr;
    }

    public void addPmtInf(PaymentInformation pmtInf) {
        this.pmtInfList.add(pmtInf);
    }
}