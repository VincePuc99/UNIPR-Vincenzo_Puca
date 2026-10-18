SECTION data
Frase: db 'Buongiorno',0
Messaggio: db 33,'Vocale (v/V) o consonante (c/C)? '
Errore: db 7,' Errore'

SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	call main

	mov ax,4c00h
	int 21h
main:
	push bp
	mov bp,sp
	xor dl,dl	;uso DL come contatore
	mov si,Frase
Input:
	mov ax,Messaggio
	push ax

	call ScriviStringa
	add sp,2

	xor ax,ax
	lodsb
	cmp al,0
	je FineInput

	mov  ah,0eh 
	mov  bx,0000h 
	int 10h

	mov bl,al	;Salvo in BL il valore di Char estrapolato

	mov ah,00h
	int 16h		;Ora in ax ho input

	push bx		;Char di frase
	push ax		;+4 Input user

	call VerificaRisposta
	add sp,4

	jmp Input

FineInput:	
	mov  ah,0eh 
	mov  bx,0000h 
	mov al,dl
	add al,30h
	int 10h
FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

VerificaRisposta:
	push bp
	mov bp,sp

	mov bx,[bp+6]
	mov ax,[bp+4]

	cmp bl,'a'
	jb Maiuscola

	cmp bl,'z'
	ja Maiuscola

	jmp Skip
Maiuscola:
	add bl,20h
Skip:
	cmp bl,'a'
	je Vocale

	cmp bl,'e'
	je Vocale

	cmp bl,'i'
	je Vocale

	cmp bl,'o'
	je Vocale

	cmp bl,'u'
	je Vocale

	jmp Consonante

Vocale:
	cmp al,'v'
	je RispostaCorretta

	cmp al,'V'
	je RispostaCorretta

	jmp RispostaErrata

Consonante:
	cmp al,'c'
	je RispostaCorretta

	cmp al,'C'
	je RispostaCorretta

	jmp RispostaErrata

RispostaCorretta:
	inc dl
	jmp FineFunc

RispostaErrata:
	mov ax,Errore
	push ax

	call ScriviStringa
	add sp,2

FineFunc:
	mov  ah,0eh 
	mov  bx,0000h 
	mov al,0dh
	int 10h
	
	mov  ah,0eh 
	mov  bx,0000h 
	mov al,0ah
	int 10h

	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 

	push si
	xor cl,cl

	mov si,[bp+4]
	mov cl,[si]
	lodsb
	
	mov  ah,0eh 
	mov  bx,0000h 
Stampa:   
	lodsb 
	int  10h 
	loop Stampa 
fineStampa: 

	pop si
	pop bp 
	ret 