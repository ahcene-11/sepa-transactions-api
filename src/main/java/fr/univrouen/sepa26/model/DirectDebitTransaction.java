package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DirectDebitTransaction {

    @XmlElement(name = "PmtId")
    private String pmtId;

    @XmlElement(name = "InstdAmt")
    private Amount instdAmt;

    @XmlElement(name = "DrctDbtTx")
    private TransactionDetails drctDbtTx;

    @XmlElement(name = "DbtrAgt")
    private DebtorAgent dbtrAgt;

    @XmlElement(name = "Dbtr")
    private Debtor dbtr;

    @XmlElement(name = "DbtrAcct")
    private DebtorAccount dbtrAcct;

    @XmlElement(name = "RmtInf")
    private List<String> rmtInf;

    public DirectDebitTransaction() {
        this.rmtInf = new ArrayList<>();
    }

    public String getPmtId() {
        return pmtId;
    }

    public Amount getInstdAmt() {
        return instdAmt;
    }

    public TransactionDetails getDrctDbtTx() {
        return drctDbtTx;
    }

    public DebtorAgent getDbtrAgt() {
        return dbtrAgt;
    }

    public Debtor getDbtr() {
        return dbtr;
    }

    public DebtorAccount getDbtrAcct() {
        return dbtrAcct;
    }

    public List<String> getRmtInf() {
        return rmtInf;
    }
// ---CLASSES INTERNES POUR LES BALISES IMBRIQUÉES ---


    // Montant avec son attribut Ccy (Devise)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Amount {
        @XmlAttribute(name = "Ccy")
        private String currency;

        @XmlValue
        private double value;

        public String getCurrency() {
            return currency;
        }

        public double getValue() {
            return value;
        }
    }

    // Détails de la transaction et Mandat
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TransactionDetails {
        @XmlElement(name = "MndtRltdInf")
        private MandateRelatedInfo mndtRltdInf;

        public MandateRelatedInfo getMndtRltdInf() {
            return mndtRltdInf;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MandateRelatedInfo {
        @XmlElement(name = "MndtId")
        private String mndtId;
        @XmlElement(name = "DtOfSgntr")
        private String dtOfSgntr;

        public String getMndtId() {
            return mndtId;
        }

        public String getDtOfSgntr() {
            return dtOfSgntr;
        }
    }

    // Banque du débiteur
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DebtorAgent {
        @XmlElement(name = "FinInstnId")
        private FinancialInstitutionId finInstnId;

        public FinancialInstitutionId getFinInstnId() {
            return finInstnId;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "TxFinancialInstitutionId")
    public static class FinancialInstitutionId {
        @XmlElement(name = "BIC")
        private String bic;

        @XmlElement(name = "Othr")
        private OtherId othr;

        public OtherId getOthr() {
            return othr;
        }

        public String getBic() {
            return bic;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "TxOtherId")
    public static class OtherId {
        @XmlElement(name = "Id")
        private String id;

        public String getId() {
            return id;
        }
    }

    // Débiteur (Nom)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Debtor {
        @XmlElement(name = "Nm")
        private String name;

        public String getName() {
            return name;
        }
    }

    // Compte du débiteur (IBAN)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DebtorAccount {
        @XmlElement(name = "Id")
        private AccountId id;

        public AccountId getId() {
            return id;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "TxAccountId")
    public static class AccountId {
        @XmlElement(name = "IBAN")
        private String iban;

        public String getIban() {
            return iban;
        }
    }
}