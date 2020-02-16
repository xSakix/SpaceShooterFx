package space.shooter.com.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Station extends Component {

    private LocalTimer attackTimer;
    private LocalTimer spawnTimer;
    private Duration nextAttack = Duration.seconds(5);
    private Duration spawnAttack = Duration.seconds(1);
    private double dMove = FXGLMath.random(1,10000);

    @Override
    public void onAdded() {
        super.onAdded();
        attackTimer = newLocalTimer();
        attackTimer.capture();
        spawnTimer = newLocalTimer();
        spawnTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        dMove += tpf;

        if(spawnTimer.elapsed(spawnAttack)){

            spawnShips();
            spawnAttack = Duration.seconds(FXGLMath.random(1.,3.));
            spawnTimer.capture();
        }

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

    private void spawnShips() {
        int numStations = byType(ComponentTypes.STATION).size();
        numStations = numStations == 0? 1: numStations;
        if(byType(ComponentTypes.STATION_ENEMY).size() < 30*numStations) {

            int max = FXGLMath.random(10, 30);
            for (int i = 0; i < max; i++) {
                spawn("StationEnemy", new SpawnData(getEntity().getPosition().getX() + FXGLMath.random(-200, -20), getEntity().getPosition().getY() + FXGLMath.random(-200, 200)));
            }
        }
    }

    private void shoot() {
        int maxBullets = FXGLMath.random(1, 5);
        for (int i = 0; i < maxBullets; i++) {
            spawn("StationBullet", entity.getPosition().subtract(15, FXGLMath.random(-200,200)));
        }
    }

    public void die(){
        spawn("BigExplosion", entity.getCenter());
        entity.removeFromWorld();
    }

}
