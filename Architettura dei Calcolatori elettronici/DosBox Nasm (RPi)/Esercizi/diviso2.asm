SECTION text
..start:
	xor cl,cl
	mov cl,7
	shr cl,2

	mov al,cl
	add al,'0'

	mov ah,0eh
	mov bx,0000h
	int 10h

	mov ax,4c00h
	int 21h
