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

        static Action Elevate(DcMotor liftRight, DcMotor liftLeft, double power){
            // max = 3091 & 3114 ticks, 56 - 17 = 39 inches
            // min = 0 ticks, 0 inches
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        if(!(liftRight.getCurrentPosition() > 2991 && power > 0)){
                            liftRight.setPower(power);
                            liftLeft.setPower(power);
                        }else{
                            liftRight.setPower(0);
                            liftLeft.setPower(0);
                        }
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action elevate(DcMotor liftRight, DcMotor liftLeft, double power) {
            return Elevate(liftRight, liftLeft, power);
        }
    }

    // intake class
    public static class intake{
        // extends the intake arm
        // made like this to allow parameters
        // replace static with class... maybe?
        static Action Extend(DcMotorEx slides, double power){
            // max value = 628 ticks
            // min value = 0 ticks
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        slides.setMode(RUN_USING_ENCODER);
                        if(!(slides.getCurrentPosition() > 628 && power > 0)){
                            slides.setPower(power);
                        }else{
                            slides.setPower(0);
                        }
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
        static Action ExtendOut(DcMotorEx slides, double in){
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
                                break
                        };
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

        public static Action extendOut(DcMotorEx slides, double in) {
            return ExtendOut(slides, in);
        }

        static Action Spin(DcMotorEx intake, double power){
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

        public static Action spin(DcMotorEx intake, double power){
            return Spin(intake, power);
        }

        // servo stuff

        /*
        left: max = 0.5, min = 0.05
        right: max = 0.55, min = 0.05
         */


        // idea is that it would get a velocity on how much to add or subtract from the current position...
        public static Action Rotate(Servo center, double velocity){
            return new Action() {
                double position;
                private boolean initalized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){

                        center.setPosition(velocity);
                        initalized = true;
                    }
                    return false;
                }
            };
        }

        public static Action rotate(Servo center, double velocity) {
            return Rotate(center, velocity);
        }

        public static Action Tilt(Servo left, Servo right, boolean up){
            return new Action() {
                private boolean initalized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){
                        if (up) {
                            left.setPosition(0.5);
                            right.setPosition(0.5);
                        } else {
                            left.setPosition(0.05);
                            right.setPosition(0.05);
                        }
                        initalized = true;
                    }
                    return false;
                }
            };
        }

        public static Action tilt(Servo left, Servo right, boolean up) {
            return Tilt(left, right, up);
        }
    }
}
