package space.shooter.com.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.newLocalTimer;

public class LaserBeam extends Component {

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        entity.translateX(tpf*600);
    }
}
