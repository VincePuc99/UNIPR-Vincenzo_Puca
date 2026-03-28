SECTION data
Stringa1:	resb 255
Stringa2:	db 16,'Prova di stringa'
		times 255-17 db ' '
SECTION text
..start: mov ax, data
	 mov ds, ax
	 mov es, ax

	; Copio Stringa2 in 
	;Stringa1
	xor ch,ch
	mov cl,[Stringa2]
	mov [Stringa1],cl
	mov si,Stringa2+1
	mov di,Stringa1+1

	rep movsb
	; Visualizzo Stringa1
	mov cl,[Stringa1]
	mov si,Stringa1+1

	mov ah,0eh
	mov bx,0000h

Stampa: lodsb	;carico un dato da ds ad ax
	int 10h	;stampo semplicemente perchè ax è già pieno
	loop Stampa
	; Riempio Stringa2 di *
	mov al,'*'
	mov cl,100
	mov [Stringa2],cl
	mov di,Stringa2+1
	rep stosb
	; Visualizzo Stringa2
	mov cl,[Stringa2]
	mov si,Stringa2+1
	mov ah,0eh
	mov bx,0000h

Stampa2: lodsb
	int 10h
	loop Stampa2
Fine:   mov ax, 4c00h
	int 21h