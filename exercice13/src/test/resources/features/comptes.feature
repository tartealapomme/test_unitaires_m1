Feature: Gestion des comptes bancaires

  Scenario: Création d'un nouveau compte
    Given aucun compte n'existe dans l'API
    When je crée un compte avec le numéro "FR001" pour le titulaire "Alice"
    Then la réponse HTTP doit être 201
    And la réponse contient le numéro "FR001"
    And la réponse contient le titulaire "Alice"
    And la réponse contient le solde 0

  Scenario: Dépôt d'argent sur un compte
    Given aucun compte n'existe dans l'API
    And un compte "FR001" appartenant à "Alice" existe avec un solde de 100
    When je dépose 50 sur le compte "FR001"
    Then la réponse HTTP doit être 200
    And la réponse contient le solde 150

  Scenario: Retrait avec fonds suffisants
    Given aucun compte n'existe dans l'API
    And un compte "FR001" appartenant à "Alice" existe avec un solde de 100
    When je retire 40 sur le compte "FR001"
    Then la réponse HTTP doit être 200
    And la réponse contient le solde 60

  Scenario: Retrait avec fonds insuffisants
    Given aucun compte n'existe dans l'API
    And un compte "FR001" appartenant à "Alice" existe avec un solde de 30
    When je retire 50 sur le compte "FR001"
    Then la réponse HTTP doit être 409
    And la réponse contient un message d'erreur

  Scenario: Virement entre deux comptes
    Given aucun compte n'existe dans l'API
    And un compte "FR001" appartenant à "Alice" existe avec un solde de 200
    And un compte "FR002" appartenant à "Bob" existe avec un solde de 50
    When je vire 75 du compte "FR001" vers le compte "FR002"
    Then la réponse HTTP doit être 200
    And le compte "FR001" a un solde de 125
    And le compte "FR002" a un solde de 125

  Scenario: Virement refusé pour solde insuffisant
    Given aucun compte n'existe dans l'API
    And un compte "FR001" appartenant à "Alice" existe avec un solde de 20
    And un compte "FR002" appartenant à "Bob" existe avec un solde de 50
    When je vire 100 du compte "FR001" vers le compte "FR002"
    Then la réponse HTTP doit être 409
    And la réponse contient un message d'erreur
