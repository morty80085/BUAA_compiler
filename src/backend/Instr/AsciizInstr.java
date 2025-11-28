package backend.Instr;

public class AsciizInstr extends MipsInstr{
    private String name;
    private String content;

    public AsciizInstr(String name, String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return "\t" + name + ": .asciiz \"" + content.replace("\n", "\\n") + "\"";
    }
}
