package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.bots.Bot;


@TeleOp(name = "EncoderTest", group = "Testing")
@Disabled
public class EncoderTest extends OpMode {
    Bot boot = new Bot(false);

    @Override
    public void init() {
        boot.init(hardwareMap);

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        boot.start();
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            boot.lift.lift();
        } else if (gamepad1.dpad_down) {
            boot.lift.drop();
        } else {
            boot.lift.stop();
        }
        boot.driveTrain.fl.setPower(gamepad1.left_stick_y);
        boot.driveTrain.fr.setPower(gamepad1.right_stick_y);
        boot.driveTrain.bl.setPower(gamepad1.left_stick_x);
        boot.driveTrain.br.setPower(gamepad1.right_stick_x);

        telemetry.addData("bl encoder", boot.driveTrain.bl.getCurrentPosition());
        telemetry.addData("br encoder", boot.driveTrain.br.getCurrentPosition());
        telemetry.addData("fl encoder", boot.driveTrain.fl.getCurrentPosition());
        telemetry.addData("fr encoder", boot.driveTrain.fr.getCurrentPosition());
        telemetry.addData("lift encoder", boot.lift.getTicks());
    }

    @Override
    public void stop() {
        boot.stop();

    }
}
