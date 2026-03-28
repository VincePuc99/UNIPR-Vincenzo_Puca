;Scrivere in Assembler per Intel 80x86 la funzione copiaVettore che riceve in ingresso due vettori di byte 
;Vett1  e  Vett2,  e  un  intero  N.  Il  primo  vettore  Vett1  è  di  lunghezza  non  nota,  ma  la  fine  del  vettore  è 
;identificata dal valore -1.  Gli elementi di Vett1 devono essere copiati in Vett2 se e solo se sono maggiori o 
;uguali al valore N. Si abbia cure di terminare (con il valore -1) anche il vettore Vett2. 
;I parametri della funzione sono passati mediante lo stack. La funzione deve riportare il AX il numero di valori 
;copiati. 
;Per l'esempio riportato, la funzione deve riportare in AX il valore 3 e il vettore Vett2 vale: 7, 55, 22, -1 
;Si scriva anche il programma main che chiama la funzione. 

SECTION data

Vett1: db 2, 5, 7, 4, 55, 22, -1 
Vett2: resb 256
N: db 6 

SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	mov ax,Vett1
	push ax

	mov ax,Vett2
	push ax

	xor ah,ah
	mov ax,[N]
	push ax

	call copiaVettore
	add sp,6

	mov ax,Vett2
	push ax

	call ScriviStringa
	add sp,2

	mov ax,4c00h
	int 21h

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

copiaVettore:
	push bp
	mov bp,sp

	xor ch,ch
	xor bx,bx

	mov cl,[bp+4]
	mov si,[bp+8]
	mov di,[bp+6]

Ciclo:
	lodsb 
	cmp al,-1
	je Fine

	cmp al,cl
	jnae NonCopio

	stosb
	inc bl
NonCopio:

	jmp Ciclo

Fine:	stosb

	mov ax,bx

	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov si,[bp+4] 

	mov  ah,0eh 
	mov  bx,0000h 
Stampa:  
	lodsb
	add al,30h
	cmp al, -1
	je fineStampa 
	int  10h 

	jmp Stampa 
fineStampa: 
	pop bp 
	ret 


	