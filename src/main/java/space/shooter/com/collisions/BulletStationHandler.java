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
import space.shooter.com.components.*;

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

        showDamage(bullet.getComponent(DamageComponent.class),enemy.getPosition());


        if (health.getValue() <= 0) {
            if(enemy.hasComponent(Station.class)) {
                Station station = enemy.getComponent(Station.class);
                station.killAllStationEnemies();
                station.die();
                inc("score", +1000);
            }else if(enemy.hasComponent(BigStation.class)){
                enemy.getComponent(BigStation.class).die();
                byType(ComponentTypes.ENEMY).stream().forEach(e -> e.getComponent(Enemy.class).die());
                byType(ComponentTypes.STATION_ENEMY).stream().forEach(e -> e.getComponent(StationEnemy.class).die());
                inc("score", +5000);
                inc("level",1);
                set("num_stations", 1);
            }

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

//        Animation<?> anim = translate(dmgTextEntity, position.add(random(-25, 0), random(-40, -25)), Duration.seconds(1));
//        anim.getAnimatedValue().
//        anim.getAnimatedValue().(Interpolators.EXPONENTIAL.EASE_OUT());
//        anim.startInPlayState();
//
//        Animation<?> anim2 = fadeOut(dmgTextEntity, Duration.seconds(1.15));
//        anim2.getAnimatedValue().setInterpolator(Interpolators.EXPONENTIAL.EASE_OUT());
//        anim2.setOnFinished(() -> getGameScene().removeGameView(dmgTextEntity, RenderLayer.DEFAULT));
//        anim2.startInPlayState();
    }
}
