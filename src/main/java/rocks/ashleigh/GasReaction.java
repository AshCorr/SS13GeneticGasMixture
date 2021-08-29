package rocks.ashleigh;

import lombok.Data;

@Data
public abstract class GasReaction {
    public abstract boolean canReact(GasMixture air);
    public abstract void handle(GasMixture air);
}
