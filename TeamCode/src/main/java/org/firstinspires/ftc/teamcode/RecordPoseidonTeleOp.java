package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

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

/** @noinspection ALL*/
@TeleOp(name="RecordPoseidonTeleOp")
public class RecordPoseidonTeleOp extends LinearOpMode {
    //Motor variables
    public DcMotor Front_L;
    public DcMotor Back_L;
    public DcMotor Front_R;
    public DcMotor Back_R;
    public DcMotor slidesLeft;
    public DcMotor slidesRight;
    public DcMotor slidesHorizontalRight;
    public DcMotor slidesHorizontalLeft;
    public Servo leftClawIn;
    public Servo rightClawIn;
    public Servo clawInRotator;
    public Servo leftClawOut;
    public Servo rightClawOut;
    public Servo clawOutRotator;
    public Servo planeShooter;

    //Servo variables
    private double clawInPosition = 0;
    private double clawOutPosition = 0;
    private double rotatorInPosition = 0;
    private double rotatorOutPosition = 0;
    private double planeShooterPosition = 0;

    private int countIn = 0;
    private int countOut = 0;
    private int rotatorCountIn = 0;
    private int rotatorCountOut = 0;
    private int planeShooterCount = 0;
    private boolean zeroPositionL = false;
    private boolean zeroPositionR = false;

    // Webcam variables
    private OpenCvCamera webcam;
    private String videoFilePath;
    private boolean isRecording = false;

    // Path to the file that stores the run count
    @SuppressLint("SdCardPath")
    private static final String RUN_COUNT_FILE = Environment.getExternalStorageDirectory().getPath() + "/opmode_run_count.txt";

    @Override
    public void runOpMode() {
        // Initialize motors
        Front_L = hardwareMap.get(DcMotor.class, "Front_L");
        Back_L = hardwareMap.get(DcMotor.class, "Back_L");
        Front_R = hardwareMap.get(DcMotor.class, "Front_R");
        Back_R = hardwareMap.get(DcMotor.class, "Back_R");

        slidesRight = hardwareMap.get(DcMotor.class, "slidesRight");
        slidesLeft = hardwareMap.get(DcMotor.class, "slidesLeft");
        slidesHorizontalRight = hardwareMap.get(DcMotor.class, "slidesHorizontalRight");
        slidesHorizontalLeft = hardwareMap.get(DcMotor.class, "slidesHorizontalLeft");

        // Initialize servos
        leftClawIn = hardwareMap.get(Servo.class, "ILClaw");
        rightClawIn = hardwareMap.get(Servo.class, "IRClaw");
        clawInRotator = hardwareMap.get(Servo.class, "Intake");
        leftClawOut = hardwareMap.get(Servo.class, "OLClaw");
        rightClawOut = hardwareMap.get(Servo.class, "ORClaw");
        clawOutRotator = hardwareMap.get(Servo.class, "Outtake");
        planeShooter = hardwareMap.get(Servo.class, "Airplane");
        // Set behaviors for motors
        slidesLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slidesLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidesLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slidesLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        slidesRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slidesRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidesRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slidesRight.setDirection(DcMotorSimple.Direction.REVERSE);

        slidesHorizontalLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slidesHorizontalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidesHorizontalLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slidesHorizontalRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slidesHorizontalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slidesHorizontalRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        planeShooter.setPosition(1);

        leftClawIn.setPosition(0.11);
        rightClawIn.setPosition(0.7);
        clawOutPosition = 0.3;

        leftClawOut.setPosition(.3);
        rightClawOut.setPosition(0.81);
        clawOutPosition = .65;

        clawOutRotator.setPosition(1);
        rotatorOutPosition = 1;

        clawInRotator.setPosition(1);
        rotatorInPosition = 1;

        // Initialize webcam and start recording
        initializeWebcam();

        // Send telemetry message to signify robot waiting
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Start recording as soon as the OpMode starts
        startWebcamRecording();

        while (opModeIsActive()) {
            // Run until the end of the match
            float leftjoystick_x = gamepad1.left_stick_x;
            float leftjoystick_y = gamepad1.left_stick_y;
            float rightjoystick_x = gamepad1.right_stick_x;
            float rightjoystick_y = gamepad1.right_stick_y;

            if (gamepad1.right_bumper) {
                leftjoystick_x /= 4F;
                leftjoystick_y /= 4F;
                rightjoystick_x /= 4F;
                rightjoystick_y /= 4F;
            } else if (!gamepad1.left_bumper) {
                leftjoystick_x /= 1.5F;
                leftjoystick_y /= 1.5F;
                rightjoystick_x /= 1.5F;
                rightjoystick_y /= 1.5F;
            }
            if (gamepad1.y) {
                planeShooter.setPosition(0);
            }
            if (gamepad1.right_stick_x > 0 && gamepad1.left_stick_x > 0) {
                Front_L.setPower(-2.0 * ((rightjoystick_x + leftjoystick_x) / 3.0));
                Back_L.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
                Front_R.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
                Back_R.setPower(2.0 * -(rightjoystick_x + leftjoystick_x) / 3.0);
            } else if (gamepad1.right_stick_x < 0 && gamepad1.left_stick_x < 0) {
                Front_L.setPower(-2.0 * ((rightjoystick_x + leftjoystick_x) / 3.0));
                Back_L.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
                Front_R.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
                Back_R.setPower(-2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
            } else {
                Front_L.setPower(4.0 * leftjoystick_y / 3.0);
                Back_L.setPower(4.0 * leftjoystick_y / 3.0);
                Front_R.setPower(4.0 * rightjoystick_y / 3.0);
                Back_R.setPower(4.0 * rightjoystick_y / 3.0);
            }

            if (gamepad1.a) {
                if (planeShooterCount % 2 == 0) {
                    planeShooter.setPosition(1);
                    planeShooterPosition = 1;
                } else {
                    planeShooter.setPosition(0);
                    planeShooterPosition = 0;
                }
                planeShooterCount++;
            }

            // Control logic for claws and rotators
            if (gamepad2.y) {
                if (countIn % 2 == 0) {
                    leftClawIn.setPosition(0.3);
                    rightClawIn.setPosition(0.5);
                    clawInPosition = 0.6;
                } else {
                    leftClawIn.setPosition(0.11);
                    rightClawIn.setPosition(0.7);
                    clawOutPosition = 0.3;
                }
                countIn++;
            }
            if (gamepad2.a) {
                if (countOut % 2 == 0) {
                    leftClawOut.setPosition(0.6);
                    rightClawOut.setPosition(0.5);
                    clawOutPosition = 0.6;
                } else {
                    leftClawOut.setPosition(.3);
                    rightClawOut.setPosition(0.81);
                    clawOutPosition = .65;
                }
                countOut++;
            }
            if (gamepad2.x) {
                if (rotatorCountIn % 2 == 0) {
                    clawInRotator.setPosition(0.4);
                    rotatorInPosition = 0.4;
                } else {
                    double clawInRotatorPosition = 0.4;
                    while (clawInRotatorPosition < 1) {
                        sleep(20);
                        slidesHorizontalRight.setPower(35.0 * gamepad2.right_stick_y / 100.0);
                        slidesHorizontalLeft.setPower(35.0 * -gamepad2.right_stick_y / 100.0);
                        clawInRotatorPosition += .012;
                        clawInRotator.setPosition(clawInRotatorPosition);
                        rotatorInPosition = clawInRotatorPosition;
                    }
                    clawInRotator.setPosition(1);
                    rotatorInPosition = 1;
                }
                rotatorCountIn++;
            }
            if (gamepad2.b) {
                if (rotatorCountOut % 2 == 0) {
                    clawOutRotator.setPosition(0.05);
                    rotatorOutPosition = 0.05;
                } else {
                    clawOutRotator.setPosition(1);
                    rotatorOutPosition = 1;
                }
                rotatorCountOut++;
            }
            if (gamepad2.right_bumper) {
                zeroPositionL = true;
                zeroPositionR = true;
            }
            if (zeroPositionL || zeroPositionR) {
                while (Math.abs(slidesHorizontalLeft.getCurrentPosition()) > 10 || Math.abs(slidesHorizontalRight.getCurrentPosition()) > 10) {
                    slidesHorizontalRight.setTargetPosition(0);
                    slidesHorizontalRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slidesHorizontalRight.setPower(0.8);
                    slidesHorizontalLeft.setTargetPosition(0);
                    slidesHorizontalLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slidesHorizontalLeft.setPower(0.8);
                }
                /*while(slidesHorizontalRight.isBusy() || slidesHorizontalLeft.isBusy()) {
                    sleep(1);
                }*/
            }
            if (gamepad2.left_bumper) {
                //retest these positions after replacing motors
                while (Math.abs(slidesHorizontalLeft.getCurrentPosition() - 60) > 5 || Math.abs(slidesHorizontalRight.getCurrentPosition() + 60) > 5) {
                    slidesHorizontalLeft.setTargetPosition(60);
                    slidesHorizontalLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slidesHorizontalLeft.setPower(0.8);
                    slidesHorizontalRight.setTargetPosition(-60);
                    slidesHorizontalRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slidesHorizontalRight.setPower(0.8);
                }
            }
            if (gamepad2.left_stick_y != 0f || gamepad2.right_stick_y != 0f) {
                zeroPositionL = false;
                zeroPositionR = false;
                slidesHorizontalLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                slidesHorizontalRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            slidesLeft.setPower(2.0 * gamepad2.left_stick_y / 3.0);
            slidesRight.setPower(2.0 * gamepad2.left_stick_y / 3.0);


            slidesHorizontalRight.setPower(gamepad2.right_stick_y / 3.0);
            slidesHorizontalLeft.setPower(-gamepad2.right_stick_y / 3.0);

            // Send telemetry data to the driver station
            telemetry.addData("ClawIn", "Position = %.2f", clawInPosition);
            telemetry.addData("ClawOut", "Position = %.2f", clawOutPosition);
            telemetry.addData("Intake", "Position = %.2f", rotatorInPosition);
            telemetry.addData("Outtake", "Position = %.2f", rotatorOutPosition);
            telemetry.addData("Airplane Servo", "Position = %.2f", planeShooterPosition);

            // Ticks
            telemetry.addData("horizontalSlidesLeft", slidesHorizontalLeft.getCurrentPosition());
            telemetry.addData("horizontalSlidesRight", slidesHorizontalRight.getCurrentPosition());
            telemetry.addData("zeroPositionL bool", zeroPositionL);
            telemetry.addData("zeroPositionR bool", zeroPositionR);
            telemetry.addData("Right Horizontal Power", gamepad2.right_stick_y / 3.0);
            telemetry.addData("Left Horizontal Power", -gamepad2.right_stick_y / 3.0);
            telemetry.update();

            // Pace the loop
            sleep(150);
        }

        // Stop recording when the OpMode ends
        stopWebcamRecording();
    }

    // Initialize the webcam
    private void initializeWebcam() {
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

        // Set a simple pipeline to just pass frames through
        webcam.setPipeline(new SimplePipeline());

        webcam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
        webcam.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Webcam Error", errorCode);
                telemetry.update();
            }
        });
    }

    // Start recording the video
    private void startWebcamRecording() {
        if (webcam != null) {
            /*  Backup file path naming system
             *  Generate a random number between 1 and 1000 for unique filename
                Random random = new Random();
                int randomNumber = random.nextInt(1000) + 1;
                videoFilePath = String.format("/sdcard/poseidonRecording_%d.mp4", randomNumber);
            */
            int runCount = readRunCount();
            videoFilePath = Environment.getExternalStorageDirectory().getPath() + "/poseidonRecording_" + runCount + ".mp4";

            webcam.startRecordingPipeline(
                    new PipelineRecordingParameters.Builder()
                            .setBitrate(4, PipelineRecordingParameters.BitrateUnits.Mbps)
                            .setEncoder(PipelineRecordingParameters.Encoder.H264)
                            .setOutputFormat(PipelineRecordingParameters.OutputFormat.MPEG_4)
                            .setFrameRate(30)
                            .setPath(videoFilePath)
                            .build());

            telemetry.addData("Recording started", videoFilePath);
            telemetry.update();
            isRecording = true;

            // Increment the run count for the next session
            incrementRunCount();
        }
    }

    // Stop recording the video
    private void stopWebcamRecording() {
        if (webcam != null && isRecording) {
            webcam.stopRecordingPipeline();
            telemetry.addData("Recording stopped", videoFilePath);
            telemetry.update();
            isRecording = false;
        }
    }

    // Read the current run count from a file
    private int readRunCount() {
        File file = new File(RUN_COUNT_FILE);
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

    // Increment and save the run count
    private void incrementRunCount() {
        int count = readRunCount();
        count++;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUN_COUNT_FILE))) {
            writer.write(String.valueOf(count));
        } catch (IOException e) {
            telemetry.addData("Error writing run count", e.getMessage());
            telemetry.update();
        }
    }

    // Simple pipeline for processing frames
    static class SimplePipeline extends OpenCvPipeline {
        @Override
        public Mat processFrame(Mat input) {
            // Just return the frame without any processing
            return input;
        }
    }
}
