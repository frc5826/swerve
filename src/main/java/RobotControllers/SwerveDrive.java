package RobotControllers;

import interfaces.JoysticksInterface;
import interfaces.RobotController;
import interfaces.RobotInterface;
import interfaces.SwerveWheelInterface;

import java.util.List;

public class SwerveDrive implements RobotController {
    @Override // this tells Java that the `loop` method implements the `loop` method specified in `RobotController`
    public void loop(JoysticksInterface joysticks, RobotInterface robot) {

        double wheelVelocity;
        double wheelAngle;

        double transitionalVelocity = (Math.sqrt(Math.pow(joysticks.getLeftStick().x - 0, 2) + Math.pow(joysticks.getLeftStick().y - 0, 2))) * 2;
        double transitionalAngle = Math.atan2(joysticks.getLeftStick().x,  joysticks.getLeftStick().y);
        double[] transitionalVectorXY;
        if (transitionalVelocity < 0.05)
            transitionalVelocity = 0;

        double rotationalVelocity;
        double rotationalAngle;
        double[] rotationalVectorXY;

        double finalVectorX;
        double finalVectorY;

        List<SwerveWheelInterface> drivetrain = robot.getDrivetrain();

        for(int i = 0; i < drivetrain.size(); i++) {
            SwerveWheelInterface wheel = drivetrain.get(i);

            rotationalVelocity = (joysticks.getRightStick().y * Math.sqrt(Math.pow(wheel.getPosition().x - 0, 2) + Math.pow(wheel.getPosition().y - 0, 2))) * 2;
            rotationalAngle = Math.atan2(-wheel.getPosition().y, wheel.getPosition().x);
            if (rotationalVelocity < 0.05 && rotationalVelocity > -0.05)
                rotationalVelocity = 0;

            transitionalVectorXY = vector(transitionalVelocity, transitionalAngle);
            rotationalVectorXY = vector(rotationalVelocity, rotationalAngle);

            finalVectorX = transitionalVectorXY[0] + rotationalVectorXY[0];
            finalVectorY = transitionalVectorXY[1] + rotationalVectorXY[1];

            wheelVelocity = Math.sqrt(Math.pow(finalVectorX - 0, 2) + Math.pow(finalVectorY - 0, 2));
            wheelAngle = calculateAngle(finalVectorX, finalVectorY);

            wheel.setWheelVelocity(wheelVelocity);
            wheel.setWheelAngle(wheelAngle);
        }
    }

     static double[] vector(double velocity, double angle) {

        double referenceAngle = 0;
        double x = 0;
        double y = 0;
        double[] vector = new double[2];

        if (angle >= 0 && angle <= 1.5708) {
            referenceAngle = 1.5708 - angle;
            x = Math.cos(referenceAngle) * velocity;
            y = Math.sin(referenceAngle) * velocity;
        }
        else if (angle > 1.5708) {
            referenceAngle = angle - 1.5708;
            x = Math.cos(referenceAngle) * velocity;
            y = -(Math.sin(referenceAngle) * velocity);
        }
        else if (angle < 0 && angle >= -1.5708) {
            referenceAngle = 1.5708 + angle;
            x = -(Math.cos(referenceAngle) * velocity);
            y = Math.sin(referenceAngle) * velocity;
        }
        else if (angle < -1.5708) {
            referenceAngle = -(1.5708 + angle);
            x = -(Math.cos(referenceAngle) * velocity);
            y = -(Math.sin(referenceAngle) * velocity);
        }

        vector[0] = x;
        vector[1] = y;

        return vector;
    }

    static double calculateAngle(double x, double y) {
        double angle;

        if (x >= 0 && y >= 0)
            angle = Math.toRadians(90) - Math.atan2(y, x);
        else if (x > 0 && y < 0)
            angle = Math.toRadians(90) + Math.atan2(y, x);
        else if (x < 0 && y > 0)
            angle = -(Math.toRadians(90) + Math.atan2(y, x));
        else if (x <= 0 && y <= 0);
            angle = -Math.toRadians(90) + Math.atan2(y, x);

        return angle;
    }
}
