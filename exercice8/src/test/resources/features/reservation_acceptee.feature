Feature: Réservation acceptée

  Scenario: Une réservation valide est acceptée
    Given une salle "S-A1" nommée "Salon A" avec une capacité de 10
    When l'utilisateur "user@mail.com" réserve la salle "S-A1" pour 5 participants du "2025-06-10 14:00" au "2025-06-10 15:00"
    Then la réservation est acceptée
    And une confirmation est envoyée à "user@mail.com"
    And le dépôt salle a été consulté pour "S-A1"
