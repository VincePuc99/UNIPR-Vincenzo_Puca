#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>

#define PORT 8088

int q=0;

void handlersegnale(int sigcode){

printf("Segnale Ricevuto: %d",sigcode);
if(sigcode==30){
    q=q+1;
}
if(sigcode==31){
    q=q-1;
}
}//fine handler

int main(int argc,char* argv[]){
/*Sezione variabili*/

char buffer[256];
char bufferuscita[256];
int m=1;//valore iniziale
int mread;

/*Variabili socket*/
struct sockaddr_in server,client;
int sock,length,codesock,rval,portno;
/*Variabili socket fine*/

/*Fine sezione variabili*/

/*Sezione segnali*/
struct sigaction act;
sigset_t mainmask,zeromask;

sigemptyset(&mainmask);
sigemptyset(&zeromask);
sigaddset(&mainmask,SIGUSR2);
sigaddset(&mainmask,SIGUSR1);

sigprocmask(SIG_BLOCK,&mainmask,NULL);

sigaction(SIGUSR1,&act,NULL);
sigaction(SIGUSR2,&act,NULL);
/*Fine segnali*/

/*Socket*/

portno = PORT;

sock=socket(AF_INET,SOCK_STREAM,0);
if(sock<0)
	{ perror("opening stream socket"); exit(2); }

server.sin_family = AF_INET;
server.sin_addr.s_addr= INADDR_ANY;
server.sin_port = htons(portno);

if (bind(sock,(struct sockaddr *)&server,sizeof(server))<0)
	{ perror("binding stream socket"); exit(3); }

length=sizeof(server);

if(getsockname(sock,(struct sockaddr *)&server,&length)<0)
	{ perror("getting socket name"); exit(4); }

printf("Socket port #%d\n\n",ntohs(server.sin_port));
listen(sock,2); //pronto

/*Fine socket*/

if(argc==2){
    q=atoi(argv[1]);

    if(q<0 || q>100){
        printf("Argomento non corretto");
        exit(2);
}
}else{
    printf("Troppi argomenti");
    exit(2);
}

    codesock = accept(sock, (struct sockaddr *)&client,
                     (socklen_t *)&length); // attende un client
 	if(codesock ==-1){
 	perror("accept"); exit(0); }

do{

if(fork()==0){
 	int n=0;
 	n = read(codesock,buffer,255);
	if (n < 0) { perror("Errore read"); exit(5); }

    sscanf(buffer, "%d", &mread);

    int letturaoriginale=mread;
    int risposta;

    if(mread%100==0){
        mread=mread/100;
        m=mread;
    }
        
    risposta=m*letturaoriginale+q;

    sprintf(bufferuscita, "Il risultato e' %d (M=%d   num=%d   q=%d)\n", risposta, m,letturaoriginale, q);
 
    if (write(codesock, bufferuscita, strlen(bufferuscita) + 1) < 0) {
        perror("Errore write");
        exit(10);}

    close(codesock);
    exit(10);

    }else{
        close(codesock);
    }

}while (1);

return 0;
}
