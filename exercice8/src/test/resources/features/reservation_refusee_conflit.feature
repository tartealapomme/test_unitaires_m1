Feature: Réservation refusée si conflit de réservation

  Scenario: Le créneau chevauche une réservation existante
    Given une salle "S-F6" nommée "Salon F" avec une capacité de 10
    And une réservation existante sur la salle "S-F6" du "2025-06-10 10:00" au "2025-06-10 11:00"
    When l'utilisateur "conflit@mail.com" réserve la salle "S-F6" pour 3 participants du "2025-06-10 10:30" au "2025-06-10 11:30"
    Then la réservation est refusée
    And aucune confirmation n'est envoyée
