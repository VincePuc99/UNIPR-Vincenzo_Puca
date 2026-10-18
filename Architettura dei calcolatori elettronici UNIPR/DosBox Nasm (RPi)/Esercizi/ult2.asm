;Scrivere in Assembler per Intel 80x86 la funzione Filtro che copia un vettore di N byte dall'indirizzo DS:SI 
;all'indirizzo ES:DI. I dati vengono copiati all'indirizzo destinazione se e solo se il bit meno significativo del dato 
;è a 1. 
;Le variabili del programma sono le seguenti: 
;N: db 100 
;Vett1: times 25 db 3, times 25 db 4, times 25 db 5, times 25 db 6 
;Vett2: resb 100 
;Si scriva anche il programma main che chiama la funzione. 

SECTION data
N: db 100 
Vett1:   times 25 db 3 
    times 25 db 4 
    times 25 db 5 
    times 25 db 6 
Vett2: resb 100 

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
	
	mov ax,Vett1
	push ax
	
	mov ax,Vett2
	push ax

	mov ax,[N]
	push ax

	call transfervect
	add sp,6
	
Fine: 	pop bp
	ret


transfervect:
	push bp
	mov bp,sp

	xor cx,cx
	mov si,[bp+8]	;Vett1
	mov di,[bp+6]	;Vett2
	mov cl,[bp+4]	;N

ciclovettore:
	lodsb

  	test al,00000001b 
	je menosign

	loop ciclovettore
	ret	

menosign:
	stosb
	loop ciclovettore
	ret
