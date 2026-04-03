# 💳 SEPA26 

## 📌 Description

SEPA26 est une application permettant de gérer des transactions SEPA au format XML via une interface REST.

Elle repose sur une **implémentation partielle de la norme ISO 20022**, avec validation des flux via XSD et transformation en HTML grâce à XSLT.

---

##   Fonctionnalités

*  Page d’accueil avec informations sur l’application
*  Page d’aide listant les endpoints disponibles
*  Consultation des transactions (liste et détail) en XML ou HTML
*  Ajout de transactions via flux XML validé (XSD)
*  Suppression de transactions
*  Recherche de transactions selon des critères (date, montant)

---

##  Technologies

* **Java / Spring Boot** : développement du service REST
* **XML / XSD** : structuration et validation des flux SEPA
* **XSLT** : transformation des flux XML en pages HTML
* **PostgreSQL** : stockage des transactions
* **Jenkins** : intégration continue et déploiement automatisé
* **Machine virtuelle (VM)** : hébergement et exécution de l’application

---

##  Déploiement

L’application est déployée sur une **machine virtuelle**, avec :

* un service REST accessible à distance
* une base de données PostgreSQL connectée
* un pipeline Jenkins permettant l’automatisation du build et du déploiement

---

##  Auteur

* Ahcène AMOUCHAS

---

##  Licence

Projet académique
