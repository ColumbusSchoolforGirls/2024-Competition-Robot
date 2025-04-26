package frc.robot;

public final class Constants {
    
    //drive train
    public static final double WHEEL_CIRCUMFERENCE = 8.0 * Math.PI; //diameter of our wheels
    public static final double DRIVE_CONTROLLER_DEADZONE = 0.1; //dead zone
    public static final double DRIVE_CONTROLLER_DRIFT_DEADZONE = 0.15;
    public static final int FRONT_LEFT_ID = 3;
    public static final int BACK_LEFT_ID = 2;
    public static final int FRONT_RIGHT_ID = 5;
    public static final int BACK_RIGHT_ID = 4;
    public static final double GEAR_RATIO = 12.75;
    public static final int SLOWING_DISTANCE = 10;
    public static final double NORMAL_SPEED = 0.5;
    public static final double CRAWL_SPEED = 0.25;
    public static final double VELOCITY_THRESHHOLD = 0.03;

    // tolerance (deadzone) for auto
    public static final double TURN_TOLERANCE = 2.0; 
    public static final double DISTANCE_TOLERANCE = 0.1;
    public static final double SQUARE_TOLERANCE = 1.5;

    //climber
    public static final int LEFT_CLIMBER_ID = 11;
    public static final int RIGHT_CLIMBER_ID = 12;

    //note stuff
    public static final double AUX_DEADZONE = 0.1;
    public static final int INTAKE_LIMIT_SWITCH_CHANNEL = 0;
    
    public static final int INTAKE_RPM = 3000; //3000 for speaker, testing this for amp;changed to 3000 from 2500 for testing, 
    public static final int REVERSE_INTAKE_RPM = -2100;
    public static final int SHOOTER_RPM = 3700; //-3500 for speaker 
    public static final int SHOOTING_VELOCITY = 1000; //3300 for speaker, could be raised 50

    public static final int AMP_INTAKE_RPM = 1825; //3300 for speaker //change to 1700
    public static final int AMP_INTAKE_VELOCITY = 1675;
    public static final int AMP_SHOOTER_RPM = -2070; //3300 for speaker //should be negative?????? //change to 1700
    public static final int AMP_SHOOTING_VELOCITY = 1920; //3300 for speaker

    public static final int SIDE_SHOOTER_RPM = 3350;
    public static final int SIDE_SHOOTING_VELOCITY = 3150;

    public static final int TRAP_SHOOTER_RPM = 3200;
    public static final int TRAP_INTAKE_RPM = 3100;
    public static final int TRAP_SHOOTING_VELOCITY = 3050;
    public static final int TRAP_INTAKE_VELOCITY = 2950;
    


}

