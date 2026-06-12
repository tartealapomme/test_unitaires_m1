Feature: Navigation par catégorie

  Background:
    Given les produits suivants sont disponibles:
      | reference | nom     | prix  | categorie    |
      | P-001     | Clavier | 50.0  | Informatique |
      | P-003     | Chaise  | 120.0 | Mobilier     |

  Scenario: Produits filtrés par catégorie
    When l'utilisateur sélectionne la catégorie "Informatique"
    Then la catégorie retourne 1 produit
    And le produit "P-001" est dans les résultats
