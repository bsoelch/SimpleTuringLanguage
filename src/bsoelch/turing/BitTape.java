package bsoelch.turing;

import java.util.ArrayDeque;

public class BitTape {
    private static final long MAX_MASK = 0x8000000000000000L;
    long mask=0x80000000L;
    long current;
    ArrayDeque<Long> left=new ArrayDeque<>(64),right=new ArrayDeque<>(64);


    public void move(boolean moveLeft){
        if(moveLeft){
            if(mask==MAX_MASK) {
                mask = 1;
                if(current!=0||right.size()>0)
                    right.addFirst(current);
                current=left.isEmpty()?0:left.removeLast();
            }else mask<<=1;
        }else{
            if(mask==1) {
                mask = MAX_MASK;
                if(current!=0||left.size()>0)
                    left.addLast(current);
                current=right.isEmpty()?0:right.removeFirst();
            }else mask>>>=1;
        }
    }

    public boolean get(){
        return (current&mask)!=0;
    }

    public void set(boolean value){
        if(value){
            current|=mask;
        }else{
            current&=~mask;
        }
    }

    @Override
    public String toString() {
        StringBuilder str=new StringBuilder(64*(left.size()+right.size()+1)+2);
        for(Long l:left)
            addBinaryString(l,str,false);
        addBinaryString(current,str,true);
        for(Long l:right)
            addBinaryString(l,str,false);
        int i0=0,i1=str.length()-1;
        while(str.charAt(i0)=='0')
            i0++;
        while(str.charAt(i1)=='0')
            i1--;
        return str.substring(i0,i1+1);
    }

    private void addBinaryString(long aLong, StringBuilder str,boolean current) {
        long mask=MAX_MASK;
        while(mask!=0){
            if(current&&(mask==this.mask))
                str.append('|');
            str.append(((aLong&mask)==0)?'0':'1');
            if(current&&(mask==this.mask))
                str.append('|');
            mask>>>=1;
        }
    }
}
