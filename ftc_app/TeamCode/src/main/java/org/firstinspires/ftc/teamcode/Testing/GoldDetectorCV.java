package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

import static java.lang.Math.sqrt;

@TeleOp(name = "GoldDetectorCV")
public class GoldDetectorCV extends OpMode {

    private GoldDetectorWrapper goldDetector;

    private EditGroup currentEditGroup = EditGroup.NONE;

    @Override
    public void init() {
        goldDetector = new GoldDetectorWrapper();
        // can replace with ActivityViewDisplay.getInstance() for fullscreen
        goldDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        // start the vision system
        goldDetector.enable();
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.RGB;
        } else if (gamepad1.dpad_right) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.HSV;
        } else if (gamepad1.dpad_down) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.BLUR;
        } else if (gamepad1.dpad_left) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.CONTOUR;
        } else if (gamepad1.a) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.HULL;
        } else if (gamepad1.b) {
            goldDetector.imageView = GoldDetectorWrapper.ImageView.DEFAULT;
        }

        telemetry.addData("editGroup", currentEditGroup);



        // update the settings of the vision pipeline

        // get a list of contours from the vision system
        if (gamepad1.right_trigger > .5) {
            List<MatOfPoint> contours = goldDetector.getContours();
            for (int i = 0; i < contours.size(); i++) {
                // get the bounding rectangle of a single contour, we use it to get the x/y center
                // yes there's a mass center using Imgproc.moments but w/e
                Rect boundingRect = Imgproc.boundingRect(contours.get(i));
                telemetry.addData("contour" + Integer.toString(i),
                        String.format(Locale.getDefault(), "(%d, %d)", (boundingRect.x + boundingRect.width) / 2, (boundingRect.y + boundingRect.height) / 2));
            }
        }
    }

    public void stop() {
        // stop the vision system
        goldDetector.disable();
    }

    public enum EditGroup {
        HUE, SAT, VAL, BLUR1, BLUR2, NONE, HSV;
    }
}