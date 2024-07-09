;Scrivere  in  Assembler  per  Intel  80x86  la  funzione  seleziona_caratteri  che  riceve  in  ingresso  4 
;parametri:  un  valore  byte  N,  un  vettore  di  byte  V  contenente  N  elementi,  una  stringa  di  caratteri  Sorg 
;memorizzata secondo convenzione Pascal (il primo byte contiene la lunghezza della stringa ( si veda esempio 
;sotto) e una stringa di caratteri Dest sempre con convenzione Pascal e lunga N-1 caratteri. Tutti i parametri 
;sono passati mediante lo stack. ---- 
;La  funzione deve scorrere V sommando due valori consecutivi. Il  risultato della somma indica l'indice del 
;carattere di Sorg che deve essere copiato in Dest. Se tale somma supera la lunghezza di Sorg, in Dest deve 
;essere copiato l'ultimo carattere di Sorg.
;Notare che Sorg e Dest NON sono zero-terminate e che il primo byte di Sorg e Dest indica la loro lunghezza. 
;Si realizzi una soluzione che funziona sempre, non solo con i dati riportati come esempio! Suggerimento: se 
;i registri a disposizione non dovessero bastare, usare lo stack per memorizzare temporaneamente i registri, 
;usarli e poi recuperarli dallo stack una volta finito di usarli. 
;Si scriva anche il programma main che chiama la funzione.

SECTION data
N: db 6 
V: db 1, 26, 6, 2, 6, 7 
Sorg: db 31, 'Buon Natale e Felice Anno Nuovo'
Dest:  db 5 
       resb 5 

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

	mov ax,[N]
	push ax

	mov ax,V
	push ax

	mov ax,Sorg
	push ax

	mov ax,Dest
	push ax

	call seleziona_caratteri
	add sp,8

	mov ax,Dest
	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

seleziona_caratteri:
	push bp
	mov bp,sp

	xor cl,cl
	xor ax,ax

	mov si,[bp+6]
	mov di,[bp+4]
	mov bx,[bp+8]	;Vettore
	mov cl,[bp+10]	;N (Anche Dest -1)

	mov dl,[si]	;Lunghezza di Sorg per compare
	dec cl	;Per non farlo sballare fuori dagli out of bounds
CicloVect:
	mov ah,[bx]
	mov al,[bx+1]
	add al,ah	

	cmp al,dl
	jbe IndiceMaggiore	;Somma maggiore dell'indice

	mov al,dl
IndiceMaggiore:
	xor ah,ah
push bx 
	mov bx,ax
	mov al,[si+bx]		;a quanto pare puoi sommare solo bx a si

	stosb
pop bx
	inc bx
	loop CicloVect

FineFunc:
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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;