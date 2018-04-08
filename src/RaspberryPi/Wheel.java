package RaspberryPi;

import com.pi4j.io.gpio.*;

/**
 * 我使用一个L298N电机驱动模块驱动小车，所以这个Wheel类是控制一侧的两个轮子的
 * */
public class Wheel {
    private GpioController gpioController;
    private GpioPinDigitalOutput first;
    private GpioPinDigitalOutput second;
    private static GpioPinPwmOutput pwn;
    private static int range = 1000;
    private static int divisor = 500;

    public Wheel(GpioController gpioController, Pin first, Pin second) {
        this.gpioController = gpioController;
        this.first = this.gpioController.provisionDigitalOutputPin(first);
        /**
         * 设置当调用shutdown时各个引脚的关闭方式
         */
        this.first.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        this.second = this.gpioController.provisionDigitalOutputPin(second);
        this.second.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        this.first.low();
        this.second.low();
    }

    public Wheel(GpioController gpioController, Pin first, Pin second, Pin pwn) {
        this(gpioController, first, second);
        //设置pwm输出
        this.pwn = gpioController.provisionPwmOutputPin(pwn);
        com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
        com.pi4j.wiringpi.Gpio.pwmSetRange(range);
        com.pi4j.wiringpi.Gpio.pwmSetClock(divisor);
        this.pwn.setPwm(0);
        this.pwn.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    }
    //轮子向前滚动
    public void forward() {
        first.high();
        second.low();
    }
    //轮子向后滚动
    public void backward() {
        first.low();
        second.high();
    }
    //轮子向相反方向滚动，可以实现转向
    public void contrast() {
        if (first.isHigh()) {
            first.low();
        } else {
            first.high();
        }
        if (second.isHigh()) {
            second.low();
        } else {
            second.high();
        }
    }
    //轮子停止转动
    public void stop() {
        first.low();
        second.low();
    }
    //设置pwm输出，从而控制住转速
    public static void setPwnRate(int rate) {
        if (rate > range) {
            rate = range;
        }
        System.out.println("Current pwn rate is : " + rate);
        pwn.setPwm(rate);
    }
    //获取pwm的输入范围
    public int getRange() {
        return range;
    }
}
