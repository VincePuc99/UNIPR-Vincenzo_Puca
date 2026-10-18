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

void handlersignal(int sigcode){
printf("Segnale ricevuto Numero: %d",sigcode);
}

int main(int argc,char* argv[]){

/*Variabili*/

int pid=0,clilen=0;
int arpipa[2];
char buffer[30];
char bufferlettura[30];
int sock,length,codesock,rval,portno;
struct sockaddr_in server;

/*end variabili*/

/*segnali*/
struct sigaction act;
sigset_t mainmask,zeromask;

sigemptyset(&mainmask);
sigemptyset(&zeromask);
sigaddset(&mainmask,SIGUSR1);

sigprocmask(SIG_BLOCK,&mainmask,NULL);

act.sa_handler=handlersignal;
act.sa_flags=0;

sigaction(SIGUSR1,&act,NULL);

/*end segnali*/

/*socketta*/
portno = PORT;

sock=socket(AF_INET,SOCK_STREAM,0);
if(sock<0)
	{ perror("opening stream socket"); exit(2); }

server.sin_family = AF_INET;
server.sin_addr.s_addr= INADDR_ANY;
server.sin_port = htons(portno);

if (bind(sock,(struct sockaddr *)&server,sizeof(server))<0)
	{ perror("binding stream socket"); exit(3); }

length= sizeof(server);

if(getsockname(sock,(struct sockaddr *)&server,&length)<0)
	{ perror("getting socket name"); exit(4); }


printf("Socket port #%d\n\n",ntohs(server.sin_port));
listen(sock,2); //pronto

/*end socketta*/

// do {
// 	codesock=accept(sock,(struct sockaddr *)0,(int *)0);

// 	if(codesock ==-1){
// 	perror("accept"); }
// 	else	//connessione riuscita
// 	{ 
// 	int n=0;
// 	n = read(codesock,buffer,255);
// 	if (n < 0) { perror("Errore read"); exit(5); }

// 	printf("Ecco il messaggio ricevuto dal client: %s\n",buffer);

// 	if(pipe(arpipa)<0){printf("errore pipe"); exit(-3);}

// 	write(arpipa[1],buffer,strlen(buffer)+1);
// 	close(arpipa[1]);

// 	read(arpipa[0],bufferlettura,30);
// 	close(arpipa[0]);

// 	printf("Prova pipa: %s",bufferlettura);

// 	for(int i=0;i<strlen(buffer);i++){
// 	buffer[i]=' ';}
// 	}

// 	close (codesock);
// } while(1);

pid=fork();
if(pid<0){perror("Errore fork"); exit(3);}
if(pid==0){ //figlio
printf("Mando messaggio\n");
kill(getppid(),SIGUSR1);
}else{ //padre
sigsuspend(&zeromask);
printf("Messaggio ricevuto");
}
exit (0);
}
