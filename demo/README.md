# 📋 Gestione Dipendenti – Documentazione Progetto

Applicazione web gestionale per la gestione di **dipendenti**, **progetti** e **assegnazioni**, con sistema di autenticazione a due livelli (ADMIN / GUEST).

---

## 🚀 Tecnologie utilizzate

| Componente | Tecnologia |
|------------|------------|
| **Backend** | Spring Boot 4.1.0 (Java 21) |
| **ORM** | Spring Data JPA / Hibernate |
| **Database** | MySQL |
| **Template engine** | Thymeleaf 3.1 |
| **Frontend** | Bootstrap 5, CSS3, JavaScript (AJAX) |
| **Validazione** | Jakarta Bean Validation |
| **Lombok** | Riduzione boilerplate code |

---

## 📦 Funzionalità implementate

### 🔐 Autenticazione e autorizzazione
- Login/Logout con sessione HTTP.
- Due ruoli:
  - **ADMIN** → accesso completo (gestione dipendenti, progetti, utenti).
  - **GUEST** → visualizzazione in sola lettura.
- Registrazione utenti **riservata all'ADMIN**.

### 👥 Gestione utenti (solo ADMIN)
- Creazione, visualizzazione ed eliminazione di utenti.
- Assegnazione ruolo (ADMIN/GUEST).
- L'email dell'utente viene utilizzata come email associata al dipendente.

### 👤 Gestione dipendenti (solo ADMIN in scrittura)
- **CRUD completo** (inserimento, modifica, eliminazione).
- **Associazione** a un utente (l'email viene presa dall'utente associato).
- **Validazioni** automatiche: nome, cognome, codice fiscale, data di nascita (maggiore età), stipendio.
- **Lista paginata** (5 elementi per pagina) con ordinamento per nome.
- **Filtri di ricerca**: nome/cognome combinato, CF, ruolo, stipendio (range).

### 📊 Gestione progetti (solo ADMIN in scrittura)
- **CRUD completo** (inserimento, modifica, eliminazione).
- Ogni progetto ha: nome, data inizio, data fine, budget, stato, descrizione.
- **Dettaglio progetto** con due sezioni:
  - **Dipendenti non assegnati** → pulsante "Assegna" (AJAX).
  - **Dipendenti assegnati** → pulsante "Dissocia" (AJAX).
- Aggiornamento in tempo reale delle due liste (senza refresh).
- **Statistiche** nella lista progetti: numero dipendenti assegnati e stipendio medio.

### 🗂️ Modello dati (entità JPA)

| Entità | Descrizione |
|--------|-------------|
| **User** | Utente del sistema (username, password, email, permesso). |
| **Permission** | Ruolo (ADMIN/GUEST). |
| **Dipendente** | Dipendente (nome, cognome, cf, dataNascita, stipendio, riferimento a User). |
| **Progetto** | Progetto (nome, dataInizio, dataFine, budget, stato, descrizione). |
| **DettaglioProgetto** | Associazione molti-a-molti tra Dipendente e Progetto. |

---

## 🚀 Come avviare il progetto

### 1️⃣ Prerequisiti
- **Java 21** installato.
- **MySQL** installato e in esecuzione.
- **Maven** installato (o usare il wrapper `mvnw` incluso).

### 2️⃣ Crea il database
Esegui questo comando SQL sul tuo database MySQL:

```sql
CREATE DATABASE gestione_dipendenti CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;