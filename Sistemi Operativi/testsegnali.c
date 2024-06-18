#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>

void sighandler (int sigcode){

printf("Handler Gestito");

}

int main(int argc,char* argv[])
{
int ppid=0,pid=0;
srand(getpid());

struct sigaction act;
sigset_t mainmask,zeromask;

/*segnali*/
sigemptyset(&mainmask);
sigemptyset(&zeromask);
sigaddset(&mainmask,SIGUSR1);

sigprocmask(SIG_BLOCK,&mainmask,NULL);

act.sa_handler=sighandler;
act.sa_flags=0;

sigaction(SIGUSR1,&act,NULL);
/**/

pid=fork();
if(pid<0){printf("fork non riuscita");return -2;}

if(pid==0){//figlio

kill(getppid(),SIGUSR1);
printf("Segnale mandato");
sigsuspend(&zeromask);

}else{//padre

sigsuspend(&zeromask);
printf("segnale ricevuto");
kill(pid,SIGKILL);

}
return 0;
}
