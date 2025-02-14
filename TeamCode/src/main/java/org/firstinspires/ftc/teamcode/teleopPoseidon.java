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

@TeleOp(name="teleopPoseidon")
public class teleopPoseidon extends OpMode {
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
    private DcMotorEx outtakeMotor;

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
        slides.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        outtakeMotor = hardwareMap.get(DcMotorEx.class, "outtakeMotor");
        outtakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        outtakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        outtakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        liftRight = hardwareMap.get(DcMotorEx.class, "liftRight");
        liftRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        liftRight.setDirection(DcMotorSimple.Direction.FORWARD);
        //liftRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        liftLeft = hardwareMap.get(DcMotorEx.class, "liftLeft");
        liftLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
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

        intakeServoRight.setPosition(0.5);
        intakeServoLeft.setPosition(0.5);
        intakeServoCenter.setPosition(0.74);
        Actions.runBlocking(new ParallelAction(
                intake.rotatePos(intakeServoClaw, 1),
                lift.rotatePos(outtakeServoCenter, 1)
        ));
    }

    boolean isInOpen = false;
    boolean isOutOpen = false;
    @Override
    public void loop() {
        /*
        FTC gamepad 2 controls
            Dpad up/Dpad down = up/down for intake (done)
            Dpad left/Dpad right = left/right for intake (done)
            Right trigger = intake sucking and hawk tuah (done missing pos values)
            Left trigger = outtake sucking and hawk tuah (done missing pos values)
            Y = dump
            A = grabby from intake
            B = nothing yet
            X = lifts go up to score specimen
            Joystick Left Y axis - hort
            Joystick Right Y axis - vert
         */
        TelemetryPacket packet = new TelemetryPacket();
        // updated based on gamepads

        // gamepad 1 driving controls
        float leftjoystick_x = gamepad1.left_stick_x;
        float leftjoystick_y = gamepad1.left_stick_y;
        float rightjoystick_x = gamepad1.right_stick_x;
        float rightjoystick_y = gamepad1.right_stick_y;

        if (gamepad1.right_bumper) {
            leftjoystick_x /= 4F;
            leftjoystick_y /= 4F;
            rightjoystick_x /= 4F;
            rightjoystick_y /= 4F;
        } else if (!gamepad1.left_bumper) { // was 1.5
            leftjoystick_x /= 1F;
            leftjoystick_y /= 1F;
            rightjoystick_x /= 1F;
            rightjoystick_y /= 1F;
        }
        // DRIVING CODE: DO NOT TOUCH!!!
        if (gamepad1.right_stick_x > 0 && gamepad1.left_stick_x > 0) {
            leftFront.setPower(-2.0 * ((rightjoystick_x + leftjoystick_x) / 3.0));
            leftBack.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
            rightFront.setPower(-2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
            rightBack.setPower(-2.0 * -(rightjoystick_x + leftjoystick_x) / 3.0);
        } else if (gamepad1.right_stick_x < 0 && gamepad1.left_stick_x < 0) {
            leftFront.setPower(-2.0 * ((rightjoystick_x + leftjoystick_x) / 3.0));
            leftBack.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
            rightFront.setPower(-2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
            rightBack.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
        } else {
            leftFront.setPower(4.0 * leftjoystick_y / 3.0);
            leftBack.setPower(4.0 * leftjoystick_y / 3.0);
            rightFront.setPower(-4.0 * rightjoystick_y / 3.0);
            rightBack.setPower(-4.0 * rightjoystick_y / 3.0);
        }

        // gamepad 2

        // intake

        // slides actions
        if(gamepad2.b){
            runningActions.add(intake.home(intakeServoLeft, intakeServoRight, intakeServoCenter, intakeServoClaw, outtakeServoLeft, slides, outtakeMotor));
        } else {
            runningActions.add(intake.extend(slides, -gamepad2.left_stick_y/2));
        }


        // grabber actions
        if (gamepad2.left_trigger > 0) {
            runningActions.add(intake.rotatePos(intakeServoClaw, 0));
        } else if (gamepad2.left_bumper) {
            runningActions.add(intake.rotatePos(intakeServoClaw, 1));
        }

        if (gamepad2.right_trigger > 0) {
            runningActions.add(lift.rotatePos(outtakeServoCenter, 0));
        } else if (gamepad2.right_bumper) {
            runningActions.add(lift.rotatePos(outtakeServoCenter, 1));
        }

        // rotates the intake
        if (gamepad2.dpad_left){
            runningActions.add(intake.rotate(intakeServoCenter, intakeServoCenter.getPosition() + 0.02));
        } else if (gamepad2.dpad_right) {
            runningActions.add(intake.rotate(intakeServoCenter, intakeServoCenter.getPosition() - 0.02));
        } else if (gamepad2.dpad_up){
            runningActions.add(intake.tilt(intakeServoLeft, intakeServoRight, intakeServoRight.getPosition() + 0.02));
        } else if (gamepad2.dpad_down){
            runningActions.add(intake.tilt(intakeServoLeft, intakeServoRight, intakeServoRight.getPosition() - 0.02));
        } else if (gamepad2.x){
            intakeServoRight.setPosition(0.5);
            intakeServoLeft.setPosition(0.5);
        }

        // lifts actions

        // if the joystick isn't active, than the lifts will keep it self up right
        // elevateOut to current position must be the second to last conditional in the whole statements
        if (gamepad2.right_stick_y == 0) {
            runningActions.add(lift.elevateOut(liftRight, liftLeft, liftRight.getCurrentPosition()/79.84615384615385));
        }  else {
            runningActions.add(lift.elevate(liftRight, liftLeft, -gamepad2.right_stick_y));
        }

        if (gamepad2.a) {
            runningActions.add(new SequentialAction(
                    lift.outtake(outtakeMotor, -0.2),
                    new SleepAction(1),
                    lift.outtake(outtakeMotor, 0.0)
            ));


            //runningActions.add((lift.outtake(outtakeMotor, -0.2)));
        } else if (gamepad2.y) {

            runningActions.add(new SequentialAction(
                    lift.outtake(outtakeMotor, 0.2),
                    new SleepAction(1),
                    lift.outtake(outtakeMotor, 0.0)
            ));


            //runningActions.add((lift.outtake(outtakeMotor, 0.2)));
        } else {
            //runningActions.add(lift.outtake(outtakeMotor, 0.0));
        }

        // update running actions
        List<Action> newActions = new ArrayList<>();
        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet)) {
                newActions.add(action);
            }
        }
        runningActions = newActions;

        dash.sendTelemetryPacket(packet);
        telemetry.addData("Servo right position", intakeServoRight.getPosition());
        telemetry.addData("Servo center position", intakeServoCenter.getPosition());
        telemetry.addData("Servo left position", intakeServoLeft.getPosition());
        telemetry.addData("Servo claw position", intakeServoClaw.getPosition());
        telemetry.addData("slides position", slides.getCurrentPosition());
        telemetry.addData("liftRight position", liftRight.getCurrentPosition());
        telemetry.addData("liftLeft position", liftLeft.getCurrentPosition());
        telemetry.addData("Outtake servo center position", outtakeServoCenter.getPosition());
        telemetry.update();
    }
}