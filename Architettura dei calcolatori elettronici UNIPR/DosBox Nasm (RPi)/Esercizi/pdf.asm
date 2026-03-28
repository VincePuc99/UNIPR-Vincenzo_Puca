SECTION data 
N: db 100 
Vett1:   times 25 db 3 
    times 25 db 4 
    times 25 db 5 
    times 25 db 6 
Vett2: resb 100 
SECTION text 
..start: 
  mov ax, data 
  mov ds, ax 
  mov es, ax 
  call Filtro 
  mov ax, 4c00h 
  int 21h 
   
   
Filtro: 
  mov si, Vett1 
  mov di, Vett2 
  xor ch, ch 
  mov cl, [N] 
Ciclo: 
  lodsb 
  test al, 00000001b 
  je salta 
  stosb 
salta: 
  loop Ciclo 
  ret 