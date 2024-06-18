#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>

#define PORT 8080

void sighandler(int sigcode){

}

int main (int argc,char*argv[])
{

int sock,length,msgsock,rval,portno;
struct sockaddr_in server;
int pipa[2];

struct sigaction act;
sigset_t zeromask,mainmask;

sigemptyset(&mainmask);
sigemptyset(&zeromask);
sigaddset(&mainmask,SIGUSR1);
sigaddset(&mainmask,SIGUSR2);
sigprocmask(SIG_UNBLOCK,&mainmask,NULL);

act.sa_handler=sighandler;
act.sa_flags=0;

sigaction(SIGUSR1,&act,NULL);
sigaction(SIGUSR2,&act,NULL);

sock=socket(AF_INET,SOCK_STREAM,0); 
if(sock<0)
{perror("opening stream socket"); exit(2); }
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
listen(sock,2);

pid=fork();

if(pid==0){
   msgsock= accept(sock,(struct sockaddr *)0,(int *)0);
   if(msgsock ==-1)
   perror("accept");

   sprintf(bufferinvio,"inserisci");

   read(msgsock,bufferarrivo,strlen(bufferarrivo)+1);
   write(msgsock,bufferinvio,strlent(bufferinvio)+1);

   write(pipa[1],bufferarrivo,strlen(bufferarrivo)+1)

   sscanf(bufferarrivo,"%d",&vardest)
   sigsuspend(&zeromask)

   kill(getppid(),SIGKILL);

   close(msgsock);

}else{
   sigsuspend(&zeromask)

}

}
