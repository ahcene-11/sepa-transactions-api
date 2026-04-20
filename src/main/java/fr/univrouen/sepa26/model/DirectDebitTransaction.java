package fr.univrouen.sepa26.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DrctDbtTxInf")
@Entity
@XmlType(propOrder = {"pmtId", "instdAmt", "drctDbtTx", "dbtrAgt", "dbtr", "dbtrAcct", "rmtInf"})
public class DirectDebitTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id_db;

    // 3. LA CLÉ ÉTRANGÈRE : Le lien de retour vers le lot parent
    @ManyToOne
    @JoinColumn(name = "payment_information_id")
    @XmlTransient // On cache ça du XML/JSON pour éviter une boucle infinie
    private PaymentInformation paymentInformation;

    @XmlElement(name = "PmtId")
    private String pmtId;

    @XmlElement(name = "InstdAmt")
    @Embedded
    private Amount instdAmt;

    @XmlElement(name = "DrctDbtTx")
    @Embedded
    private TransactionDetails drctDbtTx;

    @XmlElement(name = "DbtrAgt")
    @Embedded
    private DebtorAgent dbtrAgt;

    @XmlElement(name = "Dbtr")
    @Embedded
    private Debtor dbtr;

    @XmlElement(name = "DbtrAcct")
    @Embedded
    private DebtorAccount dbtrAcct;

    @XmlElement(name = "RmtInf")
    @ElementCollection
    @CollectionTable(name = "remittance_info", joinColumns = @JoinColumn(name = "transaction_id"))
    @Column(name = "info_text")
    private List<String> rmtInf;

    public DirectDebitTransaction() {
        this.rmtInf = new ArrayList<>();
    }

    public long getId_db(){
        return id_db;
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

    // Le Getter
    public PaymentInformation getPaymentInformation() {
        return paymentInformation;
    }

    // Le Setter
    public void setPaymentInformation(PaymentInformation paymentInformation) {
        this.paymentInformation = paymentInformation;
    }
// ---CLASSES INTERNES POUR LES BALISES IMBRIQUÉES ---


    // Montant avec son attribut Ccy (Devise)
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
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
    @Embeddable
    public static class TransactionDetails {
        @XmlElement(name = "MndtRltdInf")
        private MandateRelatedInfo mndtRltdInf;

        public MandateRelatedInfo getMndtRltdInf() {
            return mndtRltdInf;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    @XmlType(propOrder = {"mndtId", "dtOfSgntr"})
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
    @Embeddable
    public static class DebtorAgent {
        @XmlElement(name = "FinInstnId")
        private FinancialInstitutionId finInstnId;

        public FinancialInstitutionId getFinInstnId() {
            return finInstnId;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    @XmlType(name = "TxFinancialInstitutionId", propOrder = {"bic", "othr"})    public static class FinancialInstitutionId {
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
    @Embeddable
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
    @Embeddable
    public static class Debtor {
        @XmlElement(name = "Nm")
        private String name;

        public String getName() {
            return name;
        }
    }

    // Compte du débiteur (IBAN)
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DebtorAccount {
        @XmlElement(name = "Id")
        private AccountId id;

        public AccountId getId() {
            return id;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    @XmlType(name = "TxAccountId")
    public static class AccountId {
        @XmlElement(name = "IBAN")
        private String iban;

        public String getIban() {
            return iban;
        }
    }
}