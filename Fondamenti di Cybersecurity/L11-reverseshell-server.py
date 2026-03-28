import socket
SERVER_HOST='0.0.0.0'
SERVER_PORT=5003

server_socket=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
server_socket.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1)#riusa ip e porta in caso di riavvio del server

server_socket.bind((SERVER_HOST,SERVER_PORT))
server_socket.listen(1) #metto in ascolto per una sola connessione alla volta

print(f"Ascolto su {SERVER_HOST}:{SERVER_PORT}")
client_socket,client_address=server_socket.accept()

print(f"{client_address[0]}:{client_address[1]} Connesso!")

while True:
    command=input("Comando da eseguire: ")
    client_socket.send(command.encode())

    if command.lower() =="exit":
        break

    results=client_socket.recv(1024).decode()

    print(results)

    client_socket.close()
    server_socket.close()