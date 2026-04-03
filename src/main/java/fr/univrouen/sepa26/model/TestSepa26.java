package fr.univrouen.sepa26.model;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import java.io.InputStream;

public class TestSepa26 {

    public Document testUnmarshalling() {
        try {
            // 1. Charger le fichier XML depuis les ressources
            Resource resource = new DefaultResourceLoader().getResource("classpath:xml/testsepa.xml");
            InputStream is = resource.getInputStream();

            // 2. Préparer JAXB pour lire en commençant par la racine (Document)
            JAXBContext context = JAXBContext.newInstance(Document.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // 3. Transformer le XML en objets Java (Désérialisation)
            Document document = (Document) unmarshaller.unmarshal(is);

            return document;

        } catch (Exception e) {
            System.out.println("Erreur de parsing : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}