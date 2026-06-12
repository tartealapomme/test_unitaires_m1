Feature: Connexion

  Scenario: Connexion réussie
    Given un compte existant "alice" avec le mot de passe "secret123"
    When l'utilisateur se connecte avec le nom d'utilisateur "alice" et le mot de passe "secret123"
    Then la connexion est réussie
    And l'utilisateur est redirigé vers la page d'accueil

  Scenario: Connexion échouée
    Given un compte existant "alice" avec le mot de passe "secret123"
    When l'utilisateur se connecte avec le nom d'utilisateur "alice" et le mot de passe "mauvais"
    Then la connexion échoue
    And le message d'erreur est "Identifiants invalides"
