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
@Autonomous(name = "autoRight", group = "Autonomous")
public class autoRight extends LinearOpMode {

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

        Pose2d initialPose = new Pose2d(-15, 63, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Actions.runBlocking(lift.rotatePos(outtakeServoCenter, 0));
        //Actions.runBlocking(lift.outtake(outtakeServoRight, outtakeServoLeft, 2));

        waitForStart();

        Action colectFront = drive.actionBuilder(initialPose)
                .afterDisp(0, intake.extendOut(slides, 12))
                .strafeToLinearHeading(new Vector2d(-48-1, 39), Math.toRadians(270))
                //.waitSeconds(1)
                .afterDisp(1, intake.extendOut(slides, 20))
                .turnTo(Math.toRadians(90))
                //.waitSeconds(1)
                .afterDisp(1, intake.extendOut(slides, 12))
                .strafeToLinearHeading(new Vector2d(-58+3, 39), Math.toRadians(270))
                .turn(Math.toRadians(0+16))
                //.waitSeconds(1)
                //.turnTo(Math.toRadians(90))
                //.waitSeconds(1)
                .build();

        TrajectoryActionBuilder getBehindGrabSamples = drive.actionBuilder(initialPose)
                // hangs a specimen
                .afterDisp(0, new ParallelAction(
                        lift.outtake(outtakeServoRight, outtakeServoLeft, 2),
                        lift.elevateOut(liftRight, liftLeft, 18)
                ))
                .strafeTo(new Vector2d(-10, 32))
                .afterDisp(0, new SequentialAction(
                        lift.elevateOut(liftRight, liftLeft, 12),
                        new SleepAction(0.5),
                        lift.rotatePos(outtakeServoCenter, 1)
                ))
                .waitSeconds(0.5)
                .splineToLinearHeading(new Pose2d(new Vector2d(-23, 45), Math.toRadians(180)), Math.toRadians(180))
                .afterDisp(0, new ParallelAction(
                        lift.elevateOut(liftRight, liftLeft, 0),
                        lift.outtake(outtakeServoRight, outtakeServoLeft, 2)
                ))

                .splineToLinearHeading(new Pose2d(new Vector2d(-36, 32), Math.toRadians(270)), Math.toRadians(270))
                .strafeTo(new Vector2d(-36, 21))

                //.waitSeconds(0.5)

                // sample 1
                .splineToLinearHeading(new Pose2d(new Vector2d(-44, 21), Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(-46, 56))
                //.waitSeconds(0.25

                // sample 2
                .strafeTo(new Vector2d(-46, 21))
                .splineToLinearHeading(new Pose2d(new Vector2d(-55, 21), Math.toRadians(270)), Math.toRadians(90))
                //.waitSeconds(0.25)
                .strafeTo(new Vector2d(-55, 56));

                // goes up


                /*.strafeTo(new Vector2d(-54, 14+1))
                .turn(Math.toRadians(-4))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(new Vector2d(-64, 14+1), Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(-60, 56))

                 */

                //.build();

        Action hangSpecimen = getBehindGrabSamples.endTrajectory().fresh()
                // specimen 1

                // get ready to grab specimen
                .strafeToLinearHeading(new Vector2d(-47, 48), Math.toRadians(270))
                .afterDisp(0, new ParallelAction(
                        lift.rotatePos(outtakeServoCenter, 1),
                        lift.outtake(outtakeServoRight, outtakeServoLeft, 1)
                ))

                .waitSeconds(0.5)

                // grabs specimen and sets up for hanging
                .strafeToLinearHeading(new Vector2d(-47, 63+3), Math.toRadians(270))
                .afterDisp(0, new SequentialAction(
                        lift.rotatePos(outtakeServoCenter, 2),
                        new SleepAction(0.5),
                        new ParallelAction(
                                lift.outtake(outtakeServoRight, outtakeServoLeft, 2),
                                lift.elevateOut(liftRight, liftLeft, 18),
                                lift.rotatePos(outtakeServoCenter, 0)
                        )
                ))

                .waitSeconds(0.5)

                // gets more ready
                //.strafeToLinearHeading(new Vector2d(-18.5, 45.5+1), Math.toRadians(180))
                .afterDisp(1, new ParallelAction(
                        lift.outtake(outtakeServoRight, outtakeServoLeft, 2),
                        lift.elevateOut(liftRight, liftLeft, 18)
                ))

                // hangs
                .strafeToLinearHeading(new Vector2d(-10, 32+2), Math.toRadians(90))
                .afterDisp(0, new SequentialAction(
                        lift.elevateOut(liftRight, liftLeft, 12),
                        new SleepAction(0.25),
                        lift.rotatePos(outtakeServoCenter, 1)
                ))

                .waitSeconds(0.25)

                // specimen 2

                // get ready to grab specimen
                .strafeToLinearHeading(new Vector2d(-47, 48), Math.toRadians(270))
                .afterDisp(0, new ParallelAction(
                        lift.elevateOut(liftRight, liftLeft, 0),
                        lift.rotatePos(outtakeServoCenter, 1),
                        lift.outtake(outtakeServoRight, outtakeServoLeft, 1)
                ))

                .waitSeconds(0.5)

                // grabs specimen and sets up for hanging
                .strafeToLinearHeading(new Vector2d(-47, 63+3), Math.toRadians(270))
                .afterDisp(0, new SequentialAction(
                        lift.rotatePos(outtakeServoCenter, 2),
                        new SleepAction(0.5),
                        new ParallelAction(
                                lift.outtake(outtakeServoRight, outtakeServoLeft, 2),
                                lift.elevateOut(liftRight, liftLeft, 18),
                                lift.rotatePos(outtakeServoCenter, 0)
                        )
                ))

                .waitSeconds(1)

                // gets more ready
                //.strafeToLinearHeading(new Vector2d(-18.5, 45.5+1), Math.toRadians(180))
                .afterDisp(0, new ParallelAction(
                        lift.outtake(outtakeServoRight, outtakeServoLeft, 2),
                        lift.elevateOut(liftRight, liftLeft, 18)
                ))

                // hangs
                .strafeToLinearHeading(new Vector2d(-10, 32+4), Math.toRadians(90))
                .afterDisp(0, new SequentialAction(
                        lift.elevateOut(liftRight, liftLeft, 12),
                        new SleepAction(0.25),
                        lift.rotatePos(outtakeServoCenter, 1)
                ))

                .build();



        Actions.runBlocking(new SequentialAction(
                getBehindGrabSamples.build(),
                hangSpecimen
                )
        );
    }
}