package space.shooter.com.collisions;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import space.shooter.com.components.ComponentTypes;
import space.shooter.com.components.DamageComponent;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerBulletHandler extends CollisionHandler {

    public PlayerBulletHandler() {
        super(ComponentTypes.PLAYER, ComponentTypes.ENEMY_BULLET);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity bullet) {
        super.onCollisionBegin(player, bullet);
        bullet.removeFromWorld();

        spawn("BulletExplosion",bullet.getCenter());

        HealthIntComponent health = player.getComponent(HealthIntComponent.class);
        health.setValue(health.getValue()-bullet.getComponent(DamageComponent.class).computeDmg());

        if(health.getValue() <= 0){
            gameOver();
        }
    }

    private void gameOver() {
        getDisplay().showConfirmationBox("Game Over. Play Again?", yes -> {
            if (yes) {
                getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                getGameController().startNewGame();
            }else{
                getGameController().exit();
            }
        });
    }
}
