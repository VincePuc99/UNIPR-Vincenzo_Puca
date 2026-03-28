SECTION data
Stringa: db 'Hello world, con funzione in stile C.',0dh,0ah,0 ;Enter 0dh 0ah
SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax
	
	push ax		;variabili in ax

	call ScriviStringa

	add sp,2
; Fine programma
	mov ah,4ch ; Function 4Ch (76) - Terminate a Process (EXIT)
	mov al,0 ; Codice di ritorno
	int 21h

ScriviStringa:
	push bp
	mov bp,sp	

	mov ah,02h ; Function 02h (2) - Character Output

	mov bx,[bp+4] ; Leggo l'indirizzo della stringa

AltroCarattere:

	mov dl,[bx]

	cmp dl,0
	je Fine

	int 21h
	inc bx
	jmp AltroCarattere
Fine:
	pop bp
	ret