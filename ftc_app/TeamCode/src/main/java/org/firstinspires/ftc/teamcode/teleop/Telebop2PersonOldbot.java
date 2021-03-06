package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.bots.Bot;

@Disabled
@TeleOp(name = "Telebop2PersonOldbot", group = "Teleop")
public class Telebop2PersonOldbot extends OpMode {

    Bot robot = new Bot(false);
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    ElapsedTime slowTimer = new ElapsedTime();
    boolean slow = false;
    ElapsedTime reverseTimer = new ElapsedTime();
    boolean reverse = false;
    ElapsedTime rakeTimer = new ElapsedTime();
    boolean rakeDown = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void init_loop() {
            telemetry.addLine("Ready.");
            telemetry.update();
    }

    @Override
    public void start() {
        robot.start();
        slowTimer.reset();
        reverseTimer.reset();
    }

    @Override
    public void loop() {
        if (gamepad1.start && slowTimer.milliseconds() >= 750) {
            slow = !slow;
            slowTimer.reset();
        }

        if (gamepad1.right_bumper && reverseTimer.milliseconds() >= 750) {
            reverse = !reverse;
            reverseTimer.reset();
        }

        if (gamepad1.x) {
            rakeDown = false;
        } else if (gamepad1.y) {
            rakeDown = true;
            rakeTimer.reset();
        }

        if (rakeDown && rakeTimer.milliseconds() < 250) {
            robot.rake.downfirst();
        } else if (rakeDown) {
            if (gamepad1.b) {
                robot.rake.downButNotAsMuch();
            } else {
                robot.rake.down();
            }
        } else {
            robot.rake.up();
        }

        robot.driveTrain.tankControl(gamepad1, slow, reverse);

        if (gamepad1.dpad_up) robot.lift.drop();
        else if (gamepad1.dpad_down) robot.lift.lift();
        else robot.lift.stop();

        if (gamepad2.b) robot.markerDeploy.drop();
        else robot.markerDeploy.raise();

        if (gamepad2.left_stick_y < -0.5) robot.dumper.upWithFailsafe();
        else if (gamepad2.x) robot.dumper.up();
        else if (gamepad2.left_stick_y > .5) robot.dumper.down();
        else robot.dumper.stop();

        if (gamepad1.a) robot.dumper.open();
        else robot.dumper.close();

        if (gamepad2.right_trigger > .5) robot.intake.intake();
        else if (gamepad2.left_trigger > .5) robot.intake.outtake();
        else robot.intake.stop();

        telemetry.addData("Slow?", slow);
        telemetry.addData("Reverse?", reverse);
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stop();
    }
}