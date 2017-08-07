package ca.glasspelican.worldhistory.util;

import net.minecraft.util.math.BlockPos;

public class Investigation {
    BlockPos start;
    BlockPos end;

    Investigation(BlockPos blockPos) {
        this.start = blockPos;
    }

    void setEnd(BlockPos blockPos) {
        this.end = blockPos;
    }

    void getUsers() {

    }
}
