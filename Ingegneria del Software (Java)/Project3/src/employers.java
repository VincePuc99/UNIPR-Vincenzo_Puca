public class employers extends person {
    private int skipper;
    private String[][] dbemployers= new String[1][2];
    private String[][] dbrequedwin = new String[5][2];
employers(){
    skipper=0;
    for(int i=0;i<1;i++){
        for (int j=0;j<2;j++){
            dbemployers[i][j]="";}
         }

    for(int i=0;i<5;i++){
        for (int j=0;j<2;j++){
            dbrequedwin[i][j]="";}
         }
}
public void fillnewempl(){//
    dbusersinfo[0][4]="Gigiemployer";//employer name
    dbusersinfo[1][4]="Verdiemployer";

    dbemployers[0][0]="gigi@gmail.com";//email
    dbemployers[0][1]="gigi1234";//password
    dbusersinfo[2][4]=dbemployers[0][0];
}
public String showemplemail(){//
return dbemployers[0][0];}

public String showemplpass(){//
return dbemployers[0][1];}

public void addnotifytodb(String reqname,String winename){
if(skipper<=4){
    dbrequedwin[skipper][0]=reqname;
    dbrequedwin[skipper][1]=winename;
    skipper++;}
}
public String[][] shownotifydb(){
   return dbrequedwin;
}
}//class