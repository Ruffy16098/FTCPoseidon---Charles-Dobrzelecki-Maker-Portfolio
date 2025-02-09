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
@Autonomous(name = "grabSpecimens", group = "Autonomous")
public class grabSpecimens extends LinearOpMode {

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
    public Servo intakeServoClaw;

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

        intakeServoLeft = hardwareMap.get(Servo.class, "intakeServoLeft");
        intakeServoLeft.setDirection(Servo.Direction.REVERSE);

        intakeServoCenter = hardwareMap.get(Servo.class, "intakeServoCenter");
        intakeServoCenter.setDirection(Servo.Direction.FORWARD);

        intakeServoClaw = hardwareMap.get(Servo.class, "intakeServoClaw");
        intakeServoClaw.setDirection(Servo.Direction.FORWARD);

        outtakeServoRight = hardwareMap.get(Servo.class, "outtakeServoRight");
        outtakeServoRight.setDirection(Servo.Direction.FORWARD);

        outtakeServoLeft = hardwareMap.get(Servo.class, "outtakeServoLeft");
        outtakeServoLeft.setDirection(Servo.Direction.REVERSE);

        outtakeServoCenter = hardwareMap.get(Servo.class, "outtakeServoCenter");
        outtakeServoCenter.setDirection(Servo.Direction.FORWARD);

        Pose2d initialPose = new Pose2d(-15, 63, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);


        Actions.runBlocking(new ParallelAction(
                intake.rotatePos(intakeServoClaw, 1),
                lift.rotatePos(outtakeServoCenter, 0)
                //intake.extendOut(slides, 1.6)
        ));
        intakeServoRight.setPosition(0.5);
        intakeServoLeft.setPosition(0.5);
        intakeServoCenter.setPosition(0.74);

        waitForStart();

        TrajectoryActionBuilder startSpecimen = drive.actionBuilder(initialPose)
                // hangs a specimen
                .strafeToSplineHeading(new Vector2d(-15, 60), Math.toRadians(90))
                .afterDisp(0, new ParallelAction(
                        lift.elevateOut(liftRight, liftLeft, 14)
                ))
                .splineToLinearHeading(new Pose2d(new Vector2d(1, 33), Math.toRadians(90)), Math.toRadians(270))
                .afterDisp(0, new SequentialAction(
                        lift.elevateOut(liftRight, liftLeft, 22),
                        new SleepAction(0.4),
                        lift.rotatePos(outtakeServoCenter, 1)
                ))
                .waitSeconds(0.4);

        TrajectoryActionBuilder grabSamples = startSpecimen.endTrajectory().fresh()
                ;

        Action hangSpecimen = grabSamples.endTrajectory().fresh()
                // grabs second specimen
                .strafeToSplineHeading(new Vector2d(-47, 45), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(-47, 68.5), Math.toRadians(90-10))
                .afterDisp(0, new SequentialAction(
                        lift.rotatePos(outtakeServoCenter, 0),
                        new SleepAction(0.2),
                        new ParallelAction(
                                lift.elevateOut(liftRight, liftLeft, 14.5),
                                new SequentialAction(
                                        lift.outtake(outtakeMotor, 0.1),
                                        new SleepAction(2),
                                        lift.outtake(outtakeMotor, 0.0)
                                ))

                ))
                .waitSeconds(0.25)

                // goes and hangs second specimen
                .strafeToSplineHeading(new Vector2d(-23, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-1, 32-2), Math.toRadians(90)), Math.toRadians(270))
                .afterDisp(0, new SequentialAction(
                        lift.elevateOut(liftRight, liftLeft, 22.5),
                        new SleepAction(0.5),
                        lift.rotatePos(outtakeServoCenter, 1),
                        new ParallelAction(
                                lift.elevateOut(liftRight, liftLeft, 0),
                                new SequentialAction(
                                        lift.outtake(outtakeMotor, -0.1),
                                        new SleepAction(2),
                                        lift.outtake(outtakeMotor, 0.0),
                                        intake.extendOut(slides, 0)
                                )
                        )
                ))
                .waitSeconds(0.4)

                // goes and grabs third specimen
                .strafeToSplineHeading(new Vector2d(-23, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 69), Math.toRadians(90-10)), Math.toRadians(90))
                .afterDisp(0, new SequentialAction(
                        lift.rotatePos(outtakeServoCenter, 0),
                        new SleepAction(0.2),
                        new ParallelAction(
                                lift.elevateOut(liftRight, liftLeft, 15.5),
                                new SequentialAction(
                                        lift.outtake(outtakeMotor, 0.1),
                                        new SleepAction(2),
                                        lift.outtake(outtakeMotor, 0.0)
                                ))

                ))
                .waitSeconds(0.25)

                // goes and hangs third specimen
                .strafeToSplineHeading(new Vector2d(-22, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-3, 32-2), Math.toRadians(90)), Math.toRadians(270))
                .afterDisp(0, new SequentialAction(
                        lift.elevateOut(liftRight, liftLeft, 23.5),
                        new SleepAction(0.5),
                        lift.rotatePos(outtakeServoCenter, 1),
                        new ParallelAction(
                                lift.elevateOut(liftRight, liftLeft, 0),
                                new SequentialAction(
                                        lift.outtake(outtakeMotor, -0.1),
                                        new SleepAction(2),
                                        lift.outtake(outtakeMotor, 0.0),
                                        intake.extendOut(slides, 0)
                                )
                        )
                ))
                .waitSeconds(0.4)

                // goes and grabs fourth specimen
                .strafeToSplineHeading(new Vector2d(-23, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 69), Math.toRadians(90-10)), Math.toRadians(90))
                .afterDisp(0, new SequentialAction(
                        lift.rotatePos(outtakeServoCenter, 0),
                        new SleepAction(0.2),
                        new ParallelAction(
                                lift.elevateOut(liftRight, liftLeft, 15.5),
                                new SequentialAction(
                                        lift.outtake(outtakeMotor, 0.1),
                                        new SleepAction(2),
                                        lift.outtake(outtakeMotor, 0.0)
                                ))

                ))

                // goes and hangs fourth specimen
                .strafeToSplineHeading(new Vector2d(-21, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-5, 32-2), Math.toRadians(90)), Math.toRadians(270))
                .afterDisp(0, new SequentialAction(
                        lift.elevateOut(liftRight, liftLeft, 23.5),
                        new SleepAction(0.5),
                        lift.rotatePos(outtakeServoCenter, 1),
                        new ParallelAction(
                                lift.elevateOut(liftRight, liftLeft, 0),
                                new SequentialAction(
                                        lift.outtake(outtakeMotor, -0.1),
                                        new SleepAction(2),
                                        lift.outtake(outtakeMotor, 0.0),
                                        intake.extendOut(slides, 0)
                                )
                        )
                ))

                .strafeToSplineHeading(new Vector2d(-47, 60), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 69), Math.toRadians(90-10)), Math.toRadians(90))

                /*
                // goes and grabs fifth specimen

                .strafeToSplineHeading(new Vector2d(-2, 35), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-24.5, 50), Math.toRadians(90)), Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs fifth specimen
                .strafeToSplineHeading(new Vector2d(-47, 65), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-23.5, 50), Math.toRadians(90)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(new Vector2d(0, 33), Math.toRadians(90)), Math.toRadians(270))
                 */
                .build();

        Actions.runBlocking(new SequentialAction(
                        startSpecimen.build(),
                        grabSamples.build(),
                        hangSpecimen
                )
        );
    }
}