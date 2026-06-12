Feature: Création de compte

  Scenario: Inscription réussie avec des identifiants valides
    Given aucun compte avec le nom d'utilisateur "alice"
    When l'utilisateur crée un compte avec l'email "alice@mail.com", le nom d'utilisateur "alice" et le mot de passe "secret123"
    Then l'inscription est confirmée avec le message "Compte créé avec succès"
    And le dépôt utilisateur a été consulté pour "alice"

  Scenario: Inscription refusée pour un nom d'utilisateur existant
    Given un compte existant avec le nom d'utilisateur "bob"
    When l'utilisateur crée un compte avec l'email "bob2@mail.com", le nom d'utilisateur "bob" et le mot de passe "secret456"
    Then l'inscription est refusée
