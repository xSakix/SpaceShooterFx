package space.shooter.com.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Station extends Component {

    private LocalTimer attackTimer;
    private Duration nextAttack = Duration.seconds(2);
    private double dMove = FXGLMath.random(1,10000);

    @Override
    public void onAdded() {
        super.onAdded();
        attackTimer = newLocalTimer();
        attackTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        dMove += tpf;


        if(attackTimer.elapsed(nextAttack)){
            if(FXGLMath.randomBoolean(0.8f)){
                shoot();
            }
            nextAttack = Duration.seconds(5*FXGLMath.random(0.,1.));
            attackTimer.capture();
        }

        entity.translateX(tpf*600);
        entity.translateY((FXGLMath.noise1D(dMove) - 0.5) * tpf * FXGLMath.random(150, 350));
    }

    private void shoot() {
        int maxBullets = FXGLMath.random(1, 5);
        for (int i = 0; i < maxBullets; i++) {
            spawn("StationBullet", entity.getPosition().subtract(15, FXGLMath.random(-200,200)));
        }

        if(byType(ComponentTypes.STATION_ENEMY).size() < 20*byType(ComponentTypes.STATION).size()) {

            int max = FXGLMath.random(1, 20);
            for (int i = 0; i < max; i++) {
                spawn("StationEnemy", new SpawnData(getEntity().getPosition().getX() + FXGLMath.random(-200, -20), getEntity().getPosition().getY() + FXGLMath.random(-200, 200)));
            }
        }

    }

    public void die(){
        spawn("BigExplosion", entity.getCenter());
        entity.removeFromWorld();
    }

}
