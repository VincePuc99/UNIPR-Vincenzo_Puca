from Classi import *
from Oggetti import *
#Variabili
GuardaDES,GuardaSIN,GuardaGIU=False,False,False
titolo,backgroud=g2d.load_image("title.png"),g2d.load_image("Sfondo.png")
gameover=g2d.load_image("Fine.png")
g2d.init_canvas((arenaL,arenaA))

def Game():
 g2d.handle_keyboard(keydown,keyup)
 #Grafica Iniziale
 Grafica_Standard()
 #Grafica Attori
 Grafica_Movimento_Attori()
 #Condizione di blocco Game
 if RitornaVite()<=0:
  g2d.draw_image(gameover,(30,0))
  g2d.main_loop(Fine,1000//30)
  
#Metodi Grafica
def Grafica_Standard():
 g2d.draw_image(backgroud,(0,0))
 g2d.draw_image(titolo,(1,0))
 g2d.draw_text("Rane Rimanenti: ",(0,255,0),(160,2),21)
 g2d.draw_text(str(RitornaVite()),(0,255,0),(290,3),20)
 g2d.draw_text("Punteggio: ",(0,255,0),(310,2),21)
 g2d.draw_text("["+str(RitornaPunti())+"]",(0,255,0),(410,2),20)
 g2d.draw_text("Arrivo-> +1 Vita +500 punti / Surf su zattera-> +1 PuntoPerSec / Morte-> -1 Vita -500 Punti",(255,255,255),(2,462),21)

def Grafica_Movimento_Attori():
 oggVeicoloRosso.Move()
 oggVeicoloRosso.Out_Arena()
 oggVeicoloRosso.CreaAutRossa()
 oggRana.Collisione_Rana_Veicolo(oggVeicoloRosso)
 
 oggVeicoloViola.MoveIndietro()
 oggVeicoloViola.Out_Arena()
 oggVeicoloViola.CreaAutViola()
 oggRana.Collisione_Rana_Veicolo(oggVeicoloViola)

 oggTrattore.Move()
 oggTrattore.Out_Arena()
 oggTrattore.CreaTrattore()
 oggRana.Collisione_Rana_Veicolo(oggTrattore)

 oggAutobus.MoveIndietro()
 oggAutobus.Out_Arena()
 oggAutobus.CreaAutobus()
 oggRana.Collisione_Rana_Veicolo(oggAutobus)

 oggAutoGialla.MoveIndietro()
 oggAutoGialla.Out_Arena()
 oggAutoGialla.CreaAutGialla()
 oggRana.Collisione_Rana_Veicolo(oggAutoGialla)

 oggZatteraLunga.Move()
 oggZatteraLunga.Out_Arena()
 oggZatteraLunga.CreaZattLunga()
 oggRana.Collisione_Rana_Fiume(oggZatteraLunga,175,True)

 oggZatteraMedia.Move()
 oggZatteraMedia.Out_Arena()
 oggZatteraMedia.CreaZattMedia()
 oggRana.Collisione_Rana_Fiume(oggZatteraMedia,115,True)

 oggZatteraPiccola.MoveIndietro()
 oggZatteraPiccola.Out_Arena()
 oggZatteraPiccola.CreaZattPiccola()
 oggRana.Collisione_Rana_Fiume(oggZatteraPiccola,80,False)

 oggZatteraLunga1.MoveIndietro()
 oggZatteraLunga1.Out_Arena()
 oggZatteraLunga1.CreaZattLunga()
 oggRana.Collisione_Rana_Fiume(oggZatteraLunga1,175,False)

 oggZatteraMedia1.Move()
 oggZatteraMedia1.Out_Arena()
 oggZatteraMedia1.CreaZattMedia()
 oggRana.Collisione_Rana_Fiume(oggZatteraMedia1,115,True)

 oggZatteraPiccola1.MoveIndietro()
 oggZatteraPiccola1.Out_Arena()
 oggZatteraPiccola1.CreaZattPiccola()
 oggRana.Collisione_Rana_Fiume(oggZatteraPiccola1,80,False)

 oggZatteraLunga2.MoveIndietro()
 oggZatteraLunga2.Out_Arena()
 oggZatteraLunga2.CreaZattLunga()
 oggRana.Collisione_Rana_Fiume(oggZatteraLunga2,175,False)

 oggZatteraMedia2.Move()
 oggZatteraMedia2.Out_Arena()
 oggZatteraMedia2.CreaZattMedia()
 oggRana.Collisione_Rana_Fiume(oggZatteraMedia2,115,True)

 oggLontra.Move()
 oggLontra.Out_Arena()
 oggLontra.CreaLontra()

 oggCoccodrillo.Move()
 oggCoccodrillo.Out_Arena()
 oggCoccodrillo.CreaCoccodrillo()

 oggCoccodrilloChiuso.Move()
 oggCoccodrilloChiuso.Out_Arena()
 oggCoccodrilloChiuso.CreaCoccodrilloChiuso()

 OggUccello.MoveIndietro()
 OggUccello.Out_Arena()
 OggUccello.CreaUccello()

 #Movimento rana
 oggRana.Move()
 oggRana.Collisione_Rana_Arena()
 if GuardaGIU==True:
  oggRana.CreaRanaGIU()
 elif GuardaSIN==True:
  oggRana.CreaRanaSIN()
 elif GuardaDES==True:
  oggRana.CreaRanaDES()
 else:
  oggRana.CreaRanaSU()
 oggRana.stay()

def Fine():#Richiamato per bloccare il gioco
  pass

def keydown(code:str):
 global GuardaDES,GuardaSIN,GuardaGIU,life
 if code == "ArrowUp":
  oggRana.MovSU()
 elif code=="ArrowDown":
  oggRana.MovGIU()
  GuardaGIU=True
 elif code=="ArrowLeft":
  oggRana.MovSIN()
  GuardaSIN=True
 elif code=="ArrowRight":
  oggRana.MovDES()
  GuardaDES=True
 else:
  oggRana.stay()

def keyup(code):
 global GuardaDES,GuardaSIN,GuardaGIU
 GuardaDES,GuardaSIN,GuardaGIU=False,False,False
 oggRana.stay()

g2d.main_loop(Game,1000//30)