SECTION data
Vect: db 4,5,7,0,-1

SECTION text

..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	xor bl,bl
	mov bl,10 ;divisore
 
	mov si,Vect
	
infinity:
	lodsb
	
	cmp al,-1
	je Fine

	call Printata

	jmp infinity

Printata:
	add al,30h	
	mov ah,0eh
	mov bx,00h
	int 10h

	ret

Fine:	
	mov ax,4C00H
	int 21h

