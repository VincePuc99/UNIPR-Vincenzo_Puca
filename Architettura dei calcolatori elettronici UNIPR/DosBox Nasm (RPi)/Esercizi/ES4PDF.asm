SECTION data
stringa: db 'Buona Pasqua',0
mirror: resb 100

SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	mov di,mirror
	mov si,stringa
	
PrimoCiclo:
	lodsb
	cmp al,' '
	je Spaziatore
	
	jmp PrimoCiclo

Spaziatore:
	lodsb
	cmp al,0
	je Restart

	stosb
	jmp Spaziatore
Restart:
	mov si,stringa
	mov al,' '
	stosb

Scroll:	lodsb
	cmp al,' '
	je Fine

	stosb
	jmp Scroll

Fine: 	mov al, 0
	stosb ; metto il terminatore in mirror

	mov si, mirror ; mi riposiziono all'inizio di mirror per stamparla
	mov ah,0eh
	mov bx,0000h
Stampa: lodsb
	cmp al, 0
	je fineStampa
	int 10h
	jmp Stampa
fineStampa:
	mov ax, 4c00h
	int 21h


