package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static java.lang.Math.max;
import static java.lang.Math.min;

import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

// Non-RR imports
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

        static Action Elevate(DcMotor liftRight, DcMotor liftLeft, double power) {
            // max = 3091 & 3114 ticks, 56 - 17 = 39 inches
            // min = 0 ticks, 0 inches
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        liftRight.setMode(RUN_USING_ENCODER);
                        liftLeft.setMode(RUN_USING_ENCODER);
                        if (!(liftRight.getCurrentPosition() > 3500 && power > 0)){
                            liftRight.setPower(power);
                            liftLeft.setPower(power);
                        } else {
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

        // elevates out by however inches
        static Action ElevateOut(DcMotor liftRight, DcMotor liftLeft, double in){
            // max = 3091 & 3114 ticks, 56 - 17 = 39 inches
            // min = 0 ticks, 0 inches

            // ticks per inch
            in = in * 79.84615384615385;
            double finalIn = in;
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {

                        // sets lifts to run to a certain spot in inches
                        liftRight.setPower(0.8);
                        liftRight.setTargetPosition((int) finalIn);
                        liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                        liftLeft.setPower(0.8);
                        liftLeft.setTargetPosition((int) finalIn);
                        liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        initialized = true;


                        // stops the left lift trying to go down when cant
                        // saves power
                        if (liftRight.getCurrentPosition() < 0) {
                            liftLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                            liftRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                        }
                    }
                    return false;
                }
            };
        }

        public static Action elevateOut(DcMotor liftRight, DcMotor liftLeft, double in) {
            return ElevateOut(liftRight, liftLeft, in);
        }

        public static Action Rotate(Servo center, double velocity){
            return new Action() {
                double position;
                private boolean initalized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){
                        position = center.getPosition();
                        if (position == 1 && velocity > 0) {
                            position = 1;
                        } else if (position == 0 && velocity < 0) {
                            position = 0;
                        } else {
                            position += velocity;
                        }
                        center.setPosition(position);
                        initalized = true;
                    }
                    return false;
                }
            };
        }

        public static Action rotate(Servo center, double velocity) {
            return Rotate(center, velocity);
        }

        public static Action RotatePos(Servo center, int caseNum){
            return new Action() {
                double position;
                private boolean initalized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){
                        switch(caseNum){
                            // Should be open case
                            case 0:
                                center.setPosition(0);
                                break;
                            // Should be closed case
                            case 1:
                                center.setPosition(1);
                                break;
                            case 2:
                                center.setPosition(0.1);
                                break;
                        }
                    }
                    return false;
                }
            };
        }

        public static Action rotatePos(Servo center, int caseNum) {
            return RotatePos(center, caseNum);
        }

        static Action Outtake (Servo right, Servo left, int pos){
            // pos 0 = set down, pos 1 = specimen, pos 2 = bucket
            return new Action() {
                private boolean initalized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){
                        switch(pos){
                            case 0:
                                left.setPosition(0.05);
                                right.setPosition(0.05);
                                break;
                            case 1:
                                left.setPosition(.98);
                                right.setPosition(.98);
                                break;
                            case 2:
                                left.setPosition(0.8);
                                right.setPosition(0.8);
                                break;
                        }
                        initalized = true;
                    }
                    return false;
                }
            };
        }

        public static Action outtake(Servo right, Servo left, int pos) {
            return Outtake(right, left, pos);
        }


        static Action Specimen(DcMotor liftRight, DcMotor liftLeft, Servo center){
            return new Action() {
                private boolean initalized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if(!initalized) {
                        // save for later
                        // lift right 880
                        // 880 / 79.84615384615385 = 11.021
                        Actions.runBlocking(elevateOut(liftRight, liftLeft, 10));
                        if(liftRight.getCurrentPosition() <= 880){
                            Actions.runBlocking(rotatePos(center, 1));
                        }
                    }
                    initalized = true;
                    return false;
                }
            };
        }

        public static Action specimen(DcMotor liftRight, DcMotor liftLeft, Servo center) {
            return Specimen(liftRight, liftLeft, center);
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
            // new min value to avoid outtake claw = 226 ticks
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        slides.setMode(RUN_USING_ENCODER);
                        //if(!(slides.getCurrentPosition() > 628 && power > 0)) {
                            //if (!(slides.getCurrentPosition() < 226 && power < 0)) {
                                slides.setPower(power);
                            //}
                        } else {
                            //slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                            slides.setPower(0);
                        }
                        initialized = true;
                    //}
                    return false;
                }
            };
        }

        public static Action extend(DcMotorEx slides, double power) {
            return Extend(slides, power);
        }

        static Action Extend(DcMotorEx slides, Servo left, Servo right, double power){
            // max value = 628 ticks
            // min value = 0 ticks
            // new min value to avoid outtake claw = 226 ticks
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        slides.setMode(RUN_USING_ENCODER);
                        //if (!(slides.getCurrentPosition() > 628 && power > 0)) {
                            if (right.getPosition() <= 0.5 && left.getPosition() <= 0.5 && slides.getCurrentPosition() <= 200) {
                                intake.Tilt(left, right, 0.5);
                            }
                            slides.setPower(power);
                        } else{
                            slides.setPower(0);
                        }
                        initialized = true;
                    //}
                    return false;
                }
            };
        }

        public static Action extend(DcMotorEx slides, Servo right, Servo left, double power) {
            return Extend(slides, right, left, power);
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
        public static Action Rotate(Servo center, double vel){
            double position = vel;
            if (position > 1){
                position = 1;
            } else if (position < 0) {
                position = 0;
            }
            return new Action() {
                private boolean initalized = false;
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){
                        center.setPosition(vel);
                        initalized = true;
                    }
                    return false;
                }
            };
        }

        public static Action rotate(Servo center, double vel) {
            return Rotate(center, vel);
        }

        public static Action RotatePos(Servo center, int caseNum){
            return new Action() {
                double position;
                private boolean initalized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){
                        switch(caseNum){
                            case 0:
                                center.setPosition(0.9);
                                break;
                            case 1:
                                center.setPosition(1);
                                break;
                            case 2:
                                center.setPosition(0.8);
                                break;
                        }
                    }
                    return false;
                }
            };
        }

        public static Action rotatePos(Servo center, int caseNum) {
            return RotatePos(center, caseNum);
        }

        public static Action Tilt(Servo left, Servo right, boolean up){
            return new Action() {
                private boolean initalized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){
                        if (up) {
                            left.setPosition(0.12);
                            right.setPosition(0.12);
                        } else {
                            left.setPosition(0.48);
                            right.setPosition(0.48);
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

        public static Action Tilt(Servo left, Servo right, double pos) {
            return new Action() {
                private boolean initalized = false;
                double futurePos;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initalized){
                        /*double futurePos = max(0.05, pastPos + posChange);
                        futurePos = min(futurePos, 0.5);

                         */
                        if(pos > 1){
                            futurePos = 1;
                        } else if (pos < 0) {
                            futurePos = 0;
                        }else{
                            futurePos = pos;
                        }
                        left.setPosition(futurePos);
                        right.setPosition(futurePos);
                        initalized = true;
                    }
                    return false;
                }
            };
        }

        public static Action tilt(Servo left, Servo right, double pos) {
            return Tilt(left, right, pos);
        }

    }
}
