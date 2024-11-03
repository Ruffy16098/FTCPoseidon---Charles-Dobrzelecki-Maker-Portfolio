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

import org.firstinspires.ftc.onbotjava.OnBotJavaStandardFileManager;

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

    public Servo intakeServoRight;
    public Servo intakeServoLeft;
    public Servo intakeServoCenter;


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
        //intakeServoCenter.setPosition(0);

        intakeServoLeft.setPosition(0.5);
        intakeServoRight.setPosition(0.5);
        intakeServoRight.setPosition(0.48);

    }

    boolean pos = true;
    double pastPos = 0.05;
    double posChange;
    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();
        // updated based on gamepads

        // gamepad 1
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

        //gamepad 1, driving controls
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
        if(gamepad2.y){
            runningActions.add(new ParallelAction((
                    intake.tilt(intakeServoLeft, intakeServoRight, true)),
                    intake.rotate(intakeServoCenter, 0.48),
                    intake.extendOut(slides, 0))
            );
        }else{
            runningActions.add(intake.extend(slides, -gamepad2.right_stick_y/2));
        }


        // grabber actions
        if(gamepad2.b && gamepad2.right_bumper) {
            runningActions.add(intake.spin(intakeMotor, -0.3));
        }else if(gamepad2.b && !gamepad2.right_bumper){
            runningActions.add(intake.spin(intakeMotor, 0.3));
        } else{
            runningActions.add(intake.spin(intakeMotor, 0));
        }


        if(gamepad2.dpad_left){
            runningActions.add(intake.rotate(intakeServoCenter, intakeServoCenter.getPosition()+0.02));
        } else if (gamepad2.dpad_right) {
            runningActions.add(intake.rotate(intakeServoCenter, intakeServoCenter.getPosition()-0.02));
        }else if(gamepad2.dpad_down){
            //runningActions.add(intake.tilt(intakeServoLeft, intakeServoRight, false));
            runningActions.add(intake.tilt(intakeServoLeft, intakeServoRight, intakeServoRight.getPosition()+0.02));
        }else if(gamepad2.dpad_up){
            //runningActions.add(intake.tilt(intakeServoLeft, intakeServoRight, true));
            runningActions.add(intake.tilt(intakeServoLeft, intakeServoRight, intakeServoRight.getPosition()-0.02));
        }




        // lifts actions

        // if the joystick isn't active, than the lifts will keep it self up right
        // elevateOut to current position must be the second to last conditional in the whole statements
        if(gamepad2.x){
            runningActions.add(lift.elevateOut(liftRight, liftLeft, 25));
        } else if(gamepad2.left_stick_y == 0){
            runningActions.add(lift.elevateOut(liftRight, liftLeft, liftRight.getCurrentPosition()/79.84615384615385));
        }else{
            runningActions.add(lift.elevate(liftRight, liftLeft, -gamepad2.left_stick_y/2));
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
        pastPos += posChange;

        dash.sendTelemetryPacket(packet);
        telemetry.addData("Servo right position", intakeServoRight.getPosition());
        telemetry.addData("Servo center position", intakeServoCenter.getPosition());
        telemetry.addData("Servo left position", intakeServoLeft.getPosition());
        telemetry.addData("slides position", slides.getCurrentPosition());
        telemetry.addData("liftRight position", liftRight.getCurrentPosition());
        telemetry.addData("liftLeft position", liftLeft.getCurrentPosition());
        telemetry.update();
    }
}