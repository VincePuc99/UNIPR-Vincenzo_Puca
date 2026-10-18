from scapy.all import *

def CreaInvioPaccoRAW(dst_port, src_port, dest_IP, src_IP, message):
 ip= IP(src=src_IP,dst=dest_IP)
 tcp= TCP(sport=src_port,dport=dst_port)
 payload= Raw(load=message.encode("utf-8"))
 pkt=ip / tcp / payload
 try:
    return send(pkt)
 except Exception as e:
    print("ErrorSend: ",e)

def PingEntireLAN(src_LAN):

 ip=IP(src=Net(src_LAN))
 try:
  return send(ip/ICMP(id=100))
 except Exception as e:
  print("Eccezione: ", e)
 
if __name__ == "__main__":
 message="PAYLOAD"
 tcp=TCP(sport=1234,dport=8080,flags='S',seq=1000)

 print("Risposta send: ", CreaInvioPaccoRAW(8000,1234,"192.168.1.189","192.168.1.200",message))

 print("Risposta sendPing: ",PingEntireLAN("192.168.1.0/24"))