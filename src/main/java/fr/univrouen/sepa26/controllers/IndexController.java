package fr.univrouen.sepa26.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller //ici ce nest pas @RestController car on gere uniquement la vue aucune donnee
public class IndexController {
	@GetMapping("/")
	// Le mot-clé "forward:" dit à Spring d'aller chercher le fichier
	// dans src/main/resources/static/
	public String index() {
		return "forward:/index.html";
	}

	@GetMapping("/help")
	public String help() {
		return "forward:/help.html";
	}

	@GetMapping("/simulate-error")
	public String simulateError() {
		// On force le programme à planter en déclenchant une exception non gérée
		throw new RuntimeException("Ceci est un test d'erreur 500 volontaire !");
	}
}