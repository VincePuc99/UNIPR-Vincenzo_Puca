;Scrivere in Assembler per Intel 80x86 la funzione CopiaCaratteri che riceve in ingresso una stringa Sorg, 
;una stringa Dest,un vettore di byte Vett, e un intero (byte) N. N rappresenta la lunghezza delle stringhe (che 
;non sono zero terminate) e Vett contiene N elementi byte positivi. I parametri della funzione sono passati 
;mediante lo stack. La funzione deve copiare nella stringa Dest in posizione i il carattere della stringa Sorg in 
;posizione Vett[i]. Nel caso Vett[i] sia maggiore di N si copia in Dest l'ultimo carattere di Sorg (cioè in posizione N). 
;'Bniu  outuBAati  n '.

CPU 8086
SECTION data
Sorg: db 'Buon Anno a tutti' 
N: db 17 
Dest: resb 17
Vett: db 1, 7, 25, 2, 10, 3, 2, 15, 2, 1, 6, 11, 16, 17, 5, 4, 12

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

	mov ax,Sorg
	push ax

	mov ax,Dest
	push ax

	mov ax,Vett
	push ax

	mov ax,[N]
	push ax
	
	call CopiaCaratteri
	add sp,7

	mov ax,Dest
	push ax

	mov ax,[N]
	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;

CopiaCaratteri:
	push bp
	mov bp,sp

	xor cx,cx

	mov cl,[bp+4]	;N
	mov si,[bp+10]	;Sorg
	mov di,[bp+8]	;Dest
	mov bx,[bp+6]	;Vett

Ciclo:	
	mov dl,[bx]	;Metto il valore di vect in dl
			;Quando devi aggiungere qualcosa in si deve essere in 16esimi
	cmp dl,[bp+4]
	jnae AddN

	mov dl,[bp+4]
AddN:
	add [si],dx
	
	mov al,[si]	
	stosb

	inc bx
	inc si

	loop Ciclo

FineFunc:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 

	mov cl,[bp+4]
	mov si,[bp+6] 

	mov  ah,0eh 
	mov  bx,0000h 
Stampa:   
	lodsb 
	int  10h 

	loop Stampa 

	pop bp 
	ret 

