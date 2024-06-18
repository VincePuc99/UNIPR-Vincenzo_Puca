#include <netdb.h>
#include <netinet/in.h>
#include <signal.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/timeb.h>
#include <sys/types.h>

#include <errno.h>

#include <stdlib.h>
#include <unistd.h>

#define DEFAULTPORT 1515

#define SUM 0
#define PRODUCT 1

#define TRUE (1 == 1)
#define FALSE (1 == 0)

int log_is_active = FALSE;

int pid_server;
int pid1, pid2;

void sigusr_handler(int signo) {
    /*
  if (getpid() == pid_server) {
    if (pid1 > 0)
      kill(pid1, signo);
    if (pid2 > 0)
      kill(pid2, signo);
  }
*/
  if (signo == SIGUSR1) {
    log_is_active = TRUE;
    printf("Log is now active\n");
  } else if (signo == SIGUSR2) {
    log_is_active = FALSE;
    printf("Log is now inactive\n");
  }
}

int main(int argc, char *argv[]) {
  int length;
  struct sockaddr_in server1, server2, client;
  fd_set sock_fdset;
  int sock1, sock2;
  int msgsock1, msgsock2, rval, i;
  struct sigaction sig;
  char line[256], answer[256];
  int op1, op2;
  int somma, prodotto;

  sig.sa_handler = sigusr_handler;
  sigemptyset(&sig.sa_mask);
  sig.sa_flags = SA_RESTART;

  sigaction(SIGUSR1, &sig, NULL);
  sigaction(SIGUSR2, &sig, NULL);

  /* Vanno utilizzate due socket ognuna con il proprio indirizzo quindi con
   * porte diverse */

  pid_server = getpid();
  printf("Server PID=%d\n", pid_server);

  /* Crea la  socket STREAM 1 e STREAM 2*/
  sock1 = socket(AF_INET, SOCK_STREAM, 0);
  if (sock1 < 0) {
    perror("opening stream socket 1");
    exit(1);
  }

  sock2 = socket(AF_INET, SOCK_STREAM, 0);
  if (sock2 < 0) {
    perror("opening stream socket 2");
    exit(1);
  }

  /* Utilizzo della wildcard per accettare le connessioni da ogni client */
  server1.sin_family = AF_INET;
  server1.sin_addr.s_addr = INADDR_ANY;
  server1.sin_port = htons(1520);

  int on=1;
  if (setsockopt(sock1,SOL_SOCKET, SO_REUSEADDR,&on,sizeof(on))<0)
      perror("setsockopt reuseaddr sock1");


  if (bind(sock1, (struct sockaddr *)&server1, sizeof server1) < 0) {
    perror("binding stream socket");
    exit(1);
  }
  server2.sin_family = AF_INET;
  server2.sin_addr.s_addr = INADDR_ANY;
  server2.sin_port = htons(1521);
  

 
 if (setsockopt(sock2,SOL_SOCKET, SO_REUSEADDR,&on,sizeof(on))<0)
      perror("setsockopt reuseaddr sock1");
  
  
  if (bind(sock2, (struct sockaddr *)&server2, sizeof server2) < 0) {
    perror("binding stream socket");
    exit(1);
  }

  length = sizeof server1;
  if (getsockname(sock1, (struct sockaddr *)&server1, &length) < 0) {
    perror("getting socket name");
    exit(1);
  }
  printf("Socket 1 port #%d\n", ntohs(server1.sin_port));
  length = sizeof server2;
  if (getsockname(sock2, (struct sockaddr *)&server2, &length) < 0) {
    perror("getting socket name");
    exit(1);
  }
  printf("Socket 2 port #%d\n", ntohs(server2.sin_port));

  listen(sock1, 4);

  listen(sock2, 4);

  do {
    FD_ZERO(&sock_fdset);
    FD_SET(sock1, &sock_fdset);
    FD_SET(sock2, &sock_fdset);

    int n;
    do {
        n= select(sock2 + 1, &sock_fdset, NULL, NULL, NULL);
    }
    while(n==-1 && errno== EINTR);
    
    if (FD_ISSET(sock1, &sock_fdset)) {
      msgsock1 = accept(sock1, (struct sockaddr *)&client, &length);

      if ((pid1 = fork()) == 0) {
        close(sock1);
        read(msgsock1, line, sizeof(line));
        sscanf(line, "%d %d", &op1, &op2);
        somma = op1 + op2;

        if (log_is_active)
          printf("%d = %d + %d\n", somma, op1, op2);

        sprintf(answer, "%d\n", somma);

        write(msgsock1, answer, strlen(answer) + 1);
        close(msgsock1);
        exit(0);
      } else {
        close(msgsock1);
      }
    }

    if (FD_ISSET(sock2, &sock_fdset)) {
      msgsock2 = accept(sock2, (struct sockaddr *)&client, &length);

      if ((pid2 = fork()) == 0) {
        close(sock2);
        read(msgsock2, line, sizeof(line));
        sscanf(line, "%d %d", &op1, &op2);
        prodotto = op1 * op2;
        sprintf(answer, "%d\n", prodotto);

        if (log_is_active)
          printf("%d = %d * %d\n", prodotto, op1, op2);

        write(msgsock2, answer, strlen(answer) + 1);
        close(msgsock2);

        exit(0);

      } else {
        close(msgsock2);
      }
    }

  } while (1);
}

