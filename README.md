# **HireMate – Job Application Workflow Management System (Backend)**

HireMate is a **backend-focused job application management system** designed to help users track applications, manage their lifecycle, and maintain full auditability.  
 The project emphasizes **correctness, security, and domain-driven design** over UI features.

---

## **🚀 Key Features**

### **🔐 Secure Multi-User Architecture**

* JWT-based authentication using **Spring Security**

* **Row-level data isolation** ensuring users can only access and modify their own data

* Ownership enforced consistently at the service layer

### **🔁 Application Lifecycle Management**

* Applications follow a **state-machine–driven workflow**

* Validated transitions (e.g., `APPLIED → INTERVIEW → OFFER`)

* Terminal states prevent illegal updates

* All transitions are **transactional** to ensure consistency

### **🧾 Immutable Audit Trail**

* Every application status change is recorded in a dedicated history table

* Append-only design enables:

  * Traceability

  * Debugging

  * Future analytics

### **📄 Read-Optimized APIs**

* Paginated and filterable REST APIs

* Filter applications by:

  * status

  * company

  * platform

* Sorting support for scalable read performance

---

## **🧠 Design Principles**

* **Domain-driven design** with clear separation of concerns

* Business rules enforced in the **service layer**, not controllers

* State transitions modeled explicitly (no free-form updates)

* Audit data treated as immutable

* No trust in client-provided identifiers

---

## **🏗️ Tech Stack**

* **Language:** Java

* **Framework:** Spring Boot

* **Security:** Spring Security, JWT

* **Persistence:** JPA / Hibernate

* **Database:** H2 (dev), MySQL-compatible

* **API Style:** REST

---

## **📦 Core Modules**

* **Auth & User Management** – JWT-based login and access control

* **Company Management** – User-scoped company tracking

* **Resume Management** – Resume metadata and ownership enforcement

* **Job Applications** – Core domain with lifecycle workflow

* **Status History** – Audit trail for all application transitions

---

## **🔄 Application Status Workflow**

Each job application follows a controlled lifecycle:

`APPLIED → OA → INTERVIEW → OFFER → ACCEPTED`  
                    `↘`  
                     `REJECTED / WITHDRAWN / GHOSTED`

* Only predefined transitions are allowed

* Terminal states cannot be modified

---

## **📌 API Highlights**

* `POST /applications` – Create a new job application

* `GET /applications` – List applications with pagination & filters

* `PATCH /applications/{id}/status` – Change application status (validated)

* `GET /applications/{id}/history` – View status change history

---

