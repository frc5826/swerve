package RobotControllers;

import interfaces.JoysticksInterface;
import interfaces.RobotController;
import interfaces.RobotInterface;
import interfaces.SwerveWheelInterface;

import java.util.List; // test

public class FieldOrientedSwerve implements RobotController {
    @Override
    public void loop(JoysticksInterface joysticks, RobotInterface robot) {

        double[] transitionalVectorXY;
        double[] rotationalVectorXY;

        List<SwerveWheelInterface> drivetrain = robot.getDrivetrain();

        for(int i = 0; i < drivetrain.size(); i++) {
            SwerveWheelInterface wheel = drivetrain.get(i);

            //find coords of joystick
            double lJoystickX = joysticks.getLeftStick().x;
            double lJoystickY = joysticks.getLeftStick().y;
            double rJoystickX = joysticks.getRightStick().x;
            double rJoystickY = joysticks.getRightStick().y;

            //find wheel coords
            double wheelX = wheel.getPosition().x;
            double wheelY = wheel.getPosition().y;

            //calculate the vectors of the transitional and rotational swerve ([0] of array is x and [1] is y)
            transitionalVectorXY = vector(getTransitionalVelocity(lJoystickX, lJoystickY), fieldOriented(robot.getHeading(), Math.atan2(joysticks.getLeftStick().x,  joysticks.getLeftStick().y)));
            rotationalVectorXY = vector(getRotationalVelocity(rJoystickX, rJoystickY), getRotationalAngle(wheelX, wheelY));

            //calculate and set the wheel velocity and angle
            wheel.setWheelVelocity(getWheelVelocity(transitionalVectorXY[0] + rotationalVectorXY[0], transitionalVectorXY[1] + rotationalVectorXY[1]));
            wheel.setWheelAngle(calculateAngle(transitionalVectorXY[0] + rotationalVectorXY[0], transitionalVectorXY[1] + rotationalVectorXY[1]));
        }
    }

    //read the names
    public static double getTransitionalVelocity(double x, double y) { return (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) * 2; }

    public static double getRotationalVelocity(double x, double y) { return (y * Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) * 2; }

    public static double getRotationalAngle(double x, double y) { return Math.atan2(-y, x); }

    public static double getWheelVelocity(double vectorX, double vectorY) { return Math.sqrt(Math.pow(vectorX, 2) + Math.pow(vectorY, 2)); }


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

    static double fieldOriented(double robotAngle, double joystickAngle) {
        double fieldOrientedAngle = robotAngle;
        final double PI = Math.PI;

        if (joystickAngle >= 0 && robotAngle >= 0){
            if (robotAngle > joystickAngle)
                fieldOrientedAngle = robotAngle + joystickAngle;
            else if (robotAngle < joystickAngle)
                fieldOrientedAngle = robotAngle - joystickAngle;
        }
        else if (joystickAngle >= 0 && robotAngle <= 0)
            fieldOrientedAngle = robotAngle + joystickAngle;
        else if (joystickAngle <= 0 && robotAngle >= 0)
            fieldOrientedAngle = robotAngle + joystickAngle;
        else if (joystickAngle <= 0 && robotAngle <= 0)
            fieldOrientedAngle = joystickAngle + robotAngle;

        if (fieldOrientedAngle > PI)
            fieldOrientedAngle = fieldOrientedAngle - Math.toRadians(360);
        if (fieldOrientedAngle < -PI)
            fieldOrientedAngle = fieldOrientedAngle + Math.toRadians(360);

        return fieldOrientedAngle;
    }
}
