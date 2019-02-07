package org.firstinspires.ftc.teamcode.Hardware.Sensors;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.corningrobotics.enderbots.endercv.OpenCVPipeline;
import org.firstinspires.ftc.teamcode.Hardware.State;
import org.firstinspires.ftc.teamcode.Hardware.Subsystem;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


public class GoldDetectorWrapper extends OpenCVPipeline implements Subsystem {

    public ImageView imageView = ImageView.THRESH;
    public GoldDetectorPipeline grip = new GoldDetectorPipeline();
    // To keep it such that we don't have to instantiate a new Mat every call to processFrame,
    // we declare the Mats up here and reuse them. This is easier on the garbage collector.
    private Mat hsv = new Mat();
    private Mat thresholded = new Mat();
    // this is just here so we can expose it later thru getContours.
    private List<MatOfPoint> contours = new ArrayList<>();

    public synchronized List<MatOfPoint> getContours() {
        return contours;
    }

    // This is called every camera frame.
    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        grip.process(rgba);
        contours = grip.findContoursOutput();
        switch (imageView) {
            case RESIZE:
                return grip.resizeImageOutput();
            case HSV:
                return grip.cvCvtcolorOutput();
            case BLUR:
                return grip.blurOutput();
            case THRESH:
                return grip.hsvThresholdOutput();
            case CONTOUR:
                Mat contourImageOutput = grip.resizeImageOutput().clone();
                Imgproc.drawContours(contourImageOutput, grip.findContoursOutput(), -1, new Scalar(255, 255, 255), 8);
                return contourImageOutput;
            default:
                return rgba;
        }

    }

    public static final int minContourArea = 1000;
    public static final int maxContourArea = 5000;

    private MineralPosition currentState;

    @Override
    public void init(HardwareMap hwMap) {
        this.init(hwMap.appContext, CameraViewDisplay.getInstance());
    }

    @Override
    public void start() {
        this.enable();
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {
        this.disable();

    }

    private void updateState() {
        List<MatOfPoint> contours = this.getContours();

        if (contours.size() == 0) {
            this.currentState = MineralPosition.RIGHT;
        } else if (contours.size() == 1) {
            try {
                Rect boundingRect = Imgproc.boundingRect(contours.get(0));
                //telemetry.addData("x", boundingRect.x + boundingRect.width / 2);
                //telemetry.addData("y", boundingRect.y + boundingRect.height / 2);
                if (boundingRect.x + boundingRect.width / 2 > 160) {
                    this.currentState = MineralPosition.CENTER;
                } else {
                    this.currentState = MineralPosition.LEFT;
                }
            } catch (IndexOutOfBoundsException e) {
            }
        } else {
            this.currentState = MineralPosition.NOTVISIBLE;
        }
    }

    @Override
    public State getState() {
        this.updateState();
        return this.currentState;
    }

    public List<MatOfPoint> filterContours(List<MatOfPoint> contours) {
        for (int i = 0; i < contours.size(); i++) {
            Rect boundingRect = Imgproc.boundingRect(contours.get(0));
            if (!(boundingRect.area() > minContourArea) || !(boundingRect.area() < maxContourArea)) {
                contours.remove(i);
                i--;
            }
        }

        return contours;

    }

    public enum ImageView {
        RESIZE, HSV, BLUR, THRESH, CONTOUR, DEFAULT;
    }
}