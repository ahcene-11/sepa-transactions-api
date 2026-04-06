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
            //un faux flux xml minimaliste mais valide
            String fauxXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<Document xmlns=\"http://univ.fr/sepa\">\n" +
                    "    <CstmrDrctDbtInitn>\n" +
                    "        <GrpHdr>\n" +
                    "            <MsgId>TEST-UNIT-SEPA</MsgId>\n" +
                    "            <CreDtTm>2026-04-06T15:00:00</CreDtTm>\n" +
                    "            <NbOfTxs>1</NbOfTxs>\n" +
                    "            <CtrlSum>150.50</CtrlSum>\n" +
                    "        </GrpHdr>\n" +
                    "    </CstmrDrctDbtInitn>\n" +
                    "</Document>";
            //jaxb ppour lire ce xml
            JAXBContext context = JAXBContext.newInstance(Document.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(fauxXml);
            Document doc = (Document) unmarshaller.unmarshal(reader);

            assertNotNull(doc, "Le document ne devrait pas etre nul apres le parsing");

            assertNotNull(doc.getCustomerDirectDebitInitiation().getGrpHdr(), "Le GroupHeader ne devrait pas etre nul ");

            assertEquals("TEST-UNIT-SEPA", doc.getCustomerDirectDebitInitiation().getGrpHdr().getMsgId(),
                    "LE MsgId lu ne correspond pas");

        } catch (Exception e){
            // Si le parsing plante, on force le test à échouer
            org.junit.jupiter.api.Assertions.fail("Le test a planté avec une exception : " + e.getMessage());
        }
    }
}
