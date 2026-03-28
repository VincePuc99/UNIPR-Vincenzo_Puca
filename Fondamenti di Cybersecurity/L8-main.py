import sys
sys.path.append('../Cyber')

from scapy.all import IP, ICMP, send, sendp, Ether, ARP, srp

#send()
#Upper layer , nessuna manipolazione di pacchetti header ethernet e il routing ip è sufficente

#sendp lower layer, controllo accurato su frame dei dati

packet=IP(dst="192.168.1.189")/ICMP()
packetether=Ether()/IP(dst="192.168.1.189")/ICMP()
try:
    send(packet)    #sendp(packet,count=4,inter=1) mando 4 pacchi ogni secondo. lo scanner allerta 
    sendp(packetether) #manda a livello data link
except Exception as e:
    print("Error: ", e)


arp_request=ARP(pdst="192.168.1.1/24")  #solitamente arp viene mandato nel frame ethernet per indicare mac di broadcast FF:FF:FF:FF:FF:FF
#senza specificare ethernet scapy gestisce da solo

ethernet_frame= Ether(dst="ff:ff:ff:ff:ff:ff")
arp_request_pacco=ARP(pdst="192.168.1.189") #cerco mac di questo device

try:
    sendp(arp_request_pacco,iface="eth0") #interfaccia di rete da specificare
except Exception as e:
    print("Error: ", e)

#gestione di risposta 

#srp() / sendp()         #send and receive packets a livello di data link (iso osi 2) controllo mirato del traffico 
#sr() / send()           #send and receive a livello di rete (iso osi 3)        

#invia pacchetti e aspetta risposta, ricevo due liste, la prima con copie di richieste e risposte
#la seconda con pacchetti che non hanno ricevuto risposta
#srp() pkt timeout iface e verbose

arp_request_pacco2= Ether(dst="ff:ff:ff:ff:ff:ff")/ARP(pdst="192.168.1.189")

answered, unans = srp(arp_request_pacco2,timeout=2,iface="eth0",verbose=False)

for sent, recevied in answered:
    print(recevied.summary())


##########################################

#script di scansione
#alcuni device non rispondono per evitare arp poisoning (iOS,MacOS)
#sono vulnerabili solo durante la connessione di rete inziale

if __name__ == "__main__":
    broadcast="FF:FF:FF:FF:FF:FF"
    ether_layer= Ether(dst=broadcast)
    ip_range="192.168.1.1/24"
    arp_layer= ARP(pdst=ip_range)

    packetscan=ether_layer/arp_layer
    ans, unansp= srp(packetscan,timeout=2,iface="eth0")
    for snd,rcv in ans:
       ip=rcv[ARP].psrc #cos'è psrc?    ip search
       mac=rcv[Ether].src
       print("IP: ", ip, "MAC: ", mac)