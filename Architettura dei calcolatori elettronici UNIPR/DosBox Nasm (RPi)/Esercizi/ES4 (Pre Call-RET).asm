SECTION text

..start:
Restart: mov ah,00h	;AL
	 int 16h

	 cmp al,20h
	 je StampaConto

	 cmp al,1bh
	 je Fine

	 mov ah,0eh
	 mov bx,00h
	 int 10h
	
	 inc cl

	 jmp Restart

StampaConto: 	
	 mov al,cl
	 add al,'0'

	 mov ah,0eh
	 mov bx,00h
	 int 10h

	 xor cl,cl
	 jmp Restart

Fine:	 mov ax,4c00h
	 int 21h