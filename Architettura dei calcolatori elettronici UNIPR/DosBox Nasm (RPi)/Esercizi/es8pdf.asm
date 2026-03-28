; Converte un numero a 16 bit in decimale senza gli zeri iniziali.
; In input riceve un intero a 16 bit passato sullo stack e il puntatore alla stringa che deve ricevere il carattere.
; ConvertiDecimale (int16 iNumero, char *szStringa);

SECTION data
Numero: DW 134
Stringa: resb 6

SECTION text
..start:

	mov ax,data
	mov ds,ax

	call main

	mov ah,4C00h
	int 21h

main:
	push bp
	mov bp,sp
	
	mov ax,[Numero]
	push ax
	
	mov ax,Stringa		; *
	push ax

	call ConvertiDecimale
	add sp,5

	mov ax,Stringa
	push ax

	call StampaStringa
	add sp,2

Fine: 	pop bp
	ret

ConvertiDecimale:	
	push bp
	mov bp,sp
	
	sub sp,5	;allocazione di 5 caratteri su stack,perchè?

	;Carico il numero in registro
	mov ax,[bp+6]
	mov bx,10	;Divisore

CD_AltroNumero:
	dec si		;???????????

	xor dx,dx
	div bx

	add dl,30h 	; converto in codice ASCII ;??????????? Risultato di div in ax non dx
	mov [bp+si],dl 	; salvo il carattere

	cmp ax,0
	jne CD_AltroNumero

			; Copio i caratteri nella stringa destinazione
	mov di,[bp+4] 	; di è l'indice per la stringa destinazione

CD_AltroCarattere:

	mov dl,[bp+si]
	mov [di],dl
	inc di
	inc si
	jnz CD_AltroCarattere

	mov byte [di],00h ; Terminatore per la stringa destinazione

	mov sp,bp ; Libero la memoria dallo stack
	jmp Fine







