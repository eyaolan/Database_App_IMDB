/**
 * Created by yaolan on 5/20/17.
 */
public class Test {

    public static void main(String[] args){
        String template = "";
        for(int i = 0; i<10-1;i++){
            template += "?,";
        }
        template += "?";

        System.out.println(template);

    }
}