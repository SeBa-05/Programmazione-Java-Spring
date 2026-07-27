/**
 * Gestione AJAX per assegnare/rimuovere dipendenti da un progetto
 * Aggiornamento in tempo reale delle due tabelle
 */

document.addEventListener('DOMContentLoaded', function() {

    // ============================================
    // UTILITY: aggiorna contatore
    // ============================================
    function aggiornaContatore(delta) {
        const numSpan = document.getElementById('numDipendenti');
        if (numSpan) {
            const current = parseInt(numSpan.textContent) || 0;
            numSpan.textContent = current + delta;
        }
    }

    // ============================================
    // UTILITY: crea una nuova riga per la tabella
    // ============================================
    function creaRiga(dipendente, tipo, progettoId) {
        const tr = document.createElement('tr');
        
        const campi = ['nome', 'cognome', 'cf', 'stipendio'];
        campi.forEach(campo => {
            const td = document.createElement('td');
            if (campo === 'stipendio') {
                td.textContent = parseFloat(dipendente.stipendio).toFixed(2);
            } else {
                td.textContent = dipendente[campo] || '';
            }
            tr.appendChild(td);
        });

        // Email
        const tdEmail = document.createElement('td');
        tdEmail.textContent = dipendente.userEmail || 'N/A';
        tr.appendChild(tdEmail);

        // Azioni
        const tdAzione = document.createElement('td');
        const btn = document.createElement('button');
        if (tipo === 'assegna') {
            btn.className = 'btn btn-success btn-sm btn-assegna';
            btn.textContent = '➕ Assegna';
        } else {
            btn.className = 'btn btn-danger btn-sm btn-rimuovi';
            btn.textContent = '❌ Dissocia';
        }
        btn.dataset.dipendenteId = dipendente.id;
        btn.dataset.progettoId = progettoId;
        tdAzione.appendChild(btn);
        tr.appendChild(tdAzione);

        return tr;
    }

    // ============================================
    // GESTISCI ASSEGNAZIONE
    // ============================================
    function handleAssegna(button) {
        const row = button.closest('tr');
        const dipendenteId = button.dataset.dipendenteId;
        const progettoId = button.dataset.progettoId;

        if (!dipendenteId || !progettoId) {
            alert('Errore: dati mancanti');
            return;
        }

        const celle = row.querySelectorAll('td');
        const dipendente = {
            id: dipendenteId,
            nome: celle[0].textContent,
            cognome: celle[1].textContent,
            cf: celle[2].textContent,
            userEmail: celle[3].textContent,
            stipendio: parseFloat(celle[4].textContent.replace(',', '.'))
        };

        button.disabled = true;
        button.textContent = '⏳...';

        fetch(`/progetti/${progettoId}/assegna?dipendenteId=${dipendenteId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        })
        .then(response => response.text())
        .then(data => {
            if (data === 'success') {
                row.remove();
                aggiornaContatore(1);

                const tbodyAssegnati = document.querySelector('#tabellaAssegnati tbody');
                const nuovaRiga = creaRiga(dipendente, 'rimuovi', progettoId);
                if (tbodyAssegnati) {
                    tbodyAssegnati.appendChild(nuovaRiga);
                }

                const tbodyNonAssegnati = document.querySelector('#tabellaNonAssegnati tbody');
                const containerNonAssegnati = document.querySelector('#tabellaNonAssegnati')?.parentElement;
                if (tbodyNonAssegnati && tbodyNonAssegnati.children.length === 0 && containerNonAssegnati) {
                    containerNonAssegnati.querySelectorAll('p.text-muted').forEach(el => el.remove());
                    const p = document.createElement('p');
                    p.className = 'text-muted';
                    p.textContent = '✅ Tutti i dipendenti sono già assegnati a questo progetto.';
                    containerNonAssegnati.appendChild(p);
                }

            } else {
                alert('Errore: ' + data);
            }
        })
        .catch(error => {
            alert('Errore di rete: ' + error);
        })
        .finally(() => {
            button.disabled = false;
            button.textContent = '➕ Assegna';
        });
    }

    // ============================================
    // GESTISCI RIMUOVI
    // ============================================
    function handleRimuovi(button) {
        const row = button.closest('tr');
        const dipendenteId = button.dataset.dipendenteId;
        const progettoId = button.dataset.progettoId;

        if (!dipendenteId || !progettoId) {
            alert('Errore: dati mancanti');
            return;
        }

        if (!confirm('Sei sicuro di voler rimuovere questo dipendente dal progetto?')) {
            return;
        }

        const celle = row.querySelectorAll('td');
        const dipendente = {
            id: dipendenteId,
            nome: celle[0].textContent,
            cognome: celle[1].textContent,
            cf: celle[2].textContent,
            userEmail: celle[3].textContent,
            stipendio: parseFloat(celle[4].textContent.replace(',', '.'))
        };

        button.disabled = true;
        button.textContent = '⏳...';

        fetch(`/progetti/${progettoId}/rimuovi?dipendenteId=${dipendenteId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        })
        .then(response => response.text())
        .then(data => {
            if (data === 'success') {
                row.remove();
                aggiornaContatore(-1);

                const tbodyNonAssegnati = document.querySelector('#tabellaNonAssegnati tbody');
                const nuovaRiga = creaRiga(dipendente, 'assegna', progettoId);
                if (tbodyNonAssegnati) {
                    tbodyNonAssegnati.appendChild(nuovaRiga);
                }

                const containerNonAssegnati = document.querySelector('#tabellaNonAssegnati')?.parentElement;
                if (containerNonAssegnati) {
                    containerNonAssegnati.querySelectorAll('p.text-muted').forEach(el => el.remove());
                }

            } else {
                alert('Errore: ' + data);
            }
        })
        .catch(error => {
            alert('Errore di rete: ' + error);
        })
        .finally(() => {
            button.disabled = false;
            button.textContent = '❌ Dissocia';
        });
    }

    // ============================================
    // EVENT DELEGATION
    // ============================================
    document.addEventListener('click', function(e) {
        const button = e.target.closest('.btn-assegna, .btn-rimuovi');
        if (!button) return;
        e.preventDefault();

        if (button.classList.contains('btn-assegna')) {
            handleAssegna(button);
        } else if (button.classList.contains('btn-rimuovi')) {
            handleRimuovi(button);
        }
    });

});