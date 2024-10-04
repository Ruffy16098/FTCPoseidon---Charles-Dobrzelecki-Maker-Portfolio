package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

// Non-RR imports
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class actions {

    // lift class
    public static class Lift {

        // defines lift motors
        private static DcMotor liftRight;
        private static DcMotor liftLeft;
        public void liftRight(HardwareMap hardwareMap) {
            liftRight = hardwareMap.get(DcMotor.class, "liftRight");
            liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftRight.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        public void liftLeft(HardwareMap hardwareMap) {
            liftLeft = hardwareMap.get(DcMotor.class, "liftLeft");
            liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        // Lifts the lifts up by a certain amount
        // example code from RR docs
        public static class LiftUp implements Action {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    liftRight.setPower(0.8);
                    initialized = true;
                }

                double pos = liftRight.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos < 3000.0) {
                    return true;
                } else {
                    liftRight.setPower(0);
                    return false;
                }
            }
        }

        public Action liftUp() {
            return new LiftUp();
        }
    }

    // intake class
    public static class intake{

        // define intake motors and servos
        private static DcMotor slides;
        private DcMotor intakeMotor;
        private Servo intakeServoRight;
        private Servo intakeServoLeft;
        public void slides(HardwareMap hardwareMap) {
            slides = hardwareMap.get(DcMotor.class, "slides");
            slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slides.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        public void intakeMotor(HardwareMap hardwareMap) {
            intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
            intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        public void intakeServoRight(HardwareMap hardwareMap) {
            intakeServoRight = hardwareMap.get(Servo.class, "setIntakeServoRight");
            intakeServoRight.setDirection(Servo.Direction.FORWARD);
        }
        public void intakeServoLeft(HardwareMap hardwareMap) {
            intakeServoLeft = hardwareMap.get(Servo.class, "intakeServoLeft");
            intakeServoLeft.setDirection(Servo.Direction.REVERSE);
        }

        // extends the intake arm
        // made like this to allow parameters
        static Action Extend(){
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (!initialized) {
                        slides.setPower(0.8);
                        initialized = true;
                    }
                    return false;
                }
            };
        }

        public static Action extend() {
            return Extend();
        }
    }
}
