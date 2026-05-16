# Projet SEPA26 - API REST & CI/CD

**Auteur(s) :** [Vos Noms / Prénoms]  
**Dépôt Git Référence :** `ssh://giluser@10.130.162.157/var/spring/`

---

## 🌐 Accès aux Services (VPN requis)

| Service | URL / Accès | Identifiants |
| :--- | :--- | :--- |
| **API REST (Accueil)** | `http://10.130.162.157:8100/` | *Accès libre* |
| **Outil de Transfert** | `http://10.130.162.157:8100/transfert` | *Accès libre* |
| **Documentation (/help)** | `http://10.130.162.157:8100/help` | *Accès libre* |
| **Jenkins** | `http://10.130.162.157:8080/` | `admin` / `[Votre Mot de passe]` |
| **Dépôt Git (SSH)** | `ssh://giluser@10.130.162.157/var/spring/` | `giluser` / `[Votre Mot de passe]` |
| **VM Linux (SSH)** | IP: `10.130.162.157` | `spring` (ou `test`) / `[Votre Mot de passe]` |

*Note : L'application est active et la base de données PostgreSQL contient déjà quelques transactions injectées pour la correction.*

---

## 🚀 Fonctionnalités & Bonus Validés

* **Cahier des charges de base (I.1 à I.6) :** Routes d'accueil, d'aide, résumés (HTML/XML), détails (HTML via XSLT / XML), insertion (avec validation XSD) et suppression en cascade opérationnelles.
* **Outil de transfert (III.1) :** Interface client intégrée à l'application sur `/transfert`, permettant de charger et pousser un fichier XML local vers l'API.
* **Gestion des erreurs (II.1 & IV.3) :** Interception des erreurs XSD ou doublons renvoyant un statut `ERROR` et une `<description>` détaillée. Utilisation de logs SLF4J côté serveur et redirection vers une page d'erreur personnalisée en cas de mauvais ID sur l'affichage HTML.
* **Recherche multicritères (II.2) :** Route `/sepa26/search` par montant minimum ou date. *Note : filtre basé sur le montant individuel (`InstdAmt`) pour la cohérence des résultats.*
* **Usine Logicielle (II.3 & III.2) :** Automatisation complète sur Jenkins (Job 1 Construction + Tests -> Job 2 Déploiement Docker). Le pipeline intègre un **Health Check** automatique par script (`curl`) post-déploiement et une politique de tolérance aux pannes (`restart: unless-stopped`) sur Docker Compose.
* **Collection Postman (IV.5) :** Fichier de configuration `.json` inclus à la racine du projet, entièrement paramétré avec la variable `{{url_serveur}}`.