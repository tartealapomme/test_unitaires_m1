Feature: Réservation de salles de réunion

  Scenario: Réservation acceptée quand la salle existe et que le créneau est libre
    Given aucune donnée n'existe dans l'API
    And une salle "Salle A" avec une capacité de 10 existe
    When je réserve la salle 1 pour "Alice" du "2025-06-15T10:00:00" au "2025-06-15T11:00:00"
    Then la réponse HTTP doit être 201
    And la réponse contient le statut "CONFIRMED"

  Scenario: Réservation refusée quand la salle n'existe pas
    Given aucune donnée n'existe dans l'API
    When je réserve la salle 99 pour "Alice" du "2025-06-15T10:00:00" au "2025-06-15T11:00:00"
    Then la réponse HTTP doit être 404
    And la réponse contient un message d'erreur

  Scenario: Réservation refusée quand le créneau chevauche une réservation existante
    Given aucune donnée n'existe dans l'API
    And une salle "Salle A" avec une capacité de 10 existe
    And une réservation confirmée existe sur la salle 1 du "2025-06-15T10:00:00" au "2025-06-15T11:00:00"
    When je réserve la salle 1 pour "Bob" du "2025-06-15T10:30:00" au "2025-06-15T11:30:00"
    Then la réponse HTTP doit être 409
    And la réponse contient un message d'erreur
