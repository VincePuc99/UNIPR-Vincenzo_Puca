public class wine {

    public String[][] dbwine = new String [5][5]; //5 righe di vini 5 colonne descrizione vini
    //colonna 1 winename 2 producer 3 year 4 tecnical notes 5 varieties
    //field embedded in arrays
    public int [] dbnumberwine = new int [5];
    public int savepos;
    public String[] arr= new String[4];

    wine(){
        savepos=0;
      for(int i=0;i<4;i++){
       arr[i]="";
      }

        for(int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                dbwine[i][j]="";
            }
            dbnumberwine[i]=0;
        }
    }
    public void fillwine(){//
       dbwine[0][0]="Lambrusco";//info first wine
       dbwine[0][1]="Casalmonferrato";
       dbwine[0][2]="2010";
       dbwine[0][3]="Leggermente frizzante";
       dbwine[0][4]="Varietà alpina";
       dbnumberwine[0]=3;

       dbwine[1][0]="Franciacorta";//info second wine
       dbwine[1][1]="Verdicchio";
       dbwine[1][2]="2008";
       dbwine[1][3]="Malvasia";
       dbwine[1][4]="Bertani";
       dbnumberwine[1]=6;

       dbwine[2][0]="Gaja";//info third wine
       dbwine[2][1]="Masseto";
       dbwine[2][2]="2015";
       dbwine[2][3]="Montevertine";
       dbwine[2][4]="Allegrini";
       dbnumberwine[2]=9;

       dbwine[3][0]="Ruffino";//info fourth wine
       dbwine[3][1]="Petrolo";
       dbwine[3][2]="2019";
       dbwine[3][3]="Tommasi";
       dbwine[3][4]="Argiano";
       dbnumberwine[3]=2;

       dbwine[4][0]="Le Macchiole";//info fifth wine
       dbwine[4][1]="Vignamaggio";
       dbwine[4][2]="2012";
       dbwine[4][3]="Caparzo";
       dbwine[4][4]="La Marca";
       dbnumberwine[4]=0;
    }

    public int searchwine(String winn,String yea) throws InterruptedException {//
        boolean yesname=false,found=false;

        for(int d=0;d<5;d++){
         if(dbwine[d][0].equals(winn))
         { 
            yesname=true;
         }  
         if(yesname){
          if(dbwine[d][2].equals(yea))
          {            
            savepos=d;
            found=true;}
          else{
            yesname=false;}
         }
        }
        
        if(found){
            if (dbnumberwine[savepos]==0){
              arr[0]=dbwine[savepos][1];
              arr[1] = dbwine[savepos][3];
              arr[2]=dbwine[savepos][4];
                return 50;
            }else{
              arr[0]=dbwine[savepos][1];
              arr[1] = dbwine[savepos][3];
              arr[2]=dbwine[savepos][4];
                return dbnumberwine[savepos];}
            }
        return 0;
        }

        public boolean buywine(int quant,int avail){//
            if(quant<=avail)
            {
                avail=avail-quant;
                dbnumberwine[savepos]=avail;//db refresh
                return true;}
          return false;
        }

        public String[] sendtodisplay(){
         return arr;
        }
}
