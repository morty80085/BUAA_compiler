package backend.Instr;

import backend.Register;

public class LiInstr extends MipsInstr{
    private Register rs;
    private int number;

    public LiInstr(Register rs, int number) {
        this.rs = rs;
        this.number = number;
    }

    @Override
    public String toString() {
        return "\tli " + rs + ", " + number;
    }
}
