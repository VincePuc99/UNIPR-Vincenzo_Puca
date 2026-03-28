;Scrivere in Assembler per Intel 80x86 la funzione ModificaStringa che riceve in 
;ingresso una stringa zero-terminata Sorg (contenente solo caratteri alfabetici maiuscoli), 
;una seconda stringa Dest e un vettore di byte sia positivi che negativi (con lo stesso 
;numero di valori dei caratteri della stringa Sorg). I parametri devono essere passati 
;mediante lo stack. ----- La funzione deve copiare nella stringa Dest i caratteri della stringa 
;Sorg diminuiti del valore corrispondente in Vett, verificando che il carattere risultante sia 
;compreso tra 'A' e 'Z' (inclusi). Se il valore risultante è 'inferiore' a 'A' deve copiare in 
;Dest un carattere '-' (meno) e se il valore risultante è 'superiore' a 'Z' deve copiare un 
;carattere '+' (più). 

;Il risultato in Dest dovrebbe essere: '-WEPE+-LC' (es. B-3 è inferiore a A quindi metto -; 
;S-(-10) è superiore a Z quindi metto +; ecc.) 
;Si scriva anche il programma main che chiama la funzione e che alla fine deve scrivere 
;sullo schermo la stringa Dest. 

;Esame160624

SECTION data

Sorg: db 'BUONESAME',0 
Dest: resb 100 
Vett: db 3,-2,10,-2,0,-10,1,1,2 

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

	mov ax,Vett
	push ax

	call ModificaStringa
	add sp,6

	mov ax,Dest
	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret
;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ModificaStringa:
	push bp
	mov bp,sp

	mov si,[bp+8]
	mov di,[bp+6]
	mov bx,[bp+4]

Ciclo:
	lodsb

	cmp al,0
	je FineFunc

	sub al,[bx]
	
	cmp al,'A'
	jbe CopiaMeno

	cmp al,'Z'
	jae CopiaPiu

	stosb

Continue:
	inc bx
	jmp Ciclo

CopiaMeno:
	mov al,'-'
	stosb
	jmp Continue

CopiaPiu:
	mov al,'+'
	stosb
	jmp Continue

FineFunc:
	mov al,0
	stosb

	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

