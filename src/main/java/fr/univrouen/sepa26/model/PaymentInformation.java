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

    @XmlElement(name = "NbOfTxs")
    private String nbOfTxs;

    @XmlElement(name = "CtrlSum")
    private String ctrlSum;

    @XmlElement(name = "PmtTpInf")
    private PaymentTypeInformation pmtTpInf;

    @XmlElement(name = "ReqdColltnDt")
    private String reqdColltnDt;

    @XmlElement(name = "Cdtr")
    private Creditor creditor;

    @XmlElement(name = "CdtrAcct")
    private CreditorAccount creditorAccount;

    @XmlElement(name = "CdtrAgt")
    private CreditorAgent creditorAgent;

    @XmlElement(name = "CdtrSchmeId")
    private CreditorSchemeId creditorSchemeId;

    // La liste des transactions (Le niveau 3 !)
    @XmlElement(name = "DrctDbtTxInf")
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
// ==========================================
    // CLASSES INTERNES POUR LES BALISES IMBRIQUÉES
    // ==========================================

    // Type de paiement <PmtTpInf>
    @XmlAccessorType(XmlAccessType.FIELD)
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
    public static class ServiceLevel {
        @XmlElement(name = "Cd")
        private String code;

        public String getCode() {
            return code;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LocalInstrument {
        @XmlElement(name = "Cd")
        private String code;

        public String getCode() {
            return code;
        }
    }

    // Créancier <Cdtr>
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Creditor {
        @XmlElement(name = "Nm")
        private String name;

        public String getName() {
            return name;
        }
    }

    // Compte Créancier <CdtrAcct>
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CreditorAccount {
        @XmlElement(name = "Id")
        private AccountId id;

        public AccountId getId() {
            return id;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AccountId {
        @XmlElement(name = "IBAN")
        private String iban;

        public String getIban() {
            return iban;
        }
    }

    // Banque Créancier <CdtrAgt>
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CreditorAgent {
        @XmlElement(name = "FinInstnId")
        private FinancialInstitutionId finInstnId;

        public FinancialInstitutionId getFinInstnId() {
            return finInstnId;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FinancialInstitutionId {
        @XmlElement(name = "BIC")
        private String bic;

        public String getBic() {
            return bic;
        }
    }

    // Identifiant SEPA du créancier <CdtrSchmeId>
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CreditorSchemeId {
        @XmlElement(name = "Id")
        private SchemeId id;

        public SchemeId getId() {
            return id;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SchemeId {
        @XmlElement(name = "PrvtId")
        private PrivateId prvtId;

        public PrivateId getPrvtId() {
            return prvtId;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PrivateId {
        @XmlElement(name = "Othr")
        private OtherId othr;

        public OtherId getOthr() {
            return othr;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
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
    public static class SchemeName {
        @XmlElement(name = "Prtry")
        private String prtry;

        public String getPrtry() {
            return prtry;
        }
    }
}