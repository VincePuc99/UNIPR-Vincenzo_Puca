#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

#define PORT 8080

void signhandler(int sigcode){

}

int main (int argc,char*argv[]){

    struct sigaction act;
    sigset_t zeromask,mainmask;
    char pipagenitore[2];
    char pipafiglio[2];

    int sock,length,msgsock,rval,portno;
    struct sockaddr_in server;

    sigemptyset(&zeromask);
    sigemptyset(&mainmask);
    sigaddset(&mainmask,SIGUSR1);
    sigaddset(&mainmask,SIGUSR2);
    sigprocmask(SIG_UNBLOCK,&mainmask,NULL);

    act.sa_handler=signhandler;
    act.sa_flags=0;

    sigaction(SIGUSR1,&act,NULL);
    sigaction(SIGUSR1,&act,NULL);

    pipe(pipafiglio);
    pipe(pipagenitore);

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
    listen(sock,2); 

    msgsock=accept(sock,(struct sockaddr *)0,(int *)0);
    if(msgsock ==-1)
    perror("accept");

    char buffer[20];
    sprintf(buffer,"ciao utente");
    write(msgsock,buffer,strlen(buffer));
    write(pipa[1],buffer,strlen(buffer));

    read(msgsock,buffer,strlen(buffer));
    read(pipa[0],buffer,strlen(buffer));
    sscanf(buffer,"%d %s",&codice,stringa);

    buffer[0] = '\0';

    close(msgsock);

    sigsuspend(&mainmask);

    kill(getppid(),SIGUSR1);
    int pid,provanumero;
    pid=fork();

    if(pid==0){
        close(pipagenitore[1]);
        close(pipafiglio[0]);

        read(pipagenitore[0],buffer,strlen(buffer));
        write(pipafiglio[1],buffer,strlen(buffer));

        provanumero=rand()% 50 + 1; //1 50
        provanumero=rand()% 50 + 40; //40 90



    }else{ //padre
        close(pipagenitore[0]);
        close(pipafiglio[1]);

    }
 return 0;
}