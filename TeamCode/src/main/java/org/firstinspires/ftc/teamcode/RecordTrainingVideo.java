package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.PipelineRecordingParameters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Disabled
@TeleOp
public class RecordTrainingVideo extends LinearOpMode {

    OpenCvCamera webcam;
    String videoFilePath;
    boolean isRecording = false;

    // File path for storing and reading the count
    private static final String COUNT_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/teleop_run_count.txt";
    private static final String SAVED_VIDEOS_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/saved_videos/";

    @Override
    public void runOpMode() {
        // Initialize the webcam
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

        // Set up the pipeline and camera settings
        webcam.setPipeline(new SamplePipeline());
        webcam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
        webcam.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);

        // Asynchronously open the camera device and start streaming
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Start streaming with the desired resolution and rotation
                webcam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT);

                // Generate a unique filename based on the current count
                int runCount = readRunCount();
                videoFilePath = Environment.getExternalStorageDirectory().getPath() + "/coneTraining_" + runCount + ".mp4";

                // Start recording the pipeline
                webcam.startRecordingPipeline(
                        new PipelineRecordingParameters.Builder()
                                .setBitrate(4, PipelineRecordingParameters.BitrateUnits.Mbps)
                                .setEncoder(PipelineRecordingParameters.Encoder.H264)
                                .setOutputFormat(PipelineRecordingParameters.OutputFormat.MPEG_4)
                                .setFrameRate(30)
                                .setPath(videoFilePath)
                                .build());

                // Update telemetry to indicate recording has started
                telemetry.addData("Recording to", videoFilePath);
                telemetry.update();
                isRecording = true;

                // Increment the run count for the next recording
                incrementRunCount();
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("An error has occurred ", errorCode);
                telemetry.update();
            }
        });

        telemetry.addLine("Waiting for start");
        telemetry.update();

        // Wait for the start button to be pressed
        waitForStart();

        // Ensure the recording and streaming continue for the desired duration
        long startTime = System.currentTimeMillis();
        long recordingDuration = 15000; // 15 seconds

        while (opModeIsActive() && (System.currentTimeMillis() - startTime) < recordingDuration) {
            // Display telemetry data during operation
            telemetry.addData("Frame Count", webcam.getFrameCount());
            telemetry.addData("FPS", String.format("%.2f", webcam.getFps()));
            telemetry.addData("Total frame time ms", webcam.getTotalFrameTimeMs());
            telemetry.addData("Pipeline time ms", webcam.getPipelineTimeMs());
            telemetry.addData("Overhead time ms", webcam.getOverheadTimeMs());
            telemetry.addData("Theoretical max FPS", webcam.getCurrentPipelineMaxFps());
            telemetry.update();
        }

        // Stop recording and streaming after the duration
        if (isRecording) {
            webcam.stopRecordingPipeline();
            telemetry.addData("Recording stopped", videoFilePath);
            telemetry.update();
            isRecording = false;

            // Save the recorded video to a permanent location
            saveRecordedVideo(videoFilePath);
        }

        webcam.stopStreaming();
        terminateOpModeNow();
    }

    // Method to save the recorded video to a permanent location
    private void saveRecordedVideo(String videoFilePath) {
        File srcFile = new File(videoFilePath);
        if (srcFile.exists()) {
            File savedDir = new File(SAVED_VIDEOS_DIRECTORY);
            if (!savedDir.exists()) {
                savedDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Generate a new file name or use the same name, as per your naming convention
            String destFilePath = SAVED_VIDEOS_DIRECTORY + srcFile.getName();

            // Move the file to the permanent location
            if (srcFile.renameTo(new File(destFilePath))) {
                telemetry.addData("Video saved to", destFilePath);
                telemetry.update();
            } else {
                telemetry.addData("Failed to save video", destFilePath);
                telemetry.update();
            }
        } else {
            telemetry.addData("Video file not found", videoFilePath);
            telemetry.update();
        }
    }

    // Read the current run count from file
    private int readRunCount() {
        File file = new File(COUNT_FILE_PATH);
        int count = 1; // Default to 1 if the file does not exist

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                count = Integer.parseInt(reader.readLine().trim());
            } catch (IOException | NumberFormatException e) {
                telemetry.addData("Error reading run count", e.getMessage());
                telemetry.update();
            }
        }

        return count;
    }

    // Increment and save the run count to file
    private void incrementRunCount() {
        int count = readRunCount();
        count++;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNT_FILE_PATH))) {
            writer.write(String.valueOf(count));
        } catch (IOException e) {
            telemetry.addData("Error writing run count", e.getMessage());
            telemetry.update();
        }
    }

    static class SamplePipeline extends OpenCvPipeline {
        @Override
        public Mat processFrame(Mat input) {
            // Return the frame without any modifications
            return input;
        }
    }
}
