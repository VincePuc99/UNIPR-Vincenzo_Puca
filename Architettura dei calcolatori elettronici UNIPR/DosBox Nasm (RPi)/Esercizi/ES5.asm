SECTION data
VarPasquale: db 8,'Sir_Vinz'
VarVocali: resb 1

SECTION text
..start:
	mov ax,data
	mov ds,ax
	mov es,ax

	mov cl,[VarPasquale] 	;metto in cl la lunghezza della stringa
	mov si,VarPasquale+1	;metto nel source l'inizio della var in modo da farla spazzolare da lodsb

	xor dl, dl  ; Inizializza dl (registro contatore) a 0 ;MANDATORIO in quanto non funziona senza 

Spazzolino:
	lodsb
	cmp al,'a'
	je AddVocal

	cmp al,'e'
	je AddVocal

	cmp al,'i'
	je AddVocal

	cmp al,'o'
	je AddVocal

	cmp al,'u'
	je AddVocal

	loop Spazzolino
	jmp SkipAdd

AddVocal:
	inc dl
	loop Spazzolino

SkipAdd:
	mov [VarVocali],dl
	mov al,[VarVocali]

	add al,30h

	mov  ah,0eh    ; Funzione di scrittura a video 
        mov  bx,00h    ; Pagina 0 (BH) 
        int  10h 

Fine:   mov ax,4C00h
	int 21h

TrasformazioneNumero: 
	


