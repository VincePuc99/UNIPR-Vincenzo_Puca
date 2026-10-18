;Scrivere in Assembler per Intel 80x86 la  funzione contacaratteri che conta quante volte si presenta 
;nella  stringa  ASCIIZ  zero-terminata  (secondo  la  convenzione  C)  Stringa  il  carattere  ch  (con  Stringa  e  ch 
;parametri della funzione passati mediante lo stack) e riporta in AX tale numero. 
;Le variabili del programma sono le seguenti: 
;Stringa: db 'Evviva la pappa con il pomodoro',0 
;Ch: db 'p' 
;Si scriva anche il programma main che chiama la funzione. 

SECTION data

Stringa: db 'Evviva la pappa con il pomodoro',0

Parola: db 'p'

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

	mov ax,Stringa
	push ax

	mov ax,Parola
	push ax

	call contacaratteri
	add sp,5

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;;;;

contacaratteri:
	push bp
	mov bp,sp
	xor ax,ax
	xor cx,cx

	mov bl,[bp+4]	;Ch
	mov si,[bp+6]	;Stringa
	
Ciclo:
	lodsb	;AL 
	cmp al,0
	je FineStringa

	cmp al,bl
	je IncString

	jmp Ciclo

IncString:
	inc cx
	jmp Ciclo
	
FineStringa:
	mov ax,cx
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	mov  ah,0eh 
	mov  bx,0000h 

	add al,30h
	int  10h 

fineStampa: 
	
	ret 