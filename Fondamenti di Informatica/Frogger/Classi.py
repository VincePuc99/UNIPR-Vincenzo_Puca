import g2d

arenaL,arenaA,altezzaRana,life,punteggio=640,480,40,3,0
AutoRossa,autoviola=g2d.load_image("AutoRossa.png"),g2d.load_image("AutoViola.png")
trattore,autobus=g2d.load_image("Trattore.png"),g2d.load_image("Autobus.png")
autoGialla,zatterapiccola=g2d.load_image("AutoGialla.png"),g2d.load_image("ZatteraPiccola.png")
zatteralunga,zatteramedia=g2d.load_image("ZatteraLunga.png"),g2d.load_image("ZatteraMedia.png")
ranaSU,ranaSIN=g2d.load_image("RanaSU.png"),g2d.load_image("RanaSIN.png")
ranaGIU,ranaDES=g2d.load_image("RanaGIU.png"),g2d.load_image("RanaDES.png")
lontra,coccodrillo,coccodrillochiuso=g2d.load_image("Lontra.png"),g2d.load_image("Coccodrillo.png"),g2d.load_image("CoccodrilloChiuso.png")
uccello=g2d.load_image("Uccello.png")

def RitornaVite()->int:
  return life
def RitornaPunti()->int:
  return punteggio

class Rana():
 def __init__(self,x:int,y:int,dx:int,dy:int,speed:int):
  self._x,self._y,self._dx,self._dy,self._speed=x,y,dx,dy,speed
 #disegno
 def CreaRanaSU(self): 
  g2d.draw_image(ranaSU,(self._x,self._y))
 def CreaRanaGIU(self): 
  g2d.draw_image(ranaGIU,(self._x,self._y))
 def CreaRanaSIN(self): 
  g2d.draw_image(ranaSIN,(self._x,self._y))
 def CreaRanaDES(self): 
  g2d.draw_image(ranaDES,(self._x,self._y))
 #collisioni
 def Collisione_Rana_Arena(self):
  global life,punteggio
  if self._x < 0 or self._x>arenaL:
   life-=1
   self._x=int(arenaL/2) #punto di partenza
   self._y=arenaA-altezzaRana
  if self._y>arenaA:#outbasso
   life-=1
   self._x=int(arenaL/2) #punto di partenza
   self._y=arenaA-altezzaRana
  if self._y<20:#win
   life+=1
   punteggio+=500
   self._x=int(arenaL/2) #punto di partenza
   self._y=arenaA-altezzaRana
  if self._y>=47 and self._y<=78:#collisione blocchi di fiume in alto
   if self._x>=45 and self._x<=78:
    life-=1
    self._x=int(arenaL/2) #punto di partenza
    self._y=arenaA-altezzaRana
    punteggio-=500
   if self._x>=170 and self._x<=205:
    life-=1
    self._x=int(arenaL/2) #punto di partenza
    self._y=arenaA-altezzaRana
   if self._x>=305 and self._x<=335:
    life-=1
    self._x=int(arenaL/2) #punto di partenza
    self._y=arenaA-altezzaRana
    punteggio-=500
   if self._x>=430 and self._x<=460:
    life-=1
    self._x=int(arenaL/2) #punto di partenza
    self._y=arenaA-altezzaRana
   if self._x>=560 and self._x<=590:
    life-=1
    self._x=int(arenaL/2) #punto di partenza
    self._y=arenaA-altezzaRana
    punteggio-=500

 def Collisione_Rana_Veicolo(self,OggettoVeicolo:object):
  global life,punteggio
  x2,y2,speed=OggettoVeicolo.position()
  if self._x>x2 and self._x<(x2+30) and self._y>y2 and self._y<(y2+25):
   self._x=int(arenaL/2) #punto di partenza
   self._y=arenaA-altezzaRana
   g2d.draw_image(ranaSU,(self._x,self._y))
   life-=1
   punteggio-=500
  
 def Collisione_Rana_Fiume(self,OggZattera:object,LunghZattera:int,IndOAva:bool):
  global punteggio,life
  OggX,OggY,OggSpeed=OggZattera.position()
  if self._y<=(OggY+10)and self._y>=(OggY-10):
   if (self._x<=(OggX+LunghZattera) and self._x>=OggX):  
    if IndOAva == True:
     self._dx+=OggSpeed
     punteggio+=1
    else:
     self._dx-=OggSpeed
     punteggio+=1
   else:
    self._x=int(arenaL/2) #punto di partenza
    self._y=arenaA-altezzaRana
    g2d.draw_image(ranaSU,(self._x,self._y))
    life-=1
    punteggio-=500
#movimenti e posizione
 def Move(self):
  self._x+=self._dx
  self._y+=self._dy

 def MovSU(self):
  self._dy-=self._speed
 def MovGIU(self):
  self._dy+=self._speed
 def MovSIN(self):
  self._dx-=self._speed
 def MovDES(self):
  self._dx+=self._speed

 def stay(self):
  self._dx,self._dy=0,0

 def position(self)->(int,int):
  return self._x,self._y

class Attore():
 def __init__(self,x:int,y:int,dx:int,dy:int,speed:int,tipo:str):
  self._x,self._y,self._dx,self._dy,self._speed,self._tipo=x,y,dx,dy,speed,tipo

 def Move(self):
  self._x+=self._dx+self._speed

 def MoveIndietro(self):
  self._x-=self._dx+self._speed

 def Out_Arena(self):
  if self._tipo=="Veicolo":
   if self._x>arenaL:
    self._x=-50
   if self._x<-60:
    self._x=arenaL
  else:
   if self._x>arenaL:
    self._x=-180
   if self._x<-180:
    self._x=arenaL

 def CreaAutRossa(self):
  g2d.draw_image(AutoRossa,(self._x,self._y))

 def CreaAutViola(self):
  g2d.draw_image(autoviola,(self._x,self._y))

 def CreaTrattore(self):
  g2d.draw_image(trattore,(self._x,self._y))

 def CreaAutobus(self):
  g2d.draw_image(autobus,(self._x,self._y))

 def CreaAutGialla(self):
  g2d.draw_image(autoGialla,(self._x,self._y))

 def CreaZattLunga(self):
  g2d.draw_image(zatteralunga,(self._x,self._y))

 def CreaZattMedia(self):
  g2d.draw_image(zatteramedia,(self._x,self._y))

 def CreaZattPiccola(self):
  g2d.draw_image(zatterapiccola,(self._x,self._y))

 def CreaLontra(self):
  g2d.draw_image(lontra,(self._x,self._y))

 def CreaCoccodrillo(self):
  g2d.draw_image(coccodrillo,(self._x,self._y))

 def CreaCoccodrilloChiuso(self):
  g2d.draw_image(coccodrillochiuso,(self._x,self._y))

 def CreaUccello(self):
  g2d.draw_image(uccello,(self._x,self._y))

 def position(self)->(int,int,int):
  return self._x,self._y,self._speed