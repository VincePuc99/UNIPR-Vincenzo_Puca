SECTION data
vettore:     db 5, 4, 22, 12, 17, 3, 1, 8, -1 
Ndispari:    db 0 

Section text

..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	mov dl,2

	mov si,vettore

Ciclo:	lodsb
	cmp al,-1
	je Fine
	
	test al,1	;Verifica 1010 il bit meno signficativo per vedere se pari o dispari
	jne Dispari

	jmp Ciclo

Dispari:
	inc byte [Ndispari]
	jmp Ciclo

Fine:	mov al,[Ndispari]
	add al,'0'
	mov ah,0eh
	int 10h
	mov ax,4C00h
	int 21h