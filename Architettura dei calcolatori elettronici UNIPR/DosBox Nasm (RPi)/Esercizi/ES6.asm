SECTION text
..start:
	mov ax,8
	push ax

	call CalcolaFattoriale

	add sp,2

	mov ax,4c00h
	int 21h

CalcolaFattoriale:
	push bp
	mov bp,sp
			; Devo fare Num*CalcolaFattoriale(Num-1), quindi devo prima ottenere
			; il risultato di CalcolaFattoriale(Num-1)
	mov ax,1 
	mov bx,[bp+4]
	dec bx
			; Se mi hanno passato un 1 allora adesso ho uno zero e quindi posso
			; ritornare 1.
	jz Fine

	push bx

	call CalcolaFattoriale

	add sp,2
			; Il risultato è in AX, che è proprio il registro usato dalla MUL
	mul word [bp+4]
			; Il risultato va in DX:AX, ma ignoriamo la parte finita in DX.
Fine:
	pop bp
	ret
