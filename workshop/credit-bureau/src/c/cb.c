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

#define PORT 3550 				/* El puerto que será abierto */
#define BACKLOG 2 				/* El número de conexiones permitidas */
#define HEADERS_LENGTH 78 		/* Numero de caracteres de la cabecera del archivo Loans.txt */
#define RFC_LENGTH 10 			/* Numero de caracteres del RFC */
#define BUFFER_LENGTH 11 		/* Tamaño del buffer para recibir mensaje */
#define LINE_LENGTH 100 		/* Tamaño del arreglo para guardar linea por linea del archivo Loans.txt*/
#define THE_FILE "Loans.txt" 	/* Archivo para abrir */

char* searchRFC(char line[LINE_LENGTH], char buffer[BUFFER_LENGTH]){

	int searchCount;
	int searchBegin;
	
	/* Se recorre el contador hasta la posicion inicial del RFC */
	for(searchBegin = 0; line[searchBegin] != '|'; searchBegin++){}
	
	searchBegin++; 	/* Se aumenta uno el contador para que apunte al primer caracter del RFC */
	printf("\nComparando primer caracter del RFC: %c", line[searchBegin]);
	printf("\nPrimer caracter del buffer: %c", buffer[0]);
	/* Se compara caracter por caracter el buffer con los rfc de cada registro */
	for(searchCount = 0; searchCount < RFC_LENGTH && buffer[searchCount] == line[searchCount+searchBegin]; searchCount++){
		
		printf("\nComparando caracter  #%i: %c == %c",searchCount+1, line[searchCount+searchBegin], buffer[searchCount]);
		if(searchCount == 9){ 	/* Validacion de salida, cuando todos los caracteres coincidieron */
			
			/* Se envia la linea encontrada */	
			printf(" Encontrado...");							
			return line;							
		}
		
	}

	return NULL;
	
}

void doprocessing (int sock)
{
    char buffer[BUFFER_LENGTH];

    int recvMsgSize = 0;
	char opc[1]; 	/* Opcion que indica al programa las instrucciones de leer o escribir en Loans.txt */
	
	/* Se recibe la opcion para leer o escribir en Loans */
	while(recvMsgSize <= 0){
		if ((recvMsgSize = recv(sock, opc, 1, 0)) < 0)
			perror("ERROR reading to socket");
	}
	
	printf("\nOpcion: %c", opc[0]);
    
    /* Receive message from client */
    if ((recvMsgSize = recv(sock, buffer, BUFFER_LENGTH, 0)) < 0)
        perror("ERROR reading to socket");
	buffer[10] = '\0';
		
	printf("\nRFC: %s", buffer);
	
	FILE *fp;
	char line[LINE_LENGTH];
	int letter;			
	int i = 0;
	
	switch(opc[0]){
	
		case 'R':
		
			printf("\nOpcion leer elegida...");
			////////////////////////////////////////////////////////READ FILE
			
			fp = fopen(THE_FILE, "r"); 				/* Abrir archivo */
			fseek(fp, HEADERS_LENGTH, SEEK_SET); 	/* Recorrer cursor al principio del primer registro */			

			i = 0;
			
			do{ 
				letter = getc(fp); /* Obtener letra por letra */
				
				if(letter != '\n'){ 	/* Valida que no sea fin de linea */			
					line[i] = letter; 	/* Inserta letra por letra en el arreglo line */
					i++;			
				}
				
				else{	
					
					if((linep = searchRFC(line, buffer)) != NULL){	/* Se llama la funcion para buscar en el archivo */
						line[i] = '%'; 		/* Separador de registros encontrados */
						if (send(sock, line, i+1, 0) != i+1)
							perror("ERROR writing to socket");
					}
					i = 0;					
				}
				
			}while(letter != EOF); /* Leer hasta fin de archivo */
			
			/* Envio del mensaje de salida */
			if (send(sock, "\n", 1, 0) != 1)
				perror("ERROR writing to socket");
				
			break;
			/////////////////////////////////////////////////////////////READ FILE END
			
			
		case 'E':

			/* Receive message from client */
			char linep[LINE_LENGTH];
			if ((recvMsgSize = recv(sock, linep, LINE_LENGTH, 0)) < 0)
				perror("ERROR reading to socket");			
		
			printf("\nOpcion editar elegida...");
			/////////////////////////////////////////////////////////////EDIT FILE
		
			fp = fopen(THE_FILE, "r+");
			
			fseek(fp, HEADERS_LENGTH, SEEK_SET); /* Recorrer cursor al principio del primer registro */
			
			i = 0;
			
			do{ 
				letter = getc(fp); /* Obtener letra por letra */
				
				if(letter != '\n'){ 	/* Valida que no sea fin de linea */			
					line[i] = letter; 	/* Inserta letra por letra en el arreglo line */
					i++;			
				}
				else{
					
					if(strstr(line, linep)){
						line[i] = '\0';
						
						printf("\nLinea a editar: %s", line);
						
						fseek(fp, -3, SEEK_CUR);
						
						if(getc(fp) != 'N'){
						
							fseek(fp, -1, SEEK_CUR);						
							fputc('N', fp);						
							fseek(fp, 2, SEEK_CUR);
						
							registersNo++;
						}
						else{
							fseek(fp, 3, SEEK_CUR);
						}
					}					
					i = 0;
				}
			}while(letter != EOF);
			
			/* Envio del mensaje de salida */
			responseMsg[15] = registersNo + '0';
			if (send(sock, responseMsg, sizeof(responseMsg), 0) != sizeof(responseMsg))
				perror("ERROR writing to socket");

			break;
			/////////////////////////////////////////////////////////////EDIT FILE END
			
	}
    
	fclose(fp); 			/* Cerrar archivo */
    closesocket(sock);    	/* Close client socket */
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
      
      doprocessing(fd2);

   } /* end while */
}