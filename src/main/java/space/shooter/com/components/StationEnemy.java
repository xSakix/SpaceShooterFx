package space.shooter.com.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.newLocalTimer;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class StationEnemy extends Component {

    private LocalTimer attackTimer;
    private Duration nextAttack = Duration.seconds(2);
    private double dMove = FXGLMath.random(1,10000);
    private Station parent;

    public Station getParent() {
        return parent;
    }

    public void setParent(Station parent) {
        this.parent = parent;
    }

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
        spawn("SmallEnemyBullet",entity.getPosition().subtract(15,0));
    }

    public void die(){
        spawn("Explosion", entity.getCenter());
        entity.removeFromWorld();
    }
}
