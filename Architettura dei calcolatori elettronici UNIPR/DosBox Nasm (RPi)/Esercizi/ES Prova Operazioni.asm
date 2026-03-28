;Prende la variabile in memoria, la somma
;la divide,la moltiplica e la sottrae a 1.
;Scrive il risultato in CH
;Se Preme T esce dal programma

;AX = AL * sorgente;
;AL = Quoziente(AX / sorgente);
;AH = Resto(AX / sorgente);

SECTION data
msg db "Il Valore di CH è: $"
NumeroProva db 5

SECTION text

..start:
	mov ax,data	;inizializzo tutte la var in data segment
	mov ds,ax

Rest:	mov ah,00h	;Pulisic il posto per l'input
	int 16h		;Chiede da tastiera

	cmp al,116	;t	
	je Fine		;hai premuto t minuscolo

	mov al,[NumeroProva]
	add al,1		;Risultato in CH

	mov ah,0eh	;visualizza
	add al,'0'
	mov bx,00h	;pagina
	int 10h

	mov al,[NumeroProva]
	sub al,1		;Risultato in CH

	mov ah,0eh	;visualizza
	add al,'0'
	mov bx,00h	;pagina
	int 10h

	mov al,[NumeroProva]
	mov bl,1		;mul vuole registro come operando
	mul bl			;Risultato in AX

	mov ah,0eh	;visualizza
	add al,'0'
	mov bx,00h	;pagina
	int 10h
	;mov ch,ax		;Registro pieno in Mezzo registro, NO 

	mov ax,[NumeroProva]
	mov bl,1
	div bl

	mov ah,0eh	;visualizza
	add al,'0'
	mov bx,00h	;pagina
	int 10h

	jmp Fine

Fine:   mov ax,4C00h
	int 21h