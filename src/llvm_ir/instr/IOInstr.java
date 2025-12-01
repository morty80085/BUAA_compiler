package llvm_ir.instr;

import backend.Instr.*;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.Constant;
import llvm_ir.Instr;
import llvm_ir.StringLiteral;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLvmType;
import llvm_ir.type.PointerType;

public class IOInstr extends Instr {

    public IOInstr(LLvmType lLvmType, String name, InstrType instrType) {
        super(lLvmType, name, instrType);
    }

    //三个库函数：getInt(), putInt(), putStr()
    public static class GetInt extends IOInstr{
        public GetInt(String name) {
            super(BaseType.INT32, name, InstrType.IO);
        }

        public static String getDeclare() {
            return "declare i32 @getint()";
        }

        @Override
        public String toString() {
            return name + "= call i32 @getint()";
        }

    }

    public static class PutInt extends IOInstr{
        public PutInt(String name, Value target) {
            super(BaseType.VOID, name, InstrType.IO);
            addOperands(target);
        }

        public Value getTarget() {
            return operands.get(0);
        }

        public static String getDeclare() {
            return "declare void @putint(i32)";
        }

        @Override
        public String toString() {
            Value target = getTarget();
            return "call void @putint(i32 " + target.getName() + ")";
        }

        @Override
        public void genMips() {
            Value target = getTarget();
            if(target instanceof Constant) {
                LiInstr liInstr = new LiInstr(Register.a0, ((Constant) target).getValue());
                MipsBuilder.getInstance().addText(liInstr);
            } else if(MipsBuilder.getInstance().getRegisterOfValue(target) != null) {
                MoveInstr moveInstr = new MoveInstr(Register.a0, MipsBuilder.getInstance().getRegisterOfValue(target));
                MipsBuilder.getInstance().addText(moveInstr);
            } else {
                int offset = MipsBuilder.getInstance().getOffsetOfValue(target);
                MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a0, Register.sp, offset);
                MipsBuilder.getInstance().addText(mipsLoadInstr);
            }
            LiInstr liInstr = new LiInstr(Register.v0, 1);
            SystemInstr systemInstr = new SystemInstr();
            MipsBuilder.getInstance().addText(liInstr);
            MipsBuilder.getInstance().addText(systemInstr);
        }
    }

    public static class PutStr extends IOInstr{
        private StringLiteral stringLiteral;

        public PutStr(String name, StringLiteral stringLiteral) {
            super(BaseType.VOID, name, InstrType.IO);
            this.stringLiteral = stringLiteral;
        }

        public static String getDeclare() {
            return "declare void @putstr(i8*)";
        }

        @Override
        public String toString() {
            PointerType pointerType = (PointerType) stringLiteral.getType();
            return "call void @putstr(i8* getelementptr inbounds (" +
                    pointerType.getTargetType() + ", " +
                    pointerType + " " + stringLiteral.getName() + ", i64 0, i64 0))";
        }

        @Override
        public void genMips() {
            LiInstr liInstr = new LiInstr(Register.v0, 4);
            LaInstr laInstr = new LaInstr(Register.a0, stringLiteral.getName().substring(1));
            SystemInstr systemInstr = new SystemInstr();
            MipsBuilder.getInstance().addText(liInstr);
            MipsBuilder.getInstance().addText(laInstr);
            MipsBuilder.getInstance().addText(systemInstr);
        }
    }
}
