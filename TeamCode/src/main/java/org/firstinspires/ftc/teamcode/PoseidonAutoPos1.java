package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@Disabled
@Autonomous(name = "Color Recognition OpMode 720p", group = "Linear Opmode")
public class PoseidonAutoPos1 extends LinearOpMode {

    private OpenCvCamera camera;
    private final Mat processedFrame = new Mat();

    // HSV color ranges for light purple, blue, and red
    private final Scalar lightPurpleLow = new Scalar(125, 50, 50);
    private final Scalar lightPurpleHigh = new Scalar(150, 255, 255);

    private final Scalar blueLow = new Scalar(100, 50, 50);
    private final Scalar blueHigh = new Scalar(130, 255, 255);

    private final Scalar redLow = new Scalar(0, 50, 50);
    private final Scalar redHigh = new Scalar(10, 255, 255);

    // HSV color ranges for light white and dark white
    private final Scalar lightWhiteLow = new Scalar(0, 0, 200);
    private final Scalar lightWhiteHigh = new Scalar(180, 50, 255);

    private final Scalar darkWhiteLow = new Scalar(0, 0, 100);
    private final Scalar darkWhiteHigh = new Scalar(180, 50, 200);

    @Override
    public void runOpMode() {
        // Initialize the camera with 720p resolution
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
                camera.setPipeline(new ColorPipeline());
            }

            @Override
            public void onError(int errorCode) {}
        });

        waitForStart();

        while (opModeIsActive()) {
            telemetry.update();
        }

        // Stop the camera
        camera.stopStreaming();
    }

    class ColorPipeline extends OpenCvPipeline {

        @Override
        public Mat processFrame(Mat input) {
            // Convert the image to HSV
            Imgproc.cvtColor(input, processedFrame, Imgproc.COLOR_RGB2HSV);

            // Detect colors
            Mat lightPurpleMask = new Mat();
            Mat blueMask = new Mat();
            Mat redMask = new Mat();
            Mat lightWhiteMask = new Mat();
            Mat darkWhiteMask = new Mat();

            Core.inRange(processedFrame, lightPurpleLow, lightPurpleHigh, lightPurpleMask);
            Core.inRange(processedFrame, blueLow, blueHigh, blueMask);
            Core.inRange(processedFrame, redLow, redHigh, redMask);
            Core.inRange(processedFrame, lightWhiteLow, lightWhiteHigh, lightWhiteMask);
            Core.inRange(processedFrame, darkWhiteLow, darkWhiteHigh, darkWhiteMask);

            // Analyze masks to determine the position of the colors
            analyzePosition(input, lightPurpleMask, "Light Purple");
            analyzePosition(input, blueMask, "Blue");
            analyzePosition(input, redMask, "Red");
            analyzePosition(input, lightWhiteMask, "Light White");
            analyzePosition(input, darkWhiteMask, "Dark White");

            return input;
        }

        private void analyzePosition(Mat input, Mat mask, String colorName) {
            // Find contours
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            if (contours.size() > 0) {
                // Get the largest contour
                MatOfPoint largestContour = contours.get(0);
                for (MatOfPoint contour : contours) {
                    if (Imgproc.contourArea(contour) > Imgproc.contourArea(largestContour)) {
                        largestContour = contour;
                    }
                }

                // Get the bounding rectangle of the largest contour
                Rect boundingRect = Imgproc.boundingRect(largestContour);

                // Determine the position
                int centerX = boundingRect.x + (boundingRect.width / 2);
                telemetry.addData(colorName + " Center X", centerX);

                // Decide which position to move to
                if (centerX < input.width() / 3) {
                    telemetry.addData(colorName + " Position", "Left");
                    // Add code to move the robot to the left position
                } else if (centerX < 2 * input.width() / 3) {
                    telemetry.addData(colorName + " Position", "Center");
                    // Add code to move the robot to the center position
                } else {
                    telemetry.addData(colorName + " Position", "Right");
                    // Add code to move the robot to the right position
                }
            }
        }
    }
}