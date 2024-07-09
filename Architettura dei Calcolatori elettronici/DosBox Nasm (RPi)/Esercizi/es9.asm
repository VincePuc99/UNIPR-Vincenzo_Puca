SECTION data
VectOrder: db 0, 1, 2, 3, 5, 6, -1 
VarN: db 1

SECTION text

..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	;xor cx,cx

	mov si,VectOrder	;metti in SI -10?

MenoUno:
	lodsb
	cmp ax,-1	;ax perchè vettore 16 byte?
	je Fine

	cmp ax,[VarN]	;trovato valore uguale a N, ax perchè variabile 16 byte?
	jne MenoUno	;mi serve la call ma non posso usarla sotto al cmp

	push si		;salvo su richiesta dell'esercizio
	call Remove
	pop si

	dec si		;ritorno indietro di due Corrente->Trovato perchè?
	

	jmp MenoUno

Fine:	call Printata
	mov ax,4C00h
	int 21h

Remove:	mov di,si 	;backup ; copio tutti gli elementi da DS:SI a ES:DI 
	dec di		;ESEMPIO 8 19 32 45 -1 -->SI
			;	 8 19	-->DI
			;Il ciclo prende 32 e lo mette in al, poi in DI, quindi 8 19 32 -->DI
			;Se -1 torni su e ripristini SI 
	

SpazzolaDI:
	lodsb		;SI-->AL
	stosb		;AL-->DI

	cmp ax,-1
	je Finefunc
	jmp SpazzolaDI
Finefunc:	ret

Printata:
	mov si,VectOrder
tutto:	
	add al,30h
	
	mov ah,0eh
	mov bx,00h
	int 10h
	cmp al,-1
	je Ret

	jmp tutto

Ret:	ret


