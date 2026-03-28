SECTION data 
Conta:	db 0 

SECTION text

..start:
	 mov cl,[Conta]
Restart: mov ah,00h	;AL
	 int 16h

	 cmp al,20h
	 je StampaConto

	 cmp al,1bh
	 je Fine
	
	 inc cl

	 Call Stampa	;  <---

	 jmp Restart

StampaConto: 	
	 Call TrasformaNumero
	 jmp Restart

Fine:	 mov ax,4c00h
	 int 21h

Stampa: mov  ah,0eh    
        mov  bx,00h
        int  10h 
	ret 		;return torna al punto della call (qui <---)

TrasformaNumero:
	mov [Conta], cl
	mov al, cl 
        xor ah, ah 	;Azzera? Probabilmente dovuto a stampa precedente in Stampa: 
			;e per crearsi posto al risultato della divisione
	mov dl, 100	;suppongo massimo 3 cifre (max 99 lettere)
	div dl 		;quoziente in AL resto in AH (AL divisore / Sorgente(in questo caso DL) Dividendo)
	push ax		;salva il risultato in stack

	cmp al,0 	;risultato pari a 0
	je Salto1 
        add al, 30h    ; trasformo il numero nel suo codice ascii, 
        call Stampa 

Salto1: pop ax      ; recupero dallo stack 
        xchg al, ah    ; ora in al ho il resto della divisione precedente 
        xor ah, ah 
        mov dl, 10 
        div dl 
        push ax     ; salvo il risultato sullo stack 
        cmp al, 0 
        je Salto2 
        add al, 30h 
        call Stampa 

Salto2: pop ax      ; recupero dallo stack e salvo in ax
        xchg al, ah 
        add al, 30h    ; trasformo il numero nel suo codice ascii 
        call Stampa 
        call ACapo 
        mov cl, 0 ; riazzero il contatore 
        ret 
     
ACapo:  mov al, 0dh    ; Carriage return (CR) 
        call Stampa     
        mov al, 0ah    ; Line Feed (LF)  

        call Stampa 
        ret

;se 32 dividi per 100 = 0,32 cmp con 0 se è 0 salto il print
; poppo 0,32 in ax exchange scambio e ho 32 in al, diviso 10 = 3,2 pusho e valuto
; cmp 0 se si salto se no stampo il 3, repoppo 3,2 exchange quindi ho davanti il 2 in al
;stampo e ritorna all'inizio
;Commento: Che Tecnica!













