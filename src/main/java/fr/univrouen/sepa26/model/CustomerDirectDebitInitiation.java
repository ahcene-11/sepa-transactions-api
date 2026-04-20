package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerDirectDebitInitiation {

    @XmlElement(name = "GrpHdr")
    private GroupHeader grpHdr;

    @XmlElement(name = "PmtInf")
    private List<PaymentInformation> pmtInfList;

    public CustomerDirectDebitInitiation() {
        this.pmtInfList = new ArrayList<>();
    }

    public GroupHeader getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GroupHeader grpHdr) {
        this.grpHdr = grpHdr;
    }

    public void setPmtInf(List<PaymentInformation> pmtInf) {
        this.pmtInfList = pmtInf;
    }

    public List<PaymentInformation> getPmtInfList() {
        return pmtInfList;
    }

    public void addPmtInf(PaymentInformation pmtInf) {
        this.pmtInfList.add(pmtInf);
    }
}