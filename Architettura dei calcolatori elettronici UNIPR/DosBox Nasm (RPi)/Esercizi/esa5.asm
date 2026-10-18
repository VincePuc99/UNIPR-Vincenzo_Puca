;Scrivere in Assembler per Intel 80x86 un programma che scorre un vettore di N byte 
;Vett e, ad ogni passo, richiede all'utente di inserire un carattere tra 'S' (codice ASCII 83) 
;e 'N' (codice ASCII 78). Se l'utente preme 'S' l'elemento attualmente puntato su Vett deve 
;essere scambiato con il successive, altrimenti no. La richiesta all'utente continua 
;iterativamente fintanto o l'utente preme il pulsante ESC o si arriva alla fine del vettore 
;Vett. Se l'utente preme 'N' i valori di Vett non vanno scambiati ma si passa al prossimo 
;elemento di Vett, mentre se l'utente preme un qualsiasi altro tasto il programma deve 
;chiedere nuovamente il tasto all'utente rimanendo nell'elemento corrente di Vett. Il 
;programma principale deve chiamare una funzione ScambiaValori a cui passa 
;mediante lo stack l'indirizzo di Vett e il valore di N. 
;e l'utente premesse in sequenza SSNSNS, nella variabile Vett al termine del programma 
;ci sarebbero i seguenti valori: 
; -5, 7, 3, -3, 12, 22, 11
;Si scriva anche il programma main che chiama la funzione. 

SECTION data
Vett: db 3, -5, 7, 12, -3, 11, 22 
N: db 7 
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

	xor cx,cx
	xor ax,ax

InputCar:
	mov ah,00h
	int 16h

	cmp al,1bh
	je FineInput

	cmp al,'S'
	je Scambio

	cmp al,'N'
	je Incremento

	jmp InputCar

Scambio:
	mov ax,Vett
	push ax

	xor ah,ah
	mov al,[N]
	push ax

	call ScambiaValori
	add sp,4

	jmp InputCar

Incremento:
	inc cx
	jmp InputCar
	
FineInput:

	mov ax,Vett
	push ax

	xor ah,ah
	mov al,[N]
	push ax

	call ScriviStringa
	add sp,4

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;

ScambiaValori:
	push bp
	mov bp,sp

	mov si,[bp+6]	;Vett
	mov al,[bp+4]	

	dec al
	cmp al,cl
	je FineInput

	add si,cx
	lodsb

	dec si

	mov ah,[si+1]
	xchg ah,al

	mov [si],al
	mov [si+1],ah
	
	inc si

FineFunc:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov cl,[bp+4]
	mov si,[bp+6] 

	mov  ah,0eh 
	mov  bx,0000h 
Stampa:   
	lodsb 
	add al,30h
	int  10h 

	loop Stampa 

	pop bp 
	ret 

;;;;;;;;;;;;;;;;;;;;;;;;