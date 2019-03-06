package org.firstinspires.ftc.teamcode.hardware.scoring.dumper;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.State;
import org.firstinspires.ftc.teamcode.hardware.Subsystem;

public class DumperPivot implements Subsystem {
    private final double pivotScorePos = 1;
    private final double pivotNotScorePos = -1;
    private final double distanceMaxDumper = 50; // in cm, guessed value
    private final double distanceCargoHold = 50; // changes based on minerals in lander - might get sketch? have to test values and stuff, also the fact that mienrals prolly wont stack that high
    private final double distanceLander = 10;

    private final double speedUp = 1;
    private final double speedDown = -.5;
    private final double speedStop = 0;

    private Servo dumperPivot;
    private DcMotor spool;

    private DigitalChannel magSwitchDumper;
    //private Rev2mDistanceSensor distanceSensorDumper;
    //private Rev2mDistanceSensor distanceSensorLander; // mounted underneath dumper

    private DumperState state;

    public DumperPivot() {
    }

    @Override
    public void init(HardwareMap hwMap) {
        dumperPivot = hwMap.get(Servo.class, "dumperPivot");
        //backWall = hwMap.get(Servo.class, "wall");
        spool = hwMap.get(DcMotor.class, "spool");
        spool.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        magSwitchDumper = hwMap.get(DigitalChannel.class, "magSwitchDumper");
        //distanceSensor = hwMap.get(Rev2mDistanceSensor.class, "dumpdist");
        this.pivotScore();
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {
        spool.setPower(speedStop);
    }

    @Override
    public State getState() {
        return this.state;
    }

    public void pivotScore() {
        this.dumperPivot.setPosition(pivotScorePos);

    }

    public void pivotNotScore() {
        this.dumperPivot.setPosition(pivotNotScorePos);
    }

    public void upNotSafe(double d) {
        spool.setPower(d);
    }

    /*public void upSafe() {
        if (this.distanceSensorDumper.getDistance(DistanceUnit.CM) > this.distanceMaxDumper) {
            spool.setPower(speedStop);
        } else {
            spool.setPower(speedUp);
        }
    }*/

    public void downNotSafe() {
        spool.setPower(speedDown);
    }

    public void downSafe(double d) {
        if (magSwitchDumper.getState()) {
            spool.setPower(d);
        } else {
            spool.setPower(speedStop);
        }
    }

    public void idle() {
        spool.setPower(speedStop);
    }

    /*public LineupState getLineupState() {
        double distance = this.distanceSensorLander.getDistance(DistanceUnit.CM);
        if (distance > distanceCargoHold) {
            return LineupState.NONE;
        } else if (distance > distanceLander) {
            return LineupState.OVERCARGOHOLD;
        } else {
            return LineupState.OVERLANDER;
        }

    }*/

    public enum LineupState {
        OVERLANDER, OVERCARGOHOLD, NONE;
    }


    private enum DumperState implements State {
        DUMPING("Dumping"),
        HOLDING("Holding");

        private String str;

        DumperState(String str) {
            this.str = str;
        }

        @Override
        public String getStateVal() {
            return str;
        }
    }
}
