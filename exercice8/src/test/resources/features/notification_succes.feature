Feature: Notification envoyée en cas de succès

  Scenario: Une confirmation est envoyée lorsque la réservation réussit
    Given une salle "S-G7" nommée "Salon G" avec une capacité de 6
    When l'utilisateur "notif@mail.com" réserve la salle "S-G7" pour 2 participants du "2025-06-15 08:00" au "2025-06-15 09:00"
    Then la réservation est acceptée
    And une confirmation est envoyée à "notif@mail.com"
