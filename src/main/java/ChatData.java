import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatData implements Serializable {
    public String message;
    public boolean sendAll;
//   public HashMap<Integer, String> clientList ;
    public List<String> clientlist;

   public ArrayList<Integer> clientNum;

   public ChatData()
   {
       message="";
       sendAll = false;
       clientNum  = new ArrayList<>();
       clientlist = new ArrayList<>();
   }
}
