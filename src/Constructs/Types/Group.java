package Constructs.Types;

import Constructs.Construct;

import java.util.List;

public class Group implements Construct {
    private List<Construct> constructs;

    public Group(String mark) {
        System.out.println(mark);
    }

    @Override
    public int size() {
        return 0;
    }
}
