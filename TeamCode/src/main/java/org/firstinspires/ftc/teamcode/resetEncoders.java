package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.actions.intake;
import static org.firstinspires.ftc.teamcode.actions.lift;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="resetEncoders")
public class resetEncoders extends OpMode {
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
    public void init() {
        // Initialize motors
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");

        slides = hardwareMap.get(DcMotorEx.class, "slides");
        slides.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        slides.setDirection(DcMotorSimple.Direction.REVERSE);
        slides.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        outtakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
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
    }

    @Override
    public void loop() {

    }
}
