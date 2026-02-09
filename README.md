## Refactoring Decisions

------

To improve testability and design quality, the following changes were made:

External dependencies were extracted into interfaces:

PaymentApi — handles payment processing

DatabaseConnection — handles database persistence

EmailService — handles notification sending

-------

PaymentProcessor now only coordinates payment logic and does not handle infrastructure details.

