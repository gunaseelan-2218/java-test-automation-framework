Feature: Search and Place the order of the products
  
Scenario Outline: Search Experience of product search in both home and orders page
  Given The User is on Greenkart landing page
   When User searched with shortname <Name> and extracted the actual name of the product
   Then User searched for the same <Name> in the offers page to check if products exists
   And validate the landing page product name matches with offer page product name
   
Examples:
 |Name|
 |Tom |
 |Beet|