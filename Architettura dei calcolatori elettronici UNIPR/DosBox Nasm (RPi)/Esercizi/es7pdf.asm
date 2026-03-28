SECTION data
Stringa1: db "Questa è la prima stringa e",0
Stringa2: db " deve essere concatenata a questa",0
Stringa3: times 255 db

..start:

	mov ax,data
	mov ds,ax

	call main		<-- 1

	mov ax,4c00h
	int 21h
main:				;Funzione principale
	push bp			<-- 2
	mov bp,sp		;Mandatorio
	
	mov ax,Stringa2
	push ax			<-- 3

	mov ax,Stringa1
	push ax			<-- 4

	mov ax,Stringa3
	push ax			<-- 5

	call ConcatenaStringhe
	add sp,6		

	mov ax,Stringa3
	push ax			<-- 1
	call ScriviStringa ; da esempio 5
	add sp,2
Fine:
	pop bp
	ret

; Concatena szSorg1 e szSorg2 e le copia in szDest. 
; void ConcatenaStringhe (char *szDest, char *szSorg1, char *szSorg2);

ConcatenaStringhe:
	push bp
	mov bp,sp

	; Copio il contenuto di szSorg1 in szDest
	mov di,[bp+4] ; Leggo l'indirizzo della stringa di destinazione	-- Stringa3

	mov si,[bp+6] ; Leggo l'indirizzo della prima stringa sorgente -- Stringa1
	mov bx,[bp+8] ; Leggo l'indirizzo della seconda stringa sorgente -- Stringa2 -- (LIFO Last in First OUT)

LeggiTutto1:
	mov al,[si]

	cmp al,0
	je FineStringa1

	mov [di],al

	inc si
	inc di
	
	jmp LeggiTutto1


FineStringa1:
	mov al,[bx]

	cmp al,0
	je FineStringa2

	mov [di],al
	inc bi
	inc di

	jmp FineStringa1

FineStringa2:
	pop bp
	ret


