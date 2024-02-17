package frc.robot;

public final class Constants {
    
    //drive train
    public static final double WHEEL_CIRCUMFERENCE = 8.0 * Math.PI; //diameter of our wheels
    public static final double DRIVE_CONTROLLER_DEADZONE = 0.1; //dead zone
    public static final int FRONT_LEFT_ID = 3;
    public static final int BACK_LEFT_ID = 2;
    public static final int FRONT_RIGHT_ID = 5;
    public static final int BACK_RIGHT_ID = 4;
    public static final double GEAR_RATIO = 12.75;
    public static final int SLOWING_DISTANCE = 10;
    public static final double NORMAL_SPEED = 0.5;
    public static final double CRAWL_SPEED = 0.25;

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
    public static final int INTAKE_RPM = -3000;
    public static final int SHOOTER_RPM = 3000;
    public static final int SHOOTING_VELOCITY = 2800;

}

