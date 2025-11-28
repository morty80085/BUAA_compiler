package backend.Instr;

import java.util.ArrayList;

public class WordInstr extends MipsInstr{
    private String name;
    private ArrayList<Integer> value;

    public WordInstr(String name, ArrayList<Integer> value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < value.size(); i++) {
            if(i != value.size() - 1) {
                sb.append(value.get(i)).append(", ");
            } else {
                sb.append(value.get(i));
            }
        }
        return "\t" + name + ": .word " + sb.toString() + "\n";
    }
}
