Feature: Commande refusée si stock insuffisant

  Scenario: La quantité demandée dépasse le stock disponible
    Given un produit "REF-004" nommé "Webcam" au prix de 30.0 euros avec un stock de 2
    And un client "STANDARD" avec l'email "client@mail.com"
    When le client passe une commande de 5 unités du produit "REF-004"
    Then la commande est refusée
    And le dépôt produit a été consulté pour "REF-004"
