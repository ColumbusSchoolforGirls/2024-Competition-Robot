package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Climber {

    public NoteSystem noteSystem;

    WPI_TalonSRX leftClimber = new WPI_TalonSRX(Constants.LEFT_CLIMBER_ID); // motor 1
    WPI_TalonSRX rightClimber = new WPI_TalonSRX(Constants.RIGHT_CLIMBER_ID); // motor 2

    public void setClimb() {
        leftClimber.set(0);
        rightClimber.set(0);
    }

    public void climb() {

        if (DriveTrain.driveController.getLeftTriggerAxis() > Constants.TRIGGER_DEADZONE
                && DriveTrain.driveController.getRightTriggerAxis() > Constants.TRIGGER_DEADZONE) { // change to constant like trigger threshhold
            leftClimber.set(0.2);
            rightClimber.set(0.2);
            System.out.println("!!!!!!!!!!!!!!!!!!climbing");
        } else {
            leftClimber.set(0);
            rightClimber.set(0);
        }
    }

}
