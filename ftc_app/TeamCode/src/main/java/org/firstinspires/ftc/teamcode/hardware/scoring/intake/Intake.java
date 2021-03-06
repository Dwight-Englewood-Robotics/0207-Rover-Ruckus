package org.firstinspires.ftc.teamcode.hardware.scoring.intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.hardware.State;
import org.firstinspires.ftc.teamcode.hardware.Subsystem;

public class Intake implements Subsystem {
    public Intake() {}

    private DcMotor motor;
    private enum intakeState implements State {
        INTAKING("Intaking"),
        OUTTAKING("Outtaking"),
        STOPPED("Stopped");

        private String str;
        intakeState(String str) {this.str = str;}

        @Override
        public String getStateVal() {
            return this.str;
        }
    }
    private intakeState state;

    @Override
    public void init(HardwareMap hwMap) {
        motor = hwMap.get(DcMotor.class, "intake");
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        state = intakeState.STOPPED;
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {
        motor.setPower(0);
        state = intakeState.STOPPED;
    }

    @Override
    public void stop() {
        motor.setPower(0);
        state = intakeState.STOPPED;
    }

    public void intake() {
        motor.setPower(-.7);
        state = intakeState.INTAKING;
    }

    public void outtake() {
        motor.setPower(.7);
        state = intakeState.OUTTAKING;
    }

    @Override
    public State getState() {
        return state;
    }
}
