;DI-SI-SP-BP

SECTION data
Vett1: db 13,15,22,7,5,3,21,2,0,10
Vett2: db 1,7,3,2,22,21,3,28,7,11
Stringa1: db 'Il massimo è nel vettore 1',0
Stringa2: db 'Il massimo è nel vettore 2',0

SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	mov ax,Vett1
	push ax

	mov ax,Vett2 	;Salvo nello stack prima Vett1 e poi 2

	mov ax, 10	;Massì pushamo anche 10 che ce frega 
	push ax

	call TrovaMax
	
TrovaMax:
	push bp
	mov bp,sp
	push bp 
	mov bp, sp 	;Sinceramente? non ci ho capito una mazza

	mov si,[bp+6] ; Vett2 
	mov di,[bp+8] ; Vett1 
	mov bl,0    ; bl contiene il massimo attuale 
	mov cx,[bp+4] ; N = 10 
Ciclo: 
	mov ah,[si] 
	cmp ah, bl 
	ja NuovoMassimo1 



NuovoMassimo1: 
	mov bl,ah 
	mov al, 0 
	jmp CicloRef1 

CicloRef1: 
	inc si 
	mov ah,[di] 
	cmp ah,bl 
	ja NuovoMassimo2 



















