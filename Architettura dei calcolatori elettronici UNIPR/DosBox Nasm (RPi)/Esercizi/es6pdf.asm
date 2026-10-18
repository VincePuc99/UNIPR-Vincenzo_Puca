;Sia memorizzata in una variabile una stringa stile Pascal (si vedano i lucidi a tal proposito). Il programma deve 
;scorrere la stringa e riscriverla in una nuova variabile stringa in ordine inverso. Si visualizzi la nuova stringa 
;sullo schermo.

SECTION data
StringaRetroVersa: resb 256
Stringa: db 8,'Sir_Vinz'

SECTION text

..start:
	mov ax,data
	mov ds,ax
	mov es,ax
	xor cx,cx

	mov cl,[Stringa]
	mov [StringaRetroVersa],cl	;anche se ho la lunghezza della stringa originale meglio salvarla 1:1

	mov si,Stringa
	add si,cx 	;aggiungendo cx al source parti dalla fine della stringa

	mov di,StringaRetroVersa+1

Rewrite:std
	lodsb	;metto il direction flag DF a 1 in modo che SI venga decrementato
	cld	;resetto il direction flag a 0 così DI viene incrementato
	stosb	;mette al in es e decrementa di (spazzola DI)						
	loop Rewrite 

			
	mov cl,[StringaRetroVersa] ;loop
	mov si,StringaRetroVersa+1 ;lodsb

PrintRetro:
	lodsb
	mov ah,0eh
	int 10h
	loop PrintRetro
	
	mov ax,4C00h
	int 21h