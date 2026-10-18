import java.util.Scanner;

public class employers extends person {
    private static Scanner userinput = new Scanner(System.in);
    private String[][] dbemployers= new String[1][2];
    private wine objwine=new wine();
employers(){
    for(int i=0;i<1;i++){
        for (int j=0;j<2;j++){
            dbemployers[i][j]="";}
         }
}

public void fillnewempl(){//
    dbusersinfo[0][4]="Gigiemployer";//employer name
    dbusersinfo[1][4]="Verdiemployer";

    dbemployers[0][0]="gigi@gmail.com";//email
    dbusersinfo[2][4]=dbemployers[0][0];
    dbemployers[0][1]="gigi1234";//password
}

public void manageorders(String wt,String requ,int sp){//

 if(!requ.equals("")){
 int quant=0;
 System.out.println("User "+ requ +" has requested "+wt);
 System.out.println("Add new quatity: ");
 quant =Integer.parseInt(userinput.nextLine());

 objwine.dbnumberwine[sp]=quant;//upgrade only in this obj

 System.out.println("Employer "+ dbemployers[0][0]+" call "+requ);
 System.out.println("We have added "+quant+" to wine "+wt);
 System.out.println("Thank you have a nice day!");
 }else{
    System.out.println("No request found,have a nice day!");
 }
}

public String showemplemail(){//
return dbemployers[0][0];
}

public String showemplpass(){//
return dbemployers[0][1];
}
}