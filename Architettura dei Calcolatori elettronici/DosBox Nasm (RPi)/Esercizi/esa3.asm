;Scrivere in Assembler per Intel 80x86 la funzione ContaSeSuperioreMedia che riceve 
;in ingresso due vettori di byte positivi, V1 e V2 (di lunghezza N fornito come dato), e un 
;byte Nmax positivo. --- La funzione deve verificare se i valori di V2 superiori alla media dei 
;valori di V1 (media arrotondata per difetto) sono superiori o uguali a Nmax (e in questo 
;caso scrivere a video la stringa S1) o no (e in questo caso scrivere a video la stringa S2). 
;Tutti i 6 parametri devono essere passati mediante lo stack. Le stringhe S1 e S2 sono 
;zero-terminate secondo la prassi C. 

;Verrebbe scritta la stringa S1 in quanto la media dei valori di V1 è 3 (somma=59 che 
;diviso per 5 restituisce 11 con resto di 4) e il numero di valori in V2 superiori o uguali a 11 
;sono 2 (22 e 19). Visto che Nmax vale 4 si deve scrivere la stringa S2. 
;Si scriva anche il programma main che chiama la funzione. 

SECTION data
N: db 5 
V1: db 3, 7, 21, 22, 6
V2: db 9, 2, 22, 11, 19
Nmax: db 4 

S1: db "Valori superiori alla media maggiori di o uguali a Nmax",0 
S2: db "Valori superiori alla media minori di Nmax",0 

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

	xor ax,ax
	mov ax,[N]
	push ax

	xor ax,ax
	mov ax,[Nmax]
	push ax

	mov ax,V1
	push ax

	mov ax,V2
	push ax

	call ContaSeSuperioreMedia
	add sp,12
	
FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ContaSeSuperioreMedia:
	push bp	
	mov bp,sp

	xor cx,cx

	mov si,[bp+6]	;V1
	mov di,[bp+4]	;V2
	mov ch,[bp+8]	;NMAX

	mov cl,[bp+10]	;N

	xor ax,ax
	xor dx,dx
CicloSum:
	lodsb
	add ah,al

	loop CicloSum

	xchg ah,al
	xor ah,ah

	mov cl,[bp+10]
	div cl		;AH Resto - AL Risultato
	
	xchg al,ah
	xor al,al

CicloV2:
	mov al,[di]
	
	cmp al,ah
	jb MinoreMedia

	inc di
	loop CicloV2

	jmp FineFunc

MinoreMedia:
	cmp al,ch
	jbe PrintS1

	inc dh	;S2
	jmp StampaS
PrintS1:
	inc dl	;S1
StampaS:
	loop CicloV2

FineFunc:
	cmp dl,dh
	jb StampaS1

	mov ax,[bp+12]
	jmp FineStampa
StampaS1:	 
	mov ax,[bp+14]
FineStampa:
	push ax
	call ScriviStringa
	add sp,2

	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov si,[bp+4] 

	mov  ah,0eh 
	mov  bx,0000h 
Stampa:   
	lodsb 
	cmp al, 0 
	je fineStampa 
	int  10h 

	jmp Stampa 
fineStampa: 
	pop bp 
	ret 