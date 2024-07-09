;Scrivere  in  Assembler  per Intel  80x86  la  funzione sommaparidispari  che analizza  un  vettore  Vett  di 
;lunghezza N (con Vett e N parametri della funzione passati mediante lo stack) e calcola la somma dei valori 
;pari e la somma dei valori dispari. La funzione riporta in AX la differenza tra questi due valori. 
;la funzione riporta in AX il valore 24 (somma pari = 61, somma dispari = 37). 
;Si scriva anche il programma main che chiama la funzione. 

SECTION data

Vett: db 13,15,22,7,5,3,21,2,0,10 
N: db 10 
Dest:	resb 100

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

	mov ax,Vett
	push ax

	xor ax,ax
	mov ax,[N]
	push ax

	call sommaparidispari
	add sp,4

	mov ax,Dest
	push ax
	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;

sommaparidispari:
	push bp
	mov bp,sp

	xor cx,cx
	xor bx,bx	;BL Pari -- BH Dispari
	mov si,[bp+6]	;Vett
	mov cl,[bp+4]	;N
	mov di,Dest

Ciclo:
	lodsb
	dec si
	test si,1b
	je Pari

	add bh,al
Continue:	
	inc si
	loop Ciclo
	jmp FineFunc
Pari:
	add bl,al
	jmp Continue

FineFunc:
	sub bl,bh
	xor bh,bh
	mov ax,bx

	
	stosb
	mov al,'0'
	stosb

	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;


ScriviStringa: 
	push bp 
	mov bp, sp 
	mov si,[bp+4] 

	mov  ah,0eh 
	mov  bx,0000h 
	
Stampa:   
	lodsb 
	add al,24
	cmp al, 0 
	je fineStampa 

	int  10h 

	jmp Stampa 
fineStampa: 
	pop bp 
	ret 
;;;;;;;;;;;;;;;;;;;;;;
