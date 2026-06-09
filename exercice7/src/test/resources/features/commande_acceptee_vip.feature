Feature: Commande acceptée pour client VIP

  Scenario: Une commande valide avec remise de 20 pourcent
    Given un produit "REF-003" nommé "Ecran" au prix de 100.0 euros avec un stock de 3
    And un client "VIP" avec l'email "vip@mail.com"
    When le client passe une commande de 1 unités du produit "REF-003"
    Then la commande est acceptée
    And le montant total est de 80.0 euros
    And la quantité commandée est de 1
    And la référence produit du reçu est "REF-003"
    And le message de confirmation est "Commande confirmée"
    And le dépôt produit a été consulté pour "REF-003"
