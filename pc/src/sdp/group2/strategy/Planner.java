package sdp.group2.strategy;

import sdp.group2.world.Pitch;

public abstract class Planner {
    protected Pitch pitch;

    public Planner(Pitch pitch) {
        this.pitch = pitch;
    }

    public Pitch getPitch() {
        return pitch;
    }
}
