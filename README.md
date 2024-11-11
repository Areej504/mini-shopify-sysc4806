SYSC4806 Fall 2024 Project - Mini-SHopify; Milestone 1; Version 1.0; 11/11/2024

The project can be reached at:
Website: www.brightspace.carleton.ca



Authors:
___________

1. Warda Saleh
2. Rama Alkhouli
3. Areej Mahmoud
4. Mahnoor Fatima
5. Flo, Lavji

Project Description:
___________

The Mini-Shopify platform allows merchants to create customizable online shops by filling out a form with the shop’s name,
categories/tags, and other details. Merchants can upload products with descriptions, images, and inventory counts. Customers can
search for shops by name or category/tag, browse product catalogs, and add items to a shopping cart. Purchases are simulated, and
orders are restricted by available inventory.

Classes and their descriptions are listed below:
- CustomerController: Manages customer-related actions, including displaying the customer creation form, saving new customers, and showing available shops to customers​.
- MerchantController: Handles merchant operations, such as creating merchants, managing shops, and displaying a merchant dashboard with shop-related functionalities​.
- ProductController: Manages product operations within shops, including adding new products to a shop, viewing shop products, and removing products from a shop​.
- ShopController: Manages shop-specific views, displaying shop details with available products, handling cart view, and managing payment page navigation​.
- Cart: Represents a shopping cart containing items a customer intends to purchase.
- CartItem: Represents an individual item within a shopping cart, including quantity and product details.
- CartRepository: Provides database access methods for managing Cart entities.
- Category: Defines different categories that products can belong to.
- Customer: Represents a customer entity with relevant personal and account information.
- CustomerRepository: Handles database operations for customer data.
- Merchant: Represents a merchant entity who owns or manages shops.
- MerchantRepository: Provides methods for database interactions with merchant data.
- OrderInfo: Contains details related to a specific order placed by a customer.
- OrderInfoRepository: Manages database operations for order information entities.
- OrderStatus: Enum or class that represents the possible statuses an order can have, such as pending, shipped, or delivered.
- Payment: Manages information related to payments, such as amount, method, and transaction status.
- PaymentMethod: Enum or class detailing various payment methods, like credit card, PayPal, or bank transfer.
- PaymentStatus: Enum or class that represents the status of a payment, such as completed, pending, or failed.
- Product: Represents a product entity in the system, detailing attributes like price, name, and category.
- ProductRepository: Facilitates interaction with the database for Product entities, supporting CRUD operations.
- PromotionType: Enum or class that categorizes different types of promotions, such as discounts or buy-one-get-one offers.
- Shop: Represents a shop entity that contains details about a store, such as name, description, and merchant association.
- ShopRepository: Provides database interaction methods for managing Shop entities in the application.





Milestone 1 Deliverables:
___________
Demo: Provide a 10–15 minute live demo of the early prototype on November 11th, showcasing basic system functionality.
Operational Use Case: Implement one key use case that is fully functional, including data collection from the backend, processing, and displaying results (simple UI acceptable).
GitHub Repository: Maintain a public GitHub repo with project files.
Continuous Integration (CI): Integrate the GitHub repo with a CI pipeline.
Deployment on Azure: Ensure the application is deployed and running on Azure.
Ready-to-Run JAR: Set up the pom.xml file so cloning the repo and running it produces a ready-to-run JAR file.
Documentation: Include a detailed README file.
Project Management: Track tasks and progress using:
Issues and Kanban Board on GitHub.
Code Reviews to ensure quality.
Tests to verify functionality and correctness.
Team Participation: Ensure all team members actively participate in each project aspect (coding, documentation, project management).



Milestone 1 Known Issues:
___________
- removeProduct() functionality throws a backend error
- some tests are not passing because of certain functionalities



next steps:
___________
- fix removeProduct() functionality throws a backend error
- add back button navigation to front-end
- fix functionality issues, and their test methods 



Milestones:
___________

- Milestone 1 (completed milestone): a live demo of an early prototype with one operational use case, a GitHub repo integrated with CI,
Azure deployment, a ready-to-run JAR file, complete documentation, project tracking, and full team participation. 


Installation:
____________

IntelliJ IDEA 2024.2 recommended or any other Java IDE
JDK 17 recommended or any other version must be installed


