#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>

void sighandler (int sigcode){
/* 30 SIGUSR1 31 SIGUSR2 */
printf("Ricevuto %d \n",sigcode);
}


int main(int argc,char* argv[])
{
int pid=0,pidfiglio2=0, ppid=0;;
srand(getpid());
ppid=getpid();

/*segnali da imparare a memoria da scrivere nell'ordine in cui sono*/
struct sigaction act;
sigset_t mainmask,zeromask;

sigemptyset(&mainmask);
sigemptyset(&zeromask);
sigaddset(&mainmask,SIGUSR1);
sigaddset(&mainmask,SIGUSR2);

sigprocmask(SIG_BLOCK,&mainmask,NULL);

act.sa_handler=sighandler;
act.sa_flags=0;

sigaction(SIGUSR1,&act,NULL);
sigaction(SIGUSR2,&act,NULL);
/*fine segnali*/

/*fork*/

pid=fork();
if(pid<0){printf("fork non riuscita");return -2;}

if(pid==0){//figlio1

kill(getppid(),SIGUSR1);
printf("Segnale mandato\n");

pid=getpid();
printf("Io sono filgio 1: %d\n",pid);
printf("Mio padre e: %d\n",ppid);

}else{//padre

sigsuspend(&zeromask);
printf("Segnale ricevuto\n");
printf("Io sono padre %d\n",ppid);

pidfiglio2=fork();

if(pidfiglio2<0){printf("Fork figlio 2 non riuscita\n");return -2;}

if(pidfiglio2==0){ //figlio2

pidfiglio2=getpid();

printf("Io sono filgio 2: %d\n",pidfiglio2);
printf("Mio padre e: %d\n",ppid);

}//else figlio2

}//else padre
return 0;
}