package RobotControllers;

import interfaces.JoysticksInterface;
import interfaces.RobotController;
import interfaces.RobotInterface;
import interfaces.SwerveWheelInterface;

import java.util.List;

public class SpinningInPlace implements RobotController {
    @Override // this tells Java that the `loop` method implements the `loop` method specified in `RobotController`
    public void loop(JoysticksInterface joysticks, RobotInterface robot) {
        double wheelAngle;
        double wheelVelocity;

        List<SwerveWheelInterface> drivetrain = robot.getDrivetrain();

        for(int i = 0; i < drivetrain.size(); i++) { // loop through each swerve module
            SwerveWheelInterface wheel = drivetrain.get(i); // the swerve module we are looking at

            wheelAngle = -Math.atan2(-wheel.getPosition().y, wheel.getPosition().x);
            wheelVelocity = (joysticks.getRightStick().y * Math.sqrt(Math.pow(wheel.getPosition().x - 0, 2) + Math.pow(wheel.getPosition().y - 0, 2))) * 2;

            wheel.setWheelVelocity(wheelVelocity);
            wheel.setWheelAngle(wheelAngle);
        }
    }
}
