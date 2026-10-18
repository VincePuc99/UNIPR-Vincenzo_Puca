;Scrivere in Assembler per Intel 80x86 la funzione confrontaVettori che riceve in ingresso due vettori 
;di byte Vett1 e Vett2, entrambi di lunghezza N, anch'esso fornito come parametro (i parametri della funzione 
;sono passati mediante lo stack). La funzione deve riportare il AX il numero di volte in cui gli elementi della 
;stessa posizione in Vett1 e Vett2 sono uno pari e l'altro dispari, o viceversa. 
;non sono entrambi pari o entrambi dispari

SECTION data
Vett1: db 2, 5, 7, 4, 55, 22 
Vett2: db 10, 9, 4, 22, 24, 3 
N: db 6 
SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	call main
	
	mov ax,4C00h
	int 21h

main:
	push bp
	mov bp,sp
	
	mov ax,Vett1
	push ax

	mov ax,Vett2
	push ax

	mov al,byte [N]
	push ax

	call confrontaVettori
	add sp,6

	call ScriviStringa
	add sp,1

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

confrontaVettori:
	push bp
	mov bp,sp

	xor bx,bx
	xor cx,cx
	mov cl,[bp+4]
	mov si,[bp+8]	;Vett1
	mov di,[bp+6]	;Vett2

Ciclo:
	lodsb
	mov ah,[di]
	inc di	

	test al,00000001b	;con -> and al,00000001b mette 0 in al se pari 1 se dispari quindi and ah (che è 0) con [di] stessa cosa
	je Vett1Pari	;Vett1 Pari

	test ah,00000001b
	je Vett2Pari	;Vett2 Pari

	loop Ciclo
	jmp FineFunc

Vett1Pari:
	test ah,00000001b
	jne DispariVett2

	loop Ciclo
	jmp FineFunc

DispariVett2:

	inc bx
	loop Ciclo
	jmp FineFunc

Vett2Pari:

	test al,00000001b
	jne DispariVett1

	loop Ciclo
	jmp FineFunc

DispariVett1:
	inc bx

	loop Ciclo
	jmp FineFunc

FineFunc:
	mov ax,bx
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	mov  ah,0eh 
	mov  bx,0000h 
	add al,'0'
	int  10h 

	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;