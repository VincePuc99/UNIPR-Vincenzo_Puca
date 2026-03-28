SECTION data
S1: db 'God bless the Queen',0
S2: db 'God save the King',0
S3: resb 255

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

	mov ax,S1
	push ax

	mov ax,S2
	push ax
	
	mov ax,S3
	push ax

	call CopiaStringhe
	add sp,6

	mov ax,S3
	push ax

	call ScriviStringa
	add sp,2
FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;

CopiaStringhe:
	push bp
	mov bp,sp

	xor cx,cx

	mov si,[bp+8]	;S1
	mov di,[bp+4]	;S3
	mov bx,[bp+6]	;S2

Ciclo:
	lodsb
	cmp al,0
	je Continue
	inc cl		;CL Avrai grandezza S1
	jmp Ciclo

Continue:
	mov si,[bp+6]
CicloS2:
	lodsb

	cmp al,0
	je Continue2

	inc ch		;CH Avrai grandezza S2
	jmp CicloS2

Continue2:

	xor dl,dl
	xor ah,ah

	mov al,cl
	mov dl,2
	div dl	;AH-> Resto AL -> Metà

	mov cl,al
	mov bl,al	;Metà Grandezza S1
	mov si,[bp+8]

ScritturaS1:
	lodsb
	stosb
	loop ScritturaS1

	xor dl,dl
	xor ah,ah

	mov al,ch
	mov dl,2
	div dl	;AH-> Resto AL -> Metà

	mov cl,al
	;mov bh,al	;Metà Grandezza S2
	mov si,[bp+6]

	add si,bx
ScritturaS2:
	lodsb

	cmp al,0
	je CicloFineS1

	stosb
	loop ScritturaS2
	
	xor cx,cx
	mov si,[bp+8]	;S1


CiclofineS1:
	lodsb
	cmp al,0
	je ContinueMetaS1
	inc cl		;CL Avrai grandezza S1
	jmp CiclofineS1
	
ContinueMetaS1:
	mov si,[bp+8]
	dec cx
	add si,cx

	xor dl,dl
	xor ah,ah

	mov al,cl
	mov dl,2
	div dl	;AH-> Resto AL -> Metà

	mov cl,al

CicloReverse:
	mov al,[si]
	dec si
	stosb
	loop CicloReverse




FineFunc:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa:
	push bp
	mov bp,sp

	mov ah,0eh 
	mov si,[bp+4]  ;Leggo l'indirizzo della stringa
AltroCarattere:
	lodsb
	cmp al,0
	je Fine
	int 10h
	
	jmp AltroCarattere
Fine:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;