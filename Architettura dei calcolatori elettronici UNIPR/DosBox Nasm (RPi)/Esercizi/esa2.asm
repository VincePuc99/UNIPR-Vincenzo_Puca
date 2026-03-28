;Scrivere in Assembler per Intel 80x86 la funzione CalcolaValori che riceve in ingresso 
;tre vettori di word (16 bit), V1, V2 e V3, e un byte N positivo che indica la lunghezza dei 
;vettori (uguale per tutti e tre). I parametri devono essere passati mediante lo stack.----- La 
;funzione deve restituire nel registro AX il numero di volte che il valore V1[i]+V2[N-i-1] è 
;maggiore del valore V3[i]. In pratica, si deve scorrere i vettori V1 e V3 da sinistra a destra, 
;mentre il vettore V2 da destra a sinistra partendo dal fondo. 

;Il risultato in AX sarebbe 3. Infatti: 
;V1[0]+V2[4]=3+(-9)=-6 è maggiore di V3[0]=-7 --> +1 
;V1[1]+V2[3]=7+7=14 è minore di V3[1]=15 
;V1[2]+V2[2]=-21+22=1 è maggiore di V3[2]=0 --> +1 
;V1[3]+V2[1]=22+8=30 è minore di V3[3]=31 
;V1[4]+V2[0]=6+9=15 è maggiore di V3[4]=12 --> +1 

;Si scriva anche il programma main che chiama la funzione. Si verifichi con il debug il 
;valore finale di AX (dopo la chiamata) per verificare la correttezza della soluzione.

SECTION data
V1: dw 3, 7, -21, 22, 6 
V2: dw 9, 8, 22, 7, -9 
V3: dw -7, 15, 0, 31, 12 
N: db 5 

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

	mov ax,V1
	push ax

	mov ax,V2
	push ax
	
	mov ax,V3
	push ax

	xor ax,ax
	mov ax,[N]
	push ax

	call CalcolaValori
	add sp,8
	
	push ax

	call ScriviStringa
	add sp,2

FineMain:
	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

CalcolaValori:
	push bp	
	mov bp,sp

	xor cx,cx
	xor dx,dx

	mov si,[bp+10]	;V1
	mov di,[bp+8]	;V2
	mov bx,[bp+6]	;V3

	mov cl,[bp+4]	;N
	mov dx,[bp+4]	;N

	add di,dx
Ciclo:
	lodsw
	
	mov ah,[di]
	add al,ah

	cmp al,[bx]
	jb Salta

	inc ch

Salta:
	inc bx
	inc bx

	dec di
	dec di

	loop Ciclo

FineFunc:
	
	xchg ch,cl
	xor ch,ch
	xor ax,ax
	mov ax,cx

	pop bp
	ret

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ScriviStringa: 
	push bp 
	mov bp, sp 
	mov si,[bp+4] 

	mov  ah,0eh 
	mov  bx,0000h 

	lodsb 
	add al,30h

	int  10h 

	pop bp 
	ret 
