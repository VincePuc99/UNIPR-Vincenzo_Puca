;Scrivere in Assembler per Intel 80x86 la funzione mischia_stringhe che riceve in ingresso due stringhe 
;zero-terminate Sorg1 e Sorg2 e una stringa Dest (sempre zero terminata). La funzione deve copiare in Dest 
;alternativamente un carattere da Sorg1 e uno da Sorg2, partendo da Sorg1. Se una delle due stringhe termina 
;prima, i rimanenti caratteri dell'altra stringa vanno ricopiato in Dest. Tutti i parametri sono passati mediante 
;lo stack. --> ECvavrinveavale,0 

CPU 8086
SECTION data
Sorg1: db 'Evviva',0 
Sorg2: db 'Carnevale',0 
Dest: resb 100

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

	mov ax,Sorg1
	push ax

	mov ax,Sorg2
	push ax

	mov ax,Dest
	push ax

	call mischia_stringhe
	add sp,6

	mov ax,Dest
	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;

mischia_stringhe:

	push bp
	mov bp,sp

	mov si,[bp+8]
	mov bx,[bp+6]
	mov di,[bp+4]

Ciclo:
	lodsb
	cmp al,0
	je FineString1

	mov ah,[bx]
	cmp ah,0
	je FineString2
	
	stosb

	mov al,[bx]
	stosb

	inc bx
	jmp Ciclo

FineString1:	;Finisco la seconda stringa

	mov ah,[bx]
	cmp ah,0
	je FineFunc

	mov al,[bx]
	stosb
	
	inc bx

	jmp FineString1

FineString2:	;Finisco la prima stringa

	lodsb
	cmp al,0
	je FineFunc

	stosb

	jmp FineString2

FineFunc:
	mov al,0
	stosb
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;

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