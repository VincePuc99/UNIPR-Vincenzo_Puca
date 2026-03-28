import socket

def srvr():
    serversocket= socket.socket(socket.AF_INET,socket.SOCK_STREAM)  #tipo socket (TCP qui)
    srvr_addr=('localhost',12900)   #specifico ip e porta

    print(f"Waiting on {srvr_addr[0]} port {srvr_addr[1]}")  

    serversocket.bind(srvr_addr)    #bind della sock all ip
    serversocket.listen(10) #mette nella coda di attesa max 10 connessione 11esima scartata

    while True:
        print("Waiting connection")
        connect,cli_addr =serversocket.accept() #blocca il programma fino a nuova connessione, client -> accept ritorna oggetto socket connection a quel client
        try:
            print(f"Connected from {cli_addr}")
            while True:
                data=connect.recv(16) #buffer lettura, 16 byte max da leggere
                print(f"Data received: {data.decode()}")
                if data:
                    print("Invio dati al client")
                    connect.sendall(data)
                else:
                    print("Empty data")
                    break

        finally:
            connect.close()

if __name__ == "__main__":
    srvr()