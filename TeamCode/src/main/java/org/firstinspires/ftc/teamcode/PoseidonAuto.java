package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;

//TODO: fix input not a variable
@Autonomous(name="PoseidonAuto", group="Linear Opmode")
public class PoseidonAuto extends LinearOpMode {

    private OpenCvCamera camera;
    private Mat processedFrame = new Mat();

    // HSV color ranges for light purple, blue, and red
    private Scalar lightPurpleLow = new Scalar(125, 50, 50);
    private Scalar lightPurpleHigh = new Scalar(150, 255, 255);

    private Scalar blueLow = new Scalar(100, 50, 50);
    private Scalar blueHigh = new Scalar(130, 255, 255);

    private Scalar redLow = new Scalar(0, 50, 50);
    private Scalar redHigh = new Scalar(10, 255, 255);

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

            Core.inRange(processedFrame, lightPurpleLow, lightPurpleHigh, lightPurpleMask);
            Core.inRange(processedFrame, blueLow, blueHigh, blueMask);
            Core.inRange(processedFrame, redLow, redHigh, redMask);

            // Analyze masks to determine position of the colors
            // For simplicity, we assume each color is represented by the largest contour found in each mask
            analyzePosition(lightPurpleMask, "Light Purple");
            analyzePosition(blueMask, "Blue");
            analyzePosition(redMask, "Red");

            return input;
        }

        private void analyzePosition(Mat mask, String colorName) {
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
                    // Move to position 1
                } else if (centerX < 2 * input.width() / 3) {
                    telemetry.addData(colorName + " Position", "Center");
                    // Move to position 2
                } else {
                    telemetry.addData(colorName + " Position", "Right");
                    // Move to position 3
                }
            }
        }
    }
}
