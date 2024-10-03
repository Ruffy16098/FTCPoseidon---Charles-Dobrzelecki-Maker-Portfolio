package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="testMotor")
public class testMotor extends LinearOpMode {
    public DcMotor slidesHort;
    public DcMotor intakeMotor;

    public void runOpMode() {
        slidesHort = hardwareMap.get(DcMotor.class, "slidesHort");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        waitForStart();
        while (opModeIsActive()) {
            //When the a button is pressed, the power of the horizontal slides motor is set to max
            if(gamepad1.a){
                intakeMotor.setPower(1);
            } else if (gamepad1.b) {
                intakeMotor.setPower(-1);
            } else {
                intakeMotor.setPower(0);
            }
        }
    }
}
