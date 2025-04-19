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

    // CTR Electronics documentation uses "sensors" not "encoders"
    public void resetClimberEncoders() {
        leftClimber.setSelectedSensorPosition(0);
        rightClimber.setSelectedSensorPosition(0);
    }

    // test code: will stop climber at highest position, then pressing again will drive down to a max position (so it doesn't lock the chain under the arm)
    // public void testCodeClimb() {
    //     if (DriveTrain.driveController.getLeftTriggerAxis() > Constants.TRIGGER_DEADZONE) { // left climber
    //         if (leftClimber.getSelectedSensorPosition() > 500) { // TODO: CHANGE VALUE
    //             leftClimber.set(-0.8);
    //         } else {
    //             leftClimber.set(0);
    //         }
    //     } else if (reverseClimbController.getXButton()) {
    //         leftClimber.set(0.4);
    //     } else {
    //         leftClimber.set(0);
    //     }

    //     if (DriveTrain.driveController.getLeftTriggerAxis() > Constants.TRIGGER_DEADZONE) { // right climber
    //         if (rightClimber.getSelectedSensorPosition() > 500) { // TODO: CHANGE VALUE
    //             rightClimber.set(-0.8);
    //         } else {
    //             rightClimber.set(0);
    //         }
    //     } else if (reverseClimbController.getAButton()) {
    //         rightClimber.set(0.4);
    //     } else {
    //         rightClimber.set(0);
    //     }
    // }

    public void climb() {
        if (DriveTrain.driveController.getLeftTriggerAxis() > Constants.TRIGGER_DEADZONE) {
            leftClimber.set(-1);
        } else if (reverseClimbController.getXButton()) {
            leftClimber.set(0.4);
        } else if (DriveTrain.driveController.getYButton()) { // REMOVE IF USING WRENCHES
            leftClimber.set(1);
        } else {
            leftClimber.set(0);
        }
        
        if (DriveTrain.driveController.getRightTriggerAxis() > Constants.TRIGGER_DEADZONE) {
            rightClimber.set(-1); // full speed for COSI "waving" (for Devin)
        } else if (reverseClimbController.getAButton()) {
            rightClimber.set(0.4);
        } else if (DriveTrain.driveController.getBButton()) { // REMOVE IF USING WRENCHES
                rightClimber.set(1);
        } else {
            rightClimber.set(0);
        }
    }
}
