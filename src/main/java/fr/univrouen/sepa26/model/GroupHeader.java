package fr.univrouen.sepa26.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@XmlType(propOrder = {"msgId", "creDtTm", "nbOfTxs", "ctrlSum", "initgPty"})
public class GroupHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient // Dit à JAXB : "Ignore ce champ quand tu lis le XML, c'est juste pour la BDD"
    @Column(name = "pk_id")
    private Long id;

    @XmlElement(name = "MsgId")
    private String msgId;

    @XmlElement(name = "CreDtTm")
    private String creDtTm;

    @XmlElement(name = "NbOfTxs")
    private String nbOfTxs;

    @XmlElement(name = "CtrlSum")
    private String ctrlSum;

    @XmlElement(name = "InitgPty")
    @Embedded // AJOUT JPA : "Intègre les champs de InitiatingParty comme des colonnes de cette table"
    private InitiatingParty initgPty;

    // La relation inverse
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupHeader")
    @XmlTransient // Très important : dans le XML, les lots ne sont pas DANS le header, ils sont à côté !
    private List<PaymentInformation> paymentInformationList;

    public List<PaymentInformation> getPaymentInformationList() {
        return paymentInformationList;
    }

    public void setPaymentInformationList(List<PaymentInformation> paymentInformationList) {
        this.paymentInformationList = paymentInformationList;
    }

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
    public Long getId() {
        return id;
    }
    public String getMsgId() { return msgId; }
    public String getCreDtTm() { return creDtTm; }
    public String getNbOfTxs() { return nbOfTxs; }
    public String getCtrlSum() { return ctrlSum; }
    public InitiatingParty getInitgPty() { return initgPty; }

    //setters
    public void setMsgId(String msg) {
        this.msgId = msg;
    }

    public void setCreDtTm(String c) {
        this.creDtTm=c;
    }
    public void setCtrlSum(String ctrlSum) {
        this.ctrlSum = ctrlSum;
    }

    public void setNbOfTxs(String nbOfTxs) {
        this.nbOfTxs = nbOfTxs;
    }

    //classe interne pour InitgPty
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable // AJOUT JPA : "Je ne suis pas une table, je suis incrustable dans une autre table"
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