package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        Action leftRedAll = myBot.getDrive().actionBuilder(new Pose2d(-11.4, -61.5, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-47,-47), Math.toRadians(45))
                .waitSeconds(1) // goes to hook on starting sample

                .turn(Math.toRadians(45))
                .waitSeconds(1)
                .turn(Math.toRadians(-45)) // grabs first sample and puts away
                .waitSeconds(1)

                .strafeToLinearHeading(new Vector2d(-58, -47), Math.toRadians(90))
                .waitSeconds(1) // grabs second sample

                .strafeToLinearHeading(new Vector2d(-47, -47), Math.toRadians(45))
                .waitSeconds(1) // puts away second sample

                .strafeToLinearHeading(new Vector2d(-58, -47), Math.toRadians(120))
                .waitSeconds(1) // grabs third sample

                .strafeToLinearHeading(new Vector2d(-47, -47), Math.toRadians(45))
                .waitSeconds(1) // puts away third sample


                .splineTo(new Vector2d(-25,12), Math.toRadians(0))
                .waitSeconds(1) // just to show ending

                .build();

        Action leftBlueAll = myBot.getDrive().actionBuilder(new Pose2d(12, 60, Math.toRadians(270)))
                .strafeToLinearHeading(new Vector2d(47,47), Math.toRadians(225))
                .waitSeconds(1) // goes to hook on starting sample

                .turn(Math.toRadians(45))
                .waitSeconds(1)
                .turn(Math.toRadians(-45)) // grabs first sample and puts away
                .waitSeconds(1)

                .strafeToLinearHeading(new Vector2d(58, 47), Math.toRadians(270))
                .waitSeconds(1) // grabs second sample

                .strafeToLinearHeading(new Vector2d(47, 47), Math.toRadians(225))
                .waitSeconds(1) // puts away second sample

                .splineTo(new Vector2d(30,0), Math.toRadians(180))
                .waitSeconds(1) // just to show ending

                .build();

        Action rightRedSpecimens = myBot.getDrive().actionBuilder(new Pose2d(11, -61.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(0,-38), Math.toRadians(270)) // puts away starting specimen

                .splineToLinearHeading(new Pose2d(35, -36, Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(35, -11)) // gets behind the red samples

                .splineToLinearHeading(new Pose2d(48, -11, Math.toRadians(270)), Math.toRadians(270))
                .strafeTo(new Vector2d(48, -46)) // moves first sample into observation zone

                .strafeTo(new Vector2d(48, -11))
                .splineToLinearHeading(new Pose2d(58, -11, Math.toRadians(270)), Math.toRadians(270))
                .strafeTo(new Vector2d(58, -46)) // moves second sample into observation zone

                .strafeToLinearHeading(new Vector2d(48, -25.5), 0)
                .strafeToLinearHeading(new Vector2d(58, -46), Math.toRadians(270)) // moves third sample into observation zone

                .strafeTo(new Vector2d(48, -46))
                .splineToLinearHeading(new Pose2d(1.5,-38, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(1) // collects and puts away first specimen

                .strafeTo(new Vector2d(48, -46))
                .strafeTo(new Vector2d(3, -38))
                .waitSeconds(1) // collects and puts away second specimen

                .strafeTo(new Vector2d(48, -46))
                .strafeTo(new Vector2d(4.5, -38))
                .waitSeconds(1) // collects and puts away third specimen

                .strafeToLinearHeading(new Vector2d(63, -63), Math.toRadians(90)) // parks into observation zone

                .build();

        Action rightRedSpecimensLower = myBot.getDrive().actionBuilder(new Pose2d(11, -61.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(0,-38), Math.toRadians(270)) // puts away starting specimen

                .strafeToLinearHeading(new Vector2d(36, -42), Math.toRadians(45))

                .strafeToLinearHeading(new Vector2d(45, -42), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(48, -42), Math.toRadians(45))
                .turn(Math.toRadians(-135))

                .strafeToLinearHeading(new Vector2d(48, -25.5), 0)
                .strafeToLinearHeading(new Vector2d(48, -42), Math.toRadians(270)) // moves third sample into observation zone

                // puts away specimens

                .strafeTo(new Vector2d(48, -46))
                .splineToLinearHeading(new Pose2d(1.5,-38, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(1) // collects and puts away first specimen

                .strafeTo(new Vector2d(48, -46))
                .strafeTo(new Vector2d(3, -38))
                .waitSeconds(1) // collects and puts away second specimen

                .strafeTo(new Vector2d(48, -46))
                .strafeTo(new Vector2d(4.5, -38))
                .waitSeconds(1) // collects and puts away third specimen

                .strafeToLinearHeading(new Vector2d(63, -63), Math.toRadians(90)) // parks into observation zone


                .build();

        Action emote = myBot.getDrive().actionBuilder(new Pose2d(0, 0, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-8, 0), Math.toRadians(0))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(-16, 0), Math.toRadians(0))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(-15, 0), Math.toRadians(0))
                .build();

        Action right = myBot.getDrive().actionBuilder(new Pose2d(-15, 63, Math.toRadians(90)))
                // hang starting specimen
                .strafeToSplineHeading(new Vector2d(-15, 60), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(1, 33), Math.toRadians(90)), Math.toRadians(270))
                .waitSeconds(0.4)

                // get behind samples
                .splineToLinearHeading(new Pose2d(new Vector2d(-23, 45), Math.toRadians(180)), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(new Vector2d(-36, 32), Math.toRadians(270)), Math.toRadians(270))
                .strafeToSplineHeading(new Vector2d(-36, 21), Math.toRadians(270))

                // sample 1
                .splineToLinearHeading(new Pose2d(new Vector2d(-44, 21), Math.toRadians(270)), Math.toRadians(90))
                .strafeToSplineHeading(new Vector2d(-44, 50), Math.toRadians(270))

                // sample 2
                .splineToLinearHeading(new Pose2d(new Vector2d(-46, 48), Math.toRadians(270)), Math.toRadians(270))
                .strafeToSplineHeading(new Vector2d(-46, 21), Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(new Vector2d(-55, 21), Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(-55, 50))

                // goal: .13 less
                /*
                // sample 3
                .strafeToSplineHeading(new Vector2d(-55, 19), Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(new Vector2d(-62, 21), Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(-62, 56))
                */

                // grabs second specimen
                .splineToSplineHeading(new Pose2d(new Vector2d(-47, 45), Math.toRadians(90)), Math.toRadians(90))
                .strafeToSplineHeading(new Vector2d(-47, 67), Math.toRadians(90))

                // goes and hangs second specimen
                //.strafeToLinearHeading(new Vector2d(-1, 31), Math.toRadians(90))

                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-6, 33), Math.toRadians(90)), Math.toRadians(270))


                // 14.94
                // goes and grabs third specimen

                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs third specimen
                /*
                .splineToLinearHeading(new Pose2d(new Vector2d(-25.5, 50), Math.toRadians(90)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(new Vector2d(-4, 33), Math.toRadians(90)), Math.toRadians(270))
                .strafeToSplineHeading(new Vector2d(-47, 65), Math.toRadians(90))
                /*
                // goes and grabs fourth specimen
                .strafeToSplineHeading(new Vector2d(-4, 35), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-25.5, 50), Math.toRadians(90)), Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs fourth specimen
                .strafeToSplineHeading(new Vector2d(-47, 65), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-24.5, 50), Math.toRadians(90)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(new Vector2d(-2, 33), Math.toRadians(90)), Math.toRadians(270))

                // goes and grabs fifth specimen
                .strafeToSplineHeading(new Vector2d(-2, 35), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-24.5, 50), Math.toRadians(90)), Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                /*
                // goes and hangs fifth specimen
                .strafeToSplineHeading(new Vector2d(-47, 65), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-23.5, 50), Math.toRadians(90)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(new Vector2d(0, 33), Math.toRadians(90)), Math.toRadians(270))

                // parks
                .strafeToSplineHeading(new Vector2d(0, 35), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-23.5, 50), Math.toRadians(90)), Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                 */


                .build();

        Action left = myBot.getDrive().actionBuilder(new Pose2d(15, 63, Math.toRadians(270)))
                // goes behind samples
                .splineToLinearHeading(new Pose2d(new Vector2d(36, 36), Math.toRadians(270)), Math.toRadians(270))
                .strafeTo(new Vector2d(36, 12))

                // pushes first sample back, and pushes it to the side
                .splineToLinearHeading(new Pose2d(new Vector2d(44, 12), Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(44, 48))
                .strafeTo(new Vector2d(36, 48))
                .splineToLinearHeading(new Pose2d(new Vector2d(42, 58), Math.toRadians(270)), Math.toRadians(0))
                .strafeTo(new Vector2d(54, 58))

                // second sample
                .strafeTo(new Vector2d(48, 58))
                .splineToLinearHeading(new Pose2d(new Vector2d(36, 28), Math.toRadians(270)), Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(new Vector2d(52, 14), Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(52, 56))

                //park
                .strafeTo(new Vector2d(52, 40))
                .splineToLinearHeading(new Pose2d(new Vector2d(26, 12), Math.toRadians(180)), Math.toRadians(180))
                .build();

        // chose which auto you want to run here
        myBot.runAction(right);

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}