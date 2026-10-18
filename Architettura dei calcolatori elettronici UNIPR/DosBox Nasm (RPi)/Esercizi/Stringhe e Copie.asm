SECTION data
Stringa: resb 255
Stringa2: db 8,'Sir_Vinz'
	 times 255-9 db ' '
SECTION text
..start:
	mov ax,data
	mov ds,ax

	mov es,ax	;questa istruzione è necessaria perché il BIOS utilizza spesso il segmento dei dati (es) 
			;per accedere ai dati quando vengono eseguite operazioni di I/O come la stampa a schermo.

;Senza l'inizializzazione di es, il BIOS non saprebbe dove cercare i dati 
;quando viene effettuata una chiamata di I/O. Poiché le routine di I/O del BIOS si aspettano di trovare i dati nel segmento dei dati (es), 
;il mancato inizializzazione di es potrebbe causare errori o comportamenti imprevisti durante l'esecuzione del programma.

	mov cl,[Stringa2]
	mov [Stringa],cl	;carichi la lunghezza si Stringa2 nella prima posizione di Stringa

	mov si,Stringa2+1	;metti nel source index l'inizio della sorgente
	mov di,Stringa+1	;metti nel destination index la fine dell'arrivo

	rep movsb		;copia un dato da DS:SI a ES:DI; rep ripete fino a cx volte (grandezza string qui)
				;movsb copia i dati byte dalla source alla destinazione
	
	mov cl,[Stringa]	;carichi la lunghezza di Stringa in cl
	mov si,Stringa+1	;il source parte dal dato effettivo (+1) e non dalla sua lunghezza

	mov ah,0eh	;Stampa la lunghezza della stringa precendetemente messa da mov cl,[Stringa]
	int 10h

Stampa:	lodsb	;carico un dato da ds ad ax o al
	int 10h	;stampo semplicemente perchè ax è già pieno
	loop Stampa

Fine:   mov ax,4C00h
	int 21h