package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
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
                .waitSeconds(1) // goes to put away starting sample

                .turn(Math.toRadians(45))
                .waitSeconds(1)
                .turn(Math.toRadians(-45)) // grabs first sample and puts away
                .waitSeconds(1)

                .strafeToLinearHeading(new Vector2d(-58, -47), Math.toRadians(120))
                .waitSeconds(1) // grabs third sample

                .strafeToLinearHeading(new Vector2d(-47, -47), Math.toRadians(45))
                .waitSeconds(1) // puts away third sample

                .strafeToLinearHeading(new Vector2d(-58, -47), Math.toRadians(90))
                .waitSeconds(1) // grabs second sample

                .strafeToLinearHeading(new Vector2d(-47, -47), Math.toRadians(45))
                .waitSeconds(1) // puts away second sample

                .splineTo(new Vector2d(-25,12), Math.toRadians(0))
                .waitSeconds(1) // just to show ending

                .build();

        Action rightRed = myBot.getDrive().actionBuilder(new Pose2d(11, -61.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(0,-38), Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(35, -36, Math.toRadians(270)), Math.toRadians(90))
                .strafeTo(new Vector2d(35, -11)) // gets behind the red samples

                //.strafeTo(new Vector2d(48, -11))
                .splineToLinearHeading(new Pose2d(48, -11, Math.toRadians(270)), Math.toRadians(270))
                .strafeTo(new Vector2d(48, -36))
                //.strafeTo(new Vector2d(48, -11))
                .splineToLinearHeading(new Pose2d(58, -11, Math.toRadians(270)), Math.toRadians(0))
                .strafeTo(new Vector2d(58, -36))

                .build();

        //myBot.runAction(leftRedAll);
        myBot.runAction(rightRed);

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}