CPU 8086
SECTION data
Vett: dw -18,-5,20,13,3,6,23,19
N:db 8
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

	mov ax,Vett
	push ax

	xor ah,ah
	mov al,[N]
	push ax

	call CalcolaMedia
	add sp,4

	call ScriviNumeri

FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

CalcolaMedia:
	push bp
	mov bp,sp

	xor bl,bl	;uso bl per salvarmi la quantità da dividere
	xor cl,cl
	xor ax,ax
	xor dx,dx	;TrovaMax
	mov dl,5

	mov si,[bp+6]
	mov cl,[bp+4]
	mov bl,[bp+4]
	
CicloMedia:
	lodsw
	add ah,al
	loop CicloMedia
	
	xchg ah,al
	xor ah,ah
	
	div bl		;In AH ho il resto, in AL l'intero
	xor ah,ah	;Tronco per difetto 

	mov bl,al	;Media in BL

	xor cl,cl
	mov cl,[bp+4]

CicloDiscostamento:

	lodsw

	dec si
	dec si

	cmp al,bl
	jle NEgativo

	sub al,bl
	cmp dl,al
	jb NuovoMax

Continue:
	inc si
	inc si

	loop CicloDiscostamento
	jmp FineFunc

NEgativo:
	not al
	add al,bl
	cmp dl,al
	jb NuovoMax
	jmp Continue

NuovoMax:
	mov dl,[si]
	;mov dh,si	;Posizione nel vett
	jmp Continue
FineFunc:
	;Avrò in dh la pos del vettore e in dl il numero stesso
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviNumeri: 
	mov  ah,0eh 
	mov  bx,0000h 

	mov al,dh	;Stampo pos
	add al,'0'
	int 10h

	xor ax,ax

	mov al,dl
	neg al
	
	mov dl,10

	div dl
	push ax

	cmp al,0
	je UnaCifra

	mov  ah,0eh 
	mov  bx,0000h 
	add al,'0'
	int 10h

UnaCifra:
	pop ax
	xchg ah,al

	add al,'0'
	mov  ah,0eh 
	mov  bx,0000h 
	int 10h
	
	ret 
