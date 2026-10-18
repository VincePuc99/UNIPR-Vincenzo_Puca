#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define PORT 1000

void signalhandler(int codesign){

printf("Segnale ricevuto N°: %d",codesign);
}

int main(int argc,char* argv[]){
  int pipa1[2];
  pipe(pipa1);

    if(pipe(pipa1)==-1){
      perror("no pipa");
      exit(-1);
    }

char frase[]= "abubigcolossale";
char bufferlettura[100];

write(pipa1[1],frase,strlen(frase)+1);
close(pipa1[1]);

read(pipa1[0],bufferlettura,100);
close(pipa1[0]);

printf("%s",bufferlettura);

struct sigaction act;
sigset_t mainmask,zeromask;

sigemptyset(&mainmask);
sigemptyset(&zeromask);
sigaddset(&mainmask,SIGUSR1);

sigprocmask(SIG_BLOCK,&mainmask,NULL);

act.sa_handler=signalhandler;
act.sa_flags=0;

sigaction(SIGUSR1,&act,NULL);

int server_fd, new_socket, valread;
struct sockaddr_in address;
int opt = 1;
int addrlen = sizeof(address);
char buffer[1024];
 
    // Creazione socket
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    // aggancio porta 1000
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, &opt,  sizeof(opt))) {
        perror("setsockopt");
        exit(EXIT_FAILURE);
    }

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);
 
    // aggancio porta 8080
    if (bind(server_fd, (struct sockaddr*)&address, sizeof(address)) < 0) {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }
    if (listen(server_fd, 3) < 0) {
        perror("listen");
        exit(EXIT_FAILURE);
    }
    if ((new_socket  = accept(server_fd, (struct sockaddr*)&address, (socklen_t*)&addrlen)) < 0) {
        perror("accept");
        exit(EXIT_FAILURE);
    }
    valread = read(new_socket, buffer, 1024);
    printf("%s\n", buffer);
   
  // closing the connected socket
    close(new_socket);
  // closing the listening socket
    shutdown(server_fd, SHUT_RDWR);

    return 0;
}