;Scrivere in Assembler per Intel 80x86 la  funzione calcolalunghezzastringa che  riceve in ingresso 
;una stringa ASCIIZ (stringa zero terminata, come in C) Stringa (il cui indirizzo è passato mediante lo stack). La 
;funzione deve calcolare la lungezza della stringa e riportarla in AX. Inoltre deve riportare in CX il valore 1 se 
;tale lunghezza è un numero pari e 0 se è un numero dispari. 
;Si scriva anche il programma main che chiama la funzione.

CPU 8086
SECTION data
Stringa: db 'Carnevale',0
SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	call main

	mov ax,4c00h
	int 21h

;;;;;;;;;;;;;;;;;;;;;;;;

main:
	push bp
	mov bp,sp
	
	mov ax,Stringa
	push ax

	call calcolalunghezzastringa
	add sp,4

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;

calcolalunghezzastringa:

	push bp
	mov bp,sp

	xor cl,cl
	mov si,[bp+4]

Ciclo:
	lodsb
	cmp al,0
	je FineFunc

	inc cl
	jmp Ciclo

FineFunc:
	mov ax,cx
	xor cx,cx

	test ax,1b
	je Pari

Dispari:
	mov cx,0
	pop bp
	ret

Pari:	mov cx,1
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;