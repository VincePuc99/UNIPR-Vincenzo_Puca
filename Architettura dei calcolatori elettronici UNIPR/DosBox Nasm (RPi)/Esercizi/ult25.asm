;Implementare una funzione assembly che: 
;1. prenda in ingresso l'indirizzo in memoria di una stringa, ovvero di una sequenza di caratteri terminata 
;da 0 
;2. restituisca 
;1 se tutti i caratteri contenuti sono dei numeri 
;2 se tutti i caratteri sono lettere minuscole o maiuscole 
;0 altrimenti (cioè se ci sono sia numeri che lettere o se ci sono altri caratteri non alfanumerici o stringa vuota, o tutti gli altri casi) 
;Si scriva anche il programma main che chiama la funzione.

CPU 8086

SECTION data
StringaD: resb 100
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

	mov di,StringaD

Input:
	mov ah,00h
	int 16h

	cmp al,1bh	;ESC pressed
	je ESCPressed	

	cmp al,'0'
	je Fine

	stosb

	jmp Input

ESCPressed:
	xor ax,ax
	mov al,0
	stosb

	xor ax,ax
	mov ax,StringaD
	push ax

	call Func
	add sp,2
	
  	;push ax
	 
  	;call ScriviStringa 
  	;add sp, 2

Fine:	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Func:	push bp
	mov bp,sp

	mov si,[bp+4]
	xor dx,dx	;Per lunghezza stringa DH
	xor cx,cx
	xor bx,bx
	
CicloLettura:
	lodsb
	cmp al,0
	je FineFunc
	
	cmp al,1
	jb AddNumber

	cmp al,9
	ja AddNumber

	;sub al,20h	;Faccio diventare minuscola nel caso sia Upper
	cmp al,'a'
	jb AddMinusLetter
	
	cmp al,'z'
	ja AddMinusLetter

Continue:
	inc dh
	jmp CicloLettura

AddNumber:
	inc cl
	jmp Continue

AddMinusLetter:
	inc bl
	jmp Continue

FineFunc:
	xor ax,ax

	cmp cl,dh	;Stringa tutta numeri
	je Numeri

	cmp bl,dh 	;Stringa tutta Lettere
	je Caratteri

	mov ax,0 	;Caratteri Strani
	jmp End
	
Caratteri:
	mov ax,2
	jmp End
Numeri:
	mov ax,1
End:

	mov  ah,0eh 
	mov  bx,0000h 

	add ax,30h
	int 10h 


	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov si,[bp+4] 

	mov  ah,0eh 
	mov  bx,0000h 

	lodsb 		;Aggiungi ad al 30h se è un numeretto
	
	int 10h 

	pop bp 
	ret    