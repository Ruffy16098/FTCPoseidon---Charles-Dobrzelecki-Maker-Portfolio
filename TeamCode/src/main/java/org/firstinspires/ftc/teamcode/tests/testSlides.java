package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.actions.intake;
import static org.firstinspires.ftc.teamcode.actions.lift;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name="testSlides")
public class testSlides extends LinearOpMode {
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


        int in = 0;
        waitForStart();
        while (opModeIsActive()) {
            // intake
            // extend intake out by 0 or 15
            if(gamepad2.y && gamepad2.left_bumper){
                in = 15;
            } else if (gamepad2.x && gamepad2.left_bumper) {
                in = 0;
            }

            if(gamepad2.right_stick_y != 0 && !gamepad2.left_bumper) {
                Actions.runBlocking(intake.extend(slides, -gamepad2.right_stick_y));
                in = (int)(slides.getCurrentPosition()/30.73103448275862);

            }
            Actions.runBlocking(intake.extendOut(slides, in, gamepad2));
            telemetry.addLine("running test for slides");
            telemetry.addData("slides position", slides.getCurrentPosition());
            telemetry.addData("liftRight position", liftRight.getCurrentPosition());
            telemetry.addData("liftLeft position", liftLeft.getCurrentPosition());
            telemetry.update();
        }
    }
}
