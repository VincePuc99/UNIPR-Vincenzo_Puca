import socket
from dns.resolver import dns
import dns.resolver


def resolver_hostname(hostname):
    try:
        ip = socket.gethostbyname(hostname)
        print(f"IP address of {hostname} is {ip}")
    except socket.gaierror as e:
        print(f"Error resolving hostname {hostname}: {e}")

if __name__ == "__main__":
    hostname = "www.google.com"
    resolver_hostname(hostname)
    try:
        #ritorna oggetto answer che contiene gli indirizzi ipv4 associati al nome di dominio
     answer= dns.resolver.resolve(hostname, 'A') #prima consulta la cache, poi il server dns configurato
     for ans in answer:
         print(f"IP address (dns.resolver) of {hostname} is {ans.address}")
    except dns.resolver.NoAnswer or dns.resolver.NXDOMAIN as e:
        print(f"Error resolving hostname {hostname}: {e}")