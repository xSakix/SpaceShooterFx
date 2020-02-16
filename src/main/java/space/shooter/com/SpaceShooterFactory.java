package space.shooter.com;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import space.shooter.com.components.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SpaceShooterFactory implements EntityFactory {

    @Spawns("Player")
    public Entity newPlayer(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setStartColor(Color.WHITE);
        emitter.setEndColor(Color.YELLOW);
        emitter.setSize(1,3);
        emitter.setNumParticles(25);
        emitter.setEmissionRate(1.);
        emitter.setExpireFunction(i -> Duration.seconds(0.2));
        emitter.setSpawnPointFunction(i -> new Point2D(8, 24+FXGLMath.random(-3,2)));
        emitter.setVelocityFunction(i -> new Point2D(FXGLMath.random(300,400),FXGLMath.random(0.,1.)));
        emitter.setAccelerationFunction(()->Point2D.ZERO);

        return entityBuilder()
                .type(ComponentTypes.PLAYER)
                .from(data)
                .viewWithBBox(texture("pixel_ship_red.png",50,50))
                .rotate(90)
                .with(new CollidableComponent(true), new ParticleComponent(emitter))
                //Healthcomponent
                .with(new PlayerComponent(),new EffectComponent(), new KeepOnScreenComponent().onlyVertically())
                .with(new HealthIntComponent(5000))
                .build();
    }


    @Spawns("Bullet")
    public Entity newBullet(SpawnData data){

        //play sound

        return entityBuilder()
                .type(ComponentTypes.BULLET)
                .from(data)
                .viewWithBBox(new Circle(2, Color.RED))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(new Point2D(1,0),1550), new OffscreenCleanComponent())
//                .with(new DamageComponent(100,200,0.25f))
                .with(new DamageComponent(FXGL.geti("player_dmg_min"),FXGL.geti("player_dmg_max"),FXGL.getd("player_dmg_crit")))
                .build();
    }

    @Spawns("EnemyBullet")
    public Entity newEnemyBullet(SpawnData data){

        var laser = texture("pixel_laser_blue.png",100,30);
        //play sound
        return entityBuilder()
                .type(ComponentTypes.ENEMY_BULLET)
                .from(data)
                .viewWithBBox(laser)
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(new Point2D(-1,0),350), new OffscreenCleanComponent())
                .with(new DamageComponent((int)(FXGL.geti("player_dmg_min")*0.75),
                        (int)(FXGL.geti("player_dmg_max")*0.75),
                        FXGL.getd("player_dmg_crit")*0.5))
                .build();
    }

    @Spawns("StationBullet")
    public Entity newStationBullet(SpawnData data){


        //play sound
        return entityBuilder()
                .type(ComponentTypes.ENEMY_BULLET)
                .from(data)
                //30 frames
                //64x64
                .viewWithBBox(texture("IcePick_64x64.png",64*30,64).toAnimatedTexture(30,Duration.seconds(1)).play())
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(new Point2D(-1,0),350), new OffscreenCleanComponent())
                .with(new DamageComponent((int)(FXGL.geti("player_dmg_min")*5),
                        (int)(FXGL.geti("player_dmg_max")*5),
                        FXGL.getd("player_dmg_crit")*0.25))
                .build();
    }

    @Spawns("SmallEnemyBullet")
    public Entity newSmallEnemyBullet(SpawnData data){


        //play sound
        return entityBuilder()
                .type(ComponentTypes.ENEMY_BULLET)
                .from(data)
                .viewWithBBox(texture("pixel_laser_small_blue.png"))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(new Point2D(-1,0),350), new OffscreenCleanComponent())
                .with(new DamageComponent((int)(FXGL.geti("player_dmg_min")*0.25),
                        (int)(FXGL.geti("player_dmg_max")*0.25),
                        FXGL.getd("player_dmg_crit")*1.5))
                .build();
    }

    @Spawns("Laser")
    public Entity newLaser(SpawnData data){
        //play sound

        return entityBuilder()
                .type(ComponentTypes.BULLET)
                .from(data)
                .viewWithBBox(new Rectangle(getAppWidth(),2,Color.RED))
                .with(new CollidableComponent(true))
                .with(new LaserBeam())
//                .with(new ProjectileComponent(new Point2D(1,0),850), new OffscreenCleanComponent())
                .build();
    }

    @Spawns("Enemy")
    public Entity newEnemy(SpawnData data){

        return entityBuilder()
                .type(ComponentTypes.ENEMY)
                .from(data)
                .viewWithBBox(texture("pixel_ship_blue_small.png",50,50))
                .rotate(-90)
                .with(new CollidableComponent(true))
                .with(new Enemy())
                .with(new KeepOnScreenComponent().onlyVertically())
                .with(new HealthIntComponent(FXGLMath.random(500,1000)))
                .build();
    }

    @Spawns("StationEnemy")
    public Entity newStationEnemy(SpawnData data){

        return entityBuilder()
                .type(ComponentTypes.STATION_ENEMY)
                .from(data)
                .viewWithBBox(texture("pixel_ship_green_small.png",25,25))
                .rotate(-90)
                .with(new CollidableComponent(true))
                .with(new StationEnemy())
                .with(new KeepOnScreenComponent().onlyVertically())
                .with(new HealthIntComponent(100))
                .build();
    }

    @Spawns("Station")
    public Entity newStation(SpawnData data){

        return entityBuilder()
                .type(ComponentTypes.STATION)
                .from(data)
                .viewWithBBox(texture("pixel_station_red.png",200,200))
                .with(new CollidableComponent(true))
                .with(new Station())
                .with(new KeepOnScreenComponent().onlyVertically())
                .with(new HealthIntComponent(FXGLMath.random(5000,10000)))
                .build();
    }

    @Spawns("BigStation")
    public Entity newBigStation(SpawnData data){

        return entityBuilder()
                .type(ComponentTypes.STATION)
                .from(data)
                .viewWithBBox(texture("pixel_station_red.png",400,400))
                .with(new CollidableComponent(true))
                .with(new BigStation())
                .with(new KeepOnScreenComponent().onlyVertically())
                .with(new HealthIntComponent(FXGLMath.random(20000,100000)))
                .build();
    }

    @Spawns("Explosion")
    public Entity newExplosion(SpawnData data){
        return entityBuilder()
                .from(data)
                .at(data.getX()-25,data.getY()-25)
                .view(texture("Explosion_96x96.gif"))
                .with(new ExpireCleanComponent(Duration.seconds(1)))
                .build();
    }

    @Spawns("BigExplosion")
    public Entity newBigExplosion(SpawnData data){
        return entityBuilder()
                .from(data)
                .at(data.getX()-100,data.getY()-100)
                .view(texture("Explosion_96x96.gif",200,200))
                .with(new ExpireCleanComponent(Duration.seconds(1)))
                .build();
    }

    @Spawns("BulletExplosion")
    public Entity newBulletExplosion(SpawnData data) {
        //play("explosion.wav");

        var emitter = ParticleEmitters.newExplosionEmitter(10);
        emitter.setSize(1, 10);
        emitter.setNumParticles(5);

        return entityBuilder()
                .from(data)
//                .view(texture("explosion.png").toAnimatedTexture(16, Duration.seconds(0.66)).play())
                .with(new ExpireCleanComponent(Duration.seconds(0.5)))
                .with(new ParticleComponent(emitter))
                .build();
    }

    @Spawns("Bonus")
    public Entity newBonus(SpawnData data){
        return entityBuilder()
                .type(ComponentTypes.BONUS)
                .from(data)
                .at(data.getX(),data.getY())
                .viewWithBBox(texture("SmallStar_64x64.gif"))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(new Point2D(-1,0),1), new OffscreenCleanComponent())
                .build();
    }

}
