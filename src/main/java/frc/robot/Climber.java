package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.XboxController;

public class Climber {

    WPI_TalonSRX leftClimber = new WPI_TalonSRX(Constants.LEFT_CLIMBER_ID); // motor 1
    WPI_TalonSRX rightClimber = new WPI_TalonSRX(Constants.RIGHT_CLIMBER_ID); // motor 2

    public static XboxController reverseClimbController = new XboxController(2);

    public void setClimb() {
        leftClimber.set(0);
        rightClimber.set(0);
    }

    public void setCoast() {
        leftClimber.setNeutralMode(NeutralMode.Coast);
        rightClimber.setNeutralMode(NeutralMode.Coast);
    }

    public void climb() {

        if (DriveTrain.driveController.getLeftTriggerAxis() > Constants.TRIGGER_DEADZONE) {
            leftClimber.set(-0.8); //was going the wrong direction
        } else if (reverseClimbController.getXButton()) {
            leftClimber.set(0.4);
        } else {
            leftClimber.set(0);
        }
        
        if (DriveTrain.driveController.getRightTriggerAxis() > Constants.TRIGGER_DEADZONE) {
            rightClimber.set(-0.8); //was going the wrong direction 
        } else if (reverseClimbController.getAButton()) {
            rightClimber.set(0.4);
        } else {
            rightClimber.set(0);
        }

    }

}
