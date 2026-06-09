Feature: Commande acceptée pour client PREMIUM

  Scenario: Une commande valide avec remise de 10 pourcent
    Given un produit "REF-002" nommé "Souris" au prix de 100.0 euros avec un stock de 5
    And un client "PREMIUM" avec l'email "premium@mail.com"
    When le client passe une commande de 1 unités du produit "REF-002"
    Then la commande est acceptée
    And le montant total est de 90.0 euros
    And la quantité commandée est de 1
    And la référence produit du reçu est "REF-002"
    And le message de confirmation est "Commande confirmée"
    And le dépôt produit a été consulté pour "REF-002"
