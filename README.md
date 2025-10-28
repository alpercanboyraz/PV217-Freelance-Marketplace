# PV217 Freelance Marketplace
## Overview
This project is a microservices-based marketplace platform designed to connect freelancers with customers seeking custom project development.
It is a platform where users (freelancers) can publish their services as ‘gigs’, buyers can search for these gigs, message each other, and make secure purchases.

## Architecture & Design
The project consists of microservices that communicate with each other asynchronously and synchronously and can be deployed independently.
## Services Overview
### 1. User Service

* **Status:** `In Development`
* **Responsibilities:** Handles the complete user lifecycle, including registration, login, authentication, and profile management (profile picture, bio, skills, etc.).
* **Core Endpoints:**
    * `POST /api/users/register` - Register a new user
    * `POST /api/users/login` - User login (returns a JWT)
    * `GET /api/users/profile` - Get current user's profile
    * `PUT /api/users/profile` - Update user profile

### 2. Gig Service

* **Status:** `Planned`
* **Responsibilities:** Manages the lifecycle of 'gigs' (service listings). This includes creating, updating, and deleting gigs, as well as providing search and filtering capabilities.
* **Core Endpoints:**
    * `POST /api/gigs` - Create a new gig
    * `GET /api/gigs/{id}` - Get details for a single gig
    * `GET /api/gigs/search` - Search and filter all gigs
    * `PUT /api/gigs/{id}` - Update an existing gig

 ### 3. Chat Service

* **Status:** `Planned`
* **Responsibilities:** Manages real-time private communication between buyers and freelancers. Handles conversation history and inbox functionality.
* **Core Endpoints:**
    * `GET /api/chat/conversations` - List all active conversations
    * `GET /api/chat/{conversationId}` - Get message history for a conversation
    * `POST /api/chat/{conversationId}` - Send a new message
    * *(WebSocket Endpoint for real-time messages)*

### 4. Payment Service

* **Status:** `Planned`
* **Responsibilities:** Handles all monetary transactions. Integrates with an external payment provider (like Stripe) to process payments for gigs, manage payouts to freelancers, and handle refunds.
* **Core Endpoints:**
    * `POST /api/payment/checkout` - Initiates the payment process for a gig
    * `GET /api/payment/orders/{orderId}` - Get the status of a payment/order
    * `POST /api/payment/webhook` - Listens for asynchronous confirmation events from the payment provider
