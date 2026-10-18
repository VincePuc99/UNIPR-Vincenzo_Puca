package Project;

public class attivita {
    private int i=0;
    public String tiponome;
    private String[] listaiscritti;

attivita(){
    tiponome="";
   listaiscritti = new String[3];}

public void aggiungisociolista(String nome){//
  if(i>3){
  listaiscritti[i]=nome;
  i++;
  }
}

public void modificasociolista(String nuovonome,int pos){//
listaiscritti[pos]= nuovonome;
}
}

