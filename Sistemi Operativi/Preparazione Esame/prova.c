#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/types.h>
#include <string.h>

#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

#define PORT 8080
/*Port 8080 is a TCP/IP port that is used for administrative purposes.

It is used to manage web applications and servers.

Port 8080 can also be used to proxy traffic between two networks.*/

void sighandler(int sigocde){

printf("Segnale gestito");
}

int main(int agrc,char*argv[]){
    srand(getpid());
    int parentid=0,pid=0;
    char buffer[30];
    char bufferlettura[30];
    int pipa[2];
    struct sigaction act;
    sigset_t mainmask,zeromask;

    sigemptyset(&mainmask);
    sigemptyset(&zeromask);
    sigaddset(&mainmask,SIGUSR1);
    sigprocmask(SIG_UNBLOCK,&mainmask,NULL);

    act.sa_handler=sighandler;
    act.sa_flags=0;

    sigaction(SIGUSR1,&act,NULL);

    if(pipe(pipa)<0){printf("Errore creazione pipe"); exit(-3);}
///////////////////////
    int sock,length,codesock,rval,portno,msgsock;
    struct sockaddr_in server;

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

    msgsock= accept(sock,(struct sockaddr *)0,(int *)0);
    if(msgsock ==-1)
           perror("accept");

////////////////////////////////
    pid=fork();
    if(pid==0){ //figlio
    printf("Ciao sono figlio e scrivo in pipe e mando segnale");

    write(pipa[1],buffer,strlen(buffer)+1);
    close(pipa[1]);
    kill(getppid(),SIGUSR1);
    sigsuspend(&zeromask);

    }else{ //padre
    sigsuspend(&zeromask);
    printf("Segnale ricevuto,lettura...");
    int n=0;
    n = read(pipa[0],bufferlettura,strlen(bufferlettura)+1);

    if(n<0){printf("errore read");}
    close(pipa[0]);

    printf("Messaggio letto %s",bufferlettura);
    
    kill(pid,SIGKILL);
    printf("Figlio ucciso terminato");
    }
///////////////////////////////

pid=fork();
if(pid==0){
int f=0,codice=0;

char bufferinvio[30];
sprintf(bufferinvio,"inserire codice:");
write(msgsock,bufferinvio,strlen(bufferinvio)+1);

f=read(msgsock,bufferlettura,strlen(bufferlettura)+1);
if(f<0){printf("Errore lettura dalla socket");exit(-3);}

sscanf(bufferlettura,"%d",&codice);

if(codice==3){
    printf("client decide chiude");
    sprintf(msgsock,"client decide chiude");
    write(msgsock,bufferinvio,strlen(bufferinvio));
    close(codesock);
    return 2;
}else if(codice==1){
        printf("client decide return 1");
    close(codesock);
    return 1;
}

}else{//padre
printf("Faccio cose inutili");
char stringa1[20];
char stringa2[20];
sprintf(stringa1,"Hello peter");
sprintf(stringa2,"Hello mr.burns");
int compare=0;
compare=strcmp(stringa1,stringa2);

if(compare==0){
printf("stringhe uguali");
}else{
printf("stringhe diverse");
printf("Creo un random totalmente inutile");
int acaso=rand()%41 +60;    //tra 60 e 100
acaso=rand()%5 +1;      //tra 5 e 1          
}
}
    return 0;
}

//telnet localhost numeroporta(8080)