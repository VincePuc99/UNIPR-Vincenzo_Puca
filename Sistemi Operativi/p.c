#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <string.h>
#include <stdbool.h>

#define PORT 8180

void signhandler(int sigcode){
printf("segnale gestito");
}

int main(int argc,char* argv[]){

	int sock,length,msgsock,rval,portno;
	int msgsock1;
	struct sockaddr_in server;
	sigset_t zeromask;
	sigemptyset(&zeromask);
	int pipefiglio1[256];
	int pipefiglio2[256];

	char *benvenutomsg1="Benvenuto cliente 1 scrivi qui: ";
	char *benvenutomsg2="Benvenuto cliente 2: ";
	int mypid;
	struct sigaction act;

	act.sa_handler=signhandler;
    sigemptyset(&act.sa_mask);

	act.sa_flags=SA_RESTART;
	sigaction(SIGUSR1,&act,NULL);

	sock= socket(AF_INET,SOCK_STREAM,0); 
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
	listen(sock,3);

	msgsock=accept(sock,(struct sockaddr *)0,(int *)0);

    if(fork()==0){
	sigsuspend(&zeromask);
	char arrivo[256];
	write(msgsock,benvenutomsg1,strlen(benvenutomsg1)+1);
	read(msgsock,arrivo,strlen(arrivo)+1);
	write(pipefiglio2[1],arrivo,strlen(arrivo)+1);
	kill(mypid,SIGUSR1);

    }
    else{
    if(fork()==0){
	mypid=getpid();
	kill(getppid(),SIGUSR1);

	printf("%d",mypid);
	sigsuspend(&zeromask);
	char arrivo[256];
	read(pipefiglio2[0],arrivo,strlen(arrivo)+1);
	printf("%s",arrivo);
}

	} //padre
	
    return 0;
}
