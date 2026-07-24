/**
 * Gestione dinamica del form dipendenti
 * - Disabilita il campo email quando un utente è selezionato
 * - Compila automaticamente l'email con quella dell'utente selezionato
 */

document.addEventListener('DOMContentLoaded', function() {

    // Riferimenti agli elementi del DOM
    const userSelect = document.getElementById('userSelect');
    const emailInput = document.getElementById('email');
    const emailContainer = document.getElementById('emailContainer');

    if (!userSelect || !emailInput || !emailContainer) {
        return;
    }

    /**
     * Funzione che aggiorna il campo email in base alla selezione dell'utente
     */
    function updateEmailField() {
        const selectedOption = userSelect.options[userSelect.selectedIndex];
        const selectedValue = userSelect.value;

        // Rimuovo eventuali label aggiuntive
        const label = emailContainer.querySelector('label');
        const extraText = label ? label.querySelector('small.text-muted') : null;
        if (extraText) {
            extraText.remove();
        }

        if (selectedValue !== '') {
            // Se è selezionato un utente, estraggo l'email dal testo dell'opzione
            const optionText = selectedOption.text;
            const emailMatch = optionText.match(/\(([^)]+)\)/);

            if (emailMatch && emailMatch[1]) {
                // Imposto l'email e rendo il campo readonly (NON disabled!)
                emailInput.value = emailMatch[1];
                emailInput.readOnly = true;  // <-- CHIAVE: usa readOnly
                emailContainer.classList.add('field-readonly');
                
                // Aggiungo un'etichetta informativa
                if (label) {
                    const small = document.createElement('small');
                    small.className = 'text-muted';
                    small.textContent = ' (usata dall\'utente)';
                    label.appendChild(small);
                }
            }
        } else {
            // Nessun utente selezionato → campo email editabile
            emailInput.readOnly = false;
            emailInput.value = '';
            emailContainer.classList.remove('field-readonly');
        }
    }

    // Ascolto l'evento change sul select
    userSelect.addEventListener('change', updateEmailField);

    // Eseguo la funzione all'avvio per gestire il caso di modifica
    updateEmailField();

    // Pulsante "Crea nuovo utente" – apre la pagina in una nuova finestra
    const createUserBtn = document.querySelector('[data-create-user]');
    if (createUserBtn) {
        createUserBtn.addEventListener('click', function(e) {
            e.preventDefault();
            window.open(this.href, '_blank');
        });
    }

});