import random
def generate_mac_address():
    oui="00:16:3E"
    mac_tail=':'.join('%02x'%random.randint(0,255) for _ in range(3))

    mac_addr= oui + ':' + mac_tail
    return mac_addr

print(generate_mac_address())