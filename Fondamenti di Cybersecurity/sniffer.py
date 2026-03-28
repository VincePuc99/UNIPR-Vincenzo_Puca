from scapy.all import *
import re, uuid

import requests,subprocess

def packet_callback(packet):
    if packet.haslayer(IP):
        print(f"IP Packet: {packet[IP].src} -> {packet[IP].dst}")
    elif packet.haslayer(ARP):
        print(f"ARP Packet: {packet[ARP].psrc} -> {packet[ARP].pdst}")

def sniff_traffic():
    sniff(prn=packet_callback, store=0)

if __name__ == "__main__":

    # Start sniffing traffic
    sniff_traffic()