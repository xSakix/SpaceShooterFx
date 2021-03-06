package space.shooter.com.collisions;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
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

        showDamage(bullet.getComponent(DamageComponent.class),enemy.getPosition());

        if(health.getValue() <= 0){
            StationEnemy stationEnemy = enemy.getComponent(StationEnemy.class);
            stationEnemy.die();
            if(stationEnemy.getParent() != null) {
                stationEnemy.getParent().removeStationEnemy(enemy);
            }

            inc("score",+50);
        }
    }

    private void showDamage(DamageComponent dmgComponent, Point2D position) {
        Text text = new Text(String.valueOf(dmgComponent.getDmg()));
        text.setFill(dmgComponent.wasCritical() ? Color.RED : Color.WHITE);
        text.setFont(FXGL.getUIFactory().newFont(dmgComponent.wasCritical() ? 24 : 22));
        text.setStroke(dmgComponent.wasCritical() ? Color.RED : Color.WHITE);

        var dmgTextEntity = FXGL.entityBuilder()
                .at(position.getX() + random(-25, 0),position.getY())
                .view(text)
                .with(new ExpireCleanComponent(Duration.millis(300)))
                .build();

        getGameWorld().addEntity(dmgTextEntity);
    }
}
