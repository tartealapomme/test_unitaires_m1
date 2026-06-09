Feature: Réservation refusée si période invalide

  Scenario: La date de fin est égale à la date de début
    Given une salle "S-E5" nommée "Salon E" avec une capacité de 10
    When l'utilisateur "periode@mail.com" réserve la salle "S-E5" pour 3 participants du "2025-06-10 14:00" au "2025-06-10 14:00"
    Then la réservation est refusée
    And aucune confirmation n'est envoyée
