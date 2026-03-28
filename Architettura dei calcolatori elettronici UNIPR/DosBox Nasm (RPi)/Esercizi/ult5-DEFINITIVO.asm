;Scrivere in Assembler per Intel 80x86 la funzione pariodispari che analizza un vettore Vett di 
;lunghezza N (con Vett e N parametri della funzione passati mediante lo stack) e riporta in AX il valore -1 se 
;in Vett ci sono più elementi dispari che pari, 1 se invece ci sono più valori pari che dispari e 0 se sono in 
;egual numero. Il programma chiamante ha il seguente codice Assembler (si supponga che la funzione 
;ScriviStringa visualizzi a video la stringa passata come parametro): 




SECTION data
Vett: db 13,15,22,7,5,3,21,2,0,10 
N: db 10 
StringaDispari: db 'Vett contiene più elementi dispari',0 
StringaPari: db 'Vett contiene più elementi pari',0 
StringaUguale: db 'Vett contiene tanti elementi dispari quanti pari',0 

SECTION text
..start:

	mov ax,data
	mov ds,ax
	mov es,ax
	
	call main

	mov ax,4C00h
	int 21h
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
main:
	push bp
	mov bp,sp

	mov ax,Vett
	push ax

	mov ax,[N]
	push ax

	call Func
	add sp,5

	cmp ax,1
	je StringaPar

	cmp ax,0
	je StringaUgual

	mov ax,StringaDispari	

Push:	push ax

	call ScriviStringa
	add sp,2

	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Func:	push bp
	mov bp,sp
	
	xor dx,dx
	xor cx,cx
	mov si,[bp+6]	;Vett
	mov cl,[bp+4]	;N

CicloCalcolo:
	lodsb
	test al,00000001b
	je IncPari

	inc dl	;in L i dispari

	loop CicloCalcolo
	jmp Format

IncPari:
	inc dh	;in H i pari
	loop CicloCalcolo

Format:			;DL -> Dispari - DH -> Pari
	cmp dl,dh
	ja SceltaAX

	cmp dl,dh
	je Uguali

	mov ax,1
	jmp FineFunc

SceltaAX:	;Dispari Maggiori
	mov ax,-1
	jmp FineFunc

Uguali: mov ax,0

FineFunc:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

StringaPar: 
	mov ax, StringaPari
	jmp Push

StringaUgual:
	mov ax, StringaUguale
	jmp Push

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov si,[bp+4] 

	mov  ah,0eh 
	mov  bx,0000h 
Stampa:   
	lodsb 
	cmp al, 0 
	je fineStampa 
	int  10h 

	jmp Stampa 
fineStampa: 
	pop bp 
	ret 