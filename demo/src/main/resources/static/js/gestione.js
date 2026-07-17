/**
 * Gestione filtri per la pagina di gestione dipendenti
 * Author: Your Name
 * Version: 1.0
 */

(function() {
    'use strict';

    // =====================================================
    // RIFERIMENTI AGLI ELEMENTI DOM
    // =====================================================
    const tabella = document.getElementById('tabella-dipendenti');
    const righe = tabella.querySelectorAll('tbody tr');
    const contenitori = document.querySelectorAll('.filtro-container');
    const bottoni = document.querySelectorAll('[data-filtro]');

    // =====================================================
    // 1. GESTIONE MOSTRA/NASCONDI BARRE DI RICERCA
    // =====================================================
    bottoni.forEach(btn => {
        btn.addEventListener('click', function(e) {
            const tipo = this.dataset.filtro;

            // Pulsante RESET: nasconde tutto e mostra tutte le righe
            if (tipo === 'reset') {
                contenitori.forEach(c => c.style.display = 'none');
                document.querySelectorAll('.filtro-container input, .filtro-container select')
                    .forEach(el => {
                        if (el.tagName === 'SELECT') el.selectedIndex = 0;
                        else el.value = '';
                    });
                righe.forEach(row => row.style.display = '');
                bottoni.forEach(b => b.classList.remove('active'));
                return;
            }

            // Nascondo tutti i container
            contenitori.forEach(c => c.style.display = 'none');

            // Mostro il container corrispondente al filtro selezionato
            const container = document.getElementById('filtro-' + tipo);
            if (container) {
                container.style.display = 'block';
                const primoInput = container.querySelector('input, select');
                if (primoInput) {
                    setTimeout(() => primoInput.focus(), 100);
                }
            }

            // Aggiungo classe active al bottone premuto, rimuovo dagli altri
            bottoni.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // =====================================================
    // 2. LOGICA DI FILTRAGGIO (per input di testo)
    // =====================================================
    function filtraTesto(colonne, testo) {
        const testoMin = testo.toLowerCase().trim();
        righe.forEach(row => {
            const celle = row.querySelectorAll('td');
            let match = false;
            
            if (Array.isArray(colonne)) {
                // Ricerca su più colonne
                colonne.forEach(col => {
                    if (celle.length > col) {
                        const testoCell = celle[col].textContent.toLowerCase().trim();
                        if (testoCell.includes(testoMin)) {
                            match = true;
                        }
                    }
                });
            } else {
                // Ricerca su una singola colonna
                if (celle.length > colonne) {
                    const testoCell = celle[colonne].textContent.toLowerCase().trim();
                    if (testoCell.includes(testoMin)) {
                        match = true;
                    }
                }
            }
            
            row.style.display = match ? '' : 'none';
        });
    }

    // Input con data-colonna (singola colonna)
    document.querySelectorAll('.filtro-container input[data-colonna]').forEach(input => {
        input.addEventListener('input', function() {
            const colonna = parseInt(this.dataset.colonna);
            filtraTesto(colonna, this.value);
        });
    });

    // Input con data-colonne (multipla colonna)
    document.querySelectorAll('.filtro-container input[data-colonne]').forEach(input => {
        input.addEventListener('input', function() {
            const colonne = this.dataset.colonne.split(',').map(Number);
            filtraTesto(colonne, this.value);
        });
    });

    // =====================================================
    // 3. LOGICA DI FILTRAGGIO (per select del ruolo)
    // =====================================================
    const selectRuolo = document.querySelector('#filtro-ruolo select');
    if (selectRuolo) {
        selectRuolo.addEventListener('change', function() {
            const colonna = parseInt(this.dataset.colonna);
            const valore = this.value;
            righe.forEach(row => {
                const celle = row.querySelectorAll('td');
                if (celle.length > colonna) {
                    const testoCell = celle[colonna].textContent.trim();
                    row.style.display = (valore === '' || testoCell === valore) ? '' : 'none';
                }
            });
        });
    }

    // =====================================================
    // 4. LOGICA DI FILTRAGGIO (per stipendio - range)
    // =====================================================
    const btnFiltraStipendio = document.getElementById('btn-filtra-stipendio');
    if (btnFiltraStipendio) {
        btnFiltraStipendio.addEventListener('click', function() {
            const container = document.getElementById('filtro-stipendio');
            const minInput = container.querySelector('input[data-tipo="min"]');
            const maxInput = container.querySelector('input[data-tipo="max"]');
            const colonna = parseInt(minInput.dataset.colonna);

            const min = parseFloat(minInput.value) || 0;
            const max = parseFloat(maxInput.value) || Infinity;

            righe.forEach(row => {
                const celle = row.querySelectorAll('td');
                if (celle.length > colonna) {
                    const testo = celle[colonna].textContent.replace(/[^\d.,]/g, '').replace(',', '.');
                    const stipendio = parseFloat(testo) || 0;
                    row.style.display = (stipendio >= min && stipendio <= max) ? '' : 'none';
                }
            });
        });
    }

    // =====================================================
    // 5. PULSANTI DI RESET AGGIUNTIVI (se necessario)
    // =====================================================
    // Se ci sono bottoni di reset aggiuntivi, puoi gestirli qui

})();