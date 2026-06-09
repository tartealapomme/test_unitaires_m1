Feature: Commande acceptée pour client STANDARD

  Scenario: Une commande valide sans remise
    Given un produit "REF-001" nommé "Clavier" au prix de 50.0 euros avec un stock de 10
    And un client "STANDARD" avec l'email "client@mail.com"
    When le client passe une commande de 2 unités du produit "REF-001"
    Then la commande est acceptée
    And le montant total est de 100.0 euros
    And la quantité commandée est de 2
    And la référence produit du reçu est "REF-001"
    And le message de confirmation est "Commande confirmée"
    And le dépôt produit a été consulté pour "REF-001"
