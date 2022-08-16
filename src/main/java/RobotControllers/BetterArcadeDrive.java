package RobotControllers;

import interfaces.JoysticksInterface;
import interfaces.RobotController;
import interfaces.RobotInterface;
import interfaces.SwerveWheelInterface;

import java.awt.geom.Point2D;
import java.util.List;

public class BetterArcadeDrive implements RobotController {
    @Override // this tells Java that the `loop` method implements the `loop` method specified in `RobotController`
    public void loop(JoysticksInterface joysticks, RobotInterface robot) {
        double leftVelocity; //= (joysticks.getLeftStick().y + (joysticks.getLeftStick().x * -1 )) * 3.0;
        double rightVelocity; //= (joysticks.getLeftStick().y + joysticks.getLeftStick().x) * 3.0;

        // this is a list of the swerve modules that are on the robot
        List<SwerveWheelInterface> drivetrain = robot.getDrivetrain();

        Point2D.Double wheelPos;

        for(int i = 0; i < drivetrain.size(); i++) { // loop through each swerve module
            SwerveWheelInterface wheel = drivetrain.get(i); // the swerve module we are looking at

            wheel.setWheelAngle(0); // make sure the wheels are always facing forward (none of this funny swerve business)

            wheelPos = wheel.getPosition();

            leftVelocity = (joysticks.getLeftStick().y + (joysticks.getLeftStick().x * -1 * Math.sqrt(Math.pow(wheelPos.x - 0, 2) + Math.pow(wheelPos.y - 0, 2)))) * 3.0;
            rightVelocity = (joysticks.getLeftStick().y + (joysticks.getLeftStick().x * Math.sqrt(Math.pow(wheelPos.x - 0, 2) + Math.pow(wheelPos.y - 0, 2)))) * 3.0;

            // use the x position of the wheel (with the center of the robot being (0, 0)) to see if the wheel is on the right half of the robot or the left half
            if(wheel.getPosition().x > 0) { // positive x is to the right, negative x is to the left
                wheel.setWheelVelocity(rightVelocity); // wheels on the right go at our calculated right velocity
            } else {
                wheel.setWheelVelocity(leftVelocity); // wheels on the left go at our calculated left velocity
            }
        }
    }
}
