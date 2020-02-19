package space.shooter.com.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Station extends Component {

    private LocalTimer attackTimer;
    private LocalTimer spawnTimer;
    private Duration nextAttack = Duration.seconds(5);
    private Duration spawnAttack = Duration.seconds(1);
    private double dMove = FXGLMath.random(1,10000);
    private List<Entity> stationEnemies = new ArrayList();
    private static final int MAX_STATION_ENEMIES = 30;

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
        if(this.stationEnemies.size() < MAX_STATION_ENEMIES) {
            for (int i = 0; i < MAX_STATION_ENEMIES -this.stationEnemies.size(); i++) {
                var entity = spawn("StationEnemy", new SpawnData(getEntity().getPosition().getX() + FXGLMath.random(-200, -20), getEntity().getPosition().getY() + FXGLMath.random(-200, 200)));
                entity.getComponent(StationEnemy.class).setParent(this);
                this.stationEnemies.add(entity);
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

    public void removeStationEnemy(Entity stationEnemy){
        this.stationEnemies.remove(stationEnemy);
    }

    public void killAllStationEnemies(){
        this.stationEnemies.stream().forEach(e -> e.getComponent(StationEnemy.class).die());
    }
}
