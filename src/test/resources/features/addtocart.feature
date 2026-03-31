Feature: Add products and validate checkout page details

  Scenario: Validate product details in the checkout page

    Given The User is on Greenkart landing page
    When User searched with shortname Tom and extracted the actual name of the product
    And increases the product quantity to 4
    And adds the product to the cart
    And proceeds to checkout
    Then the product name in the checkout page should match the landing page
    And the selected quantity should match the checkout quantity
    
    