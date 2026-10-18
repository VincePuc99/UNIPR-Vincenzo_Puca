;Scrivere  in  Assembler  per  Intel  80x86  la  funzione  fondiStringhe  che  riceve  in  ingresso  tre  stringhe 
;StringaS1, StringaS2 e StringaD, e un intero a 16 bit  N (passati mediante lo stack). Le due stringhe sorgenti 
;StringaS1  e  StringaS2  sono  di  lunghezza  fissa  indicata  dal  parametro  N.  La  funzione  deve  ricopiare  nella 
;stringa  destinazione  StringaD  un  carattere  dalla  stringa  sorgente  con  il  codice  ASCII  inferiore.  Si  tenga 
;presente che le lettere maiuscole hanno codice ASCII inferiore delle lettere minuscole e  che lo  spazio ha 
;codice  ASCII  inferiore alle  lettere maiuscole. Quindi, ad esempio,  se  le  due  stringhe  sorgenti  sono  'Bzon 
;Pazame' e 'Cutu Netolo' (N=11), la stringa destinazione deve essere 'Buon Natale'. 

CPU 8086

SECTION data
StringaS1: db 'Bzon Pazame' 
StringaS2: db 'Cutu Netolo' 
StringaD: resb 11  
N: db 11

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

	mov ax, [N]
	push ax

	mov ax, StringaS1
	push ax

	mov ax, StringaS2
	push ax

	mov ax, StringaD
	push ax 

	call fondiStringhe 
	add sp,10 

	mov ax,StringaD
	push ax

	mov ax, [N]
	push ax

	call ScriviStringa
	add sp,2

	pop bp 
	ret

;;;;;;;;;;;;;;;;;;;;;;;

fondiStringhe:
	push bp
	mov bp,sp

	xor cx,cx
	xor bx,bx

	mov di,[bp+4]	;StringaD
	mov bx,[bp+6]	;StringaS2
	mov si,[bp+8]	;StringaS1
	mov cl,[bp+10]	;N

Ciclo:
	lodsb

	mov ah,[bx]	;Por el Confruento	
	inc bx

	cmp al,ah
	jb ALMaggiore

	mov [di],ah
	inc di

	loop Ciclo
	jmp FineFunc

ALMaggiore:	;prendo AL
	stosb
	loop Ciclo

FineFunc:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 

	xor cx,cx
	mov cl,[bp+4] 
	mov si,[bp+6]

	mov  ah,0eh 
	mov  bx,0000h 
Stampa:   
	lodsb 
	int  10h 

	loop Stampa 

	pop bp 
	ret 

;;;;;;;;;;;;;;;;;;;;;;;