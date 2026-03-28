;Scrivere in Assembler per Intel 80x86 la  funzione InvertiStringa che riceve in ingresso due stringhe 
;StringaS e StringaD, e  un  intero a  16  bit  N  (passati mediante  lo  stack). ------ La  stringa  sorgente StringaS è  di 
;lunghezza  fissa indicata  dal  parametro N.  La  funzione  deve  ricopiare  nella  stringa  destinazione StringaD i 
;caratteri della stringa sorgente, ma in ordine inverso e un carattere sì e uno no. Quindi, ad esempio, se la 
;stringa sorgente è 'Andrea Prati' (N=12), la stringa destinazione deve essere 'iaParn'.

SECTION data

StringaS: db 'Andrea Prati' 
StringaD: resb 100 
N: db 12

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

	mov ax,StringaS
	push ax
	
	mov ax,StringaD
	push ax

	xor ax,ax
	mov ax,[N]
	push ax

	call InvertiStringa
	add sp,6	

	mov ax,StringaD
	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

InvertiStringa:
	push bp
	mov bp,sp

	xor dx,dx
	xor cx,cx
	mov si,[bp+8]
	mov di,[bp+6]
	mov cl,[bp+4]
	mov dx,0FFh
Ciclo:
	cmp dx,0FFh
	jne Skip

	mov si,[bp+8]
	add si,cx
	dec si
	mov al,[si]
	stosb

Skip:
	not dx
	loop Ciclo

FineFunc:
	mov al,0
	stosb
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;