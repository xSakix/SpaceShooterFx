package space.shooter.com.collisions;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import space.shooter.com.components.ComponentTypes;
import space.shooter.com.components.DamageComponent;
import space.shooter.com.components.Enemy;
import space.shooter.com.components.StationEnemy;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BulletStationEnemyHandler extends CollisionHandler {
    public BulletStationEnemyHandler() {
        super(ComponentTypes.BULLET, ComponentTypes.STATION_ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        super.onCollisionBegin(bullet, enemy);
        bullet.removeFromWorld();

        spawn("BulletExplosion",bullet.getCenter());

        HealthIntComponent health = enemy.getComponent(HealthIntComponent.class);
        health.setValue(health.getValue()-bullet.getComponent(DamageComponent.class).computeDmg());

        if(health.getValue() <= 0){
            enemy.getComponent(StationEnemy.class).die();
            inc("score",+50);
        }
    }
}
