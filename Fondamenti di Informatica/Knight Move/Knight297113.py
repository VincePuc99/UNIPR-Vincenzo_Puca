from boardgame_g2d import BoardGameGui
from boardgame import BoardGame
import g2d

class Knight(BoardGame):
 def __init__(self,Larg,Altez):
  self._Contafine=0
  self._Nmossa=1
  self._OldX=0
  self._OldY=0
  self._FirstMove=0
  self._cols, self._rows = Larg,Altez
  self._board = self.RiempiScacchiera(Larg,Altez)

 def RiempiScacchiera(self, Larg, Altez):
  scacchiera = []
  for i in range(Larg):
   Riga = []
   for j in range(Altez):
    Riga.append(0)
   scacchiera.append(Riga)
  return scacchiera

 def message(self) -> str:
  return "Gioco Finito!!!"
 def cols(self) -> int:
  return self._cols
 def rows(self) -> int:
  return self._rows

 def flag_at(self,x,y,H,W)->(int,int):#backtraking guarda 1 mossa dopo
   Conta1,Conta2,Conta3,Conta4,Conta5,Conta6,Conta7,Conta8=0,0,0,0,0,0,0,0
   if (y-2)>=0 and (x+1)<Dimens:
    if self._board[y-2][x+1]==0:
     Conta1=self.GuardaAttorno(y-2,x+1)
   if (y-1)>=0 and (x+2)<Dimens:
    if self._board[y-1][x+2]==0:
     Conta2=self.GuardaAttorno(y-1,x+2)
   if (y+1)<Dimens and (x+2)<Dimens:
    if self._board[y+1][x+2]==0:
     Conta3=self.GuardaAttorno(y+1,x+2)
   if (y+2)<Dimens and (x+1)<Dimens:
    if self._board[y+2][x+1]==0:
     Conta4=self.GuardaAttorno(y+2,x+1)
   if (y+1)<Dimens and (x-2)>=0:
    if self._board[y+1][x-2]==0:
     Conta5=self.GuardaAttorno(y+1,x-2)
   if (y+2)<Dimens and (x-1)>=0:
    if self._board[y+2][x-1]==0:
     Conta6=self.GuardaAttorno(y+2,x-1)
   if (y-2)>=0 and (x-1)>=0:
    if self._board[y-2][x-1]==0:
     Conta7=self.GuardaAttorno(y-2,x-1)
   if (y-1)>=0 and (x-2)>=0:
    if self._board[y-1][x-2]==0:
     Conta8=self.GuardaAttorno(y-1,x-2)

   if Conta1==0 and Conta2==0 and Conta3==0 and Conta4==0 and Conta5==0 and Conta6==0 and Conta7==0 and Conta8==0:
     print("BackTracking non disponibile")
     return (0,0)

   if Conta1>=Conta2 and Conta1>=Conta3 and Conta1>=Conta4 and Conta1>=Conta5 and Conta1>=Conta6 and Conta1>=Conta7 and Conta1>=Conta8:
     return (-2,+1)
   if Conta2>=Conta1 and Conta2>=Conta3 and Conta2>=Conta4 and Conta2>=Conta5 and Conta2>=Conta6 and Conta2>=Conta7 and Conta2>=Conta8:
     return (-1,+2)
   if Conta3>=Conta2 and Conta3>=Conta1 and Conta3>=Conta4 and Conta3>=Conta5 and Conta3>=Conta6 and Conta3>=Conta7 and Conta3>=Conta8:
     return (+1,+2)
   if Conta4>=Conta2 and Conta4>=Conta3 and Conta4>=Conta1 and Conta4>=Conta5 and Conta4>=Conta6 and Conta4>=Conta7 and Conta4>=Conta8:
     return (+2,+1)
   if Conta5>=Conta2 and Conta5>=Conta3 and Conta5>=Conta4 and Conta5>=Conta1 and Conta5>=Conta6 and Conta5>=Conta7 and Conta5>=Conta8:
     return (+1,-2)
   if Conta6>=Conta2 and Conta6>=Conta3 and Conta6>=Conta4 and Conta6>=Conta5 and Conta6>=Conta1 and Conta6>=Conta7 and Conta6>=Conta8:
     return (+2,-1)
   if Conta7>=Conta2 and Conta7>=Conta3 and Conta7>=Conta4 and Conta7>=Conta5 and Conta7>=Conta6 and Conta7>=Conta1 and Conta7>=Conta8:
     return (-2,-1)
   if Conta8>=Conta2 and Conta8>=Conta3 and Conta8>=Conta4 and Conta8>=Conta5 and Conta8>=Conta6 and Conta8>=Conta7 and Conta8>=Conta1:
     return (-1,-2)

 def GuardaAttorno(self,ypass,xpass)->int:
  CountLiberi=0
  if (ypass-2)>=0 and (xpass+1)<Dimens:
    if self._board[ypass-2][xpass+1]==0:
     CountLiberi+=1
  if (ypass-1)>=0 and (xpass+2)<Dimens:
    if self._board[ypass-1][xpass+2]==0:
     CountLiberi+=1
  if (ypass+1)<Dimens and (xpass+2)<Dimens:
    if self._board[ypass+1][xpass+2]==0:
     CountLiberi+=1
  if (ypass+2)<Dimens and (xpass+1)<Dimens:
    if self._board[ypass+2][xpass+1]==0:
     CountLiberi+=1
  if (ypass+1)<Dimens and (xpass-2)>=0:
    if self._board[ypass+1][xpass-2]==0:
     CountLiberi+=1
  if (ypass+2)<Dimens and (xpass-1) >=0:
    if self._board[ypass+2][xpass-1]==0:
     CountLiberi+=1
  if (ypass-2)>=0 and (xpass-1)>=0:
    if self._board[ypass-2][xpass-1]==0:
     CountLiberi+=1
  if (ypass-1)>=0 and (xpass-2)>=0:
    if self._board[ypass-1][xpass-2]==0:
     CountLiberi+=1
  return CountLiberi

 def play_at(self,x,y):
  Val=0
  if self._FirstMove==False:
   if x < Dimens and y < Dimens:
    self._board[y][x]=self._Nmossa
    self._Nmossa+=1
    self._FirstMove=True
    self._OldX=x
    self._OldY=y
    self._Contafine+=1
   else:
    print("Posizione fuori scacchiera")
  else:
   if x < Dimens and y < Dimens:
    if (x==self._OldX+1 and y==self._OldY-2) or (x==self._OldX+2 and y==self._OldY-1):
     if self._board[y][x]==0:    
      self._board[y][x]=self._Nmossa
      self._Nmossa+=1
      self._OldX=x
      self._OldY=y
      self._Contafine+=1
     else:
      print("Posto già occupato")
      Val=self._board[y][x]
      for i in range(len(self._board)):
       for j in range(len(self._board[i])):
        if Val<=self._board[i][j]:
         self._board[i][j]=0
      self._Nmossa=Val
    elif (x==self._OldX+2 and y==self._OldY+1) or (x==self._OldX+1 and y==self._OldY+2):
     if self._board[y][x]==0:    
      self._board[y][x]=self._Nmossa
      self._Nmossa+=1
      self._OldX=x
      self._OldY=y
      self._Contafine+=1
     else:
      print("Posto già occupato")
      Val=self._board[y][x]
      for i in range(len(self._board)):
       for j in range(len(self._board[i])):
        if Val<=self._board[i][j]:
         self._board[i][j]=0
      self._Nmossa=Val
    elif (x==self._OldX-1 and y==self._OldY+2) or (x==self._OldX-2 and y==self._OldY+1):
     if self._board[y][x]==0:    
      self._board[y][x]=self._Nmossa
      self._Nmossa+=1
      self._OldX=x
      self._OldY=y
      self._Contafine+=1
     else:
      print("Posto già occupato")
      Val=self._board[y][x]
      for i in range(len(self._board)):
       for j in range(len(self._board[i])):
        if Val<=self._board[i][j]:
         self._board[i][j]=0
      self._Nmossa=Val
    elif (x==self._OldX-1 and y==self._OldY-2) or (x==self._OldX-2 and y==self._OldY-1):
     if self._board[y][x]==0:    
      self._board[y][x]=self._Nmossa
      self._Nmossa+=1
      self._OldX=x
      self._OldY=y
      self._Contafine+=1
     else:
      print("Posto già occupato")
      Val=self._board[y][x]
      for i in range(len(self._board)):
       for j in range(len(self._board[i])):
        if Val<=self._board[i][j]:
         self._board[i][j]=0
      self._Nmossa=Val
    else:
     print("Mossa non ad L")
   else:
    print("Posizione fuori scacchiera")

 def get_val(self,y,x):
  return str(self._board[x][y])

 def finished(self):
  contafine=0
  for a in range(len(self._board)):
   for b in range(len(self._board[a])):
    if self._board[a][b]!=0:
      contafine+=1

  if contafine==Dimens*Dimens:
   return False
  contafine=0

def Start():
 global Dimens,OggGame
 Dimens=0
 while Dimens<=4:
  Dimens=int(input("Inserire dimensioni scacchiera: "))

 OggGame=Knight(Dimens,Dimens)
 OggGame.RiempiScacchiera(Dimens,Dimens)

 gui=BoardGameGui(OggGame)
 gui.main_loop()

Start()