Feature: Validation de commande

  Scenario: Validation réussie
    Given une commande "CMD-001" contenant 1 unité du produit "P-001"
    When l'utilisateur valide la commande "CMD-001"
    Then la validation est confirmée avec le message "Commande validée"

  Scenario: Validation refusée pour une commande inexistante
    Given aucune commande avec l'identifiant "CMD-999"
    When l'utilisateur valide la commande "CMD-999"
    Then la validation est refusée
