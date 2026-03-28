;Scrivere in Assembler per Intel 80x86 un programma che  richiama la  funzione SaturaElementi la cui definizione C è la seguente: 
;void SaturaElementi (unsigned char *vettore, int N)
;Tale funzione deve controllare ciascun elemento del vettore e se maggiore di N, modificarlo con il valore di 
;N. Il vettore non è ordinato e contiene valori byte positivi ed è terminato con il valore -1 (che non può essere 
;mai presente come valore di un elemento). 
;La funzione deve essere richiamata dal programma principale con passaggio di parametri tramite lo stack. 
;Le variabili del programma sono le seguenti:
 
;vettore: db 12, 33, 22, 11, 55, 23, 12, 120, 1, -1 
;Si scriva anche il programma main che chiama la funzione. 

CPU 8086
SECTION data
vettore: db 12, 33, 22, 11, 55, 23, 12, 120, 1, -1
N: db 50 ;Non scritto in pdf
SECTION text

..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	call Main

	mov ax,4c00h
	int 21h

Main:
	push bp
	mov bp,sp

	mov ax,vettore
	push ax

	mov ax,[N]
	push ax

	Call SaturaElementi
	add sp,6

Fine: 	pop bp
	ret

SaturaElementi:	
	push bp
	mov bp,sp
	xor cl,cl
	mov si,[bp+6]	;Vettore
	mov cl,[bp+4]	;N

CicloVettore:
	lodsb
	cmp al,-1
	je FineVett

	cmp al,cl
	ja ResetValore

	jmp CicloVettore

ResetValore:
	mov [si-1],cl	;Vero che lodsb sposta in AL ma ha già incrementato SI okkio
	jmp CicloVettore

FineVett:
	ret
	

