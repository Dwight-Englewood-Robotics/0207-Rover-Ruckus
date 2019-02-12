package wen.control.function.Quintic;

import static wen.control.function.Quintic.QuinticHermiteBasis.h0;
import static wen.control.function.Quintic.QuinticHermiteBasis.h1;
import static wen.control.function.Quintic.QuinticHermiteBasis.h2;
import static wen.control.function.Quintic.QuinticHermiteBasis.h3;
import static wen.control.function.Quintic.QuinticHermiteBasis.h4;
import static wen.control.function.Quintic.QuinticHermiteBasis.h5;


public class QuinticHermiteSpline extends ParamatricFunction {

    public Coordinate p0;
    public Coordinate v0;
    public Coordinate a0;
    public Coordinate p1;
    public Coordinate v1;
    public Coordinate a1;

    public QuinticHermiteSpline(Coordinate p0, Coordinate v0, Coordinate a0, Coordinate p1, Coordinate v1, Coordinate a1) {
        this.p0 = p0;
        this.v0 = v0;
        this.a0 = a0;
        this.p1 = p1;
        this.v1 = v1;
        this.a1 = a1;
    }

    @Override
    public Coordinate eval(double t) {
        return (new Coordinate(h0.eval(t).y * p0.x + h1.eval(t).y * v0.x + h2.eval(t).y * a0.x + h3.eval(t).y * a1.x + h4.eval(t).y * v1.x + h5.eval(t).y * p1.x, h0.eval(t).y * p0.y + h1.eval(t).y * v0.y + h2.eval(t).y * a0.y + h3.eval(t).y * a1.y + h4.eval(t).y * v1.y + h5.eval(t).y * p1.y));
    }
}
