Feature: Réservation acceptée à capacité maximale

  Scenario: Le nombre de participants est égal à la capacité maximale
    Given une salle "S-B2" nommée "Salon B" avec une capacité de 8
    When l'utilisateur "max@mail.com" réserve la salle "S-B2" pour 8 participants du "2025-06-11 09:00" au "2025-06-11 10:00"
    Then la réservation est acceptée
    And une confirmation est envoyée à "max@mail.com"
