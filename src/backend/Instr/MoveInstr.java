package backend.Instr;

import backend.Register;

public class MoveInstr extends MipsInstr{
    private Register rt;
    private Register rs;

    public MoveInstr(Register rt, Register rs) {
        this.rt = rt;
        this.rs = rs;
    }

    @Override
    public String toString() {
        return "\tmove " + rt + ", " + rs + "\n";
    }
}
