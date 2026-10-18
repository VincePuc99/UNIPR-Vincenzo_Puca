;Scrivere in Assembler per Intel 80x86 la funzione elimina_lettera che riceve in ingresso una stringa 
;Sorg (zero terminata), una stringa Dest  (sempre zero terminata) e un carattere (byte) C. La  funzione deve 
;copiare in Dest la stringa Sorg eccetto per i caratteri C che si trovano in posizione pari (il primo carattere è in 
;posizione 0 da considerare come posizione pari). Tutti i parametri sono passati mediante lo stack.  
;La funzione deve copiare in Dest la seguente stringa: 'Orma nza l'estate',0 
;Si scriva anche il programma main che chiama la funzione.

SECTION data
Sorg: db "Ormai inizia l'estate",0 
Dest: resb 100 
C: db 'i'
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

	mov ax,Sorg
	push ax

	mov ax,Dest
	push ax

	xor ah,ah
	mov al,[C]
	push ax

	call elimina_lettera
	add sp,6

	mov ax,Dest
	push ax	

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;

elimina_lettera:
	push bp
	mov bp,sp

	xor dl,dl
	mov si,[bp+8]
	mov di,[bp+6]
	mov bl,[bp+4]
	
Ciclo:
	lodsb
	cmp al,0
	je FineFunc

	cmp al,bl
	je TrovaChar

	stosb
Continue:
	jmp Ciclo

TrovaChar:
	dec si
	test si,1b
	je Pari

	stosb
	inc si
	jmp Continue
Pari:
	inc si
	jmp Ciclo


FineFunc:
	stosb
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
;;;;;;;;;;;;;;;;;;;;;;;;;
