SECTION text
..start:
				;mov al,-3
				
				;cmp al,0
				;jl negativo
			;negativo:
				;neg al
	
	mov al,'A'
	add al,20h
	



				;add al,4

	mov ah,0eh
				;add al,30h
	int 10h

	mov ax,4c00h
				
	int 21h

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
mov al, 0dh	;A Capo
Print 		;
mov al, 0ah	;
Print		;