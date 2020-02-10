package space.shooter.com.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;

public class DamageComponent extends Component {

    private int minDmg = 0;
    private int maxDmg = Integer.MAX_VALUE;
    private float critical = 0.0f;

    public DamageComponent(int minDmg, int maxDmg, float critical) {
        this.minDmg = minDmg;
        this.maxDmg = maxDmg;
        this.critical = critical;
    }

    public int computeDmg(){
        int dmg = FXGLMath.random(minDmg,maxDmg);

        if(FXGLMath.random(0.,1.) < critical){
            dmg = (int)Math.round(dmg*1.5);
        }

        return dmg;
    }
}
