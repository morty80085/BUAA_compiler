package llvm_ir;

public class Use {
    private User user;
    private Value value;

    public Use(User user, Value value) {
        this.user = user;
        this.value = value;
    }

    public User getUser() {
        return this.user;
    }

    public Value getValue() {
        return this.value;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
