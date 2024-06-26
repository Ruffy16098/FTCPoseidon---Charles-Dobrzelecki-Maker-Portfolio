package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "KratosCompetitionTankDrive")
public class KratosCompetitionTankDrive extends LinearOpMode {

  private DcMotor Front_L;
  private DcMotor Back_L;
  private DcMotor Front_R;
  private DcMotor Back_R;
  private DcMotor slidesLeft;
  private DcMotor slidesRight;
  private DcMotor slidesHorizontalRight;
  private DcMotor slidesHorizontalLeft;
  private Servo rightFlipper;
  private Servo leftFlipper;
  private Servo servoTest;
  //private Servo armTiltLeft;
  /*
    private Servo clawTilt;
    private Servo clawRight;
    private Servo clawLeft;
    private Servo planeShooter;
  */


  @Override
  public void runOpMode() throws InterruptedException {
    ElapsedTime timer = new ElapsedTime();
    float leftjoystick_x;
    float leftjoystick_y;
    float rightjoystick_x;
    float rightjoystick_y;

    Front_L = hardwareMap.get(DcMotor.class, "Front_L");
    Back_L = hardwareMap.get(DcMotor.class, "Back_L");
    Front_R = hardwareMap.get(DcMotor.class, "Front_R");
    Back_R = hardwareMap.get(DcMotor.class, "Back_R");

    slidesRight = hardwareMap.get(DcMotor.class, "slidesRight");
    slidesLeft = hardwareMap.get(DcMotor.class, "slidesLeft");
    slidesHorizontalRight = hardwareMap.get(DcMotor.class, "slidesHorizontalRight");
    slidesHorizontalLeft = hardwareMap.get(DcMotor.class, "slidesHorizontalLeft");


    rightFlipper = hardwareMap.get(Servo.class, "rightFlipper");
    leftFlipper = hardwareMap.get(Servo.class, "leftFlipper");

    servoTest = hardwareMap.get(Servo.class, "servoTest");
/*
    //armTiltRight = hardwareMap.get(Servo.class, "armTiltRight");
    //armTiltLeft = hardwareMap.get(Servo.class, "armTiltLeft");
    clawTilt = hardwareMap.get(Servo.class, "clawTilt");
    clawRight = hardwareMap.get(Servo.class, "clawRight");
    clawLeft = hardwareMap.get(Servo.class, "clawLeft");
*/
    slidesLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    slidesLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    slidesLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    slidesLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    slidesRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    slidesRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    slidesRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);





    waitForStart();
    if (opModeIsActive()) {
     /* rightFlipper.setDirection(Servo.Direction.REVERSE);
      rightFlipper.setPosition(0.42);
      leftFlipper.setDirection(Servo.Direction.REVERSE);
      leftFlipper.setPosition(0.6);
*/
      /*armTiltRight.setDirection(Servo.Direction.REVERSE);
      armTiltRight.setPosition(1);
      armTiltLeft.setDirection(Servo.Direction.REVERSE);
      armTiltLeft.setPosition(0); */

      //clawTilt.setDirection(Servo.Direction.REVERSE);
      //clawTilt.setPosition(0); //0
      /*rightFlipper.setPosition(0);  //retracted
      leftFlipper.setPosition(1); */
      while (opModeIsActive()) {
        double seconds = timer.seconds();
        if (gamepad1.right_bumper) { //slow
          leftjoystick_x = gamepad1.left_stick_x / 4;
          leftjoystick_y = gamepad1.left_stick_y / 4;
          rightjoystick_x = gamepad1.right_stick_x / 4;
          rightjoystick_y = gamepad1.right_stick_y / 4;
        } else if (gamepad1.left_bumper) { //fast
          leftjoystick_x = gamepad1.left_stick_x;
          leftjoystick_y = gamepad1.left_stick_y;
          rightjoystick_x = gamepad1.right_stick_x;
          rightjoystick_y = gamepad1.right_stick_y;
        } else { //normal
          leftjoystick_x = (float) (gamepad1.left_stick_x / 1.5);
          leftjoystick_y = (float) (gamepad1.left_stick_y / 1.5);
          rightjoystick_x = (float) (gamepad1.right_stick_x / 1.5);
          rightjoystick_y = (float) (gamepad1.right_stick_y / 1.5);
        }
        if (gamepad1.right_stick_x > 0 && gamepad1.left_stick_x > 0) {
          Front_L.setPower(-((rightjoystick_x + leftjoystick_x) / 2));
          Back_L.setPower((rightjoystick_x + leftjoystick_x) / 2);
          Front_R.setPower(((rightjoystick_x + leftjoystick_x) / 2));
          Back_R.setPower(-(rightjoystick_x + leftjoystick_x) / 2);
        } else if (gamepad1.right_stick_x < 0 && gamepad1.left_stick_x < 0) {
          Front_L.setPower(-((rightjoystick_x + leftjoystick_x) / 2));
          Back_L.setPower((rightjoystick_x + leftjoystick_x) / 2);
          Front_R.setPower(((rightjoystick_x + leftjoystick_x) / 2));
          Back_R.setPower(-(rightjoystick_x + leftjoystick_x) / 2);
        } else {
          Front_L.setPower(leftjoystick_y);
          Back_L.setPower(leftjoystick_y);
          Front_R.setPower(rightjoystick_y);

          Back_R.setPower(rightjoystick_y);
        }

        slidesLeft.setPower(gamepad2.left_stick_y/2);
        slidesRight.setPower(gamepad2.left_stick_y/2);

        slidesHorizontalRight.setPower(gamepad2.right_stick_y/3);
        slidesHorizontalLeft.setPower(-gamepad2.right_stick_y/3);


        if (gamepad2.left_bumper) { // open
          rightFlipper.setDirection(Servo.Direction.FORWARD);
          rightFlipper.setPosition(0.2);
          leftFlipper.setDirection(Servo.Direction.FORWARD);
          leftFlipper.setPosition(0.8);
        } else if(gamepad2.right_bumper) { // retracted
          rightFlipper.setDirection(Servo.Direction.REVERSE);
          rightFlipper.setPosition(0.45);
          leftFlipper.setDirection(Servo.Direction.REVERSE);
          leftFlipper.setPosition(0.6);
        }

        if(gamepad2.b) { //arm up
          servoTest.setPosition(1);
        /*armTiltRight.setDirection(Servo.Direction.REVERSE);
        armTiltRight.setPosition(0.4); //0
        armTiltLeft.setDirection(Servo.Direction.REVERSE);
        armTiltLeft.setPosition(0.6); //1 */

          //clawTilt.setDirection(Servo.Direction.REVERSE);
          //clawTilt.setPosition(0.4); //0

        } else if(gamepad2.a) { //arm down
          servoTest.setPosition(0);
        /*armTiltRight.setDirection(Servo.Direction.REVERSE);
        armTiltRight.setPosition(1);
        armTiltLeft.setDirection(Servo.Direction.REVERSE);
        armTiltLeft.setPosition(0); */

          //clawTilt.setDirection(Servo.Direction.REVERSE);
          //clawTilt.setPosition(0); //0


        }

        if (gamepad2.y) { // open   y
          /*clawRight.setDirection(Servo.Direction.FORWARD);
          clawRight.setPosition(0);
          clawLeft.setDirection(Servo.Direction.FORWARD);
          clawLeft.setPosition(1); */
        } else if(gamepad2.x) { // retracted    x
          /*clawRight.setDirection(Servo.Direction.FORWARD);
          clawRight.setPosition(0.1);
          clawLeft.setDirection(Servo.Direction.FORWARD);
          clawLeft.setPosition(0.9); */
        }

        if(gamepad2.dpad_up) {
          //planeShooter.setDirection(Servo.Direction.REVERSE);
          //planeShooter.setPosition(0.4);

        } else {
          //planeShooter.setDirection(Servo.Direction.FORWARD);
          //planeShooter.setPosition(0.2);
        }

        telemetry.addData("slidesLeft position", slidesLeft.getCurrentPosition()); //3472
        telemetry.addData("slidesRight position", slidesRight.getCurrentPosition());
        telemetry.update();
      }
    }
  }
}