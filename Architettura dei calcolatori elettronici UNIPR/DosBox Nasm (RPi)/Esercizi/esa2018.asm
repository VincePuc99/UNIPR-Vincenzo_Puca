SECTION data
Stringa1: db 18, 'Buon esame a tutti'
Stringa2: db 18, 'caratteri presenti'
Stringa3: db 22, "Carattere non presente"
SECTION text
..start:

	mov ax,data
	mov ds,ax
	mov es,ax

	call main

	mov ax,4c00h
	int 21h

main:
	push bp
	mov bp,sp

Input:
	mov ah,00h
	int 16h

	cmp al,1bh
	je FineMain

	push ax

	mov ax,Stringa3
	push ax	

	mov ax,Stringa2
	push ax

	mov ax,Stringa1
	push ax

	call ContaOccorrenze
	add sp,8

	jmp Input

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;

ContaOccorrenze:
	push bp
	mov bp,sp

	xor dh,dh
	xor cl,cl

	mov bx,[bp+10]	;Car
	mov si,[bp+4]	;Stringa1
	mov cl,[si]

	lodsb

SpazzolaStringa:
	lodsb
	cmp al,bl
	je IncOcc

	loop SpazzolaStringa
	jmp FineFunc
IncOcc:
	inc dh
	loop SpazzolaStringa

FineFunc:
	xor ax,ax
	cmp dh,0
	je StampaNO

	mov ah,0eh
	mov bx,0000h

	mov al,dh
	add al,30h

	int 10h

	mov ah,0eh
	mov bx,0000h

	mov al,' '
	int 10h

	mov ax,[bp+6]
	push ax

	call ScriviStringa
	add sp,2
	jmp FineF

StampaNO:
	mov ax,[bp+8]
	push ax

	call ScriviStringa
	add sp,2
FineF:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa:
	push bp
	mov bp,sp
	xor cl,cl

	mov si,[bp+4] ; Leggo l'indirizzo della stringa
	mov cl,[si]
	lodsb
	

AltroCarattere:
	mov ah,0eh
	mov bx,0000h
	lodsb
	int 10h
	loop AltroCarattere
Fine:
	mov ah,0eh
	mov bx,0000h

	mov al,0dh
	int 10h

	mov ah,0eh
	mov bx,0000h

	mov al,0ah
	int 10h

	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;