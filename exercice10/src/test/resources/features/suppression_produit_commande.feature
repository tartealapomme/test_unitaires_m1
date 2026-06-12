Feature: Suppression de produit d'une commande

  Scenario: Diminution de quantité
    Given une commande "CMD-001" contenant 2 unités du produit "P-001"
    When l'utilisateur supprime le produit "P-001" de la commande "CMD-001"
    Then la suppression est confirmée
    And la quantité du produit "P-001" dans la commande "CMD-001" est de 1

  Scenario: Retrait du produit lorsque la quantité est de 1
    Given une commande "CMD-001" contenant 1 unité du produit "P-001"
    When l'utilisateur supprime le produit "P-001" de la commande "CMD-001"
    Then la suppression est confirmée
    And le produit "P-001" n'est plus dans la commande "CMD-001"

  Scenario: Suppression refusée pour un produit absent
    Given une commande "CMD-001" vide
    When l'utilisateur supprime le produit "P-001" de la commande "CMD-001"
    Then la suppression est refusée

  Scenario: Suppression refusée pour une commande inexistante
    Given aucune commande avec l'identifiant "CMD-999"
    When l'utilisateur supprime le produit "P-001" de la commande "CMD-999"
    Then la suppression est refusée
