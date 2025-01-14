package frc.robot.subsystems;

import com.ctre.phoenix6.configs.DifferentialSensorsConfigs;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.Constants.SwerveConstants;

public class SwerveModule {
    public int moduleID; 

    private TalonFX driveMotor, turningMotor; 
    private CANcoder absoluteEncoder; 

    private PIDController turningPidController; 
    private double encOffset ; 
    
    public SwerveModule (int moduleID, SwerveModuleConstants moduleConstants) {
        this.moduleID = moduleID; 

        this.encOffset = moduleConstants.encOffset;

        driveMotor = new TalonFX(moduleConstants.driveMotorCANID); 
        turningMotor = new TalonFX(moduleConstants.turningMotorCANID); 
        absoluteEncoder = new CANcoder(moduleConstants.canCoderID); 
        
        //CONFIGS 
        driveMotor.getConfigurator().apply(new MotorOutputConfigs().withInverted(moduleConstants.driveMotorInverted));
        driveMotor.getConfigurator().apply(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake)); 
        turningMotor.getConfigurator().apply(new MotorOutputConfigs().withInverted(moduleConstants.turningMotorInverted));
        turningMotor.getConfigurator().apply(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

        absoluteEncoder.getConfigurator().apply(new MagnetSensorConfigs().withAbsoluteSensorDiscontinuityPoint(0.5));
        absoluteEncoder.getConfigurator().apply(new MagnetSensorConfigs().withSensorDirection(SensorDirectionValue.CounterClockwise_Positive));

        turningPidController = new PIDController(SwerveConstants.turningKP, SwerveConstants.turningKI, SwerveConstants.turningKD); 
        turningPidController.enableContinuousInput(-180, 180);
    }
}