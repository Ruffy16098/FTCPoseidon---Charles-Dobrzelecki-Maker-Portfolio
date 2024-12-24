package org.firstinspires.ftc.teamcode;
import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.tuning.MecanumDrive;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.actions.intake;
import static org.firstinspires.ftc.teamcode.actions.lift;

import java.util.ArrayList;
import java.util.List;

@Config
@Autonomous(name = "autoBlueLeft", group = "Autonomous")
public class autoBlueLeft extends LinearOpMode {

    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    public DcMotorEx leftFront;
    public DcMotorEx leftBack;
    public DcMotorEx rightFront;
    public DcMotorEx rightBack;

    public DcMotorEx slides;
    public DcMotorEx intakeMotor;
    public DcMotorEx liftRight;
    public DcMotorEx liftLeft;

    public Servo intakeServoRight;
    public Servo intakeServoLeft;
    public Servo intakeServoCenter;

    public Servo outtakeServoRight;
    public Servo outtakeServoLeft;
    public Servo outtakeServoCenter;

    @Override
    public void runOpMode() {
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");

        slides = hardwareMap.get(DcMotorEx.class, "slides");
        slides.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        slides.setDirection(DcMotorSimple.Direction.REVERSE);
        slides.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        liftRight = hardwareMap.get(DcMotorEx.class, "liftRight");
        liftRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        liftRight.setDirection(DcMotorSimple.Direction.FORWARD);
        liftRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        liftLeft = hardwareMap.get(DcMotorEx.class, "liftLeft");
        liftLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        liftLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        intakeServoRight = hardwareMap.get(Servo.class, "intakeServoRight");
        intakeServoRight.setDirection(Servo.Direction.FORWARD);
        //intakeServoRight.setPosition(0);

        intakeServoLeft = hardwareMap.get(Servo.class, "intakeServoLeft");
        intakeServoLeft.setDirection(Servo.Direction.REVERSE);

        intakeServoCenter = hardwareMap.get(Servo.class, "intakeServoCenter");
        intakeServoCenter.setDirection(Servo.Direction.FORWARD);

        outtakeServoRight = hardwareMap.get(Servo.class, "outtakeServoRight");
        outtakeServoRight.setDirection(Servo.Direction.FORWARD);

        outtakeServoLeft = hardwareMap.get(Servo.class, "outtakeServoLeft");
        outtakeServoLeft.setDirection(Servo.Direction.REVERSE);

        outtakeServoCenter = hardwareMap.get(Servo.class, "outtakeServoCenter");
        outtakeServoCenter.setDirection(Servo.Direction.FORWARD);

        Pose2d initialPose = new Pose2d(15, 63, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Actions.runBlocking(lift.rotatePos(outtakeServoCenter, 0));

        waitForStart();

        Action specimenOne = new SequentialAction(
                //lift.rotatePos(outtakeServoCenter, 0),
                lift.outtake(outtakeServoRight, outtakeServoLeft, 1),
                drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(9, 32))
                .build(),
                new SleepAction(1),
                lift.elevateOut(liftRight, liftLeft, 10)
        );

        Action sampleOne = new SequentialAction(
                intake.tilt(intakeServoLeft, intakeServoRight, true),
                intake.rotate(intakeServoCenter, 0.76),
                drive.actionBuilder(new Pose2d(9,32, Math.toRadians(90)))
                        .strafeToLinearHeading(new Vector2d(54,54), Math.toRadians(222))
                        .waitSeconds(1)

                        .turn(Math.toRadians(45))
                        .strafeTo(new Vector2d(48, 47))
                        .waitSeconds(1)
                        .build(),
                intake.extendOut(slides, 22),
                new SleepAction(2),
                intake.tilt(intakeServoLeft, intakeServoRight, false),
                new SleepAction(1),
                intake.spin(intakeMotor, -0.3),
                new SleepAction(2),
                intake.spin(intakeMotor, 0),
                new SleepAction(2),
                new ParallelAction(
                        intake.extendOut(slides, 0),
                        intake.tilt(intakeServoLeft, intakeServoRight, true)
                ),
                new SleepAction(5),
                drive.actionBuilder(new Pose2d(54, 54, Math.toRadians(262)))
                        .strafeToLinearHeading(new Vector2d(15,63), Math.toRadians(270))
                        .waitSeconds(1)
                        .build(),
                new SleepAction(2)
        );

        Actions.runBlocking(new SequentialAction(
                specimenOne,
                sampleOne
            )
        );
    }
}