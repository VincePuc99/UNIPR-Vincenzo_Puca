import sys
sys.path.append('../Cyber')

from scapy.all import sniff,TCP,IP
from collections import defaultdict
import time

syn_cont=defaultdict(int)
RESET_INTERVAL=10
SYN_THRESHOLD=100 #100 richieste da un singolo ip -> azione sospetta

def reset_cont():
    global syn_cont
    syn_cont=defaultdict(int)
    print("Cont reset done")

def syn_flood(pkt):
    if pkt.haslayer(TCP) and pkt[TCP].flags=='S':
        src_ip=pkt[IP].src
        syn_cont[src_ip]+=1

    if syn_cont[src_ip] > SYN_THRESHOLD:
        print(f"Possibile SYN flood from {src_ip}")

if __name__ == "__main__":
    reset_timer=time.time()+RESET_INTERVAL

    while True:
        sniff(filter="tcp",prn=syn_flood,store=0,count=10)

        if time.time() > reset_timer:
            reset_cont()
            reset_timer=time.time()+RESET_INTERVAL

