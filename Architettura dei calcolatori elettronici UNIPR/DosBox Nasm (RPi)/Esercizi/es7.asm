SECTION data
Var1: db 8,'Sir_Vinz',0	;il virgola 0 è in stile C quindi usi test o anche cmp al,0
Var2: db 10,'JesusMemes',0	;senza il ,0 usi Pascal quindi cmp dx,di

SECTION text

..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	xor cx,cx
	xor dx,dx

	mov cl,[Var1]
	mov dl,[Var2]

	mov si,0	;addirittura puoi usarli come indici da incrementare
	mov di,0

Infinty:
	cmp cx,si
	je Var1Full

	cmp dx,di	;di è a 16 bit quindi usi dx
	je Var2Full

	mov al,[Var1+1+si]
	call Printata

	mov al,[Var2+1+di]
	call Printata

	inc si
	inc di

	jmp Infinty

Printata:
	mov ah,0eh
	int 10h
	ret


Var1Full:
end1:	mov al,[Var2+1+di]
	call Printata
	inc di
	test al,al	;test vede se raggiunto '0' (Stile C) ;si poteva fare come su -> cmp dx,di (Stile Pascal)
	je Fine
	jmp end1

Var2Full:
end2:	mov al,[Var1+1+si]
	call Printata
	inc si
	test al,al
	je Fine
	jmp end2
	
Fine: 	mov ax,4C00H
	int 21h