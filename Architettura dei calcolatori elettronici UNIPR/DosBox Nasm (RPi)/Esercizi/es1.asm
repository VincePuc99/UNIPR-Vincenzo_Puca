SECTION data 
Numero db 0

SECTION text

..start:
Leggi: mov ax,data 
       mov ds,ax 
       xor bx,bx
Ripeti:mov ah,00h
       int 16h
       cmp al,1bh
       je Fine     ;SE è UGUALE
       cmp al,'0'
       jb Ripeti   ;SE è MINORE
       cmp al,'9'
       ja Ripeti   ;SE è MAGGIORE
       mov bl,al
       sub bl,30h  ;to int
       mov ax,[Numero]
       mov dx,10h
       mul dx
       add ax,bx
       mov [Numero],ax 
       jmp Ripeti
Fine: mov ax,4C00h
      int 21h


