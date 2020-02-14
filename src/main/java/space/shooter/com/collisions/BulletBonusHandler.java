package space.shooter.com.collisions;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import space.shooter.com.components.ComponentTypes;
import space.shooter.com.components.DamageComponent;
import space.shooter.com.components.PlayerComponent;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BulletBonusHandler extends CollisionHandler {
    /**
     * The order of types determines the order of entities in callbacks.
     */
    public BulletBonusHandler() {
        super(ComponentTypes.BULLET, ComponentTypes.BONUS);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity bonus) {
        super.onCollisionBegin(bullet,bonus);
        bullet.removeFromWorld();
        bonus.removeFromWorld();

        spawn("BulletExplosion", bullet.getCenter());

        List<Entity> entities = byType(ComponentTypes.PLAYER);
        if(entities.size() == 1){
            Entity playerEntity = entities.get(0);
            PlayerComponent component = playerEntity.getComponent(PlayerComponent.class);

            var p = FXGLMath.random(0.,1.);
            if(p < 0.05){
                FXGL.inc("player_dmg_crit",0.01f);
            }else if(p >= 0.05 && p <= 0.2){
                FXGL.inc("player_dmg_min",10);
                FXGL.inc("player_dmg_max",10);
            }else{
                HealthIntComponent health = playerEntity.getComponent(HealthIntComponent.class);
                health.setValue(health.getValue()+FXGLMath.random(100,200));
            }

        }

    }
}
