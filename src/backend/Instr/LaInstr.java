package backend.Instr;

import backend.Register;

public class LaInstr extends MipsInstr{
    private Register rt;
    private String name;

    public LaInstr(Register rt, String name) {
        this.rt = rt;
        this.name = name;
    }

    @Override
    public String toString() {
        return "\tla " + rt + ", " + name + "\n";
    }
}
