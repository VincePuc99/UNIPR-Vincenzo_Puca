import subprocess

print("ES1")
# Esegui il ping e cattura l'output
result = subprocess.run(["ping", "-c", "2", "1.1.1.1"], capture_output=True, text=True)  #DNS Cloudflare
line_count = 0

for line in result.stdout.split("\n"):  #result.sdout è una stringa perchè usare solo result si andrebbe 
                #usare un CompletedProcess
    if line_count == 1:
        if "Destination Host Unreachable" in line:
            print("Connessione Fallita")
        else:
            print("Connessione IP Esterno riuscita")
    line_count += 1

print("####################################################")

print("ES2")

result = subprocess.run("ifconfig", capture_output=True, text=True)
interfaces = [
        line.split(":")[0]  #prendo solo il nome dell'interfaccia
        for line in result.stdout.split("\n") 
        if line and not line.startswith(" ")    #se la linea non è vuota e non inizia con uno spazio
    ]

# Stampa le interfacce di rete
print("Interfacce di rete:")
for interface in interfaces:
    print(interface)

print("####################################################")

print("ES3")

import nmap

scanner = nmap.PortScanner()
target = "192.168.1.189"

print("Scansione in corso...")
scanner.scan(target)    #salva in scanner il risultato della scansione

for host in scanner.all_hosts():
    print("Host: ", host)   #prendi indirizzo ip dall'host
    print("State: ", scanner[host].state()) #stato attività dell'host (up o down)

    for proto in scanner[host].all_protocols(): #protocolli attivi
        print("Protocollo: ", proto)  #tcp o udp
        ports = scanner[host][proto].keys() #porte,lista di porte
        print("Porte aperte:")
        for port in ports:
            if scanner[host][proto][port]['state'] == "open":
             print(port)

# se scanner[host][proto] :
# {
#     22: {'state': 'open'},
#     80: {'state': 'closed'}
# }
# ports sarà [22, 80]

print("####################################################")

print("ES4")

# Funzione per ottenere l'indirizzo MAC di un'interfaccia
def get_mac_address(interface):
    result = subprocess.run(["ip", "link", "show", interface], capture_output=True, text=True)
    for line in result.stdout.split("\n"):
        if "link/ether" in line:
            return line.split()[1]
    return None

# Ottieni l'elenco delle interfacce di rete
result = subprocess.run("ifconfig", capture_output=True, text=True)
interfaces = [
    line.split(":")[0]  # Prendo solo il nome dell'interfaccia
    for line in result.stdout.split("\n") 
    if line and not line.startswith(" ")  # Se la linea non è vuota e non inizia con uno spazio
]

# Stampa le interfacce di rete e i loro indirizzi MAC
for interface in interfaces:
    if interface != "lo":   #filtro il loopback
     mac_address = get_mac_address(interface)
     print(f"Interfaccia: {interface}, MAC: {mac_address}")

print("####################################################")

print("ES5")

macaddr = "00:00:00:00:00:00"
valid_chars = "0123456789abcdefABCDEF"
cont__char = 0
valid = True

for char in macaddr:
    if char != ':':
        cont__char += 1
        if char not in valid_chars or cont__char == 3:
            print("MAC Address non valido")
            valid=False
            break
    else:
        cont__char = 0

if valid:
    print("MAC Address valido")

print("####################################################")