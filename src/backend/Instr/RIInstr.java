package backend.Instr;

import backend.Register;

//addi rt, rs, immediate
public class RIInstr extends MipsInstr{
    public enum Op{
        addi,andi,ori,slti;
    }

    private Op op;
    private Register rt;
    private Register rs;
    private int number;

    public RIInstr(Op op, Register rt, Register rs, int number) {
        this.op = op;
        this.rt = rt;
        this.rs = rs;
        this.number = number;
    }

    @Override
    public String toString() {
        return "\t" + op + " " + rt + ", " + rs + ", " + number + "\n";
    }
}
