import java.util.ArrayList;

/**
 * instruction object
 */
@SuppressWarnings({"unused"})
public class Instruction {
    private String word;
    private char type;
    private int opLength;

    /**
     * the variables for R type instructions
     */
    private int Rm;
    private int Rn;
    private int Shamt;
    private int Rd;

    /**
     * variable for I type instructions
     */
    private int ALU;

    /**
     * variables for D type instructions
     */
    private int DT_Address;

    /**
     * variable for branches
     */
    private int B_Address;
    private String  condition;

    /**
     * a constructor of an instruction of the given enum type
     * @param e
     */
    public Instruction(Enum<InstructionMap.Instruction> e){
        word = e.toString();
        if(word.equals("ADD") || word.equals("AND") || word.equals("BR") || word.equals("EOR") || word.equals("LSL") || word.equals("LSR") || word.equals("ORR") || word.equals("SUB") || word.equals("SUBS") || word.equals("MUL") || word.equals("PRNT") || word.equals("PRNL") || word.equals("DUMP") || word.equals("HALT")){//R type instructions
            type = 'R';
            opLength = 11;
        }else if(word.equals("ADDI") || word.equals("ANDI") || word.equals("SUBI") || word.equals("SUBIS") || word.equals("EORI") ||  word.equals("ORRI")){//I type instructions
            type  = 'I';
            opLength = 10;
        }else if(word.equals("LDUR") || word.equals("STUR")){//D type instructions
            type = 'D';
            opLength = 11;
        }else if(word.equals("B") || word.equals("BL")){//B type instructions
            type = 'B';
            opLength = 6;
        }else if(word.equals("BCOND") || word.equals("CBNZ") || word.equals("CBZ")){//CB type instructions
            type = 'C';
            opLength = 8;
        }else {//invalid
            type = 'N';
        }
    }

    /**
     * gets the info from the string of binary
     * @param binary
     */
    public void parseInfo(String binary){
        if(type == 'R'){
            Rm = toDecimal(binary.substring(opLength, opLength+5));
            Shamt = toDecimal(binary.substring(opLength+5, opLength+11));
            Rn = toDecimal(binary.substring(opLength+11, opLength + 16));
            Rd = toDecimal(binary.substring(opLength+16, opLength + 21));
        }else if(type == 'I'){
            ALU = toDecimal(binary.substring(opLength, opLength+12));
            Rn = toDecimal(binary.substring(opLength+12, opLength + 17));
            Rd = toDecimal(binary.substring(opLength+17, opLength + 22));
        }else if(type == 'D'){
            DT_Address = toDecimal(binary.substring(opLength, opLength+9));
            Rn = toDecimal(binary.substring(opLength+11, opLength+16));
            Rd = toDecimal(binary.substring(opLength+16, opLength + 21));
        }else if(type == 'B'){
            B_Address = toDecimal(binary.substring(opLength));
        }else if(type == 'C'){
            B_Address = toDecimal(binary.substring(opLength, opLength+19));
            Rd = toDecimal(binary.substring(opLength+19));
            if(word.equals("BCOND")){
                switch(Rd){
                    case 0:
                        condition = "EQ";
                        break;
                    case 1:
                        condition = "NE";
                        break;
                    case 2:
                        condition = "HS";
                        break;
                    case 3:
                        condition = "LO";
                        break;
                    case 4:
                        condition = "MI";
                        break;
                    case 5:
                        condition = "PL";
                        break;
                    case 6:
                        condition = "VS";
                        break;
                    case 7:
                        condition = "VC";
                        break;
                    case 8:
                        condition = "HI";
                        break;
                    case 9:
                        condition = "LS";
                        break;
                    case 10:
                        condition = "GE";
                        break;
                    case 11:
                        condition = "LT";
                        break;
                    case 12:
                        condition = "GT";
                        break;
                    case 13:
                        condition = "LE";
                        break;
                }
            }
        }
    }

    /**
     * will add this instruction's string representation to the given arraylist
     * @param arr
     */
    public void addToArray(ArrayList<String> arr){
        //arr.add("new label: ");
        arr.add(this.toString());
        
    }

    /**
     * helper testing method
     * @return
     */
    public char getType(){
        return type;
    }

    /**
     * takes a binary string and returns the decimal representation
     * @param bin
     * @return
     */
    public static int toDecimal(String bin){
        int value = 0;
        int sign = 1;
        if(bin.charAt(0) == '1' && bin.length() > 5){ //have to subtract 1 then invert the value
            sign = -1;
            for(int i = bin.length()-1;i>0;i--){
                if(bin.charAt(i)=='1'){
                    String inverse = bin.substring(0, i);
                    inverse += '0';
                    inverse += bin.substring(i+1);
                    bin = inverse;
                    break;
                }else{
                    String inverse = bin.substring(0, i);
                    inverse += '1';
                    inverse += bin.substring(i+1);
                    bin = inverse;
                }
            }
            String inverse = "";
            for(int i = 0; i < bin.length(); i ++){
                inverse += Math.abs(bin.charAt(i)-49);
            }
            bin = inverse;
        }
        for(int i = 0; i <bin.length(); i++){
            value += (bin.charAt(bin.length()-1-i)-48)*Math.pow(2,i);
        }
        return value*sign;
    }

    

    /**
     * helper, returns the string interpretation of the instruction
     */
    @Override
    public String toString() {

        String out = "    invalid";
        if(type == 'B'){
            out = "    " + word;
            out += " " + B_Address;
        }else if(type == 'R'){
            if(word.equals("ADD") || word.equals("AND") || word.equals("EOR") || word.equals("ORR") || word.equals("SUB") || word.equals("SUBS") || word.equals("MUL")){
                out = "    " + word;
                out += " X" + Rd + ", X" + Rn + ", X" + Rm;    
            }else if(word.equals("LSL") || word.equals("LSR")){
                out = "    " + word;
                out += " X" + Rd + ", X" + Rn + ", #" + Shamt;
            }else if(word.equals("PRNT") || word.equals("BR")){
                out = "    " + word;
                if(word.equals("PRNT")){
                    out += " X" + Rd;
                }else{
                    out += " X" + Rn;
                }
            }else if(word.equals("DUMP") || word.equals("HALT") || word.equals("PRNL")){
                out = "    " + word;
            }
        }else if(type == 'I'){
            out = "    " + word;
            out += " X" + Rd + ", X" + Rn + ", #" + ALU;
        }else if(type == 'D'){
            out = "    " + word;
            out += " X" + Rd + ", [X" + Rn + ", #" + DT_Address + "]";
        }else if(type == 'C'){
            if(word.equals("BCOND")){
                out = "    B." +    condition + " " + B_Address;
            }else{
                out = "    " + word;
                out += " X" + Rd + ", " + B_Address;
            }
        }
        return out;
    }
}
