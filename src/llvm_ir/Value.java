package llvm_ir;

import llvm_ir.type.LLvmType;

import java.util.ArrayList;
import java.util.Iterator;

public class Value {
    protected LLvmType type;
    protected String name;
    protected ArrayList<Use> useList;

    public Value(LLvmType type, String name) {
        this.type = type;
        this.name = name;
        this.useList = new ArrayList<>();
    }

    public void addUse(User user) {
        //添加新的关系
        Use use = new Use(user, this);
        useList.add(use);
    }

    public void deleteUser(User user) {
        //删除某一个User的引用
        Iterator<Use> iterator = useList.iterator();
        while(iterator.hasNext()) {
            Use use = iterator.next();
            if(use.getUser() == user) {
                iterator.remove();
                break;
            }
        }
    }

    public LLvmType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Use> getUseList() {
        return this.useList;
    }

    public void genMips() {

    }
}
