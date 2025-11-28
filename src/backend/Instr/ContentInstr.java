package backend.Instr;

public class ContentInstr extends MipsInstr{
    private String string;

    public ContentInstr(String content) {
        this.string = content;
    }

    @Override
    public String toString() {
        return string;
    }
}
