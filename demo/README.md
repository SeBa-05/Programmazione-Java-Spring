# GESTIONE DIPENDENTI - APPLICAZIONE GESTIONALE

## 📌 Introduzione
L'applicazione **Gestione Dipendenti** è un gestionale web sviluppato con **Spring Boot** che consente ad un amministratore di gestire in modo completo i dipendenti di un'azienda, con un sistema di autenticazione a due livelli (ADMIN e GUEST).

---

## 🎯 Funzionalità principali

### 🔐 Autenticazione e autorizzazione
- **Login** con username e password.
- **Logout** per terminare la sessione.
- Due ruoli:
  - **ADMIN**: accesso completo a tutte le funzionalità.
  - **GUEST**: visualizzazione in sola lettura della lista dipendenti.

### 👥 Gestione utenti (solo ADMIN)
- Creazione di nuovi utenti con username, password, email e ruolo (ADMIN/GUEST).
- Visualizzazione della lista di tutti gli utenti registrati.
- Eliminazione di utenti (senza possibilità di modificare password o email).
- Ogni utente può essere associato a un dipendente.

### 👤 Gestione dipendenti (solo ADMIN in scrittura)
- **Inserimento** di un nuovo dipendente con tutti i dati anagrafici e contrattuali.
- **Modifica** di un dipendente esistente.
- **Eliminazione** di un dipendente.
- **Associazione** di un dipendente a un utente (l'email del dipendente viene automaticamente sincronizzata con quella dell'utente).
- **Validazione** automatica dei dati (nome, cognome, codice fiscale, email, data di nascita, stipendio, data assunzione, ruolo).
- **Controllo maggiore età** tramite data di nascita.

### 👁️ Visualizzazione dipendenti (ADMIN e GUEST)
- **Lista paginata** (5 elementi per pagina) con ordinamento per nome.
- **Filtri di ricerca**:
  - Nome e Cognome (ricerca combinata)
  - Codice Fiscale
  - Ruolo (dropdown)
  - Stipendio (range minimo/massimo)
- **Colori distintivi** per i ruoli (PROGRAMMATORE, TEAM_LEADER, PROJECT_MANAGER, TOP_MANAGER).
- **Sola lettura** per gli utenti GUEST (i bottoni di modifica/eliminazione sono nascosti).

### 🖥️ Interfaccia utente
- Design moderno e responsive con **Bootstrap 5** e CSS personalizzato.
- **Form dinamico**: il campo email viene automaticamente compilato e reso readonly quando viene selezionato un utente.
- **Messaggi di errore** chiari e visualizzati direttamente nei campi del form.
- **Barre di ricerca** che compaiono/nascondono in base al filtro selezionato.
- **Paginazione** intuitiva con numeri di pagina.

---

## 👥 Ruoli utente e autorizzazioni

| Funzionalità | ADMIN | GUEST |
|--------------|-------|-------|
| Login / Logout | ✅ | ✅ |
| Visualizzare lista dipendenti | ✅ | ✅ |
| Filtrare dipendenti | ✅ | ✅ |
| Inserire nuovo dipendente | ✅ | ❌ |
| Modificare dipendente | ✅ | ❌ |
| Eliminare dipendente | ✅ | ❌ |
| Gestire utenti (CRUD) | ✅ | ❌ |

---

## 🔄 Flusso di lavoro tipico

### 1. Primo avvio
- L'applicazione viene avviata con un utente **admin** predefinito (username: `admin`, password: `admin`).
- L'admin effettua il login e può iniziare a creare altri utenti e dipendenti.

### 2. Creazione di un nuovo utente (solo ADMIN)
- L'admin va su **"Gestisci Utenti"** → **"Nuovo Utente"**.
- Inserisce username, password, email e ruolo (ADMIN/GUEST).
- L'utente viene salvato e può accedere al sistema.

### 3. Creazione di un nuovo dipendente (solo ADMIN)
- L'admin va su **"Nuovo Dipendente"**.
- Compila i campi obbligatori (nome, cognome, codice fiscale, data di nascita, stipendio, email, data assunzione, ruolo).
- **Opzionale**: associa il dipendente a un utente esistente (l'email viene presa automaticamente dall'utente).
- Il sistema valida i dati e, se corretti, salva il dipendente nel database.

### 4. Visualizzazione e gestione (ADMIN e GUEST)
- Gli utenti loggati (sia ADMIN che GUEST) possono visualizzare la lista dipendenti.
- Gli ADMIN possono modificare o eliminare qualsiasi dipendente.
- I GUEST possono solo visualizzare.

---

## 🛠️ Tecnologie utilizzate

| Componente | Tecnologia |
|------------|------------|
| **Backend** | Spring Boot 4.1.0 |
| **Database** | MySQL |
| **ORM** | Spring Data JPA / Hibernate |
| **Frontend** | Thymeleaf 3.1, Bootstrap 5, CSS3, JavaScript |
| **Validazione** | Jakarta Bean Validation |
| **Lombok** | Per ridurre il boilerplate |
| **Gestione sessioni** | HttpSession integrata |
| **Paginazione** | Spring Data Pageable |

---

## 🚀 Come avviare l'applicazione

1. **Clona** il repository.
2. **Crea** un database MySQL (es. `gestione_dipendenti`).
3. **Aggiorna** le credenziali nel file `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gestione_dipendenti
   spring.datasource.username=root
   spring.datasource.password=password