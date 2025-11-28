package backend.Instr;

import backend.Register;

//lw rt, offset(base)
//R[Rt] <- memory[R[base] + offset]
public class LoadInstr extends MipsInstr{
    private Register rt;
    private Register base;
    private int offset;

    public LoadInstr(Register rt, Register base, int offset) {
        this.rt = rt;
        this.base = base;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "\tlw " + rt + ", " + offset + "(" + base + ")";
    }
}
