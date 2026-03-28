SECTION data 
vettoreOrdinato: dw -10, -8, -3, 0, 1, 1, 1, 10, 24, 33, 37, -1
N:          dw 1 
SECTION text 
..start: 
        mov ax, data 
        mov ds, ax 
        mov es, ax 
        mov si, vettoreOrdinato 
Ciclo:      lodsw 
        cmp ax, -1 
        je Fine 
        cmp ax, [N] 
        jne Ciclo 
        push si       ; devo salvare SI perchè Rimuovi lo modifica 
        call Rimuovi    ; in SI-1 ho l'elemento da rimuovere 
        pop si        ; recupero SI 
        dec si        ; riporto indietro SI di 2 perchè ho spostato 
;gli elementi del vettore indietro 
        dec si 
        jmp Ciclo 
Fine:         
        mov ax, 4C00h         ; servizio esci (return code=0)  
        int 21h 
     
Rimuovi:    mov di, si      ; elemento successivo a quello da rimuovere 
        dec di        ; DI punta sull'elemento da rimuovere 
        dec di         ; due decrementi per tenere conto che sono 
;word (2 byte) 
        ; copio tutti gli elementi da DS:SI a ES:DI 
Ciclo2:      lodsw 
        stosw 
        cmp ax, -1      ; verifico se sono a fine vettore 
        je FineFunzione 
        jmp Ciclo2 
FineFunzione:  ret  