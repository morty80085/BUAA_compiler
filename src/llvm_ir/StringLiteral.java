package llvm_ir;

import backend.Instr.AsciizInstr;
import backend.MipsBuilder;
import llvm_ir.type.ArrayType;
import llvm_ir.type.BaseType;
import llvm_ir.type.PointerType;

public class StringLiteral extends Value{
    private String content;

    public StringLiteral(String name, String content) {
        super(new PointerType(new ArrayType(content.length() + 1, BaseType.INT8)), name);
        this.content = content;
        if(IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addStringLiteral(this);
        }
    }

    @Override
    public String toString() {
        String escapedContent = content.replace("\n", "\\0A");
        return name + " = private unnamed_addr constant " +
                ((PointerType)type).getTargetType() + " c\"" + escapedContent + "\\00\", align 1";
    }

    @Override
    public void genMips() {
        AsciizInstr asciizInstr = new AsciizInstr(name.substring(1), content);
        MipsBuilder.getInstance().addDate(asciizInstr);
    }
}
