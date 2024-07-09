#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

#define PORT 8080

void sighandler(int sigcode){}

int main(int argc,char*argv[]){

int sock,length,msgsock,rval,portno;
struct sockaddr_in server;

int pid=0;
int pipa[2];

struct sigaction act;
sigset_t zeromask,mainmask;

sigemptyset(&mainmask);
sigemptyset(&zeromask);
sigaddset(&mainmask,SIGUSR1)
sigprocmask(SIG_UNBLOCK,&mainmask,NULL)

act.sa_handler=sighandler;
act.sa_flags=0;

sigaction(SIGUSR1,&act,NULL)

int(pipe(pipa)<0){printf("Errore creazione pipe"); exit(-3);}

sock= socket(AF_INET,SOCK_STREAM,0); /* Crea la socket STREAM */

if(sock<0)
{ perror("opening stream socket");
exit(2); }

portno = PORT;

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


//////////////////////////////////////////////////////
char bufferinvio[30];
char bufferarrivo[30];

pid=fork()
if(pid<0){
perror("Errore fork"); exit(3); }

if(pid==0){
msgsock=accept(sock,(struct sockaddr *)0,(int *)0);
if(msgsock ==-1) perror("accept");

printf("Ciao sono figlio e scrivo in pipe e mando segnale");
int n = write(pipa[1],bufferinvio,strlen(bufferinvio)+1)
if(n<0){printf("errore scrittura");}
kill(getppid(),SIGUSR1);

sigsuspend(&zeromask); //attesa uccisione
}

printf("padre attesa segnale");
sigsuspend(&zeromask)
printf("segnale ricevuto,leggo pipa")
int a=read(pipa[0],bufferarrivo,strlen(bufferarrivo)+1)
if(a<0){
    printf("errore lettura");exit(-3); }
kill(pid,SIGKILL); //figlio ucciso

//////////////////////////////////////////////////////

//doppio figlio
int codice;

pid=fork()
if(pid<0){ //da fare anche nella seconda fork
perror("Errore fork"); exit(3); }

if(pid==0)//figlio 1
{
    pidsec=fork()
    if(pidsec==0)
    {
        msgsock=accept(sock,(struct sockaddr *)0,(int *)0);
        if(msgsock ==-1) perror("accept");

        sprintf(bufferinvio,"Inserire iput");
        write(msgsock,bufferinvio,strlen(bufferinvio)+1) //mando richiesta input

        read(msgsock,bufferarrivo,strlen(bufferarrivo)+1)
        sscanf(bufferarrivo,"%d",&codice)

        if(codice ==0){
            //3+3
        }
        else if(codice==1){
            //55-6
        }
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

exit (0);
}



