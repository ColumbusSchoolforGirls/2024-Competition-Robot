package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.XboxController;


public class Climber {
    public NoteSystem aux;
    
    public Climber(NoteSystem aux) {
        this.aux = aux;           
    }  
    
    WPI_TalonSRX leftClimber = new WPI_TalonSRX(11); //motor 1
    WPI_TalonSRX rightClimber = new WPI_TalonSRX(12); //motor 2

    public void climb() {
        if(NoteSystem.aux.getLeftBumper()){
            leftClimber.set(0.2);
            rightClimber.set(0.2);


        }            
    }   

}
