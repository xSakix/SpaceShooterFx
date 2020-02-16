package space.shooter.com.collisions;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.EntityBuilder;
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

        showDamage(bullet.getComponent(DamageComponent.class),enemy.getPosition());

        if(health.getValue() <= 0){
            enemy.getComponent(Enemy.class).die();
            inc("score",+100);
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
