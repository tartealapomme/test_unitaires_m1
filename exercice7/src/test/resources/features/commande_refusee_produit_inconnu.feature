Feature: Commande refusée si produit inconnu

  Scenario: Le produit demandé n'existe pas
    Given aucun produit avec la référence "INCONNU"
    And un client "STANDARD" avec l'email "client@mail.com"
    When le client passe une commande de 1 unités du produit "INCONNU"
    Then la commande est refusée
    And le dépôt produit a été consulté pour "INCONNU"
