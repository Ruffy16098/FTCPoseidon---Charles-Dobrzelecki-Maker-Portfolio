package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.actions.intake.extend;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="teleOp")
public class teleOp extends LinearOpMode {
    public DcMotor Front_L;
    public DcMotor Back_L;
    public DcMotor Front_R;
    public DcMotor Back_R;

    public DcMotor slides;
    public DcMotor intakeMotor;

    public void runOpMode() {
        // Initialize motors
        Front_L = hardwareMap.get(DcMotor.class, "Front_L");
        Back_L = hardwareMap.get(DcMotor.class, "Back_L");
        Front_R = hardwareMap.get(DcMotor.class, "Front_R");
        Back_R = hardwareMap.get(DcMotor.class, "Back_R");

        slides = hardwareMap.get(DcMotor.class, "slides");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
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

            // gamepad 2

            //When the a button is pressed, the power of the horizontal slides motor is set to max
            if(gamepad2.a){
                intakeMotor.setPower(1);
            } else if (gamepad2.b) {
                intakeMotor.setPower(-1);
            } else {
                intakeMotor.setPower(0);
            }

            if(gamepad1.y){
                extend();
            }


        }
    }
}
