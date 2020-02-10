package space.shooter.com.collisions;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import space.shooter.com.components.ComponentTypes;
import space.shooter.com.components.DamageComponent;
import space.shooter.com.components.Station;
import space.shooter.com.components.StationEnemy;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class BulletStationHandler extends CollisionHandler {
    public BulletStationHandler() {
        super(ComponentTypes.BULLET, ComponentTypes.STATION);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        super.onCollisionBegin(bullet, enemy);
        bullet.removeFromWorld();

        spawn("BulletExplosion", bullet.getCenter());

        HealthIntComponent health = enemy.getComponent(HealthIntComponent.class);
        health.setValue(health.getValue() - bullet.getComponent(DamageComponent.class).computeDmg());

        if (health.getValue() <= 0) {
            enemy.getComponent(Station.class).die();
            byType(ComponentTypes.STATION_ENEMY).stream().forEach(e -> e.getComponent(StationEnemy.class).die());

            inc("score", +1000);
        }
    }
}
