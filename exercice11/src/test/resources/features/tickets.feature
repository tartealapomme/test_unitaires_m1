Feature: Gestion des tickets de support

  Scenario: Création d'un ticket valide
    Given aucun ticket n'existe dans l'API
    When je crée un ticket avec le titre "Problème de connexion" et la priorité "HIGH"
    Then la réponse HTTP doit être 201
    And la réponse contient le titre "Problème de connexion"
    And la réponse contient le statut "OPEN"

  Scenario: Résolution d'un ticket
    Given aucun ticket n'existe dans l'API
    And un ticket existe avec le titre "Bug affichage" et la priorité "MEDIUM"
    When je modifie le statut du ticket créé vers "RESOLVED"
    Then la réponse HTTP doit être 200
    And la réponse contient le statut "RESOLVED"

  Scenario: Refus de modification d'un ticket déjà résolu
    Given aucun ticket n'existe dans l'API
    And un ticket résolu existe avec le titre "Bug résolu" et la priorité "LOW"
    When je modifie le statut du ticket créé vers "IN_PROGRESS"
    Then la réponse HTTP doit être 409
    And la réponse contient un message d'erreur

  Scenario: Consultation d'un ticket inexistant
    Given aucun ticket n'existe dans l'API
    When je consulte le ticket avec l'identifiant 99
    Then la réponse HTTP doit être 404
    And la réponse contient un message d'erreur
