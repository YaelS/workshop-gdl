/**
 * Este aplicacion representa a una entidad crediticia la cual
 * concentra todos los creditos otorgados a los clientes de
 * diferentes entidades.
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <winsock2.h>
#include <unistd.h>

#define PORT 3550 /* El puerto que será abierto */
#define BACKLOG 2 /* El número de conexiones permitidas */
#define BEGIN_OF_RFC 4
#define HEADERS_LENGTH 77
#define RFC_LENGTH 10

void doprocessing (int sock)
{
    int n;
    char buffer[10];

    memset(&(buffer), '0', 10);
    int recvMsgSize;
    
    /* Receive message from client */
	//while(recvMsgSize > 0){
    if ((recvMsgSize = recv(sock, buffer, 10, 0)) < 0)
        perror("ERROR reading to socket");
		
	//}
	////////////////////////////////////////////////////////READ FILE
	FILE *fp;

	fp = fopen("Loans.txt", "r");
	fseek(fp, HEADERS_LENGTH, 0);
	
	int letter;
	char line[100];
	
	int i = 0, x;
	do{ 
        letter = getc(fp); //obtener letra por letra
		if(letter == '\n'){ //valida que no sea fin de linea
		
			int inicio_busqueda = 0;
			printf("\n*%c == %c* ",line[inicio_busqueda+BEGIN_OF_RFC],buffer[inicio_busqueda]);
			while(line[inicio_busqueda+BEGIN_OF_RFC] == buffer[inicio_busqueda] && inicio_busqueda < RFC_LENGTH){ //condicion de fin de busqueda
				printf("\n*%c == %c*",line[inicio_busqueda+BEGIN_OF_RFC],buffer[inicio_busqueda]);
				if(inicio_busqueda == 9){ //si se llego al final de la busqueda y fueron iguales los caracteres					
					if (send(sock, line, 256, 0) != recvMsgSize)
						perror("ERROR writing to socket");
					printf("Encontrado...");
				}
				inicio_busqueda++;
			}
			i = 0;
		}			
		else{
			line[i] = letter;
			i++;
		}
    }while(letter != EOF); //leer hasta fin de archivo
	/////////////////////////////////////////////////////////////READ FILE

    /* Send received string and receive again until end of transmission */
    
        /* Echo message back to client */
        /*if (send(sock, buffer, recvMsgSize, 0) != recvMsgSize)
            perror("ERROR writing to socket");*/

        /* See if there is more data to receive */
        /*if ((recvMsgSize = recv(sock, buffer, 10, 0)) < 0)
            perror("ERROR reading to socket");*/
    

    //closesocket(sock);    /* Close client socket */
}

BOOL initW32() 
{
		WSADATA wsaData;
		WORD version;
		int error;
		
		version = MAKEWORD( 2, 0 );
		
		error = WSAStartup( version, &wsaData );
		
		/* check for error */
		if ( error != 0 )
		{
		    /* error occured */
		    return FALSE;
		}
		
		/* check for correct version */
		if ( LOBYTE( wsaData.wVersion ) != 2 ||
		     HIBYTE( wsaData.wVersion ) != 0 )
		{
		    /* incorrect WinSock version */
		    WSACleanup();
		    return FALSE;
		}	
}

int main()
{

	 initW32(); /* Necesaria para compilar en Windows */ 
	 	
   int fd, fd2; /* los descriptores de archivos */

   /* para la información de la dirección del servidor */
   struct sockaddr_in server;

   /* para la información de la dirección del cliente */
   struct sockaddr_in client;

   unsigned int sin_size;

   pid_t pid;

   /* A continuación la llamada a socket() */
   if ((fd=socket(AF_INET, SOCK_STREAM, 0)) == -1 ) {
      printf("error en socket()\n");
      exit(-1);
   }

   server.sin_family = AF_INET;

   server.sin_port = htons(PORT);

   server.sin_addr.s_addr = INADDR_ANY;
   /* INADDR_ANY coloca nuestra dirección IP automáticamente */

   //bzero(&(server.sin_zero),8);
   memset(&(server.sin_zero), '0', 8);
   /* escribimos ceros en el reto de la estructura */


   /* A continuación la llamada a bind() */
   if(bind(fd,(struct sockaddr*)&server, sizeof(struct sockaddr))==-1) {
      printf("error en bind() \n");
      exit(-1);
   }

   if(listen(fd,BACKLOG) == -1) {  /* llamada a listen() */
      printf("error en listen()\n");
      exit(-1);
   }

   while(1) {
      sin_size=sizeof(struct sockaddr_in);
      /* A continuación la llamada a accept() */
      if ((fd2 = accept(fd,(struct sockaddr *)&client, &sin_size))==-1) {
         printf("error en accept()\n");
         exit(-1);
      }

      printf("Se obtuvo una conexión desde %s\n", inet_ntoa(client.sin_addr) );
      /* que mostrará la IP del cliente */

      send(fd2,"Bienvenido a mi servidor.\n",27,0);
      /* que enviará el mensaje de bienvenida al cliente */
      
      doprocessing(fd2);

   } /* end while */
}

