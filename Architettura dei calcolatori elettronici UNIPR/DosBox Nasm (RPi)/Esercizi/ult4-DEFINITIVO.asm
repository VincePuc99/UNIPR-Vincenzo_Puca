;Scrivere in Assembler  per Intel  80x86  un  programma  che  riceve in ingresso  (tramite variabili  definite  nel 
;segmento dati) tre stringhe ASCIIZ zero-terminate (secondo la convenzione C) Stringa1, Stringa2 e 
;Stringa3. Il  programma  deve  riportare in Stringa3   una  parola  di Stringa1   seguita  da  una  di Stringa2, 
;seguita da una Stringa1, e così via. Le parole sono delimitate da singoli spazi. 
;A titolo di esempio, considerando il seguente segmento dati: 
;Stringa1: db 'Io sto bene.',0 
;Stringa2: db 'oggi molto', 0 
;Stringa3: resb 255 
;il programma deve riportare in Stringa3 la stringa ASCIIZ 'Io oggi sto molto bene.' 

CPU 8086

SECTION data
Stringa1: db 'Io sto bene.',0 
Stringa2: db 'oggi molto', 0 
Stringa3: resb 255 

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

	mov ax,Stringa1
	push ax

	mov ax,Stringa2
	push ax

	mov ax,Stringa3
	push ax

	call Func
	add sp,6

	mov ax, Stringa3 
  	push ax
	 
  	call ScriviStringa 
  	add sp, 2 

Fine:	pop bp
	ret

Func:	push bp
	mov bp,sp

	mov di,[bp+4]	;Stringa3 arrivo
	mov si,[bp+8]	;Stringa1
	mov bx,[bp+6]	;Stringa2	;Perchè in bx dirai? Perchè dopo posso fare la mov al,bx questo perchè ax e bx sono interoperabili
					;Se sei hombre de bal prova a metterlo in dx, potrai soltanto fare mov al,dl e non al,dx

	jmp Ciclo1
PrimaStringa:
	inc bx
	stosb

Ciclo1:	lodsb
	cmp al,0
	je FinePrima

	cmp al,' '
	je SwitchPrima

	stosb

	jmp Ciclo1

SwitchPrima:
	stosb

Ciclo2:	mov al,[bx]
	cmp al,0
	je FineSeconda

	cmp al,' '
	je PrimaStringa

	inc bx
	stosb

	jmp Ciclo2

FineSeconda:
	mov al,' '
	stosb
	
CicloF1:
	lodsb
	cmp al,0
	je FineFunc

	stosb

	jmp CicloF1

FinePrima:
	mov al,' '
	stosb	
	
CicloF2:
	mov al,[bx]
	cmp al,0
	je FineFunc

	inc bx
	stosb

	jmp CicloF2

FineFunc:
	mov al, 0 
  	stosb 

	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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
	ret                   ;Ritorno alla procedura chiamante 