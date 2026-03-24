package fr.univrouen.sepa26.model;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TestSepa26 {

    public String loadFileXML() {
        try {
            Resource resource = new DefaultResourceLoader().getResource("classpath:xml/testsepa.xml");
            
            // Ouvrir le flux de lecture (InputStream)
            InputStream is = resource.getInputStream();
            
            // 3. Lire tout le contenu et le transformer en String
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
            String xmlContent = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
            
            return xmlContent;
            
        } catch (Exception e) {
            return "<error>Erreur de lecture du fichier : " + e.getMessage() + "</error>";
        }
    }
}