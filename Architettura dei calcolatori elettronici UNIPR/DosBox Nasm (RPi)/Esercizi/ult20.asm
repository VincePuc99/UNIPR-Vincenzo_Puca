;Scrivere in Assembler per Intel 80x86 la funzione trova_max che riceve in ingresso un vettore Vett di byte 
;(dati a 8 bit) e un valore intero N (che rappresenta il numero di elementi del vettore Vett). La funzione deve 
;trovare il valore massimo in Vett e riportare in AX l'indice (posizione) corrispondente in Vett (nel caso di più 
;elementi con il valore massimo si deve riportare l'ultimo). Tutti i parametri sono passati mediante lo stack.   
;La funzione deve ritornare in AX il valore 3 (si ricorda che il primo elemento del vettore ha indice 0). 
;Si scriva anche il programma main che chiama la funzione. 

SECTION data
Vett: db 4, 9, 2, 7, 4 
N: db 5 

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

	call trova_max
	add sp,4

	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

trova_max:
	push bp
	mov bp,sp

	xor cl,cl
	mov si,[bp+6]
	mov cl,[bp+4]

	mov ah,[si]	;Primo valore di referenza
Ciclo:
	lodsb
	cmp ah,al
	jbe Maggiore

	loop Ciclo
	jmp FineFunc
	
Maggiore:
	xchg ah,al	;Mi salvo il maggiore in ah
	mov bx,si

	loop Ciclo

FineFunc:
	mov ax,bx
	add ax,30h
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov al,[bp+4] 

	mov  ah,0eh 
	mov  bx,0000h 

	int  10h 


	pop bp 
	ret 

