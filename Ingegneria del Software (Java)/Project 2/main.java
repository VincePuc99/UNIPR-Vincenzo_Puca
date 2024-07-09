import java.util.Scanner;
import static java.lang.System.out;
import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static Scanner userinput = new Scanner(System.in);
    public static int count=2;

    public static void main(String[] args) throws IOException, InterruptedException { 
        out.println("Program Search & Buy italian wine");
        Thread.sleep(1500);

        String requester="";
        oggwine.fillwine();
        oggUsers.fillusers();
        oggempl.fillnewempl();

        accessoaut();
        writeonfile1method("User 1 authenticated or Signed up successfully");
        requester = winesearchandbuy();
        writeonfile1method("Wine bought or requested successfully for user 1");

        accessoaut();
        writeonfile1method("User 2 authenticated or Signed up successfully");
        requester = winesearchandbuy();
        writeonfile1method("Wine bought or requested successfully for user 2");

        //employers time
        employeraccess();
        writeonfile1method("Employer logged successfully");
        oggempl.manageorders(oggwine.dbwine[oggwine.savepos][0], requester,oggwine.savepos);
        writeonfile1method("employer restored wine stock successfully (maybe not)");
        writeonfile1method("Program Finished");
    }

   public static void employeraccess(){//
    String nameinput="",password="";

    do{
    out.println("Type employer email: ");
    nameinput=userinput.nextLine();

    out.println("Type employer password: ");
    password=userinput.nextLine();

    if(oggempl.showemplemail().equals(nameinput)){
       if(oggempl.showemplpass().equals(password)){
            out.println("Welcome back: "+oggempl.dbusersinfo[0][0]);
            out.println("Name: "+oggempl.dbusersinfo[0][4]);
            out.println("Surname: "+oggempl.dbusersinfo[1][4]);

            return;
        }else{
            out.println("Wrong email or password try again");
        }
    }else{
        out.println("Wrong email or password try again");
    }
    }while(true);
   }


    public static void accessoaut() throws InterruptedException {//
        String choise="",passwordinput="",pnameinput="";
        boolean exist=false,existpass=false;
        int checkindbpers=0;

        out.println("Sign up or Log in?");
        choise = userinput.nextLine();

        if(choise.equals("Log in")) {
            do{
         out.println("Type email: ");
         pnameinput=userinput.nextLine();

         out.println("Type password: ");
         passwordinput = userinput.nextLine();

         for(int i=0;i<4;i++){
           if(oggUsers.existingdb4login[0][i].equals(pnameinput))
           {exist=true;}

           if(exist){
            if(oggUsers.existingdb4login[1][i].equals(passwordinput))
            {existpass=true;
             checkindbpers=i;}
           }
         }
         if(exist && existpass)
         {
            out.println("Welcome back: "+oggUsers.dbusersinfo[2][checkindbpers]);
            out.println("Name: "+oggUsers.dbusersinfo[0][checkindbpers]);
            out.println("Surname: "+oggUsers.dbusersinfo[1][checkindbpers]);
            Thread.sleep(1500);
            return;
         }else{
            out.println("Wrong email or password try again");
         }
        }while(true);

        }else{ //sign up
            out.println("Type new email: ");
            oggUsers.existingdb4login[0][count]=userinput.nextLine();

            out.println("Type new name: ");
            oggUsers.db4refresh[0]=userinput.nextLine();

            out.println("Type new surname: ");
            oggUsers.db4refresh[1]=userinput.nextLine();
   
            out.println("Type password: ");
            oggUsers.existingdb4login[1][count] = userinput.nextLine();
            oggUsers.refresnewuser(count);

            out.println("Welcome: "+oggUsers.existingdb4login[0][count]);
            Thread.sleep(1500);
            count++;
        }
    }

    public static String winesearchandbuy() throws InterruptedException {//
        String winename="",year="",choise="",requester="";
        int result=10,quan=0;
        boolean resultvend=false;
 
        do{ 
        do{
        out.println("Insert wine name: ");
        winename=userinput.nextLine();

        out.println("Year: ");
        year=userinput.nextLine();
        out.println("Searching...");
        Thread.sleep(1500);
        
        result = oggwine.searchwine(winename,year);

        }while(result==0);
        Thread.sleep(1500);
        out.println("Do you want to buy the selected wine? yes,no: ");
        choise=userinput.nextLine();

        if(choise.equals("yes"))
        {
            if(result!=50){
            do{
            out.println("Quantity?: ");
            quan=Integer.parseInt(userinput.nextLine());
            resultvend= oggwine.buywine(quan,result);
            Thread.sleep(1500);
            out.println("The purchase was successful,Bye!");
            out.println("Disconnecting...");
            }
            while(resultvend==false);

        }else{               
            out.println("Not enough amount for the current employer...");
            out.println("Input requester's name: ");
            requester=userinput.nextLine();
            Thread.sleep(1500);
            out.println("Successfully requested! Thanks for the patience");
            out.println("Have a good day!");
            return requester;}
        }
    }while(choise.equals("no"));
    return requester;
    }

    public static void writeonfile1method(String data) throws IOException {
        FileWriter fw = new FileWriter("Path for output file",true); // <- change path here
        fw.write(data+" ");
        fw.close();
    }



}
