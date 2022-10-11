package RobotControllers;

import interfaces.JoysticksInterface;
import interfaces.RobotController;
import interfaces.RobotInterface;
import interfaces.SwerveWheelInterface;

import java.util.List;

public class SwerveDrive implements RobotController {
    @Override // this tells Java that the `loop` method implements the `loop` method specified in `RobotController`
    public void loop(JoysticksInterface joysticks, RobotInterface robot) {

        double[] transitionalVectorXY;
        double[] rotationalVectorXY;

        //get every wheel on the robot
        List<SwerveWheelInterface> drivetrain = robot.getDrivetrain();

        //loop through every wheel one at a time
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
            transitionalVectorXY = vector(getTransitionalVelocity(lJoystickX, lJoystickY), getTransitionalAngle(lJoystickX, lJoystickY));
            rotationalVectorXY = vector(getRotationalVelocity(rJoystickX, rJoystickY), getRotationalAngle(wheelX, wheelY));

            //calculate and set the wheel velocity and angle
            wheel.setWheelVelocity(getWheelVelocity(transitionalVectorXY[0] + rotationalVectorXY[0], transitionalVectorXY[1] + rotationalVectorXY[1]));
            wheel.setWheelAngle(getWheelAngle(transitionalVectorXY[0] + rotationalVectorXY[0], transitionalVectorXY[1] + rotationalVectorXY[1]));
        }
    }

    //just read the titles on these i dont wanna type comments above them all
    public static double getTransitionalVelocity(double x, double y) { return (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) * 2; }

    public static double getTransitionalAngle(double x, double y) { return Math.atan2(x, y); }

    public static double getRotationalVelocity(double x, double y) { return (y * Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) * 2; }

    public static double getRotationalAngle(double x, double y) { return Math.atan2(-y, x); }

    public static double getWheelVelocity(double vectorX, double vectorY) { return Math.sqrt(Math.pow(vectorX, 2) + Math.pow(vectorY, 2)); }

    public static double getWheelAngle(double x, double y) {
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

    public static double[] vector(double velocity, double angle) {
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
}
