package fr.univrouen.sepa26.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@XmlType(propOrder = {"pmtInfId", "nbOfTxs", "ctrlSum", "pmtTpInf", "reqdColltnDt", "creditor", "creditorAccount", "creditorAgent", "creditorSchemeId", "transactions"})
public class PaymentInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    @Column(name = "pk_id")
    private Long id;

    @XmlElement(name = "PmtInfId")
    private String pmtInfId;

    @XmlElement(name = "NbOfTxs")
    private String nbOfTxs;

    @XmlElement(name = "CtrlSum")
    private String ctrlSum;

    @XmlElement(name = "PmtTpInf")
    @Embedded
    private PaymentTypeInformation pmtTpInf;

    @XmlElement(name = "ReqdColltnDt")
    private String reqdColltnDt;

    @XmlElement(name = "Cdtr")
    @Embedded
    private Creditor creditor;

    @XmlElement(name = "CdtrAcct")
    @Embedded
    private CreditorAccount creditorAccount;

    @XmlElement(name = "CdtrAgt")
    @Embedded
    private CreditorAgent creditorAgent;

    @XmlElement(name = "CdtrSchmeId")
    @Embedded
    private CreditorSchemeId creditorSchemeId;

    // La clé étrangère vers le Header
    @ManyToOne
    @JoinColumn(name = "group_header_id")
    @XmlTransient // On cache ça du XML pour ne pas perturber JAXB
    private GroupHeader groupHeader;

    public GroupHeader getGroupHeader() {
        return groupHeader;
    }

    public void setGroupHeader(GroupHeader groupHeader) {
        this.groupHeader = groupHeader;
    }

    // La liste des transactions (Le niveau 3 !)
    @XmlElement(name = "DrctDbtTxInf")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentInformation")
    private List<DirectDebitTransaction> transactions;

    // Constructeur vide obligatoire pour JAXB
    public PaymentInformation() {
        this.transactions = new ArrayList<>();
    }

    // Constructeur pratique pour vos tests
    public PaymentInformation(String pmtInfId, String reqdColltnDt) {
        this.pmtInfId = pmtInfId;
        this.reqdColltnDt = reqdColltnDt;
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(DirectDebitTransaction tx) {
        this.transactions.add(tx);
    }

    //getter utile pour jackson car il ne lis pas les variables private
    public Long getId() {
        return id;
    }
    public String getPmtInfId() {
        return pmtInfId;
    }

    public String getNbOfTxs() {
        return nbOfTxs;
    }

    public String getCtrlSum() {
        return ctrlSum;
    }

    public PaymentTypeInformation getPmtTpInf() {
        return pmtTpInf;
    }

    public String getReqdColltnDt() {
        return reqdColltnDt;
    }

    public Creditor getCreditor() {
        return creditor;
    }

    public CreditorAccount getCreditorAccount() {
        return creditorAccount;
    }

    public CreditorAgent getCreditorAgent() {
        return creditorAgent;
    }

    public CreditorSchemeId getCreditorSchemeId() {
        return creditorSchemeId;
    }

    public List<DirectDebitTransaction> getTransactions() {
        return transactions;
    }

    public void setDrctDbtTxInf(List<DirectDebitTransaction> directDebitTransactions) {
        this.transactions = directDebitTransactions;
    }
// ==========================================
    // CLASSES INTERNES POUR LES BALISES IMBRIQUÉES
    // ==========================================

    // Type de paiement <PmtTpInf>
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    @XmlType(propOrder = {"svcLvl", "lclInstrm", "seqTp"})
    public static class PaymentTypeInformation {
        @XmlElement(name = "SvcLvl")
        private ServiceLevel svcLvl;
        @XmlElement(name = "LclInstrm")
        private LocalInstrument lclInstrm;
        @XmlElement(name = "SeqTp")
        private String seqTp;

        public ServiceLevel getSvcLvl() {
            return svcLvl;
        }

        public LocalInstrument getLclInstrm() {
            return lclInstrm;
        }

        public String getSeqTp() {
            return seqTp;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class ServiceLevel {
        @XmlElement(name = "Cd")
        @Column(name = "svc_lvl_code")
        private String code;

        public String getCode() {
            return code;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class LocalInstrument {
        @XmlElement(name = "Cd")
        @Column(name = "lcl_instrm_code")
        private String code;

        public String getCode() {
            return code;
        }
    }

    // Créancier <Cdtr>
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class Creditor {
        @XmlElement(name = "Nm")
        private String name;

        public String getName() {
            return name;
        }
    }

    // Compte Créancier <CdtrAcct>
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class CreditorAccount {
        @XmlElement(name = "Id")
        private AccountId id;

        public AccountId getId() {
            return id;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class AccountId {
        @XmlElement(name = "IBAN")
        private String iban;

        public String getIban() {
            return iban;
        }
    }

    // Banque Créancier <CdtrAgt>
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class CreditorAgent {
        @XmlElement(name = "FinInstnId")
        private FinancialInstitutionId finInstnId;

        public FinancialInstitutionId getFinInstnId() {
            return finInstnId;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class FinancialInstitutionId {
        @XmlElement(name = "BIC")
        private String bic;

        public String getBic() {
            return bic;
        }
    }

    // Identifiant SEPA du créancier <CdtrSchmeId>
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class CreditorSchemeId {
        @XmlElement(name = "Id")
        private SchemeId id;

        public SchemeId getId() {
            return id;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class SchemeId {
        @XmlElement(name = "PrvtId")
        private PrivateId prvtId;

        public PrivateId getPrvtId() {
            return prvtId;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class PrivateId {
        @XmlElement(name = "Othr")
        private OtherId othr;

        public OtherId getOthr() {
            return othr;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    @XmlType(propOrder = {"id", "schmeNm"})
    public static class OtherId {
        @XmlElement(name = "Id")
        private String id;
        @XmlElement(name = "SchmeNm")
        private SchemeName schmeNm;

        public SchemeName getSchmeNm() {
            return schmeNm;
        }

        public String getId() {
            return id;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class SchemeName {
        @XmlElement(name = "Prtry")
        private String prtry;

        public String getPrtry() {
            return prtry;
        }
    }
}