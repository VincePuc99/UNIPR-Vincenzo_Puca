;Scrivere in Assembler per Intel 80x86 la funzione TrovaMax che cerca il massimo tra due vettori non ordinati 
;di byte (numeri senza segno) e ritorna in AL 0 se il massimo si trova nel primo vettore, 1 se il massimo si trova 
;nel secondo. Viene passato anche il numero N di elementi dei due vettori. Di conseguenza il programma 
;chiamante  stampa  a  video  una  stringa  diversa,  come  riportato  di  seguito  (si  supponga  che  la  funzione 
;ScriviStringa visualizzi a video la stringa passata come parametro):  

SECTION data 
Vett1: db 13,15,22,7,5,3,21,2,0,10 
Vett2: db 1,7,3,2,22,21,3,28,7,11 
Stringa1: db 'Il massimo è nel vettore 1',0 
Stringa2: db 'Il massimo è nel vettore 2',0

SECTION text
..start:

	mov ax,data
	mov ds,ax
	mov es, ax

	call main

	mov ax,4C00h
	int 21h

main:
	push bp
	mov bp,sp

	mov ax,Vett1
	push ax

	mov ax,Vett2
	push ax

	mov ax,10
	push ax

	call TrovaMax
	add sp,7

	;call StampaStringa
	;add sp,4

Fine: 	pop bp
	ret	

TrovaMax:
	push bp
	mov bp,sp

	mov cx,[bp+4]	;Grandezza array
	mov si,[bp+8] 	;Lavoro prima il primo vett
	mov bx,[si]	;registro di confronto

CicloPrimo:
	mov ax,[si]

	cmp ax,bx
	ja TrovatoMaggiore

	inc si

	loop FinePrimo

TrovatoMaggiore:
	mov bx,[si]
	mov cl,1
	inc si
	jmp CicloPrimo

FinePrimo:
	mov si,[bp+6] 	;Lavoro il secondo vett
	mov ax,[si]

	cmp ax,bx
	ja TrovatoMaggiore2

	inc si

	loop FineSecondo

TrovatoMaggiore2:
	mov bx,[si]
	mov cl,0
	inc si
	jmp FinePrimo

FineSecondo: 

	mov dl,bl
	mov ah,02h

	int 21h

	pop bp
	ret

StampaStringa:
	push bp
	mov bp,sp

	mov ax,Stringa1
	push ax

	mov ax,Stringa2
	push ax

	mov ah,02h

	cmp cl,0
	je Stampo1

	jmp Stampo2

Stampo1:
	mov bx,[bp+6]
	mov dl,[bx]	
	
ciclostampo1:
	mov dl,[bx]
	cmp dl,0
	je Fineciclo

	int 21h
	inc bx
	jmp ciclostampo1		

Stampo2:
	mov bx,[bp+4]

ciclostampo2:
	mov dl,[bx]
	cmp dl,0
	je Fineciclo

	int 21h
	inc bx
	jmp ciclostampo2

Fineciclo: ret