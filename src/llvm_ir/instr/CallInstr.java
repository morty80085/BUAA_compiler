package llvm_ir.instr;

import backend.Instr.*;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.*;
import llvm_ir.type.OtherType;

import java.util.ArrayList;
import java.util.List;

public class CallInstr extends Instr {
    public CallInstr(String name, Function function, ArrayList<Value> params) {
        super(function.getRetType(), name, InstrType.CALL);
        addOperands(function);
        for(Value param : params) {
            addOperands(param);
        }
    }

    public Function getFunction() {
        return (Function) operands.get(0);
    }

    public List<Value> getParams() {
        return operands.subList(1, operands.size());
    }

    @Override
    public String toString() {
        Function function = getFunction();
        List<Value> params = getParams();
        ArrayList<String> paramsInfo = new ArrayList<>();
        for(Value param: params) {
            paramsInfo.add(param.getType() + " " + param.getName());
        }
        if(type.isVoid()) {
            return "call void " + function.getName() + "(" + String.join(", ", paramsInfo) + ")";
        } else {
            return name + " = call i32 " + function.getName() + "(" + String.join(", ", paramsInfo) + ")";
        }
    }

    @Override
    public void genMips() {
        Function function = getFunction();
        String name = function.getName();
        System.out.println(name);
        if(name.equals("@getint")) {
            LiInstr liInstr = new LiInstr(Register.v0, 5);
            SystemInstr systemInstr = new SystemInstr();
            MipsBuilder.getInstance().addText(liInstr);
            MipsBuilder.getInstance().addText(systemInstr);
            if(MipsBuilder.getInstance().getRegisterOfValue(this) != null) {
                MoveInstr moveInstr = new MoveInstr(MipsBuilder.getInstance().getRegisterOfValue(this), Register.v0);
                MipsBuilder.getInstance().addText(moveInstr);
            } else {
                MipsBuilder.getInstance().subCurrentOffset(4);
                int offset = MipsBuilder.getInstance().getCurrentOffset();
                MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(Register.v0, Register.sp, offset);
                MipsBuilder.getInstance().addText(mipsStoreInstr);
                MipsBuilder.getInstance().putOffset(this, offset);
            }
        } else {
            List<Value> params = getParams();
            ArrayList<Register> registers = MipsBuilder.getInstance().getAllocRegister();
            int curOffset = MipsBuilder.getInstance().getCurrentOffset();
            //先保存所有的寄存器
            int regNum = 0;
            for(Register register: registers) {
                regNum = regNum + 1;
                MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(register, Register.sp, curOffset - 4 * regNum);
                MipsBuilder.getInstance().addText(mipsStoreInstr);
            }
            //保存sp和ra
            MipsStoreInstr saveSp = new MipsStoreInstr(Register.sp, Register.sp, curOffset - 4 * regNum - 4);
            MipsStoreInstr savaRa = new MipsStoreInstr(Register.ra, Register.sp, curOffset - 4 * regNum - 8);

            for(int i = 0; i < params.size(); i++) {
                if(i < 4) {
                    //存在寄存器中
                    if(i == 0) {
                        if(params.get(i) instanceof Constant) {
                            LiInstr liInstr = new LiInstr(Register.a0, Integer.parseInt(params.get(i).getName()));
                            MipsBuilder.getInstance().addText(liInstr);
                        } else if(MipsBuilder.getInstance().getRegisterOfValue(params.get(i)) != null) {
                            Register register = MipsBuilder.getInstance().getRegisterOfValue(params.get(i));
                            //如果当前的实参是函数的形参，则存在$a0-a4中，此时可能已经被覆盖，要从栈上取
                            if(params.get(i) instanceof Param) {
                                MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a0, Register.sp, curOffset - (registers.indexOf(register) + 1) * 4);
                                MipsBuilder.getInstance().addText(mipsLoadInstr);
                            } else {
                                MoveInstr moveInstr = new MoveInstr(Register.a0, register);
                                MipsBuilder.getInstance().addText(moveInstr);
                            }
                        } else {
                            MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a0, Register.sp, MipsBuilder.getInstance().getOffsetOfValue(params.get(i)));
                            MipsBuilder.getInstance().addText(mipsLoadInstr);
                        }
                    } else if(i == 1) {
                        if(params.get(i) instanceof Constant) {
                            LiInstr liInstr = new LiInstr(Register.a1, Integer.parseInt(params.get(i).getName()));
                            MipsBuilder.getInstance().addText(liInstr);
                        } else if(MipsBuilder.getInstance().getRegisterOfValue(params.get(i)) != null) {
                            Register register = MipsBuilder.getInstance().getRegisterOfValue(params.get(i));
                            //如果当前的实参是函数的形参，则存在$a0-a4中，此时可能已经被覆盖，要从栈上取
                            if(params.get(i) instanceof Param) {
                                MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a1, Register.sp, curOffset - (registers.indexOf(register) + 1) * 4);
                                MipsBuilder.getInstance().addText(mipsLoadInstr);
                            } else {
                                MoveInstr moveInstr = new MoveInstr(Register.a1, register);
                                MipsBuilder.getInstance().addText(moveInstr);
                            }
                        } else {
                            MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a1, Register.sp, MipsBuilder.getInstance().getOffsetOfValue(params.get(i)));
                            MipsBuilder.getInstance().addText(mipsLoadInstr);
                        }
                    } else if(i == 2) {
                        if(params.get(i) instanceof Constant) {
                            LiInstr liInstr = new LiInstr(Register.a2, Integer.parseInt(params.get(i).getName()));
                            MipsBuilder.getInstance().addText(liInstr);
                        } else if(MipsBuilder.getInstance().getRegisterOfValue(params.get(i)) != null) {
                            Register register = MipsBuilder.getInstance().getRegisterOfValue(params.get(i));
                            //如果当前的实参是函数的形参，则存在$a0-a4中，此时可能已经被覆盖，要从栈上取
                            if(params.get(i) instanceof Param) {
                                MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a2, Register.sp, curOffset - (registers.indexOf(register) + 1) * 4);
                                MipsBuilder.getInstance().addText(mipsLoadInstr);
                            } else {
                                MoveInstr moveInstr = new MoveInstr(Register.a2, register);
                                MipsBuilder.getInstance().addText(moveInstr);
                            }
                        } else {
                            MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a2, Register.sp, MipsBuilder.getInstance().getOffsetOfValue(params.get(i)));
                            MipsBuilder.getInstance().addText(mipsLoadInstr);
                        }
                    } else {
                        if(params.get(i) instanceof Constant) {
                            LiInstr liInstr = new LiInstr(Register.a3, Integer.parseInt(params.get(i).getName()));
                            MipsBuilder.getInstance().addText(liInstr);
                        } else if(MipsBuilder.getInstance().getRegisterOfValue(params.get(i)) != null) {
                            Register register = MipsBuilder.getInstance().getRegisterOfValue(params.get(i));
                            //如果当前的实参是函数的形参，则存在$a0-a4中，此时可能已经被覆盖，要从栈上取
                            if(params.get(i) instanceof Param) {
                                MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a3, Register.sp, curOffset - (registers.indexOf(register) + 1) * 4);
                                MipsBuilder.getInstance().addText(mipsLoadInstr);
                            } else {
                                MoveInstr moveInstr = new MoveInstr(Register.a3, register);
                                MipsBuilder.getInstance().addText(moveInstr);
                            }
                        } else {
                            MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(Register.a3, Register.sp, MipsBuilder.getInstance().getOffsetOfValue(params.get(i)));
                            MipsBuilder.getInstance().addText(mipsLoadInstr);
                        }
                    }
                } else {
                    //存在栈上
                    Register targetRegister = Register.t0;

                    if(params.get(i) instanceof Constant) {
                        LiInstr liInstr = new LiInstr(targetRegister, Integer.parseInt(params.get(i).getName()));
                        MipsBuilder.getInstance().addText(liInstr);
                    } else if(MipsBuilder.getInstance().getRegisterOfValue(params.get(i)) != null) {
                        Register register = MipsBuilder.getInstance().getRegisterOfValue(params.get(i));
                        //如果当前的实参是函数的形参，则存在$a0-a4中，此时可能已经被覆盖，要从栈上取
                        if(params.get(i) instanceof Param) {
                            MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(targetRegister, Register.sp, curOffset - (registers.indexOf(register) + 1) * 4);
                            MipsBuilder.getInstance().addText(mipsLoadInstr);
                        } else {
                            targetRegister = register;
                        }
                    } else {
                        MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(targetRegister, Register.sp, MipsBuilder.getInstance().getOffsetOfValue(params.get(i)));
                        MipsBuilder.getInstance().addText(mipsLoadInstr);
                    }
                    MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(targetRegister, Register.sp, curOffset - regNum * 4 - 8 - (i + 1) * 4);
                    MipsBuilder.getInstance().addText(mipsStoreInstr);
                }
            }
            //执行跳转
            RIInstr riInstr = new RIInstr(RIInstr.Op.addi, Register.sp, Register.sp, curOffset - 4 * regNum - 8);
            MipsJumpInstr mipsJumpInstr = new MipsJumpInstr(MipsJumpInstr.Op.jal, function.getName().substring(1));
            MipsBuilder.getInstance().addText(riInstr);
            MipsBuilder.getInstance().addText(mipsJumpInstr);
            //恢复sp,ra
            MipsLoadInstr loadRa = new MipsLoadInstr(Register.ra, Register.sp, 0);
            MipsLoadInstr loadSp = new MipsLoadInstr(Register.sp, Register.sp, 4);
            MipsBuilder.getInstance().addText(loadRa);
            MipsBuilder.getInstance().addText(loadSp);

            // 从栈中恢复之前保存的寄存器值
            regNum = 0;
            for (Register register : registers) {
                regNum = regNum + 1;
                MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(register, Register.sp, curOffset - regNum * 4);
                MipsBuilder.getInstance().addText(mipsLoadInstr);
            }

            //如果有返回值
            if(MipsBuilder.getInstance().getRegisterOfValue(this) != null) {
                MoveInstr moveInstr = new MoveInstr(MipsBuilder.getInstance().getRegisterOfValue(this), Register.v0);
                MipsBuilder.getInstance().addText(moveInstr);
            } else {
                //在栈上
                MipsBuilder.getInstance().subCurrentOffset(4);
                curOffset = MipsBuilder.getInstance().getCurrentOffset();
                MipsStoreInstr saveV0 = new MipsStoreInstr(Register.v0, Register.sp, curOffset);
                MipsBuilder.getInstance().addText(saveV0);
                MipsBuilder.getInstance().putOffset(this, curOffset);
            }
        }
    }
}
