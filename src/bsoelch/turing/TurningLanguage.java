package bsoelch.turing;

import java.io.*;
import java.util.HashMap;

public class TurningLanguage {

    private static final int AMBIGUOUS = -1;
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private static final int ID = 2;
    private static final int FLIP = -1;

    //addLater? initState, IO
    public static TuringMachine compile(BufferedReader code) throws IOException {
       HashMap<Integer, TuringMachine.Transition> table=new HashMap<>();
       String line;
       while((line=code.readLine())!=null){
           for(String s:line.split("[\\p{javaWhitespace}]")){
               s=s.trim();
               if(s.startsWith("//")){
                   break;//line comment
               }else if(s.length()>0)
                   readTransition(s,table);
           }
       }
       return new TuringMachine(table);
    }

    private static void readTransition(String command, HashMap<Integer, TuringMachine.Transition> table) {
        int i=command.indexOf(":");
        if(i== -1){
            throw new IllegalArgumentException("missing : in transition \""+command+"\"");
        }
        String l=command.substring(0,i),r=command.substring(i+1);
        int prevValue;
        if(l.charAt(0)=='0')
            prevValue=FALSE;
        else if(l.charAt(0)=='1')
            prevValue=TRUE;
        else if(l.charAt(0)=='?')
            prevValue= AMBIGUOUS;
        else
            throw new IllegalArgumentException("IllegalLine prevValue: '"+l.charAt(0)+"' Allowed 0,1,?");
        int prevState=Integer.parseInt(l.substring(1));
        int nextValue;
        int i0=0;
        if(r.charAt(i0)=='0')
            nextValue=FALSE;
        else if(r.charAt(i0)=='1')
            nextValue=TRUE;
        else if(r.charAt(i0)=='=')
            nextValue=prevValue== AMBIGUOUS ? ID :prevValue;
        else if(r.charAt(i0)=='!')
            nextValue=prevValue== AMBIGUOUS ?FLIP:TRUE-prevValue;
        else
            throw new IllegalArgumentException("IllegalLine nextValue: '"+r.charAt(0)+"' Allowed 0,1,=,!");
        i0++;
        boolean moveLeft;
        if(r.charAt(i0)=='>')
            moveLeft=false;
        else if(r.charAt(i0)=='<')
            moveLeft=true;
        else
            throw new IllegalArgumentException("IllegalLine direction: '"+r.charAt(i0)+"' Allowed <,>");
        r=r.substring(i0+1);
        int nextState;
        if(r.equalsIgnoreCase("x")){
            nextState= -1;
        }else{
            nextState=Integer.parseInt(r);
        }
        if (prevValue == AMBIGUOUS) {
            table.put(prevState, new TuringMachine.Transition(moveLeft, nextValue == FLIP || nextValue == TRUE, nextState));
            table.put(0x80000000 | prevState, new TuringMachine.Transition(moveLeft, nextValue == ID || nextValue == TRUE, nextState));
        } else {
            table.put((prevValue == TRUE ? 0x80000000 : 0) | prevState, new TuringMachine.Transition(moveLeft, nextValue == TRUE, nextState));
        }
    }
    public static void main(String[] args) throws IOException {
        if(args.length==0){
            System.out.println("Not enough arguments");
            System.out.println("usage: <filename>");
            return;
        }

        File file=new File(args[0]);
        if(!file.exists()){
            System.out.println("The file: \""+args[0]+"\" does not exist");
            return;
        }
        TuringMachine turing=compile(new BufferedReader(new FileReader(file)));
        do{
            System.out.println(turing);
        }while (turing.step());
        System.out.println(turing);
    }
}
