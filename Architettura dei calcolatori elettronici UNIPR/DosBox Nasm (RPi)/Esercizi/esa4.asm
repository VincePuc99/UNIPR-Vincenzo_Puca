;Scrivere in Assembler per Intel 80x86 un programma che fa inserire all'utente da tastiera 
;una serie di caratteri che termina quando l'utente preme il pulsante ESC. Il programma, 
;tramite una funzione LeggiEModifica riceve, uno ad uno, i caratteri inseriti dall'utente 
;e li modifica aumentando il loro codice ASCII di un valore che aumenta ad ogni carattere 
;(partendo da zero). La nuova stringa così modificata va inserita nella stringa zero terminata (secondo la convenzione C) Stringa. 
;In pratica, se l'utente inserisse i caratteri: 
;Ben tornati dalle ferie! 
;nella Stringa verrebbe inserito 
;Bfp#xtxui}s+pnz{u1xxå~{8 
;(B+0=B e+1=f n+2=p ...) 
;Il programma deve poi visualizzare a schermo la stringa risultante e memorizzare in AX il 
;numero di vocali (considerando solo le standard a,e,i,u,o) nella Stringa modificata 
;(nell'esempio in AX va il valore 3 , u, i, u). 
;I parametri devono essere passati alla funzione mediante lo stack.

SECTION data
StringaOUT: resb 100
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

	xor cx,cx
Input:
	mov ah,00h
	int 16h

	cmp al,1bh
	je EsciInput

	push ax

	xor ax,ax
	mov ax,StringaOUT
	push ax

	call LeggiEModifica
	add sp,4

	jmp Input

EsciInput:
	mov al,0
	stosb

	xor ax,ax
	mov ax,StringaOUT
	push ax

	call ScriviStringa
	add sp,2

	xor cx,cx
	
	xor ax,ax
	mov ax,StringaOUT
	push ax

	call ContaVocali
	add sp,2

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

LeggiEModifica:
	push bp
	mov bp,sp
	
	mov di,[bp+4]	;StrOUT
	mov ax,[bp+6]	;Carattere

	add di,cx
	add al,cl
	stosb
	inc cl
Finefunc:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ContaVocali:

	push bp
	mov bp,sp

	mov si,[bp+4]

CicloConta:
	lodsb

	cmp al,0
	je FinefuncConta

	cmp al,'a'
	je IncrVocal

	cmp al,'e'
	je IncrVocal

	cmp al,'i'
	je IncrVocal

	cmp al,'o'
	je IncrVocal

	cmp al,'u'
	je IncrVocal

	jmp CicloConta

IncrVocal:
	inc cl
	jmp CicloConta


FinefuncConta:
	xor ax,ax
	mov ax,cx
	
	add ax,30h
	mov ah,0eh 
	mov bx,0000h 
	int  10h

	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov si,[bp+4] 

	mov ah,0eh 
	mov bx,0000h 
Stampa:   
	lodsb 
	cmp al, 0 
	je fineStampa 
	int  10h 

	jmp Stampa 
fineStampa: 
	pop bp 
	ret 