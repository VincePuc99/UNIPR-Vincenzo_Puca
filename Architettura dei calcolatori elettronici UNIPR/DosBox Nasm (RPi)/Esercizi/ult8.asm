;Scrivere in Assembler per Intel 80x86 la funzione decrementaStringa che riceve in ingresso una stringa 
;ASCIIZ (zero terminata) Stringa e un vettore di byte Vett. Si supponga che il vettore contenga tanti elementi 
;quanti sono i caratteri della stringa. I parametri della funzione sono passati mediante lo stack. La funzione 
;deve sostituire ogni carattere della stringa decrementandone il valore ASCII del corrispondente valore del 
;vettore, eccetto nel caso di carattere ' ' (spazio). 
;Asnn Maqajd

SECTION data
Stringa: db 'Buon Natale',0 
Vett: db 1, 2, 1, 0, 5, 1, 0, 3, 0, 2, 1 
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
	
	mov ax,Stringa
	push ax

	mov ax,Vett
	push ax

	call decrementaStringa
	add sp,5

	mov ax,Stringa
	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;

decrementaStringa:
	push bp
	mov bp,sp

	mov si,[bp+6]	;Stringa
	mov di,[bp+4]	;Vett

Ciclo:
	lodsb 
  	cmp al, 0 
  	je FineFunc 
  	cmp al,' '
  	je Spazio 
	
  	sub al, [di]  
	inc di 
 	mov [si-1], al 
  	jmp Ciclo 

Spazio:
	jmp Ciclo

FineFunc:			
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;

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