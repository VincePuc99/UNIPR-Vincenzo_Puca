SECTION text
..start:
	mov al,3
	test al,1b
	je Pari

	mov ax,4c00h
	int 21h

Pari:
	mov ah,0eh
	add al,30h
	int 10h

	mov ax,4c00h
	int 21h
