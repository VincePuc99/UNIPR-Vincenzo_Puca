;Scrivere in Assembler per Intel 80x86 la funzione calcolaSAD che riceve in ingresso un vettore di word 
;(dati a 16 bit) Vett di lunghezza N nota (entrambi i parametri sono passati mediante lo stack). Ogni elemento 
;a 16 bit del vettore è in realtà composto da due valori a 8 bit (il più significativo MSB e il meno significativo 
;LSB). La funzione deve scorrere il vettore e ogni qualvolta il MSB è di valore inferiore al LSB, li deve scambiare 
;nel vettore. Inoltre, la funzione deve calcolare la somma delle differenza in valore assoluto tra MSB e LSB 
;(SAD = Sum of Absolute Differences) e riportarla in uscita nel registro AX. 

;ret

CPU 8086

SECTION data
Vett: dw 1234h, 1144h, 4412h, 2323h, 2324h, 2423h 
N: dw 6 

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

	mov ax, Vett 
	push ax 

	mov ax, [N] 
	push ax 

	call calcolaSAD 
	add sp, 6 

	pop bp
	mov ax,4c00h
	int 21h

;;;;;;;;;;;;;;;;;;;;;;;
calcolaSAD:
	push bp
	mov bp,sp

	mov si,[bp+6]	;Vett
	xor cx,cx
	xor bx,bx
	mov cl,[bp+4]	;N
	mov bx,0

Ciclo:
	lodsw		;Mette in AL la word
	cmp ah,al	;MSB in AH -- LSB in AL
	ja LSBMaggiore
	
Continue:
	sub ah,al
	;add bx,ah	;Non puoi farlo quindi ti metti il valore in AL con xchg
			;E poi lo add direttamente

	xchg al,ah
	xor ah,ah	;in modo da pulire AH
	add bx,ax	;Che sarebbe ah iniziale

	loop Ciclo
	jmp FineFunc

LSBMaggiore:
	xchg ah,al
	mov [si-2],ax
	jmp Continue

FineFunc:
	mov ax,bx
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;
