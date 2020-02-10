package space.shooter.com.components;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import com.almasb.sslogger.Logger;
import javafx.util.Duration;

import java.time.LocalDateTime;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {

    private double speed;
    private LocalTimer shootTimer = newLocalTimer();
    private boolean canShoot = true;
    private WeaponType weapon = WeaponType.NORMAL;
    private Entity beam;

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);

        speed = tpf * 300;
        entity.translateX(tpf * 600);
        if (shootTimer.elapsed(Duration.seconds(0.12))) {
            canShoot = true;
        }
    }

    public void up() {
        entity.translateY(-speed);
    }

    public void down() {
        entity.translateY(speed);
    }

    public void changeWeapon() {
        if(weapon == WeaponType.NORMAL)
            weapon = WeaponType.LASER;
        else
            weapon = WeaponType.NORMAL;
    }

    public void shoot() {
        if (!canShoot) {
            return;
        }

//        if (geti("bullets") == 0) {
//            return;
//        }
//
//        if (!canShoot || getb("overheating")) {
//            return;
//        }

        if (weapon == WeaponType.LASER) {
            this.beam = spawn("Laser", getEntity().getCenter().add(13,-1));
//            inc("laser", -1);
//            inc("heat", +5);
        } else {
            spawn("Bullet", getEntity().getCenter().add(-20, 19));
            spawn("Bullet", getEntity().getCenter().add(-20, - 19));
//            inc("bullets", -1);
//            inc("heat", +2);
        }

        shootTimer.capture();
        canShoot = false;
    }

    public boolean isBeamWeapon(){
        return this.weapon != null && this.weapon == WeaponType.LASER;
    }

    public void removeBeam() {
        if(beam != null){
            beam.removeFromWorld();
        }
    }
}
