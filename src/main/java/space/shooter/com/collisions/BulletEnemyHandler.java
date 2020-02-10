package space.shooter.com.collisions;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import space.shooter.com.components.ComponentTypes;
import space.shooter.com.components.DamageComponent;
import space.shooter.com.components.Enemy;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BulletEnemyHandler extends CollisionHandler {
    public BulletEnemyHandler() {
        super(ComponentTypes.BULLET, ComponentTypes.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        super.onCollisionBegin(bullet, enemy);
        bullet.removeFromWorld();

        spawn("BulletExplosion",bullet.getCenter());

        HealthIntComponent health = enemy.getComponent(HealthIntComponent.class);
        health.setValue(health.getValue()-bullet.getComponent(DamageComponent.class).computeDmg());

        if(health.getValue() <= 0){
            enemy.getComponent(Enemy.class).die();
            inc("score",+100);
        }
    }
}
