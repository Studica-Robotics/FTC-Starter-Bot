/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/*
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="StarterBot", group="Iterative OpMode")

public class StarterBot extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor armDrive = null;
    private CRServo intake = null;
    private ServoImplEx linearServo = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "leftMotor");
        rightDrive = hardwareMap.get(DcMotor.class, "rightMotor");
        armDrive = hardwareMap.get(DcMotor.class, "armMotor");
        intake = hardwareMap.get(CRServo.class, "intake");
        linearServo = hardwareMap.get(ServoImplEx.class, "linearServo");
        linearServo.setPwmRange(new PwmControl.PwmRange(900, 2100));

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armDrive.setDirection(DcMotor.Direction.FORWARD);
        armDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower;
        double rightPower;
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;


        //Arcade drive with one joystick
        leftPower  = y + x;
        rightPower = y - x;
        
        //Normalize
        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (max >= 1.0)
        {
            leftPower /= max;
            rightPower /= max;
        }

        // Send calculated power to wheels
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
        
        // Move arm up and down, hold when not pushed
        if (gamepad1.a)
        {
            armDrive.setPower(0.5);
        }
        else if (gamepad1.b)
        {
            armDrive.setPower(-0.5);
        }
        else
        {
            armDrive.setPower(0.0);
        }
        
        // Intake or outtake object
        if (gamepad1.square)
        {
            intake.setPower(0.2);
        }
        else if (gamepad1.triangle)
        {
            intake.setPower(-0.2);
        }
        else
        {
            intake.setPower(0.0);
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
