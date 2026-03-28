;Scrivere in Assembler per Intel 80x86 la funzione correggi_stringa che riceve in ingresso 2 parametri: 
;una  stringa  di  caratteri  zero-terminata  Sorg  e  un  singolo  carattere  Car.  Tutti  i  parametri  sono  passati 
;mediante lo stack.  
;La funzione deve scorrere la stringa Sorg e cercare il carattere Car. Dove lo trova, deve sostituire il carattere 
;Car con il carattere precedente nel codice ASCII. 
;In questo caso alla fine in Sorg ci dovrebbe essere la stringa zero-terminata 'Buom Natale e Felice Ammo Nuovo'. 
;Si scriva anche il programma main che chiama la funzione.

SECTION data
Sorg: db 'Buon Natale e Felice Anno Nuovo',0 
Car: db 'n'

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

	mov ax,Sorg
	push ax

	xor ah, ah 
	mov al, [Car] 
	push ax
 
	call correggi_stringa
	add sp,4

	mov ax,Sorg
	push ax

	mov ax,ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;

correggi_stringa:
	push bp
	mov bp,sp

	mov si,[bp+6]	;Sorg
	mov ah,[bp+4]	;Car

Ciclo:
	lodsb
	cmp al,0
	je FineFunc

	cmp al,ah
	jne Salta

	dec al
	mov [si-1],al
Salta:
	jmp Ciclo

FineFunc:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov si,[bp+4] 

	mov  ah,0eh 
	mov  bx,0000h 
Stampa:   
	lodsb 
	cmp al,0 
	je fineStampa 
	int  10h 

	jmp Stampa 
fineStampa: 
	pop bp 
	ret 
