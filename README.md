SYSC4806 Fall 2024 Project - Mini-SHopify; Milestones 1 & 2 & 3; Version 1.0; 06/12/2024

The project can be reached at:
Azure Deployment: https://mini-shopify-4806-fme9dbcfcmfjbuaw.canadaeast-01.azurewebsites.net/
Email: areejmahmoud@cmail.carleton.ca, wardasaleh@cmail.carleton.ca


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
- OrderController: manages order-related functionalities.
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
- Shipping: Encapsulates shipping details for an order, including name, address, and contact information.
- ShippingRepository: Handles database interactions for Shipping entities.
- cartView.html: Displays the contents of a shopping cart, including product items, quantities, and total cost.
- createCustomer.html: Provides a form for creating a new customer profile with fields for personal details.
- createMerchant.html: Contains a form for registering a new merchant with necessary identification details.
- createShop.html: Allows merchants to create a new shop by entering relevant shop details.
- customerScreen.html: Serves as the main customer interface to view products and manage their shopping experience.
- index.html: Functions as the landing page or homepage for the application.
- manageStores.html: Provides a management interface for merchants to oversee their stores and available products.
- merchantShop.html: Displays a list of products specific to a merchant's shop, allowing for product management.
- merchantView1.html: Acts as the main screen for merchants, offering options to create shops or manage existing stores.
- paymentView.html: Presents a checkout form for customers to enter shipping and payment details before placing an order.
- shopPage.html: Shows an individual shop's page with a list of products, search functionality, and options to add items to the cart.
- searchShops.html: Provides a search interface for customers to find shops, with real-time filtering functionality based on shop names or categories​.
- shopCheckout.html: Implements a checkout page for users, supporting both registered and guest checkout options, with dynamic visibility for the password field and guest button based on email validation​.
- merchantLogin.html: A login interface for merchants.
- orderConfirmation.html: Displays order details for customers after completing a purchase, likely confirming the successful transaction.


Contributions:
___________
Areej
Milestone 1
Implemented MerchantController, CustomerController, ShopController, and ProductController to handle persistence and process GET/POST requests.
Set up GitHub Actions CI/CD and deployed the app to Azure.
Created and managed the Kanban Board for project tracking.
Addressed the following issues:
Set up SpringBoot MVC Controllers (#2).
Added and removed products for Merchant Shop (#21).
Enabled CI/CD and deployed to Azure (#6).
Milestone 2
Implemented Merchant and Customer Login functionalities on both back-end and front-end:
Issues addressed: #71, #59, #73, #76.
Added a back-to-homepage-on-logo-click feature across all pages:
Issue addressed: #66.
Extended web layer testing for the CartController:
Issue addressed: #37.

Rama
Milestone 1
Contributed to the design architecture of the Shopify web app.
Implemented all merchant-side views:
createMerchant.html
createShop.html
manageStores.html
shopPage.html
Set up the deployment to Azure.
Participated in debugging sessions for testing and resolving GitHub conflicts.
Milestone 2
Implemented Cart functionality:
Added items to cart (#36).
Removed items from cart (#62).
Update Web Layer Testing/ Redis Testing/ Unit Testing(#101)
Update Diagrams + README(#110)


Warda
Milestone 1
Implemented all JUnit test classes and Web Layer test classes.
Milestone 2
Implemented Store-Wide Promotions:
Front-end and back-end functionality.
Issues addressed:
Shop Wide Promotions (#80).
Set Store Promotion (front-end) (#41).
Backend Model for setting Store Promotion (#43).
Merchant's should be able to view all orders for their specific stores (#61).
Update Diagrams + README(#110)

Mahnoor
Milestone 1
Implemented entity classes and designed their relational schema for proper persistence.
Developed enums for better organization.
Worked on customer-side Thymeleaf views:
cartView, customerScreen, index, paymentView, shopPage.
Contributed to Controllers to improve navigation between pages and debug issues.
Generated class diagrams and database schema using IntelliJ.
Addressed the following issues:
Created entity classes (#1).
Developed initial Thymeleaf HTML views (#3).
Added functionality to retrieve/search for shop/product from inventory (#7).
Milestone 2
Conducted extensive bug fixing:
Fixed issues related to shop promotions, carts, and store promotions back-end.
Added checkout capabilities to the app.
Adding functionality to the payment's page, process payments
fix enums, fix shop description and dates(#102).


Flo
Initiated project kickoff and assisted with debugging and Azure deployment.


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

Milestone 2 Deliverables:
___________
Demo Presentation: Conduct a 10–15 minute live demonstration during the lab session, showcasing the app’s usability with several integrated features working together.
Functional Requirements: Ensure that a few related features are functional and integrated. Demonstrate the system’s ability to perform useful tasks, even if the app is not feature-complete.
Updated Documentation: Update the README on GitHub to include a summary of the current system’s capabilities, and a clear plan for the next sprint, outlining the features and improvements to be implemented.

Milestone 3 Deliverables:
___________
Final Presentation: Deliver a 10–15 minute live demonstration during the lab session, showcasing the complete and polished product with all planned features fully implemented.
Final Product Requirements: Ensure the system is feature-complete and fully usable. The interface must be intuitive, with no links or placeholders for unimplemented features.
Usability and Usefulness: Demonstrate the product’s usefulness in fulfilling its intended purpose. Verify that all implemented features are fully functional and seamlessly integrated.
Update the README on GitHub to include:
•	A description of the final scope and features of the product.
•	A summary of implemented features and any known limitations.
•	User instructions or guides for using the app.



Milestone 1 Known Issues:
___________
- removeProduct() functionality throws a backend error
- some tests are not passing because of certain functionalities

Milestone 2 Known Issues:
___________
- fix the sliding promootion
- user's cannot choose a product image they want, its limited
- checkout is not fully functional
- passwords are insecure, need to implement password policy
- allow customers to reset password

Milestone 3 Known Issues:
___________


next steps:
___________



Milestones:
___________

- Milestone 1 (completed milestone): a live demo of an early prototype with one operational use case, a GitHub repo integrated with CI,
Azure deployment, a ready-to-run JAR file, complete documentation, project tracking, and full team participation.
- Milestone 2 (completed milestone): Conduct a 10–15 minute demo showcasing integrated features, ensure functional and useful tasks are demonstrated, and update the README with the system’s current capabilities and next sprint plan.
- Milestone 3 (completed milestone): Deliver a 10–15 minute demo showcasing the complete, feature-complete product with an intuitive interface, and update the README with the final scope, features, limitations, and user instructions.

Installation:
____________

IntelliJ IDEA 2024.2 recommended or any other Java IDE
JDK 17 recommended or any other version must be installed


