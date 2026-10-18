import mariadb
import sys
import random
import datetime

def GetCursorConn(): #connect to mariaDB return cursor + conn var
    try:
     ConnToDB = mariadb.connect(
        user="myuser",
        password="root",
        host="192.168.1.112",
        port=3306,
        database="DBStore")
    except mariadb.Error as e:
        print(f"Errore di connessione a MariaDB Platform: {e}")
        sys.exit(1)

    Curs = ConnToDB.cursor() 
    return Curs,ConnToDB

def InjectInfo(Curs,ConnToDB): #inject 20 records into the DB (pseudo-random)

 datarand = datetime.date.today()
 for x in range(20):
   datotemp = random.randint(0,35)
   datolum = random.randint(0,100)
   try: 
    Curs.execute("INSERT INTO Dato (Temperatura,LuminositàPerc) VALUES (?, ?)", (datotemp,datolum)) 
   except mariadb.Error as e: 
    print(f"Errore Aggiunta dati al DB: {e}")

 ConnToDB.commit() 

def PurgeRecords(Curs,ConnToDB): #remove all records into the DB
    try:  
     Curs.execute("SET FOREIGN_KEY_CHECKS = 0")
     Curs.execute("TRUNCATE TABLE Dato")
     Curs.execute("TRUNCATE TABLE Gestione")
     Curs.execute("SET FOREIGN_KEY_CHECKS = 1")
    except mariadb.Error as e:
     print(f"Errore eliminazione dati dal DB: {e}")
    ConnToDB.commit() 

def PurgeAllSensors(Curs,ConnToDB): #remove all sensors into the DB
    try:  
     Curs.execute("SET FOREIGN_KEY_CHECKS = 0")
     Curs.execute("TRUNCATE TABLE Sensore")
     Curs.execute("TRUNCATE TABLE Produzione")
     Curs.execute("TRUNCATE TABLE Installazione")
     Curs.execute("TRUNCATE TABLE Dato")
     Curs.execute("SET FOREIGN_KEY_CHECKS = 1")
    except mariadb.Error as e:
     print(f"Errore eliminazione sensori dal DB: {e}")
    ConnToDB.commit() 

def GetData(Curs): #get output data return double lists tempdata + lumendata
    riga=0
    tempdata=[]
    lumendata=[]
    IDRecord=[]

    #getting temperatura value
    try:
     Curs.execute("SELECT Temperatura FROM Dato GROUP BY IDDato")
     for riga in Curs:
      tempdata.extend(riga) 
    except mariadb.Error as e:
      print(f"Errore recupero dati dal DB: {e}")

    #getting percentuale luminosità value
    riga=0
    try:
     Curs.execute("SELECT LuminositàPerc FROM Dato GROUP BY IDDato")
     for riga in Curs:
      lumendata.extend(riga) 
    except mariadb.Error as e:
      print(f"Errore recupero dati dal DB: {e}")

    riga=0
    try:
     Curs.execute("SELECT IDDato FROM Dato GROUP BY IDDato")
     for riga in Curs:
      IDRecord.extend(riga) 
    except mariadb.Error as e:
      print(f"Errore recupero dati dal DB: {e}") 
      
    return tempdata,lumendata,IDRecord

def DeleteOneFromID(Curs,ConnToDB,ID): #delete one record from given id (textbox)
  try:     
   Curs.execute("SET FOREIGN_KEY_CHECKS = 0")
   Curs.execute("DELETE FROM Dato WHERE IDDato = ?",(ID,))
   Curs.execute("SET FOREIGN_KEY_CHECKS = 1")
  except mariadb.Error as e:
   print(f"Errore eliminazione dato dal DB: {e}")

  ConnToDB.commit() 

def DeleteOneFromTemp(Curs,ConnToDB,Temp): #delete one record from given temp (textbox)
  try:     
   Curs.execute("SET FOREIGN_KEY_CHECKS = 0")
   Curs.execute("DELETE FROM Dato WHERE Temperatura = ?",(Temp,))
   Curs.execute("SET FOREIGN_KEY_CHECKS = 1")
  except mariadb.Error as e:
   print(f"Errore eliminazione dato dal DB: {e}")

  ConnToDB.commit() 

def DeleteOneFromLumen(Curs,ConnToDB,Lumen): #delete one record from given Lumen (textbox)
  try:     
   Curs.execute("SET FOREIGN_KEY_CHECKS = 0")
   Curs.execute("DELETE FROM Dato WHERE LuminositàPerc = ?",(Lumen,))
   Curs.execute("SET FOREIGN_KEY_CHECKS = 1")
  except mariadb.Error as e:
   print(f"Errore eliminazione dato dal DB: {e}")

  ConnToDB.commit() 

def InjectInfoTempLumen(Curs,ConnToDB,temp,lumen): #inject info with temp lumen given
  try: 
   Curs.execute("INSERT INTO Dato (Temperatura,LuminositàPerc) VALUES (?, ?)", (temp,lumen)) 
  except mariadb.Error as e: 
   print(f"Errore inserimento: {e}")

  ConnToDB.commit()

def EditData(Curs,ConnToDB,id,temp,lumen): #edit data from given id, than edit values lumen and temp
  try: 
   Curs.execute("UPDATE Dato SET Temperatura = ?, LuminositàPerc = ? WHERE IDDato = ?", (temp,lumen,id)) 
  except mariadb.Error as e: 
   print(f"Errore modifica: {e}")

  ConnToDB.commit()

def AddSens(Curs,ConnToDB,SensProd): #add a new sensor
  try: 
   Curs.execute("INSERT INTO Sensore (Produttore) VALUES (?)", (SensProd,)) 
  except mariadb.Error as e: 
   print(f"Errore inserimento: {e}")

  ConnToDB.commit()

def RemoveSensID(Curs,ConnToDB,IDSens): #delete one record from given idsens (textbox)
  try:     
   Curs.execute("SET FOREIGN_KEY_CHECKS = 0")
   Curs.execute("DELETE FROM Sensore WHERE IDSensore = ?",(IDSens,))
   Curs.execute("SET FOREIGN_KEY_CHECKS = 1")
  except mariadb.Error as e:
   print(f"Errore eliminazione dato dal DB: {e}")

  ConnToDB.commit() 

def GetSensData(Curs): #get output data return lists tipo + produttore
    riga=0
    produttore=[]
    idsensore=[]

    #getting Produttore value
    riga=0
    try:
     Curs.execute("SELECT Produttore FROM Sensore GROUP BY IDSensore")
     for riga in Curs:
      produttore.extend(riga) 
    except mariadb.Error as e:
      print(f"Errore recupero dati dal DB: {e}")
      
    #getting IDSensore value
    riga=0
    try:
     Curs.execute("SELECT IDSensore FROM Sensore GROUP BY IDSensore")
     for riga in Curs:
      idsensore.extend(riga) 
    except mariadb.Error as e:
      print(f"Errore recupero dati dal DB: {e}") 
      
    return produttore,idsensore

def SendLogin(Curs,ConnToDB,adminID,sensorID):
  try:
   Curs.execute("CREATE OR REPLACE TRIGGER WhoAddData after insert ON dato FOR EACH ROW insert into gestione (IDDato,IDAmministratore)select (select IDDato from dato ORDER BY IDDato desc LIMIT 1),(SELECT IDAmministratore FROM amministratori WHERE IDAmministratore= ? LIMIT 1)",(adminID,))
   Curs.execute("CREATE OR REPLACE TRIGGER WhoAddedSensor after insert ON sensore FOR EACH ROW insert into installazione (IDSensore,IDAmministratore) select (select IDSensore  from sensore s  ORDER BY IDSensore desc LIMIT 1), (SELECT IDAmministratore FROM amministratori WHERE IDAmministratore= ? LIMIT 1)",(adminID,))
   Curs.execute("CREATE OR REPLACE TRIGGER WhatSensorAddedData after insert ON dato FOR EACH ROW insert into produzione (IDSensore,IDDato) select (select IDSensore from sensore WHERE IDSensore= ? ORDER BY IDSensore desc LIMIT 1), (SELECT IDDato FROM dato ORDER BY IDDato desc LIMIT 1)",(sensorID,))
  except mariadb.Error as e:
   print(f"Errore modifica trigger: {e}") 

  ConnToDB.commit() 

def CloseDB(Curs,ConnToDB): #close all connections
    Curs.close()
    ConnToDB.close()