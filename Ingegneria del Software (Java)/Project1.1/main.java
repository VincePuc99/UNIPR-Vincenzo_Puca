import static java.lang.System.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

public class main{ //
    private static Scanner userinput = new Scanner(System.in);
    private static products[] objprod;
    private static products[] objprodstart;
    private static users[] objus;
    private static users[] objussign;
    private static users objadmi;
    private static int skipsign=0;
    private static String[][] notifydb;
    private static int skipnotifydb;
    private static String currentuser;
    public static void main(String args[]) throws IOException {//
        notifydb=new String[5][3];

        for(int i=0;i<5;i++){
            for(int j=0;j<3;j++){ notifydb[i][j]=""; }}

        skipnotifydb=0;
        letsfill();
        writeonfile1method("data filled");
        accessauth();
        writeonfile1method("1°User Logged/Signed");
        searchandbuy();
        writeonfile1method("1°User search and buy done");
        accessauth();
        writeonfile1method("2°User Logged/Signed");
        searchandbuy();
        writeonfile1method("2°User search and buy done");
        out.println("Starting admin section...");
        adminlogin();
        writeonfile1method("Admin Logged/Signed");
        checknotification();
        writeonfile1method("Notification checked");
        printstatus();
        writeonfile1method("status printed");
    }
/*  method to check if you have any notification
 *  if you have at least one you will get it printed and you will be able to fill the product
 *  if not you will get notified that no notifies are present
 *  */
    public static void checknotification(){
        Random rand = new Random();
        boolean atleastone=false;
        String next;

        for(int i=0;i<5;i++){
            for(int j=0;j<2;j++){
                if(!notifydb[i][j].equals(""))
                 {
                    atleastone=true;}} }

        if(atleastone){
           for(int i=0;i<5;i++){
                if(!notifydb[i][0].equals("")){
                out.println("Requester name: "+notifydb[i][0]);
                out.println("Product requester: "+notifydb[i][1]);
                out.print("Do you want to fill this product with a random number? Yes-No: ");
                next=userinput.nextLine();

                if(next.equals("Yes"))
                {
                    objprod[Integer.parseInt(notifydb[i][2])].setquant(rand.nextInt(10));
                    out.println("Quantity updated");
                }
               }//for ending db 
            }
        }
        else{
            out.println("No notify have a nice day");}
    }
/* method to search and buy the products
 * if the product is present you will be able to search and buy it and in which quantities
 * if the quantity is more than the one present on stock you will get notified that the quantity is not available
 * if it's enough you will be able to buy it
 * after the whole process you will be able to exit or not from the program*/
    public static void searchandbuy(){
        String inputsearchname,inputsearchproducer,choise;
        boolean exist=false;
        String exitcond;
        int savepos=0;

        do{

        do{
        out.print("Product name: ");
        inputsearchname=userinput.nextLine();

        out.print("Producer name: ");
        inputsearchproducer=userinput.nextLine();

            for(int i=0;i<3;i++){
                if(inputsearchname.equals(objprod[i].getname())){
                    if(inputsearchproducer.equals(objprod[i].getproducer())){
                    exist=true;
                    savepos=i;}
                }
            }
            if(exist==true){      
                showinfo(savepos);
                break;}
            else{
                out.println("Product not found");}
        }while(true);

        out.print("Do you want to buy it? Yes-No ");
        choise=userinput.nextLine();
        
        if (choise.equals("Yes")){

            out.print("Quantity: ");
            choise=userinput.nextLine();

            if(objprod[savepos].getquant()==0){
                out.print("Sending notify to admin...");  
                notifydb[skipnotifydb][0] = objprod[savepos].getname();
                notifydb[skipnotifydb][1] = currentuser;
                notifydb[skipnotifydb][2] = Integer.toString(savepos);//pos in dbproduct
                skipnotifydb++;
            }
            else if(objprod[savepos].getquant()<Integer.parseInt(choise)){
                out.print("Quantity not available");    
            }
            else{
                objprod[savepos].removequant(Integer.parseInt(choise));
                out.println("Product bought");  
            }
            out.print("Do you want to exit? Yes-No ");
            exitcond=userinput.nextLine();
        }
        else{
            out.print("Do you want to exit? Yes-No ");
            exitcond=userinput.nextLine();
        }
        if(exitcond.equals("Yes")){break;}
        }while(true);
    }
/* method to actually login and sign up in the program for the users
 * if you are already registered you can login with your email and password
 * if one of them is wrong you will get notified that the informations you have put are wrong
 * if not you will be able to login successfully
 * if you don't have an account you can register yourself with an email and password*/
    public static void accessauth(){//
        String choise="",email="",password="";
        String newmail="",newpass="";
        boolean exist=false;
        int savepos=0;

        out.print("Login or Sign?: ");
        choise=userinput.nextLine();

        if(choise.equals("Login")){
            do{
            out.print("Email: ");
            email=userinput.nextLine();

            out.print("Password: ");
            password=userinput.nextLine();

                for(int i=0;i<2;i++){
                    if(email.equals(objus[i].getemail())){
                        if(password.equals(objus[i].getpass())){
                            exist=true;
                            savepos=i;
                        }
                    }
                }
                if(exist==true){
                    out.println("Welcome: "+ objus[savepos].getemail());
                    skipsign++;
                    currentuser=objus[savepos].getemail();
                    break;
                }
                else{out.println("Wrong info");}
            }while(true);

        }else{
            out.print("New email: ");
            newmail=userinput.nextLine();

            out.print("New password: ");
            newpass=userinput.nextLine();
            objussign[skipsign].setnewemail(newmail);
            objussign[skipsign].setnewpass(newpass);
            out.println("Welcome: "+ objussign[skipsign].getemail());
            currentuser=objussign[skipsign].getemail();
            skipsign++;
        }
    }
/* method to login for the admin
 * if the email and password are right you will login
 * otherwise the program will tell you that you have inserted wrong info */
    public static void adminlogin(){//
        String email,password;
        do{
        out.print("Email: ");
        email=userinput.nextLine();

        out.print("Password: ");
        password=userinput.nextLine();

        if(email.equals(objadmi.getemail())){
            if(password.equals(objadmi.getpass())){
                out.println("Admin Logged successfully");
                break;
            }else{out.println("Wrong info");}
        }
        else{out.println("Wrong info");}}
        while(true);
    }
/* method to check every info of a product present in the system*/
    public static void showinfo(int position){
        out.println("Name: ");
        out.println(objprod[position].getname()+"\n");
        out.println("Producer: ");
        out.println(objprod[position].getproducer()+"\n");
        out.println("Price: ");
        out.println(objprod[position].getprice()+"\n");
        out.println("Technical notes: ");
        out.println(objprod[position].gettechn()+"\n");
        out.println("Quantity: ");
        out.println(objprod[position].getquant()+"\n");
    }
/* method to print out the starting and the final status of the product*/
    public static void printstatus(){
        out.println("\n\nStarting status: ");
        for(int i=0;i<3;i++){
            out.println("Name: "+objprodstart[i].getname());
            out.println("Producer: "+objprodstart[i].getproducer());
            out.println("Quantity: "+objprodstart[i].getquant());
            out.println("Technical notes: "+objprodstart[i].gettechn());
            out.println("Price: "+objprodstart[i].getprice());
        }

        out.println("\n\nFinal status: ");
        for(int i=0;i<3;i++){
            out.println("Name: "+objprod[i].getname());
            out.println("Producer: "+objprod[i].getproducer());
            out.println("Quantity: "+objprod[i].getquant());
            out.println("Technical notes: "+objprod[i].gettechn());
            out.println("Price: "+objprod[i].getprice());
        }
    }
    /* method to fill in new products and users*/
    public static void letsfill(){
        objus=new users[2];
        objussign=new users[2];
        objprod=new products[3];
        objprod[0] =new products(49,"Smartphone A1221","Apple","Faster processor",0); //price name producer technicaln
        objprod[1] =new products(69,"Smartfridge XCT56","Samsung","-20°C in 3 second",2);
        objprod[2] =new products(420,"TV SM906578","Samsung","Oled 4k",1);

        objus[0]=new users("gigi@gmail.com", "1234");
        objus[1]=new users("luigi@gmail.com", "1234");
        objadmi=new users("admin@gmail.com", "1234");
        objussign[0]=new users("","");
        objussign[1]=new users("","");

        objprodstart=objprod;
    }//endfill
/* method to write on a output*/
    public static void writeonfile1method(String data) throws IOException {
        FileWriter fw = new FileWriter("/Users/vincenzopuca/Desktop/Project1.1/output.txt",true); // <- change path here
        fw.write(data+" ");
        fw.close();
    }//end write
}//end class