; Se richiesto tipo C metti al posto del loop il test al,al com ,0 alla fine delle var sotto test je Fine

SECTION data
Var1: db 8,'Sir_Vinz'
Var2: db 9,'FifaDigio'
Var3: db 6,'Uguali'

SECTION text

..start:
	mov ax,data
	mov ds,ax
	mov es,ax
	xor cx,cx
	
	mov cl,[Var1]	;[Var] ->Chiedo quanto Grande --- Var+1 Prende il valore effettivo 4Vinz -> V --- 4 indica grandezza Stringa
	mov dl,[Var2]
	
	cmp cl,dl
	je Uguali

	cmp cl,dl
	ja CLMaggiore

	cmp cl,dl
	jb CLMinore

Printa:	mov ah,0eh
	int 10h
	ret

CLMaggiore:
	mov cl,[Var1]
	mov si,Var1+1
Magg:	lodsb
	call Printa 
	loop Magg
	jmp Fine
	
CLMinore:
	mov cl,[Var2]
	mov si,Var2+1
Min:	lodsb
	call Printa 
	loop Min
	jmp Fine
Uguali:
	mov cl,[Var3]
	mov si,Var3+1
Ugu:	lodsb
	call Printa 
	loop Ugu
	jmp Fine

Fine: 	mov ax,4C00h
	int 21h