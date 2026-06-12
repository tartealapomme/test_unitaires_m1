Feature: Ajout de produit à une commande

  Scenario: Ajout d'un nouveau produit
    Given une commande "CMD-001" vide
    And un produit "P-001" nommé "Clavier" au prix de 50.0 euros
    When l'utilisateur ajoute le produit "P-001" à la commande "CMD-001"
    Then l'ajout est confirmé
    And la quantité du produit "P-001" dans la commande "CMD-001" est de 1

  Scenario: Augmentation de quantité pour un produit déjà présent
    Given une commande "CMD-001" contenant 1 unité du produit "P-001"
    And un produit "P-001" nommé "Clavier" au prix de 50.0 euros
    When l'utilisateur ajoute le produit "P-001" à la commande "CMD-001"
    Then l'ajout est confirmé
    And la quantité du produit "P-001" dans la commande "CMD-001" est de 2

  Scenario: Ajout refusé pour une commande inexistante
    Given aucune commande avec l'identifiant "CMD-999"
    And un produit "P-001" nommé "Clavier" au prix de 50.0 euros
    When l'utilisateur ajoute le produit "P-001" à la commande "CMD-999"
    Then l'ajout est refusé
