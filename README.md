# BankingOperationsEngine

A domain-driven banking system built in Java that models real-world financial operations using immutable transactions and derived balances.

This project focuses on **correctness, auditability, and clean architecture** rather than UI or frameworks.

---

## ✨ Key Features

- Immutable transaction ledger
- Derived account balances (no stored balance state)
- Explicit account state enforcement
- Insufficient funds protection
- Safe transfer modeling using linked transactions
- Domain-specific exceptions for business rule violations

---

## 🧠 Design Principles

- **Transactions are immutable facts**
- **Balance is derived, never stored**
- **State rules enforced at the domain level**
- **Money safety validated before facts are created**
- **Clear separation of responsibilities**

---

## 🏗️ Architecture Overview

---

## 🔒 Business Rules Enforced

- No overdrafts
- No partial transfers
- No illegal state transitions
- No mutation of transaction history
- All failures are explicit and intentional

---

## 🚧 Current Scope

- No persistence layer (repositories coming later)
- No concurrency handling (intentionally deferred)
- No UI or API layer (planned future work)
- No frameworks (domain-first approach)

---

## 🧪 Future Work

- Repository interfaces & persistence adapters
- Service layer
- Concurrency protection
- Web & desktop UI (JavaScript + Electron)

---

## 📜 License

This project is licensed under the **MIT License**.

