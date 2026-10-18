;Scrivere  in  Assembler  per Intel  80x86  la  funzione estrai_sottostringa  che  riceve  in  ingresso  una 
;stringa  ASCIIZ  (stringa  zero  terminata,  come  in  C)  Stringa1,  un  carattere  Car  e  un  intero  N  (tutti  passati 
;mediante  lo  stack).  La  funzione  deve  restituire  nella  stringa  ASCIIZ  Stringa2  (questa  volta  inserita  come 
;variabile) gli N caratteri di Stringa1 successivi alla prima occorrenza di Car. Se Car non è presente in Stringa1, 
;Stringa2 rimarrà stringa nulla (vuota). Se ci sono meno di N carattere tra la prima occorrenza di Car e la fine 
;di Stringa1, in Stringa2 vengono copiati solo i caratteri presenti.

;Stringa1= 'Pomodoro' Car='o' N=4 --> Stringa2='modo' 
;Stringa1= 'Pomodoro' Car='m' N=3 --> Stringa2='odo' 
;Stringa1= 'Pomodoro' Car='a' N=5 --> Stringa2=' '
;Stringa1= 'Pomodoro' Car='d' N=5 --> Stringa2='oro'

CPU 8086

SECTION data
Stringa1:  db 'Pomodoro',0 
Stringa2: resb 100 
Car: db 'd' 
N: db 5
SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	;mov ah,00h	;Inserisci in AL un Carattere
	;int 16h

	;mov ah,0eh	;Lo Stampo
	;mov bx,0000h
	;int 10h

	;Metto il carattere in Car

	;mov di,Car
	;stosb

	call main

	mov ax,4c00h
	int 21h
main:
	push bp
	mov bp,sp

	mov ax,Stringa1
	push ax

	mov ax,Stringa2
	push ax

	mov ax,Car
	push ax

	mov ax,[N]
	push ax

	call estrai_sottostringa
	add sp,7

	mov ax,Stringa2
	push ax
	
	call ScriviStringa
	add sp,2

FineMain:
	pop bp

	mov ax,4c00h
	int 21h

;;;;;;;;;;;;;;;;;;;;;;;;;
estrai_sottostringa:

	push bp
	mov bp,sp

	xor cl,cl
	mov si,[bp+10] 	;Source
	mov di,[bp+8]	;Destination
	mov cl,[bp+4]	;N Caratteri da prendere
	mov ah,[bp+6]	;Carattere da comparare

Ciclo:
	lodsb
	cmp al,'0'
	je FineFunc

	cmp al,ah	;Comparo il carattere di source con la varibile
	je CarTrovato

	jmp Ciclo

CarTrovato:
	lodsb
	cmp al,'0'
	je FineFunc

	stosb

	loop CarTrovato
	jmp End

FineFunc:
	mov al,'0'
	stosb
End:
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
	int 10h 

	jmp Stampa 

fineStampa: 
	pop bp 
	ret 
;;;;;;;;;;;;;;;;;;;;;;;;;