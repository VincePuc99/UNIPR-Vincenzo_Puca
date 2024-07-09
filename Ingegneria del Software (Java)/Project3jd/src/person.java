
/* class person that contains name,surname and email of every user */

public class person {
    public String[][] dbusersinfo = new String[3][5];
    //1 riga name 2 riga surname 3 riga email
    person(){
        for(int i=0;i<3;i++){
            for (int j=0;j<5;j++){
                dbusersinfo[i][j]="";}
             }
     }
}