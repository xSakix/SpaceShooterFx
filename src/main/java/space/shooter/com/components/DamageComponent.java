package space.shooter.com.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;

public class DamageComponent extends Component {

    private int minDmg = 0;
    private int maxDmg = Integer.MAX_VALUE;
    private double critical = 0.0;
    private boolean wasCritical;
    private int dmg;

    public DamageComponent(int minDmg, int maxDmg, double critical) {
        this.minDmg = minDmg;
        this.maxDmg = maxDmg;
        this.critical = critical;
    }

    public int computeDmg(){
        this.wasCritical = false;
        dmg = FXGLMath.random(minDmg,maxDmg);

        if(FXGLMath.random(0.,1.) < critical){
            dmg = (int)Math.round(dmg*1.5);
            this.wasCritical = true;
        }

        return dmg;
    }

    public boolean wasCritical() {
        return wasCritical;
    }

    public int getDmg(){
        return this.dmg;
    }
}
