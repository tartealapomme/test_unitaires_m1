Feature: Réservations d'ouvrages MédiaCity

  Scenario: Réservation d'un ouvrage indisponible
    Given un ouvrage "978-1" intitulé "1984" existe
    And l'adhérent "A1" nommé "Alice" existe
    And l'ouvrage "978-1" est emprunté par "A2"
    When l'adhérent "A1" réserve l'ouvrage "978-1"
    Then la réservation est acceptée

  Scenario: Plusieurs réservations sur le même ouvrage
    Given un ouvrage "978-1" intitulé "1984" existe
    And l'adhérent "A1" nommé "Alice" existe
    And l'adhérent "A2" nommé "Bob" existe
    And l'adhérent "A3" nommé "Claire" existe
    And l'ouvrage "978-1" est emprunté par "A2"
    When l'adhérent "A1" réserve l'ouvrage "978-1"
    And l'adhérent "A3" réserve l'ouvrage "978-1"
    Then il y a 2 réservations sur l'ouvrage "978-1"

  Scenario: Restitution d'un ouvrage réservé
    Given un ouvrage "978-1" intitulé "1984" existe
    And l'adhérent "A1" nommé "Alice" existe
    And l'adhérent "A2" nommé "Bob" existe
    And l'ouvrage "978-1" est emprunté par "A2"
    And l'adhérent "A1" a réservé l'ouvrage "978-1"
    When l'ouvrage "978-1" est restitué
    Then l'ouvrage "978-1" devrait être emprunté par "A1"

  Scenario: Refus d'une réservation pour un adhérent suspendu
    Given un ouvrage "978-1" intitulé "1984" existe
    And l'adhérent "A1" nommé "Alice" est suspendu
    And l'ouvrage "978-1" est emprunté par "A2"
    When l'adhérent "A1" réserve l'ouvrage "978-1"
    Then la réservation est refusée

  Scenario: Refus d'un nouvel emprunt pour un adhérent suspendu
    Given un ouvrage "978-1" intitulé "1984" existe
    And l'adhérent "A1" nommé "Alice" est suspendu
    When l'adhérent "A1" emprunte l'ouvrage "978-1"
    Then l'emprunt est refusé
