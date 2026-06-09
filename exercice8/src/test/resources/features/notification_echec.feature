Feature: Notification non envoyée en cas d'échec

  Scenario: Aucune confirmation n'est envoyée lorsque la réservation échoue
    Given une salle "S-H8" nommée "Salon H" avec une capacité de 4
    When l'utilisateur "echec@mail.com" réserve la salle "S-H8" pour 10 participants du "2025-06-15 10:00" au "2025-06-15 11:00"
    Then la réservation est refusée
    And aucune confirmation n'est envoyée
