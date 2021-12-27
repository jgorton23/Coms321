import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class parseInstructions {
    private static InstructionMap map = new InstructionMap();
    private static ArrayList<String> arr = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        String filename = "";
        String binInstruction = ""; //variable for the binary instruction
        if(args.length > 0){
            filename = args[0]; 
        }
        File file = new File(filename);
        if(file.exists()){
            int offset = 0;
            RandomAccessFile in = new RandomAccessFile(file, "r");
            byte[] b = new byte[4];
            while(offset < in.length()){
                offset+=4;
                binInstruction = "";
                in.read(b);
                for (byte c : b) {
                    binInstruction+=byteToBitString(c);
                }
                Instruction instruction = new Instruction(map.getValue(binInstruction));
                instruction.parseInfo(binInstruction);
                instruction.addToArray(arr);
            } 
            fixArray();
            in.close();
            for (String string : arr) {
                 System.out.println(string); 
            }
        }
    }

    /**
     * convert a byte to bits
     */
    public static String byteToBitString(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    /**
     * adds labels to the arraylist, and switches labels X28-X31 with their corresponding register names XSP-XZR
     */
    private static void fixArray(){ //fix B.COND and LSL
        int label = 1;
        for (int i = 0; i < arr.size(); i++) {
            String s = arr.get(i);
            if(s.contains("X28")){
                s = s.replace("X28", "SP");
            }
            if(s.contains("X29")){
                s = s.replace("X29", "FP");
            }
            if(s.contains("X30")){
                s = s.replace("X30", "LR");
            }
            if(s.contains("X31")){
                s = s.replace("X31", "XZR");
            }
            arr.remove(i);
            arr.add(i, s);
            if((s!= "" && s.charAt(0)==' ')&&(s.charAt(4)=='B' || s.charAt(4)=='C')){
                int j = s.length()-1;
                String value = "";
                while(s.charAt(j)!=' '){
                        value = s.charAt(j)+ value;
                        j--;
                    }
                if(value.charAt(0)!= 'X' && value.charAt(0)!='L'  && value.charAt(0)!='S'  && value.charAt(0)!='F'){
                    int offset = Integer.parseInt(value); //only count instructions when determining the offset
                    int lines = 0;
                    if(offset > 0){
                        for(int instructions = 0; instructions < offset;){//starting at current index
                            lines++;//increase lines every time
                            if(lines+i >= arr.size() || arr.get(lines+i).charAt(0)==' '){ //iff the line is not a label
                                instructions++; //increase the number of instructions
                            }
                        }
                    }else{
                        for(int instructions=0; instructions > offset;){
                            lines --;
                            if(lines+i == 0 || arr.get(lines+i).charAt(0)==' '){
                                instructions --;
                            }
                        }
                    }
                    if((lines+i==arr.size() && arr.get(arr.size()-1).charAt(0) == ' ') || lines+i==0 || arr.get(i+lines-1).charAt(0)==' '){ //new label
                        s = s.replace(value, "l" + label);
                        arr.remove(i);
                        arr.add(i, s);
                        arr.add(i+lines, "l" + label + ":");
                        label++;
                        if(lines<=0){
                            i++;
                        }
                    }else{
                        s = s.replace(value, arr.get(i+lines-1).replace(":", ""));
                        arr.remove(i);
                        arr.add(i, s);
                    }
                }
            }
        }
    }
}
