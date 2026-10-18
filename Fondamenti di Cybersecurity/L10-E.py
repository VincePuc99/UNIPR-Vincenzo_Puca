import socket,os

SERVER_IP_PORT = ("0.0.0.0",1000)

if __name__ == "__main__":
    srvsock=socket.socket(socket.AF_INET,socket.SOCK_STREAM)  #tipo socket (TCP qui)
    srvsock.bind(SERVER_IP_PORT) #doppia 
    srvsock.listen(10) #max 10 users, 11 scartrato

    processid = os.fork() #creo client  #tutte le variabili fino a questo punto verrano condivise, il resto sarà operato in sandbox

    if processid > 0 :  # processid > 0 represents the parent process (SERVER)
  
        conn,cliaddr = srvsock.accept() #client=oggettoconnessione "buffer" cliaddress=ip cliente
        print(f"Connessione da -> IP: {cliaddr[0]}\tPorta: {cliaddr[1]}") #accept da lasciare fuori dal while

        while(True): #comunicazione fork() 
            try:
                rawdata=conn.recv(1024) #buffer di ricezione bloccante
                if rawdata:
                    print(f"Ricevuto da CLIENT: {rawdata.decode()}")
                    rawdata="Pong!"
                    conn.sendall(rawdata.encode())
                else:
                    print("Fine Connessione")
                    break
            except Exception as e:
                    print("Error processing data")

    else: #CLIENT
        # Crea una socket TCP 
        client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
        client_socket.connect(SERVER_IP_PORT) 
        try: 
            # Invia dati al server 
            message = 'Ping!'
            client_socket.sendall(message.encode()) 
            # Ricevi la risposta dal server 
            response = client_socket.recv(1024) #bloccante
            print(f"Ricevuto da SERVER: {response.decode()}") 
        finally: 
            # Chiudi la socket 
            client_socket.close() 
            exit()

    conn.close()
    exit()