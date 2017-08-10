package ca.glasspelican.worldhistory.lib;

import java.util.List;

public class AsyncQueObject {

    String string;
    List<Object> object;

    AsyncQueObject(String string, List<Object> object) {
        this.string = string;
        this.object = object;
    }

    public String getString() {
        return string;
    }

    public List<Object> getList() {
        return object;
    }

}
