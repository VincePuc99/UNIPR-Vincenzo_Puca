;Scrivere  in  Assembler  per  Intel  80x86  la  funzione  concatena_stringhe  che  riceve  in  ingresso  3 
;parametri: due stringhe sorgenti di caratteri zero-terminate Sorg1 e Sorg2, e una stringa destinazione (che 
;deve essere zero terminata) Dest. Tutti i parametri sono passati mediante lo stack.  
;La funzione deve copiare in Dest le due stringhe sorgenti, una di seguito all'altra, mettendo prima quella più 
;corta (nel caso siano lunghe uguali, l'ordine non importa). Per calcolare la lunghezza delle stringhe si consiglia 
;di definire una funzione apposita (che va comunque riportata!).
;In questo caso alla fine in Dest ci dovrebbe essere la stringa zero-terminata 'Vi auguro un felice 2009'. 
;Si scriva anche il programma main che chiama la funzione. 

SECTION data

Sorg1: db 'Vi auguro ',0 
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

;;;;;;;;;;;;;;;;;;;;;;;;;

concatena_stringhe:
	push bp
	mov bp,sp

	mov si,[bp+8]
	mov di,[bp+4]
	mov bx,[bp+6]

Ciclo:


;Già fatto in esercizio passato



FineFunc:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;

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
