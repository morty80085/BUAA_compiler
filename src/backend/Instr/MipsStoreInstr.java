package backend.Instr;

import backend.Register;

//sw rt, offset(base)
public class MipsStoreInstr extends MipsInstr{
    private Register rt;
    private Register base;
    private int offset;

    public MipsStoreInstr(Register rt, Register base, int offset) {
        this.rt = rt;
        this.base = base;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "\tsw " + rt + ", " + offset + "(" + base + ")";
    }
}
