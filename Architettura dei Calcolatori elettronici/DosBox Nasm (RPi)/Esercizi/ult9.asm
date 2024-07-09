;La funzione deve copiare nella stringa Dest in posizione i il carattere della stringa Sorg in 
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
	

	mov ax,Sorg
	push ax
	
	mov ax,Dest
	push ax

	xor ah,ah
	mov ax,[N]
	push ax

	mov ax,Vett
	push ax	

	call CopiaCaratteri
	add sp,8

	mov ax,Dest
	push ax

	xor ah,ah
	mov ax,[N]
	push ax

	call ScriviStringa
	add sp,4

	mov ax,4c00h
	int 21h

;;;;;;;;;;;;;;;;;;;;;

CopiaCaratteri:
	push bp
	mov bp,sp

	xor ch,ch
	xor dh,dh
	mov di,[bp+8]
	mov cl,[bp+6]
	mov bx,[bp+4]

Ciclo:	mov si,[bp+10]

	mov dl,[bx]

	cmp dl,[bp+6]
	jae Maggiore	
Salta:
	dec dl

	add si,dx
	mov al,[si]
	mov [di],al

	inc bx
	inc di	
	
	loop Ciclo
	jmp FineFunc

Maggiore:
	mov dl,[bp+6]
	jmp Salta

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

