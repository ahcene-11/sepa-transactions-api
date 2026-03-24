package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class GroupHeader {

    @XmlElement(name = "MsgId")
    private String msgId;

    @XmlElement(name = "CreDtTm")
    private String creDtTm;

    @XmlElement(name = "NbOfTxs")
    private String nbOfTxs;

    @XmlElement(name = "CtrlSum")
    private String ctrlSum;

    public GroupHeader() {}

    public GroupHeader(String msgId, String creDtTm, String nbOfTxs, String ctrlSum) {
        this.msgId = msgId;
        this.creDtTm = creDtTm;
        this.nbOfTxs = nbOfTxs;
        this.ctrlSum = ctrlSum;
    }
}