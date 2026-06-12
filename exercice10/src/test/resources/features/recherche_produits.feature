Feature: Recherche de produits

  Background:
    Given les produits suivants sont disponibles:
      | reference | nom     | prix  | categorie    |
      | P-001     | Clavier | 50.0  | Informatique |
      | P-002     | Souris  | 25.0  | Informatique |
      | P-003     | Chaise  | 120.0 | Mobilier     |

  Scenario: Recherche par mot-clé
    When l'utilisateur recherche le mot-clé "Clavier"
    Then la recherche retourne 1 produit
    And le produit "P-001" est dans les résultats

  Scenario: Recherche par prix maximum
    When l'utilisateur recherche des produits avec un prix maximum de 30.0 euros
    Then la recherche retourne 1 produit
    And le produit "P-002" est dans les résultats
