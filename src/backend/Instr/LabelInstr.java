package backend.Instr;

public class LabelInstr extends MipsInstr{
    String Label;

    public LabelInstr(String Label) {
        this.Label = Label;
    }

    @Override
    public String toString() {
        return Label + ":";
    }
}
