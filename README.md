# 💳 SEPA26 REST API

## 📌 Description

Ce projet consiste à concevoir et déployer un service RESTful permettant de gérer des flux **SEPA (format sepa26)** en XML.

L’objectif est de proposer une API complète capable de :

* recevoir,
* stocker,
* consulter,
* transformer
  des transactions SEPA conformes à un schéma **XSD**.

Le projet s’inscrit dans le cadre du module *Langage Web 2 - XML (M1 GIL)*.

---

## 🚀 Fonctionnalités principales

### 🔹 Accueil

* `GET /`
* Affiche une page HTML contenant :

  * Nom du projet
  * Version
  * Auteurs
  * Logo de l’université

---

### 🔹 Aide

* `GET /help`
* Liste toutes les routes disponibles avec :

  * URL
  * Méthode HTTP
  * Description des opérations

---

### 🔹 Transactions (SEPA)

#### 📄 Liste des transactions

* `GET /sepa26/resume/xml` → Retour XML
* `GET /sepa26/resume/html` → Retour HTML

Affiche les **10 dernières transactions** avec :

* ID
* Date (`<CreDtTm>`)
* Identifiant (`<PmtId>`)
* Montant (`<CtrlSum>`)

---

#### 🔍 Détail d’une transaction

* `GET /sepa26/xml/{id}` → XML
* `GET /sepa26/html/{id}` → HTML

Retourne le contenu complet d’une transaction.

✔️ Gestion des erreurs :

* ID invalide → réponse avec `status: ERROR`

---

#### ➕ Ajout d’une transaction

* `POST /sepa26/insert`
* Reçoit un flux XML conforme au schéma XSD

✔️ Vérifications :

* Validation XSD
* Unicité de la transaction

✔️ Réponses :

* Succès → `status: INSERTED` + `id`
* Échec → `status: ERROR`

---

#### ❌ Suppression d’une transaction

* `DELETE /sepa26/delete/{id}`

✔️ Réponses :

* Succès → `status: DELETED`
* Échec → `status: ERROR`

---

## 📊 Bonus (optionnels)

### 🔎 Recherche

* `GET /sepa26/search?date=...&sum=...`

Permet de filtrer les transactions selon :

* Date (`>=`)
* Montant (`>=`)

---

### ⚠️ Gestion avancée des erreurs

* Ajout d’un champ `description` dans les réponses XML

---

### 🏗️ Usine logicielle

* Intégration CI/CD (Jenkins)
* Déploiement automatique
* Tests avant mise en production

---

## 🛠️ Technologies utilisées

* Java / Spring Boot
* XML / XSD (validation)
* XSLT (transformation XML → HTML)
* XQuery (recherche)
* REST API
* Postman (tests)
* Git / Jenkins (CI/CD)

---

## 📦 Architecture du projet

Le projet repose sur :

* Contrôleurs REST (Spring)
* Validation XML via XSD
* Transformation via XSLT
* Stockage des transactions (base de données ou fichiers XML)

---

## 🧪 Tests

Les endpoints sont testés via **Postman** avec une collection dédiée :

* Requêtes complètes (GET, POST, DELETE)
* Variables d’environnement pour le serveur

---

## 📡 Déploiement

L’application est déployée sur une machine virtuelle avec :

* Serveur actif
* Base de données connectée
* Accès distant aux endpoints

---

## 📁 Outil de transfert

Une application client permet :

* de sélectionner un fichier XML
* de l’envoyer au service REST
* d’afficher la réponse du serveur

---

## ⚠️ Contraintes

* Tous les flux doivent être valides XML
* Validation obligatoire via XSD
* Aucune insertion si erreur
* Formats de sortie :

  * XML (API)
  * HTML/XHTML (affichage)

---

## 👨‍💻 Auteurs

* Ahcène AMOUCHAS

---

## 📅 Deadline

📌 22 mai 2026 - 23h00

---

## 📄 Licence

Projet académique – usage pédagogique
