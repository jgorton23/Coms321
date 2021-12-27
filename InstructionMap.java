//import java.util.ArrayList;
import java.util.HashMap;

/**
 * a class for deciphering opcodes
 */
public class InstructionMap {

    /**
     * the hashmap of instructions
     */
    private HashMap<String, Enum<Instruction>> map = new HashMap<>(Instruction.values().length);

    // /**
    //  * arraylist to store each line to be printed (need arraylist so it can change length, and i can add to the middle easily)
    //  */
    // public ArrayList<String> instructions = new ArrayList<>();

    /**
     * the types of instructions
     */
    public enum Instruction{
        ADD, ADDI, AND, ANDI, B, BCOND, BL, BR, 
        CBNZ, CBZ, EOR, EORI, LDUR, LSL, LSR, ORR, 
        ORRI, STUR, SUB, SUBI, SUBIS, SUBS, MUL, 
        PRNT, PRNL, DUMP, HALT,
        INVALID;
    }

    /**
     * constructor makes a hashmap of instructions with corresponding opcodes as keys
     */
    public InstructionMap(){
        map.put("10001011000", Instruction.ADD);
        map.put("1001000100", Instruction.ADDI);
        map.put("10001010000", Instruction.AND);
        map.put("1001001000", Instruction.ANDI);
        map.put("000101", Instruction.B);
        map.put("01010100", Instruction.BCOND);
        map.put("100101", Instruction.BL);
        map.put("11010110000", Instruction.BR);
        map.put("10110101", Instruction.CBNZ);
        map.put("10110100", Instruction.CBZ);
        map.put("11001010000", Instruction.EOR);
        map.put("1101001000", Instruction.EORI);
        map.put("11111000010", Instruction.LDUR);
        map.put("11010011011", Instruction.LSL);
        map.put("11010011010", Instruction.LSR);
        map.put("10101010000", Instruction.ORR);
        map.put("1011001000", Instruction.ORRI);
        map.put("11111000000", Instruction.STUR);
        map.put("11001011000", Instruction.SUB);
        map.put("1101000100", Instruction.SUBI);
        map.put("1111000100", Instruction.SUBIS);
        map.put("11101011000", Instruction.SUBS);
        map.put("10011011000", Instruction.MUL);
        map.put("11111111101", Instruction.PRNT);
        map.put("11111111100", Instruction.PRNL);
        map.put("11111111110", Instruction.DUMP);
        map.put("11111111111", Instruction.HALT);
        map.put("null", Instruction.INVALID);
    }

    /**
     * given a binary instruction, look through the opfield to determine which instruction it is
     * @param key
     * @return
     */
    public Enum<Instruction> getValue(String key){
        for(int i = 6; i <= 11;i++){
            if(map.containsKey(key.substring(0,i))){
                return map.get(key.substring(0,i));
            }
        }
        return map.get("null");
    }
}
