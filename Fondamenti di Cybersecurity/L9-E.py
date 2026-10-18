import dns.resolver
import socket
#L'obiettivo dell'esercizio sarà risolvere un nome di dominio per 
#ottenere diversi tipi di record DNS, come A (indirizzi IPv4), MX (Mail Exchange), e NS (Name 
#Server)

def GetDNS(google,gmail):
    print("###############")
    #A = ipv4
    try:
     answer=dns.resolver.resolve(google, 'A') #prima consulta la cache, poi il server dns configurato
     for ans in answer:
         print(f"IP address of {google} is {ans.address}")
    except dns.resolver.NoAnswer or dns.resolver.NXDOMAIN as e:
        print(f"Error resolving hostname {google}: {e}")

    print("###############")
    #AAAA = ipv6
    try:
     answer=dns.resolver.resolve(google, 'AAAA') 
     for ans in answer:
         print(f"IPV6 address of {google} is {ans.address}")
    except dns.resolver.NoAnswer or dns.resolver.NXDOMAIN as e:
        print(f"Error resolving hostname {google}: {e}")

    print("###############")
    try:
     count=0
     answer=dns.resolver.resolve(gmail, 'MX') #Specifica i server di posta elettronica responsabili della ricezione delle email per un dominio.
     for ans in answer:
         count+=1
         print(f"MX{count} records of {gmail} is:\nExchange -> {ans.exchange} Preference -> {ans.preference}")
    except dns.resolver.NoAnswer or dns.resolver.NXDOMAIN as e:
        print(f"Error resolving hostname {gmail}: {e}")

    print("###############")
    try:
     count=0
     answer=dns.resolver.resolve(gmail, 'NS') 
     for ans in answer:
         count+=1
         print(f"NS{count} records of {gmail} is: {ans.to_text()}")
    except dns.resolver.NoAnswer or dns.resolver.NXDOMAIN as e:
        print(f"Error resolving hostname {gmail}: {e}")

if __name__ == "__main__":

    GetDNS("www.google.com","gmail.com")

#Per i record A, stampiamo l'indirizzo IP (ans.address).
#Per i record MX, stampiamo l'exchange e la preferenza (record.exchange e record.preference).
#Per i record NS, stampiamo il nome del server (record.to_text()).


# A (Address) Record:
# 
# Mappa un nome di dominio a un indirizzo IPv4.
# Esempio: www.example.com -> 93.184.216.34
# 
# AAAA (IPv6 Address) Record:
# 
# Mappa un nome di dominio a un indirizzo IPv6.
# Esempio: www.example.com -> 2606:2800:220:1:248:1893:25c8:1946
# 
# CNAME (Canonical Name) Record:
# 
# Alias per un altro nome di dominio. Utilizzato per puntare un dominio a un altro dominio.
# Esempio: mail.example.com -> www.example.com
# 
# MX (Mail Exchange) Record:
# 
# Specifica i server di posta elettronica responsabili della ricezione delle email per un dominio.
# Esempio: example.com -> mail.example.com (priorità 10)
# 
# NS (Name Server) Record:
# 
# Specifica i server DNS autoritativi per un dominio.
# Esempio: example.com -> ns1.example.com, ns2.example.com
#
# PTR (Pointer) Record:
# 
# Utilizzato per la risoluzione inversa del DNS, mappa un indirizzo IP a un nome di dominio.
# Esempio: 93.184.216.34 -> www.example.com
# 
# SOA (Start of Authority) Record:
# 
# Contiene informazioni sull'origine della zona DNS, inclusi il server DNS primario, l'email dell'amministratore, il numero di serie della zona e i timer di aggiornamento.
# Esempio: example.com -> ns1.example.com admin.example.com 2020101001 7200 3600 1209600 3600
# 
# TXT (Text) Record:
# 
# Contiene informazioni di testo per varie finalità, come la verifica del dominio e le politiche di posta elettronica (SPF, DKIM).
# Esempio: example.com -> "v=spf1 include:_spf.example.com ~all"
# 
# SRV (Service) Record:
# 
# Specifica i servizi disponibili per un dominio, come i server di messaggistica istantanea o VoIP.
# Esempio: _sip._tcp.example.com -> 0 5 5060 sipserver.example.com
# 
# CAA (Certification Authority Authorization) Record:
# 
# Specifica quali autorità di certificazione sono autorizzate a emettere certificati per un dominio.
# Esempio: example.com -> 0 issue "letsencrypt.org"