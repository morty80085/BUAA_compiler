package backend.Instr;

import backend.Register;

//mfhi rd;
//mflo rd;
//mthi rd;
//mtlo rd;
public class HiLoInstr extends MipsInstr{
    public enum Op{
        mfhi,
        mflo,
        mthi,
        mtlo;
    }

    private Register rd;
    private Op hiLoType;

    public HiLoInstr(Register rd, Op hiLoType) {
        this.rd = rd;
        this.hiLoType = hiLoType;
    }

    @Override
    public String toString() {
        return "\t" + hiLoType + " " + rd + "\n";
    }

}
