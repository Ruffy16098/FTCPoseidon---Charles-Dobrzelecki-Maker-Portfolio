package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

// Non-RR imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
public class actions {

    // drive class
    public static class drive {

        // forward action or whatever
        static Action Forward(){
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        // drive controls here
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action forward() {
            return Forward();
        }
    }

    // lift class
    public static class lift {

        // defines lift motors
        /*private static DcMotor liftRight;
        private static DcMotor liftLeft;
        public  void liftRight(HardwareMap hardwareMap) {
            liftRight = hardwareMap.get(DcMotor.class, "liftRight");
            liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftRight.setDirection(DcMotorSimple.Direction.FORWARD);
            liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        public void liftLeft(HardwareMap hardwareMap) {
            liftLeft = hardwareMap.get(DcMotor.class, "liftLeft");
            liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

         */

        // Lifts the lifts up by a certain amount
        /*
        static Action Elevate(DcMotor liftRight, DcMotor liftLeft, double power){
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        liftRight.setPower(power);
                        liftLeft.setPower(power);
                        initialized = true;
                    }
                    return false;
                }
            };
        }

         */

        static Action Elevate(DcMotor liftRight, DcMotor liftLeft, double power){
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        liftRight.setPower(power);
                        liftLeft.setPower(power);
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action elevate(DcMotor liftRight, DcMotor liftLeft, double power) {
            return Elevate(liftRight, liftLeft, power);
        }

        static Action ElevateOther(DcMotor liftRight, DcMotor liftLeft, double power){
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        liftRight.setPower(power);
                        liftLeft.setPower(0.2);
                        liftLeft.setTargetPosition(liftRight.getCurrentPosition());
                        liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action elevateOther(DcMotor liftRight, DcMotor liftLeft, double power) {
            return Elevate(liftRight, liftLeft, power);
        }

        /*
        static Action ElevateOut(DcMotor liftRight, DcMotor liftLeft, double in){
            in = in * ;
            double finalIn = in;
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        while(liftRight.getCurrentPosition() != ) {
                            if(liftRight.getCurrentPosition() > (int)finalIn){
                                liftRight.setPower(-0.1);
                            }else{
                                liftRight.setPower(0.1);
                            }

                        }
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action elevateOut(DcMotor liftRight, DcMotor liftLeft, double in) {
            return Elevate(liftRight, liftLeft, in);
        }

         */

        /*static Action Correct(DcMotor liftRight, DcMotor liftLeft){
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        while(liftLeft.getCurrentPosition() != liftRight.getCurrentPosition()) {
                            if(liftLeft.getCurrentPosition() > liftRight.getCurrentPosition()){
                                liftLeft.setPower(-0.2);
                            }else{
                                liftLeft.setPower(0.2);
                            }

                        }
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action correct(DcMotor liftRight, DcMotor liftLeft) {
            return Correct(liftRight, liftLeft);
        }

         */
    }

    // intake class
    public static class intake{

        // define intake motors and servos
        /*public static DcMotor slides;
        private DcMotor intakeMotor;
        private Servo intakeServoRight;
        private Servo intakeServoLeft;
        public void slides(HardwareMap hardwareMap) {
            slides = hardwareMap.get(DcMotor.class, "slides");
            slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slides.setDirection(DcMotorSimple.Direction.FORWARD);
            slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        public void intakeMotor(HardwareMap hardwareMap) {
            intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
            intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        public void intakeServoRight(HardwareMap hardwareMap) {
            intakeServoRight = hardwareMap.get(Servo.class, "setIntakeServoRight");
            intakeServoRight.setDirection(Servo.Direction.FORWARD);
        }
        public void intakeServoLeft(HardwareMap hardwareMap) {
            intakeServoLeft = hardwareMap.get(Servo.class, "intakeServoLeft");
            intakeServoLeft.setDirection(Servo.Direction.REVERSE);
        }

         */

        // extends the intake arm
        // made like this to allow parameters
        // replace static with class... maybe?
        static Action Extend(DcMotorEx slides, double power){
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        //if(!(slides.getCurrentPosition() >= 628 && power > 0) && !(slides.getCurrentPosition() <= 0 && power < 0)){
                        slides.setVelocity(power);
                       // }else{
                            //slides.setPower(0);
                        //}
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action extend(DcMotorEx slides, double power) {
            return Extend(slides, power);
        }

        // extends out by however inches
        static Action ExtendOut(DcMotorEx slides, Gamepad gamepad2, double in){
            // setTargetPosition

            // inches
            // 20 7/8 = 20.875

            // ticks
            // 628

            // math
            // 628 / 20.875 = 30.08383233532934

            // ticks per inch
            in = in * 30.08383233532934;
            double finalIn = in;
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        /*while(slides.getCurrentPosition() != (int)finalIn) {
                            if(slides.getCurrentPosition() > (int)finalIn){
                                slides.setPower(-0.3);
                            }else{
                                slides.setPower(0.3);
                            }
                            //abandons the movement/extension for if the intake gets stuck on something
                            if(gamepad2.right_bumper){
                                break;
                            }
                        }
                         */
                        slides.setPower(0.3);
                        slides.setTargetPosition((int) finalIn);
                        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action extendOut(DcMotorEx slides, double in, Gamepad gamepad2) {
            return ExtendOut(slides, gamepad2, in);
        }

        static Action Spin(DcMotor intake, double power){
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        intake.setPower(power);
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action spin(DcMotor intake, double power){
            return Spin(intake, power);
        }
    }
}
