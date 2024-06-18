#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>

#define PORT 8081

void sighandler(int sigcode){


}

int main (int argc,char* argv[]){


int sock,length,msgsock,rval,portno;
struct sockaddr_in server;
char *hellomsg="Benvenuto manda un numero: \n";
int codicearrivo=0;

int pipeclient[2],pid;

struct sigaction act;
sigset_t zeromask,mainmask;

sigemptyset(&zeromask);
sigemptyset(&mainmask);
sigaddset(&mainmask,SIGUSR1);
sigaddset(&mainmask,SIGUSR2);

act.sa_handler=sighandler;
act.sa_flags=0;
sigaction(SIGUSR1,&act,NULL);
sigaction(SIGUSR2,&act,NULL);


pipe(pipeclient);

sock= socket(AF_INET,SOCK_STREAM,0); /* Crea la socket STREAM */
if(sock<0)
{
perror("opening stream socket");
exit(2);
}
portno = PORT;
server.sin_family = AF_INET;
server.sin_addr.s_addr= INADDR_ANY;
server.sin_port = htons(portno);
if (bind(sock,(struct sockaddr *)&server,sizeof(server))<0)
{
perror("binding stream socket");
exit(3);
}
length= sizeof(server);
if(getsockname(sock,(struct sockaddr *)&server,&length)<0)
{
perror("getting socket name");
exit(4);
}
printf("Socket port #%d\n\n",ntohs(server.sin_port));
listen(sock,2); /* Pronto ad accettare connessioni */


for(int i=0;i<2;i++){

msgsock=accept(sock,(struct sockaddr *)0,(int *)0);
if(msgsock ==-1)
perror("accept");

pid=fork();
if(pid==0){//figlio
close(sock);


close(pipeclient[0]);
char buffarrivo[20];


write(msgsock,hellomsg,strlen(hellomsg)+1);

read(msgsock,buffarrivo,strlen(buffarrivo)+1);
write(pipeclient[1],buffarrivo,strlen(buffarrivo)+1);

kill(getppid(),SIGUSR1);
memset(buffarrivo,0,







20);

char buffarrivo[20];


printf("sono padre");
sigsuspend(&zeromask);
read(pipeclient[0],buffarrivo,strlen(buffarrivo)+1);
sscanf(buffarrivo,"%d",&codicearrivo);
printf("Codice arrivato: %d",codicearrivo);
char code[20];
sprintf(code,"codice appena mandato %d",codicearrivo);
write(msgsock,code,strlen(code)+1);
memset(buffarrivo,0,20);
memset(code,0,1);
write(pipeclient[1],buffarrivo,strlen(buffarrivo)+1);}



}else{//padre server




}
close(msgsock);
return 0;}
