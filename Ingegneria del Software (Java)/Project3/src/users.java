public class users extends person {
    public String[][] existingdb4login = new String[2][4];    
    public String[] db4refresh = new String[2]; //used for insert name and surname in personinfodb                    
    users(){
        db4refresh[0]="";
        db4refresh[1]="";
        for(int i=0;i<2;i++){
            for (int j=0;j<4;j++){
                existingdb4login[i][j]="";
            }
        }
    }
    public void fillusers(){
        existingdb4login[0][0]="mario@gmail.com";//riga email
        existingdb4login[0][1]="luigi@gmail.com";
        existingdb4login[0][2]="fill";
        existingdb4login[0][3]="fill";

        existingdb4login[1][0]="1234"; //riga password
        existingdb4login[1][1]="qwerty";
        existingdb4login[1][2]="fill";
        existingdb4login[1][3]="fill";

        //fill dbuserinfo with standard dbpreesist
        dbusersinfo[0][0]="mario";
        dbusersinfo[1][0]="Rossi";
        dbusersinfo[2][0]=existingdb4login[0][0];

        dbusersinfo[0][1]="luigi";
        dbusersinfo[1][1]="bianchi";
        dbusersinfo[2][1]=existingdb4login[0][1];

        dbusersinfo[0][2]="fill";
        dbusersinfo[1][2]="fill";
        dbusersinfo[2][2]=existingdb4login[0][2];

        dbusersinfo[0][3]="fill";
        dbusersinfo[1][3]="fill";
        dbusersinfo[2][3]=existingdb4login[0][3];
    }
    public void refresnewuser(int count){//
        dbusersinfo[0][count]=db4refresh[0];
        dbusersinfo[1][count]=db4refresh[1];
        dbusersinfo[2][count]=existingdb4login[0][count];
    }
}