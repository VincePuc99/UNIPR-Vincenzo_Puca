
SECTION text
..start:

Ciclo:
	mov ah,00h
	int 16h

	cmp al,'A'
	jb Fine
	
	cmp al,'Z'
	ja Fine

	cmp al,'a'
	jb Fine

	cmp al,'b'
	ja Fine

	mov ah,0eh
	int 10h

	jmp Ciclo

Fine:
	mov ax,4c00h
	int 21h