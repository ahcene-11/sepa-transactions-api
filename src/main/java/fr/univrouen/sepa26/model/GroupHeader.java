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

    @XmlElement(name = "InitgPty")
    private InitiatingParty initgPty;

    public GroupHeader() {}

    // constructeur pour les tests
    public GroupHeader(String msgId, String creDtTm, String nbOfTxs, String ctrlSum, InitiatingParty initgPty) {
        this.msgId = msgId;
        this.creDtTm = creDtTm;
        this.nbOfTxs = nbOfTxs;
        this.ctrlSum = ctrlSum;
        this.initgPty = initgPty;
    }
    // getters
    public String getMsgId() { return msgId; }
    public String getCreDtTm() { return creDtTm; }
    public String getNbOfTxs() { return nbOfTxs; }
    public String getCtrlSum() { return ctrlSum; }
    public InitiatingParty getInitgPty() { return initgPty; }

    //classe interne pour InitgPty
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class InitiatingParty {
        @XmlElement(name = "Nm")
        private String name;

        // constructeur vide pour XML
        public InitiatingParty() {}

        // Constructeur pratique
        public InitiatingParty(String name) {
            this.name = name;
        }
        public String getName() { return name; }
    }
}