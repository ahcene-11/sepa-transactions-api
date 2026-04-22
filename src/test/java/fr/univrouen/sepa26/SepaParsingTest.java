package fr.univrouen.sepa26;

import fr.univrouen.sepa26.model.Document;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SepaParsingTest {

    @Test
    public void testXmlParsing(){
        try {
            // Un faux flux XML avec le BON namespace (sepa26) et la structure à jour
            String fauxXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<Document xmlns=\"http://univ.fr/sepa26\">\n" +
                    "    <CstmrDrctDbtInitn>\n" +
                    "        <GrpHdr>\n" +
                    "            <MsgId>TEST-UNIT-SEPA</MsgId>\n" +
                    "            <CreDtTm>2026-04-06T15:00:00</CreDtTm>\n" +
                    "            <NbOfTxs>1</NbOfTxs>\n" +
                    "            <CtrlSum>150.50</CtrlSum>\n" +
                    "            <InitgPty>\n" +
                    "                <Nm>Testeur</Nm>\n" +
                    "            </InitgPty>\n" +
                    "        </GrpHdr>\n" +
                    "    </CstmrDrctDbtInitn>\n" +
                    "</Document>";

            // JAXB pour lire ce XML
            JAXBContext context = JAXBContext.newInstance(Document.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(fauxXml);
            Document doc = (Document) unmarshaller.unmarshal(reader);

            assertNotNull(doc, "Le document ne devrait pas être nul après le parsing");

            // Si la méthode s'appelle getSepa() au lieu de getCustomerDirectDebitInitiation(), adaptez-la ici :
            assertNotNull(doc.getCustomerDirectDebitInitiation().getGrpHdr(), "Le GroupHeader ne devrait pas être nul ");

            assertEquals("TEST-UNIT-SEPA", doc.getCustomerDirectDebitInitiation().getGrpHdr().getMsgId(),
                    "Le MsgId lu ne correspond pas");

        } catch (Exception e){
            org.junit.jupiter.api.Assertions.fail("Le test a planté avec une exception : " + e.getMessage());
        }
    }
}