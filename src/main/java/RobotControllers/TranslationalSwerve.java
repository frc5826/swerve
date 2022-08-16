package RobotControllers;

import interfaces.JoysticksInterface;
import interfaces.RobotController;
import interfaces.RobotInterface;
import interfaces.SwerveWheelInterface;

import java.util.List;

public class TranslationalSwerve implements RobotController {

    @Override // this tells Java that the `loop` method implements the `loop` method specified in `RobotController`
    public void loop(JoysticksInterface joysticks, RobotInterface robot) {
        double wheelVelocity = (Math.sqrt(Math.pow(joysticks.getLeftStick().x - 0, 2) + Math.pow(joysticks.getLeftStick().y - 0, 2))) * 2;
        double wheelAngle = Math.atan2(joysticks.getLeftStick().x,  joysticks.getLeftStick().y);

        List<SwerveWheelInterface> drivetrain = robot.getDrivetrain();

        for(int i = 0; i < drivetrain.size(); i++) {
            SwerveWheelInterface wheel = drivetrain.get(i);

            System.out.println(Math.toDegrees(wheelAngle));

            wheel.setWheelAngle(-wheelAngle);
            wheel.setWheelVelocity(wheelVelocity);
        }
    }
}
