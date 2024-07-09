SECTION data
Vett: db 2,3,4,5,6
N: db 5
SECTION text
..start:

	mov ax,data
	mov ds,ax
	mov es,ax
	
	xor ah,ah
	xor cl,cl
	xor dl,dl

	mov si,Vett
	mov cl,byte [N]

Somma:
	lodsb
	add ah,al
	loop Somma
	
	xchg al,ah
	xor ah,ah

	mov dl,byte [N]
	div dl		;AH Resto AL risultato

	xor ah,ah
	
	mov ah,0eh
	add al,'0'
	int 10h

	mov ax,4c00h
	int 21h
	