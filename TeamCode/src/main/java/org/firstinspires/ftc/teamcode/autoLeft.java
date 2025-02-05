package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
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
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.actions.intake;
import static org.firstinspires.ftc.teamcode.actions.lift;

import java.util.ArrayList;
import java.util.List;

@Config
@Autonomous(name = "autoLeft", group = "Autonomous")
public class autoLeft extends LinearOpMode {

    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    public DcMotorEx leftFront;
    public DcMotorEx leftBack;
    public DcMotorEx rightFront;
    public DcMotorEx rightBack;

    public DcMotorEx slides;
    public DcMotorEx outtakeMotor;
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

        outtakeMotor = hardwareMap.get(DcMotorEx.class, "outtakeMotor");
        outtakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        outtakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        outtakeMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        outtakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

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

        TrajectoryActionBuilder specimen = drive.actionBuilder(initialPose)
                // hangs a specimen
                .afterDisp(0, new ParallelAction(
                        //lift.outtake(outtakeServoRight, outtakeServoLeft, 2),
                        lift.elevateOut(liftRight, liftLeft, 11)
                ))
                .strafeTo(new Vector2d(8, 32.75))
                .afterDisp(0, new SequentialAction(
                        lift.elevateOut(liftRight, liftLeft, 22),
                        new SleepAction(0.75),
                        lift.rotatePos(outtakeServoCenter, 1)
                ));

        TrajectoryActionBuilder sampleOneBack = specimen.endTrajectory().fresh()
                // goes behind samples
                .splineToLinearHeading(new Pose2d(new Vector2d(36, 36), Math.toRadians(270)), Math.toRadians(270))
                .strafeTo(new Vector2d(36, 12))

                // pushes first sample back, and pushes it to the side
                .splineToLinearHeading(new Pose2d(new Vector2d(44, 12), Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(44, 48))
                .strafeTo(new Vector2d(36, 48))
                .splineToLinearHeading(new Pose2d(new Vector2d(42, 58), Math.toRadians(270)), Math.toRadians(0))
                .strafeTo(new Vector2d(52, 58))

                // pushes second sample back
                .strafeTo(new Vector2d(48, 58))
                .splineToLinearHeading(new Pose2d(new Vector2d(36, 28), Math.toRadians(270)), Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(new Vector2d(53, 14), Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(53, 56))

                // park
                .strafeTo(new Vector2d(52, 40))
                .splineToLinearHeading(new Pose2d(new Vector2d(26, 12), Math.toRadians(180)), Math.toRadians(180));


        Actions.runBlocking(new SequentialAction(
                specimen.build(),
                sampleOneBack.build()
            )
        );
    }
}