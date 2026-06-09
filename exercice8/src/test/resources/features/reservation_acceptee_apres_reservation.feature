Feature: Réservation acceptée après une réservation existante

  Scenario: Le créneau commence après une réservation existante
    Given une salle "S-C3" nommée "Salon C" avec une capacité de 12
    And une réservation existante sur la salle "S-C3" du "2025-06-12 10:00" au "2025-06-12 11:00"
    When l'utilisateur "apres@mail.com" réserve la salle "S-C3" pour 4 participants du "2025-06-12 11:00" au "2025-06-12 12:00"
    Then la réservation est acceptée
    And une confirmation est envoyée à "apres@mail.com"
