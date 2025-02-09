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

        Action pushSpecimens = myBot.getDrive().actionBuilder(new Pose2d(-15, 63, Math.toRadians(90)))
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
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-1, 33), Math.toRadians(90)), Math.toRadians(270))

                // goes and grabs third specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs third specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-3, 33), Math.toRadians(90)), Math.toRadians(270))

                // goes and grabs fourth specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs fourth specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-5, 33), Math.toRadians(90)), Math.toRadians(270))

                // goes and grabs fifth specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs fifth specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-7, 33), Math.toRadians(90)), Math.toRadians(270))

                // parks
                .strafeToSplineHeading(new Vector2d(-47, 60), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                .build();

        Action grabSpecimens = myBot.getDrive().actionBuilder(new Pose2d(-15, 63, Math.toRadians(90)))
                // hang starting specimen
                .strafeToSplineHeading(new Vector2d(-15, 60), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(1, 33), Math.toRadians(90)), Math.toRadians(270))
                .waitSeconds(0.4)

                .splineToSplineHeading(new Pose2d(new Vector2d(-17, 46), Math.toRadians(225)), Math.toRadians(180))

                .strafeToSplineHeading(new Vector2d(-31, 46), Math.toRadians(225))
                .strafeToSplineHeading(new Vector2d(-35.5, 46), Math.toRadians(135))

                .strafeToSplineHeading(new Vector2d(-40, 46), Math.toRadians(225))
                .strafeToSplineHeading(new Vector2d(-44.5, 46), Math.toRadians(135))

                .strafeToSplineHeading(new Vector2d(-49, 46), Math.toRadians(225))
                .strafeToSplineHeading(new Vector2d(-48, 50), Math.toRadians(90))

                // grabs second specimen
                .splineToSplineHeading(new Pose2d(new Vector2d(-47, 45), Math.toRadians(90)), Math.toRadians(90))
                .strafeToSplineHeading(new Vector2d(-47, 67), Math.toRadians(90))

                // goes and hangs second specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-1, 33), Math.toRadians(90)), Math.toRadians(270))

                // goes and grabs third specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs third specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-3, 33), Math.toRadians(90)), Math.toRadians(270))

                // goes and grabs fourth specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs fourth specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-5, 33), Math.toRadians(90)), Math.toRadians(270))

                // goes and grabs fifth specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                // goes and hangs fifth specimen
                .strafeToSplineHeading(new Vector2d(-26.5, 50), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-7, 33), Math.toRadians(90)), Math.toRadians(270))

                // parks
                .strafeToSplineHeading(new Vector2d(-47, 60), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-47, 67), Math.toRadians(90)), Math.toRadians(90))

                .build();

        // chose which auto you want to run here
        myBot.runAction(pushSpecimens);

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}