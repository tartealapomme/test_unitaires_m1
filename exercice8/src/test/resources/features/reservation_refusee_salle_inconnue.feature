Feature: Réservation refusée si salle inconnue

  Scenario: La salle demandée n'existe pas
    Given aucune salle avec le code "INCONNU"
    When l'utilisateur "user@mail.com" réserve la salle "INCONNU" pour 3 participants du "2025-06-10 14:00" au "2025-06-10 15:00"
    Then la réservation est refusée
    And aucune confirmation n'est envoyée
    And le dépôt salle a été consulté pour "INCONNU"
