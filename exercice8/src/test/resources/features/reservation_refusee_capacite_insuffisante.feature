Feature: Réservation refusée si capacité insuffisante

  Scenario: Le nombre de participants dépasse la capacité
    Given une salle "S-D4" nommée "Petite salle" avec une capacité de 5
    When l'utilisateur "trop@mail.com" réserve la salle "S-D4" pour 6 participants du "2025-06-10 14:00" au "2025-06-10 15:00"
    Then la réservation est refusée
    And aucune confirmation n'est envoyée
