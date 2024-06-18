from Classi import *
import random

oggRana=object.__new__(Rana)
oggRana=Rana(int(arenaL/2),(arenaA-altezzaRana),0,0,20)

oggVeicoloRosso=object.__new__(Attore)
oggVeicoloRosso=Attore(0,405,0,0,random.randint(2,15),"Veicolo")

oggVeicoloViola=object.__new__(Attore)
oggVeicoloViola=Attore(640,370,0,0,random.randint(2,10),"Veicolo")

oggTrattore=object.__new__(Attore)
oggTrattore=Attore(0,336,0,0,random.randint(2,8),"Veicolo")

oggAutobus=object.__new__(Attore)
oggAutobus=Attore(640,310,0,0,random.randint(2,8),"Veicolo")

oggAutoGialla=object.__new__(Attore)
oggAutoGialla=Attore(640,275,0,0,random.randint(2,18),"Veicolo")

VelRand1=random.randint(1,3)
oggZatteraLunga=object.__new__(Attore)
oggZatteraLunga=Attore(-160,220,0,0,VelRand1,"S")

oggZatteraMedia=object.__new__(Attore)
oggZatteraMedia=Attore(-180,200,0,0,random.randint(3,5),"S")

oggZatteraPiccola=object.__new__(Attore)
oggZatteraPiccola=Attore(670,180,0,0,random.randint(5,8),"S")

oggZatteraLunga1=object.__new__(Attore)
oggZatteraLunga1=Attore(670,160,0,0,random.randint(1,3),"S")

VelRand2=random.randint(3,5)
oggZatteraMedia1=object.__new__(Attore)
oggZatteraMedia1=Attore(-160,140,0,0,VelRand2,"S")

VelRand3=random.randint(5,8)
oggZatteraPiccola1=object.__new__(Attore)
oggZatteraPiccola1=Attore(670,120,0,0,VelRand3,"S")

oggZatteraLunga2=object.__new__(Attore)
oggZatteraLunga2=Attore(670,100,0,0,random.randint(1,3),"S")

VelRand=random.randint(3,5)
oggZatteraMedia2=object.__new__(Attore)
oggZatteraMedia2=Attore(-180,80,0,0,VelRand,"S")

oggLontra=object.__new__(Attore)
oggLontra=Attore(-10,80,0,0,VelRand,"S")

oggCoccodrillo=object.__new__(Attore)
oggCoccodrillo=Attore(120,210,0,0,VelRand1,"S")

oggCoccodrilloChiuso=object.__new__(Attore)
oggCoccodrilloChiuso=Attore(0,140,0,0,VelRand2,"S")

OggUccello=object.__new__(Attore)
OggUccello=Attore(500,123,0,0,VelRand3,"S")