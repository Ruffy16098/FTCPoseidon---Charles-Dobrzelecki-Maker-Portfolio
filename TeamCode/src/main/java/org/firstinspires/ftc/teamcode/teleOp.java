package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.actions.intake;
import static org.firstinspires.ftc.teamcode.actions.lift;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name="teleOp")
public class teleOp extends LinearOpMode {
    public DcMotor leftFront;
    public DcMotor leftBack;
    public DcMotor rightFront;
    public DcMotor rightBack;

    public DcMotor slides;
    public DcMotor intakeMotor;
    public DcMotor liftRight;
    public DcMotor liftLeft;

    public void runOpMode() {
        // Initialize motors
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        slides = hardwareMap.get(DcMotor.class, "slides");
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides.setDirection(DcMotorSimple.Direction.REVERSE);
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        liftRight = hardwareMap.get(DcMotor.class, "liftRight");
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftRight.setDirection(DcMotorSimple.Direction.FORWARD);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        liftLeft = hardwareMap.get(DcMotor.class, "liftLeft");
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        waitForStart();
        while (opModeIsActive()) {

            // gamepad 1
            // robot movement
            // Ben gets to decide what go's here

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

            if (gamepad1.right_stick_x > 0 && gamepad1.left_stick_x > 0) {
                leftFront.setPower(-2.0 * ((rightjoystick_x + leftjoystick_x) / 3.0));
                leftBack.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
                rightFront.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
                rightBack.setPower(2.0 * -(rightjoystick_x + leftjoystick_x) / 3.0);
            } else if (gamepad1.right_stick_x < 0 && gamepad1.left_stick_x < 0) {
                leftFront.setPower(-2.0 * ((rightjoystick_x + leftjoystick_x) / 3.0));
                leftBack.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
                rightFront.setPower(2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
                rightBack.setPower(-2.0 * (rightjoystick_x + leftjoystick_x) / 3.0);
            } else {
                leftFront.setPower(4.0 * leftjoystick_y / 3.0);
                leftBack.setPower(4.0 * leftjoystick_y / 3.0);
                rightFront.setPower(4.0 * rightjoystick_y / 3.0);
                rightBack.setPower(4.0 * rightjoystick_y / 3.0);
            }

            // gamepad 2

            // lifts
            if(gamepad2.a && gamepad2.left_bumper){
                //Actions.runBlocking(intake.extendOut(slides, 15));
            } else if (gamepad2.b && gamepad2.left_bumper) {
                //Actions.runBlocking(intake.extendOut(slides, 0));
            }

            if(gamepad2.a && !gamepad2.left_bumper){
                Actions.runBlocking(lift.elevate(liftRight, liftLeft, 0.2));
            } else if(gamepad2.b && !gamepad2.left_bumper) {
                Actions.runBlocking(lift.elevate(liftRight, liftLeft, -0.2));
            } else{
                Actions.runBlocking(lift.elevate(liftRight, liftLeft, 0));
            }

            // intake
            // extend intake out by 0 or 15
            if(gamepad2.y && gamepad2.left_bumper){
                Actions.runBlocking(intake.extendOut(slides, 15, gamepad2));
            } else if (gamepad2.x && gamepad2.left_bumper) {
                Actions.runBlocking(intake.extendOut(slides, 0, gamepad2));
            }

            if(gamepad2.right_stick_y != 0 && !gamepad2.left_bumper){
                Actions.runBlocking(intake.extend(slides, -gamepad2.right_stick_y));
            } else{
                Actions.runBlocking(intake.extend(slides, 0));
            }

            Actions.runBlocking(intake.spin(intakeMotor, -gamepad2.left_stick_y));


            telemetry.addData("slides position", slides.getCurrentPosition());
            telemetry.addData("liftRight position", liftRight.getCurrentPosition());
            telemetry.addData("liftLeft position", liftLeft.getCurrentPosition());
            telemetry.update();
        }
    }
}
