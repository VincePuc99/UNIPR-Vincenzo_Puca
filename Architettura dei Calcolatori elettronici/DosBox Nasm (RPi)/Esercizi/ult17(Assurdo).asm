;Scrivere  in  Assembler  per  Intel  80x86  la  funzione  concatena_stringhe  che  riceve  in  ingresso  3 
;parametri: due stringhe sorgenti di caratteri zero-terminate Sorg1 e Sorg2, e una stringa destinazione (che 
;deve essere zero terminata) Dest. Tutti i parametri sono passati mediante lo stack.  
;La funzione deve copiare in Dest le due stringhe sorgenti, una di seguito all'altra, mettendo prima quella più 
;corta (nel caso siano lunghe uguali, l'ordine non importa). Per calcolare la lunghezza delle stringhe si consiglia 
;di definire una funzione apposita (che va comunque riportata!).
;In questo caso alla fine in Dest ci dovrebbe essere la stringa zero-terminata 'Vi auguro un felice 2009'. 
;Si scriva anche il programma main che chiama la funzione.

SECTION data
Sorg1: db 'Vi auguro',0 
Sorg2: db 'un felice 2009',0 
Dest: resb 100

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

	mov ax,Sorg1
	push ax

	mov ax,Sorg2
	push ax

	call Calcola_Lunghezza
	add sp,4

	mov ax,Sorg1
	push ax

	mov ax,Sorg2
	push ax

	mov ax,Dest
	push ax

	call concatena_stringhe
	add sp,6

	mov ax,Dest
	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
concatena_stringhe:
	push bp
	mov bp,sp

	mov di,[bp+4]	;Dest
	mov si,[bp+8]	;Sorg1
	;mov bx,[bp+6]	;Sorg2

;In dx ho 0 se Sorg1 > Sorg2 altrimenti 1

	cmp dx,1
	je Sorg2Maggiore

CicloSorg:
	lodsb
	cmp al,0
	je Next

	stosb

	jmp CicloSorg

Next: 	mov al,' '
	stosb
	mov si,[bp+6]	;Concateno la 2
CicloSorg1:
	lodsb
	cmp al,0
	je FineFunc

	stosb

	jmp CicloSorg1

Sorg2Maggiore:
mov si,[bp+6]
CicloSorg2:
	lodsb
	cmp al,0
	je Next1

	stosb

	jmp CicloSorg2

Next1: 	mov al,' '
	stosb
	mov si,[bp+8]	;Concateno la 2
CicloSorg3:
	lodsb
	cmp al,0
	je FineFunc

	stosb

	jmp CicloSorg3


FineFunc:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Calcola_Lunghezza:
	push bp 
	mov bp, sp 

	xor cl,cl
	mov si,[bp+6]

CicloCalcolo:
	lodsb
	cmp al,0
	je FineStringa1

	inc cl
	jmp CicloCalcolo

FineStringa1:
	mov si,[bp+4]
CicloCalcolo1:
	lodsb
	cmp al,0
	je FineCalcolo

	inc ch
	jmp CicloCalcolo1

FineCalcolo:
	cmp cl,ch
	jb Stringa1Maggiore

	mov dx,1 	;1 se Stringa2 più grande 
	jmp Fine

Stringa1Maggiore:
	mov dx,0	;0 se Stringa1 più grande 

Fine:	pop bp 
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