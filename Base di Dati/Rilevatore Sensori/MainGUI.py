import PySimpleGUI as GUIController
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from tkinter import *
import sys

import Connect as CT

def SplashScreen():
  GUIController.theme('LightBlue')
  GUIController.set_options(font=('Roboto', 12))

  layout = [
    [GUIController.Text("~~~~~~~~~ Dati utente ~~~~~~~~~")],
    [GUIController.Text("ID Gestore"),GUIController.Multiline(size=(5, 1), key='adminid')],
    [GUIController.Text("ID Sensore gestito"),GUIController.Multiline(size=(5, 1), key='sensorid')],
    [GUIController.Text("Mostra Sensori"),GUIController.Button("Mostra Sensori")],
    [GUIController.Text("~~~~~~~~~~~~~~~~~~~~~~~~~~~~")],
    [GUIController.Button("Login")],
    ]

  window = GUIController.Window("Amministrazione", layout, finalize=True,element_justification='center')
  window.SetIcon(r'AppLogoSensor.ico')
 
  while True:
    event, values = window.read()

    if event=="Mostra Sensori":
      listprod,listidsens=CT.GetSensData(Curs)
      ShowSensData(listprod,listidsens)

    if event=="Login":
      if values['adminid']=="" or values['sensorid']=="":
       GUIController.popup(f"Valore non valido riprova")
      else:
       CT.SendLogin(Curs,ConnToDB,values['adminid'],values['sensorid'])
       window.close()
       break

    if event == GUIController.WIN_CLOSED:
      window.close()
      sys.exit()
      break

#################### Main Var Part #######################

Curs,ConnToDB = CT.GetCursorConn()
blanklist=[]

################# DB Show ###################################

def ShowDB(ListTemp,ListLumen,IDRecord):

 #################### DB Show Var Part #######################
 TitoloColonne=['IDDato','Temperatura','Luminosità %']
 lista=list(map(list, zip(IDRecord,ListTemp, ListLumen)))

 #################### GUI Section DB Show Window #######################
 GUIController.theme('LightBlue')
 GUIController.set_options(font=('Roboto', 12))

 layout=[
  [GUIController.Table(values=lista,headings=TitoloColonne, max_col_width=30,
              auto_size_columns=True,
              display_row_numbers=False,
              justification='right',
              num_rows=len(ListTemp),
              key='-TABLE-',
              row_height=18)]
 ]

 window = GUIController.Window("Database", layout, finalize=True,element_justification='center',location=(0,0),resizable=True)
 window.SetIcon(r'AppLogoSensor.ico')
 ################ GUI Action Section DB Show Window ##########################
 while True:
  event, values = window.read()
  if event == "Esci" or event == GUIController.WIN_CLOSED:
   window.close()
   break

################# Show Sensor Data ###################################

def ShowSensData(listprod,listidsens):

 #################### DB Show Var Part #######################
 TitoloColonne=['IDSensore','Produttore']
 lista=list(map(list, zip(listidsens,listprod)))

 #################### GUI Section DB Show Window #######################
 GUIController.theme('LightBlue')
 GUIController.set_options(font=('Roboto', 12))

 layout=[
  [GUIController.Table(values=lista,headings=TitoloColonne, max_col_width=30,
              auto_size_columns=True,
              display_row_numbers=False,
              justification='right',
              num_rows=len(listprod),
              key='-TABLE-',
              row_height=18)]
 ]

 window = GUIController.Window("Database Sensori", layout, finalize=True,element_justification='center',resizable=True)
 window.SetIcon(r'AppLogoSensor.ico')
 ################ GUI Action Section DB Show Window ##########################
 while True:
  event, values = window.read()
  if event == "Esci" or event == GUIController.WIN_CLOSED:
   window.close()
   break

################ Second Window ###########################

def SecondWindow():
#################### Second window Var Part #######################
    ListTemp,ListLumen,IDRecord=CT.GetData(Curs)
    ListTemp.sort()
    ListLumen.sort()
    plt.clf() #pulisce grafo precedente

    #################### GUI Section Second Window #############################
    GUIController.theme('LightBlue')
    GUIController.set_options(font=('Roboto', 12))

    layout = [
       [GUIController.Canvas(size=(1000,1000), key="-CANVAS-")],
       [GUIController.Button("Esci")],
    ]

    window = GUIController.Window("Finestra Grafico", layout, finalize=True,element_justification='center')
    window.SetIcon(r'AppLogoSensor.ico')

    disegna_grafo(window['-CANVAS-'].TKCanvas,creaplot(ListTemp,ListLumen))

    if ListTemp==blanklist or ListLumen==blanklist: #error no data
     GUIController.popup("Dati vuoti impossibile creare il grafico")
     window.close()
    
    ################ GUI Action Section Second Window ##########################
    while True:
        event, values = window.read()
        if event == "Esci" or event == GUIController.WIN_CLOSED:
            window.close()
            break

def creaplot(ListTemp,ListLumen):
    plt.plot(ListTemp,ListLumen,color='blue',marker='o')
    plt.title('Grafo Sensori',fontsize='14')
    plt.xlabel('Temperatura', fontsize='14')
    plt.ylabel('Luminosità %', fontsize='14')
    plt.grid(True)
    return plt.gcf()

def disegna_grafo(canvas,figure):
    figure_canvas_agg= FigureCanvasTkAgg(figure,canvas)
    figure_canvas_agg.draw()
    figure_canvas_agg.get_tk_widget().pack(side='top',fill='both',expand=True)
    return figure_canvas_agg

#################### GUI Section #############################
SplashScreen()

GUIController.theme('LightBlue')
GUIController.set_options(font=('Roboto', 12))

layout= [
        [GUIController.Text("                                         "),GUIController.Image(r'Progetto DB\MainApp.png')],
        [GUIController.Text("~~~~~~~~~~~~~~~~~~~~~~~~ Sezione Gestione Dati ~~~~~~~~~~~~~~~~~~~~~~~~")],
        [GUIController.Text("Genera grafico dati di tutti i sensori:"),GUIController.Button("Genera Grafico")],

        [GUIController.Text("Rimuovi dato (ID):"),GUIController.Multiline(size=(5, 1), key='textbox'),GUIController.Button("Elimina Dato per ID")],
        [GUIController.Text("Rimuovi dato (Temp):"),GUIController.Multiline(size=(5, 1), key='textboxtemp'),GUIController.Button("Elimina Dato per Temp")],
        [GUIController.Text("Rimuovi dato (Lumen):"),GUIController.Multiline(size=(5, 1), key='textboxLumen'),GUIController.Button("Elimina Dato per Lumen")],
        [GUIController.Text("Inserisci dato:"),GUIController.Text("Lumen"),GUIController.Multiline(size=(5, 1), key='textboxgivenlumen'),GUIController.Text("Temp"),GUIController.Multiline(size=(5, 1), key='textboxgiventemp'),GUIController.Button("Aggiungi")],
        [GUIController.Text("Modifica dato:"),GUIController.Text("ID"),GUIController.Multiline(size=(5, 1), key='textboxeditid'),GUIController.Text("Lumen"),GUIController.Multiline(size=(5, 1), key='textboxeditlumen'),GUIController.Text("Temp"),GUIController.Multiline(size=(5, 1), key='textboxedittemp'),GUIController.Button("Modifica")],

        [GUIController.Text("~~~~~~~~~~~~~~~~~~~~~~~~ Sezione Gestione Sensori ~~~~~~~~~~~~~~~~~~~~~~")],
        [GUIController.Text("Aggiungi sensore:"),GUIController.Text("Costruttore"),GUIController.Multiline(size=(10, 1), key='sensmanufact'),GUIController.Button("Aggiungi Sensore")],
        [GUIController.Text("Rimuovi sensore:"),GUIController.Text("ID"),GUIController.Multiline(size=(10, 1), key='sensid'),GUIController.Button("Rimuovi Sensore")],

        [GUIController.Text("~~~~~~~~~~~~~~~~~~~~~~~~ Sezione Finalità di Test ~~~~~~~~~~~~~~~~~~~~~~~~")],
        [GUIController.Text("Mostra Dati:", font='bold'),GUIController.Button("Mostra Dati")],
        [GUIController.Text("Mostra Sensori:", font='bold'),GUIController.Button("Mostra Sensori")],

        [GUIController.Text("Genera 20 valori casuali:"),GUIController.Button("Inserisci Dati")],
        [GUIController.Text("Svuota tabella Dati:"),GUIController.Button("Svuota Tabella Dati")],
        [GUIController.Text("Rimuovi Tutti i sensori:"),GUIController.Button("Svuota Tabella Sensori")],
        [GUIController.Text("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")],
        [GUIController.Text("Chiudi Programma:"),GUIController.Button("Esci")],
]

window = GUIController.Window("Sensor HUB Emulator", layout)
window.SetIcon(r'AppLogoSensor.ico')

################ Main GUI Action Section ##########################

while True:
    event,values=window.read()

    if event=="Mostra Dati":
      ListTemp,ListLumen,IDRecord=CT.GetData(Curs)
      ShowDB(ListTemp,ListLumen,IDRecord)

    if event=="Mostra Sensori":
      listprod,listidsens=CT.GetSensData(Curs)
      ShowSensData(listprod,listidsens)

    if event=="Inserisci Dati":
      CT.InjectInfo(Curs,ConnToDB)
      GUIController.popup(f"Valori generati, ID Ultima Riga inserita: {Curs.lastrowid}")

    if event=="Genera Grafico":
     SecondWindow()

    if event=="Elimina Dato per ID":
     textboxvalue=values['textbox']
     if (textboxvalue==""):
      GUIController.popup(f"Valore non valido riprova")
     else:
      CT.DeleteOneFromID(Curs,ConnToDB,textboxvalue)
      GUIController.popup(f"Valore eliminato")

    if event=="Elimina Dato per Temp":
     textboxvaluetemp=values['textboxtemp']
     if (textboxvaluetemp==""):
      GUIController.popup(f"Valore non valido riprova")
     else:
      CT.DeleteOneFromTemp(Curs,ConnToDB,textboxvaluetemp)
      GUIController.popup(f"Valore eliminato")

    if event=="Elimina Dato per Lumen":
     textboxvalueLumen=values['textboxLumen']
     if (textboxvalueLumen==""):
      GUIController.popup(f"Valore non valido riprova")
     else:
      CT.DeleteOneFromLumen(Curs,ConnToDB,textboxvalueLumen)
      GUIController.popup(f"Valore eliminato")

    if event=="Aggiungi":
      datoluce=values['textboxgivenlumen']
      datotemp=values['textboxgiventemp']
      if (datoluce=="" or datotemp==""):
       GUIController.popup(f"Valore non valido riprova")
      else:
       CT.InjectInfoTempLumen(Curs,ConnToDB,datoluce,datotemp)
       GUIController.popup(f"Valore aggiunto, ID Riga aggiunta: {Curs.lastrowid}")

    if event=="Svuota Tabella Dati":
     CT.PurgeRecords(Curs,ConnToDB)
     GUIController.popup(f"Valori cancellati, (None = Tabella Dati vuota)): {Curs.lastrowid}")

    if event=="Svuota Tabella Sensori":
     CT.PurgeAllSensors(Curs,ConnToDB)
     GUIController.popup(f"Sensori cancellati, (None = Tabella Sensori vuota)): {Curs.lastrowid}")

    if event=="Modifica":
     editluce=values['textboxeditlumen']
     editid=values['textboxeditid']
     edittemp=values['textboxedittemp']
     if (editluce=="" or edittemp=="" or editid==""):
      GUIController.popup(f"Valore non valido riprova")
     else:
      CT.EditData(Curs,ConnToDB,editid,edittemp,editluce)
      GUIController.popup(f"Record Numero:", editid ,"Modificato con successo")

    if event=="Aggiungi Sensore":
      if values["sensmanufact"]=="":
       GUIController.popup(f"Valore non valido riprova")
      else:
       CT.AddSens(Curs,ConnToDB,values["sensmanufact"])
       GUIController.popup(f"Sensore Aggiunto con successo")

    if event=="Rimuovi Sensore":
      if values["sensid"]=="":
       GUIController.popup(f"Valore non valido riprova")
      else:
       CT.RemoveSensID(Curs,ConnToDB,values["sensid"])
       GUIController.popup(f"Sensore Rimosso con successo")
            
    if event == "Esci" or event == GUIController.WIN_CLOSED: #button pressed close program
      CT.CloseDB(Curs,ConnToDB)
      window.close()
      break