package space.shooter.com;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.LocalTimer;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Orientation;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import space.shooter.com.collisions.*;
import space.shooter.com.components.ComponentTypes;
import space.shooter.com.components.PlayerComponent;
import space.shooter.com.components.Station;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SpaceShooter extends GameApplication {

    private PlayerComponent playerComponent;
    private LocalTimer stationTimer;
    private LocalTimer bigStationTimer;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Space shooter FX");
        settings.setVersion("0.2");
        settings.setWidth(1680);
        settings.setHeight(1050);
        settings.setFullScreenFromStart(true);
        settings.setFullScreenAllowed(true);
        //config
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void onPreInit() {
        super.onPreInit();
        //play background music
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        super.initGameVars(vars);
//        vars.put("bullets",999);
//        vars.put("laser",50);
//        vars.put("rockets",10);
//        vars.put("heat",0);
//        vars.put("overheating",false);
        vars.put("player_dmg_min",100);
        vars.put("player_dmg_max",200);
        vars.put("player_dmg_crit",0.25);
        vars.put("num_stations",1);
        vars.put("min_num_enemies",3);
        vars.put("max_num_enemies",10);
        vars.put("score",0);
    }

    @Override
    protected void initGame() {
        super.initGame();
        getGameWorld().addEntityFactory(new SpaceShooterFactory());
        Texture background = getAssetLoader().loadTexture("background-black.png",getAppWidth(),getAppHeight());
        entityBuilder()
                .view(new ScrollingBackgroundView(background.superTexture(background, HorizontalDirection.RIGHT), Orientation.HORIZONTAL))
                .buildAndAttach();

        Entity player = getGameWorld().spawn("Player", 180, getAppHeight()/2);
        playerComponent = player.getComponent(PlayerComponent.class);

        getGameScene().getViewport().setBounds(0,0, Integer.MAX_VALUE,getAppHeight());
        getGameScene().getViewport().bindToEntity(player, 180, getAppHeight()/2);

        runOnce(()->{
            stationTimer = newLocalTimer();
            stationTimer.capture();
            bigStationTimer = newLocalTimer();
            bigStationTimer.capture();
        },Duration.seconds(1));

        run(this::spawnEnemies, Duration.seconds(1));
        run(this::spawnBonus, Duration.seconds(10));
    }

    private void spawnBonus() {
        if(byType(ComponentTypes.BONUS).size() >= 3){
            return;
        }
        int max = FXGLMath.random(1,3);
        for(int i = 0;i < max;i++) {
            spawn("Bonus", new SpawnData(playerComponent.getEntity().getPosition().getX() + FXGLMath.random(1400, 1600), getAppHeight() / 2 + FXGLMath.random(-400, 400)));
        }
    }


    @Override
    protected void initInput() {
        super.initInput();

//        getInput().addAction(new UserAction("shoot") {
//            @Override
//            protected void onActionBegin() {
//                super.onActionBegin();
//                playerComponent.shoot();
//            }
//
//            @Override
//            protected void onActionEnd() {
//                super.onActionEnd();
//                playerComponent.removeBeam();
//            }
//        },KeyCode.SPACE);

        onKey(KeyCode.SPACE,()->playerComponent.shoot());

        onKey(KeyCode.UP, ()->{
            playerComponent.up();
        });
        onKey(KeyCode.DOWN, ()->{
            playerComponent.down();
        });
//        onKey(KeyCode.C, ()->playerComponent.changeWeapon());
    }

    @Override
    protected void initPhysics() {
        super.initPhysics();
        getPhysicsWorld().addCollisionHandler(new PlayerBulletHandler());
        getPhysicsWorld().addCollisionHandler(new BulletEnemyHandler());
        getPhysicsWorld().addCollisionHandler(new BulletStationEnemyHandler());
        getPhysicsWorld().addCollisionHandler(new BulletStationHandler());
        getPhysicsWorld().addCollisionHandler(new BulletBonusHandler());
    }

    @Override
    protected void initUI() {
        super.initUI();

        getGameScene().setUIMouseTransparent(true);

        ProgressBar hp = new ProgressBar(false);
        hp.setHeight(10.);
        hp.setWidth(getAppWidth()-20);
        hp.setLabelVisible(false);
        hp.setFill(Color.RED);
        hp.setBackgroundFill(Color.BLACK);
        hp.setTraceFill(new Color(1.,0.8,.7961,1.));
        hp.setMaxValue(playerComponent.getEntity().getComponent(HealthIntComponent.class).getMaxValue());
        hp.currentValueProperty().bind(playerComponent.getEntity().getComponent(HealthIntComponent.class).valueProperty());
        hp.setTranslateX(20);
        hp.setTranslateY(10);
        getGameScene().addUINode(hp);



        Text txtScore = getUIFactory().newText("", Color.WHITE,22);
        txtScore.setTranslateX(20);
        txtScore.setTranslateY(40);
        txtScore.textProperty().bind(getip("score").asString("Score: %d"));

        getGameScene().addUINode(txtScore);
    }

    private void spawnEnemies(){

        if(!byType(ComponentTypes.STATION).isEmpty()){
            return;
        }
        if(bigStationTimer != null && bigStationTimer.elapsed(Duration.minutes(5))){
            spawn("BigStation", new SpawnData(playerComponent.getEntity().getPosition().getX() + FXGLMath.random(800, 1000), getAppHeight() / 2 ));
            bigStationTimer.capture();
            stationTimer.capture();
        }

        if(stationTimer != null && stationTimer.elapsed(Duration.minutes(1))){
            for(int i = 0;i < geti("num_stations");i++) {
                spawn("Station", new SpawnData(playerComponent.getEntity().getPosition().getX() + FXGLMath.random(800, 1200), getAppHeight() / 2 + FXGLMath.random(-500, 500)));
            }
            stationTimer.capture();
            inc("num_stations",1);
            inc("min_num_enemies",2);
            inc("max_num_enemies",2);
        }

        if(!byType(ComponentTypes.ENEMY).isEmpty()){
            return;
        }

        int max = FXGLMath.random(geti("min_num_enemies"), geti("max_num_enemies"));
        for (int i = 0; i < max; i++) {
            spawn("Enemy", new SpawnData(playerComponent.getEntity().getPosition().getX() + FXGLMath.random(1000, 1400), getAppHeight() / 2 + FXGLMath.random(-400, 400)));
        }
    }
}
