package space.shooter.com.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BigStation extends Component {

    private LocalTimer attackTimer;
    private LocalTimer spawnTimer;
    private Duration nextAttack = Duration.seconds(3);
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

            nextAttack = Duration.seconds(3*FXGLMath.random(0.,1.));
            attackTimer.capture();
        }

        entity.translateX(tpf*600);
        entity.translateY((FXGLMath.noise1D(dMove) - 0.5) * tpf * FXGLMath.random(150, 350));
    }

    private void spawnShips() {
        int max_num_enemies = geti("max_num_enemies") * 2;

        if(byType(ComponentTypes.ENEMY).size() < max_num_enemies) {

            int max = FXGLMath.random(10, max_num_enemies);
            for (int i = 0; i < max; i++) {
                spawn("Enemy", new SpawnData(getEntity().getPosition().getX() + FXGLMath.random(-200, -20), getEntity().getPosition().getY() + FXGLMath.random(-400, 400)));
            }
        }

        if(byType(ComponentTypes.STATION_ENEMY).size() < max_num_enemies*2) {

            int max = FXGLMath.random(20, max_num_enemies*2);
            for (int i = 0; i < max; i++) {
                spawn("StationEnemy", new SpawnData(getEntity().getPosition().getX() + FXGLMath.random(-200, -20), getEntity().getPosition().getY() + FXGLMath.random(-400, 400)));
            }
        }

    }

    private void shoot() {
        Point2D pos = entity.getPosition().subtract(15, FXGLMath.random(-400, 400));
        if (!byType(ComponentTypes.PLAYER).isEmpty()){
            var playerPos = byType(ComponentTypes.PLAYER).get(0).getCenter();
            pos = new Point2D(entity.getPosition().getX()-15,playerPos.getY());
        }
        spawn("StationBullet", pos);
    }

    public void die(){
        spawn("BigExplosion", entity.getCenter());
        entity.removeFromWorld();
    }

}
