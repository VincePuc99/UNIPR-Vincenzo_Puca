#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
int sockfd, newsockfd, portno, clilen;
char buffer[256]="";
struct sockaddr_in serv_addr, cli_addr;
int n,on,ret;

sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) { perror("Errore socket"); exit(2); }

// Per riutilizzare l’indirizzo di una socket in fase di chiusura
on=1;
ret = setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on));

portno = atoi(argv[1]);
serv_addr.sin_family = AF_INET;
serv_addr.sin_addr.s_addr = INADDR_ANY;
serv_addr.sin_port = htons(portno);

    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) { 
    perror("Errore bind");exit(3); }

listen(sockfd,5);

clilen = sizeof(cli_addr);
newsockfd = accept(sockfd,(struct sockaddr *)&cli_addr,(socklen_t *)&clilen);

    if (newsockfd < 0) { perror("Errore accept"); exit(4); }

n = read(newsockfd,buffer,255);
if (n < 0) { perror("Errore read"); exit(5); }

printf("(SERVER) Ecco il messaggio ricevuto dal client: %s\n",buffer);
n = write(newsockfd,"MESSAGGIO RICEVUTO, FINE COMUNICAZIONE",38);

    if (n < 0) { perror("Errore write"); exit(6); }

close(sockfd);
close(newsockfd);
return 0; 
}
