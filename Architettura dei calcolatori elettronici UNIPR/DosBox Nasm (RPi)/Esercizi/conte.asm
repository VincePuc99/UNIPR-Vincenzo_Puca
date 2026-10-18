
SECTION text
..start:

Ciclo:
	mov ah,00h
	int 16h

	cmp al,'A'
	jb Maiusc
	
	cmp al,'Z'
	ja minusc

	mov ah,0eh
	int 10h

	jmp Ciclo


Maiusc:
	mov ah,0eh
	int 10h


	mov ax,4c00h
	int 21h