

/* class users
 * it comprehends an email,password
 * you will be able to set a new email and/or password aswell*/
public class users {
    private String email;
    private String pass;
    users(String e,String p){
        email=e;
        pass=p;
    }
    public String getemail(){
        return email;}
    public String getpass(){
        return pass;}
    public void setnewpass(String npass){
        pass=npass;}
    public void setnewemail(String nemail){
        email=nemail;}
}