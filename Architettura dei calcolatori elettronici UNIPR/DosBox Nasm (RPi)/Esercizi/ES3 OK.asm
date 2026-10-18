SECTION text

..start:
Seq:	mov ah,00h	;Lettura in AL
	int 16h
	
	cmp al,1bh ;ESC
	je Fine		;finito il metodo torno al punto di partenza

	cmp al,'a'
	jb Check
	
	cmp al,'z'
	ja Check

	sub al,20h
	jmp Stampa
	
Check:  cmp al,'A'
	jb Cambio
	
	cmp al,'Z'
	ja Cambio

Cambio: mov al,'*'
	jmp Stampa

Stampa: mov ah,0eh	
	mov bx,00h
	int 10h

	jmp Seq

Fine:	mov ax,4C00h
	int 21h