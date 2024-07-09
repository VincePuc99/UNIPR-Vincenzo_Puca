package Project;

import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Scanner;

import jdk.javadoc.internal.doclets.toolkit.util.DocFinder.Output;

public class main{
    public static boolean once;
    public static String path="/Users/vincenzopuca/Desktop/Project/output.txt";
    public static void main(String[] args) {

      creafile();
       utenti socio1=new utenti();
       attivita attivsocio1=new attivita();
       creautenti(socio1, attivsocio1);

       utenti socio2=new utenti();
       attivita attivsocio2=new attivita();
       creautenti(socio2, attivsocio2);

       utenti socio3=new utenti();
       attivita attivsocio3=new attivita();
       creautenti(socio3, attivsocio3);

   // scrivisufile("Passaggio creazione utenti");

//caso in cui socio 1 sia admin scelto da input precedente
//è possibile implementare la casistica in cui socio 2 o 3 siano admin
Scanner inpututente = new Scanner(System.in);
      if (socio1.isadmin){
        String scelta1;
        System.out.println("Modifica socio 2? si/no");
        scelta1=inpututente.nextLine();
        if(scelta1.equals("si")){
          modificasocio(socio2,attivsocio2,1);}

        System.out.println("Elimina socio 2? si/no");
        String scelta= inpututente.nextLine();
        if(scelta.equals("si")){
          Eliminasocio(socio2,attivsocio2,1);}

        System.out.println("Aggiungere nuovo utente al posto di socio 2? si/no");
        String scelta2= inpututente.nextLine();
        if(scelta2.equals("si")){
          modificasocio(socio2,attivsocio2,1);}
       //modifica
         System.out.println("Modifica socio 3? si/no");
          String s= inpututente.nextLine();
          if(s.equals("si")){
            modificasocio(socio3,attivsocio3,2);}
  
          System.out.println("Elimina socio 3? si/no");
          String sc= inpututente.nextLine();
          if(sc.equals("si")){
            Eliminasocio(socio3,attivsocio3,2);}
  
          System.out.println("Aggiungere nuovo utente al posto di socio 3? si/no");
          String sce= inpututente.nextLine();
          if(sce.equals("si")){
            modificasocio(socio3,attivsocio3,2);}}
          
//scrivisufile("Passaggio gestione admin");

            double casuale = (int)(Math.random()*3);

   if (casuale==1)
   { System.out.println("E' stato scelto socio 1");
     System.out.println("Inserire attivita o corso a cui iscriversi : "); 
     attivsocio1.tiponome= inpututente.nextLine();
     attivsocio1.modificasociolista(attivsocio1.tiponome,0);

     System.out.println("Inserire attivita o corso a da cancellare  : "); 
     attivsocio1.tiponome= inpututente.nextLine();
     attivsocio1.modificasociolista("",0);
   }

   if (casuale==2)
   { System.out.println("E' stato scelto socio 2");
     System.out.println("Inserire attivita o corso a cui iscriversi: "); 
     attivsocio2.tiponome= inpututente.nextLine();
     attivsocio2.modificasociolista(attivsocio2.tiponome,1);

     System.out.println("Inserire attivita o corso a da cancellare: "); 
     attivsocio2.tiponome= inpututente.nextLine();
     attivsocio2.modificasociolista("",1);
   }

   if (casuale==3)
   { System.out.println("E' stato scelto socio 3");
     System.out.println("Inserire attivita o corso a cui iscriversi: "); 
     attivsocio3.tiponome= inpututente.nextLine();
     attivsocio3.modificasociolista(attivsocio3.tiponome,2);

     System.out.println("Inserire attivita o corso a da cancellare: "); 
     attivsocio3.tiponome= inpututente.nextLine();
     attivsocio3.modificasociolista("",2);
   }

  // scrivisufile("Passaggio modifica del socio");

visualizzainformazioni(socio1,socio2,socio3,attivsocio1,attivsocio2,attivsocio3);

// scrivisufile("Programma completa e visualizzazione informazioni");
}
 public static void Eliminasocio(utenti socio,attivita att,int possocio)//
  {
   socio.nome="";
   socio.cognome="";
   socio.password="";
   socio.email="";
   att.tiponome="";
   att.modificasociolista(socio.nome, possocio);
  }
public static void visualizzainformazioni(utenti s1,utenti s2,utenti s3,attivita a1,attivita a2, attivita a3){//
  System.out.println("Informazioni socio 1");
  System.out.println("Nome "+ s1.nome);
  System.out.println("Cognome "+ s1.cognome);
  System.out.println("Email "+ s1.email);
  System.out.println("Tipo Attività "+ a1.tiponome);
  System.out.println("Is Admin true/false "+ s1.isadmin);

  System.out.println("Informazioni socio 2");
  System.out.println("Nome socio 2 "+ s2.nome);
  System.out.println("Cognome socio 2 "+ s2.cognome);
  System.out.println("Email socio 2 "+ s2.email);
  System.out.println("Tipo Attività socio 2 "+ a2.tiponome);
  System.out.println("Is Admin true/false: "+ s2.isadmin);

  System.out.println("Informazioni socio 1");
  System.out.println("Nome socio 3 "+ s3.nome);
  System.out.println("Cognome socio 3"+ s3.cognome);
  System.out.println("Email socio 3"+ s3.email);
  System.out.println("Tipo Attività socio 3"+ a3.tiponome);
  System.out.println("Is Admin true/false : "+ s3.isadmin);
}

 public static void modificasocio(utenti socio, attivita attivsocio,int possocio){//

 Scanner inpututente = new Scanner(System.in); 

 System.out.println("Inserire nuovo nome: ");
 socio.nome=inpututente.nextLine();

 System.out.println("Inserire nuovo cognome: ");
 socio.cognome=inpututente.nextLine();

 System.out.println("Inserire nuova email: ");
 socio.email=inpututente.nextLine();

 System.out.println("Inserire nuova password: ");
 socio.password=inpututente.nextLine();

 System.out.println("Inserire nuova attivita: ");
 attivsocio.tiponome=inpututente.nextLine();

 attivsocio.modificasociolista(socio.nome, possocio);
 }   

 public static void creautenti(utenti userlist,attivita varattivi){//metodo che crea gli utenti con input
  String isadmin;
  Scanner inpututente = new Scanner(System.in);  // Create a Scanner object

  System.out.println("Inserire nome: ");
  userlist.nome = inpututente.nextLine();  // Read user input

  System.out.println("Inserire cognome: ");
  userlist.cognome = inpututente.nextLine();  // Read user input

  System.out.println("Inserire email: ");
  userlist.email = inpututente.nextLine();  // Read user input

  System.out.println("Inserire password: ");
  userlist.password= inpututente.nextLine();  // Read user input

  if(once){

  System.out.println("Admin? si/no: ");
  isadmin = inpututente.nextLine();

  if (isadmin.equals("si")){
    userlist.isadmin=true;
    once=false;
  } 
  else {userlist.isadmin=true;}
}else {userlist.isadmin=true;}

  System.out.println("Attività? (gare o corsi): ");
  String tipoatt= inpututente.nextLine();

  if(tipoatt.equals("gare")){
  varattivi.tiponome="gare";}
  else{varattivi.tiponome="corsi";}

  varattivi.aggiungisociolista(userlist.nome);
}

public static void creafile() {
  try {
    File myObj = new File(path);
    if (myObj.createNewFile()) {
      System.out.println("File created: " + myObj.getName());
    } else {
      System.out.println("File already exists.");
    }
  } catch (IOException e) {
    System.out.println("An error occurred.");
    e.printStackTrace();
  }
}

public static void scrivisufile(String passaggio) {
  try {
    FileWriter scrivi=FileWriter(path);
    scrivi.write("Esecuzione "+ passaggio);
    scrivi.close();
    System.out.println("Esecuzione "+ passaggio);
  } catch (IOException e) {
    System.out.println("An error occurred.");
    e.printStackTrace();
}
}
}
