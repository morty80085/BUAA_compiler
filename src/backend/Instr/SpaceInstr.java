package backend.Instr;

public class SpaceInstr extends MipsInstr{
    private String name;
    private Integer size;

    public SpaceInstr(String name,Integer size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String toString() {
        return "\t" + name + ": .space " + size + "\n";
    }
}
