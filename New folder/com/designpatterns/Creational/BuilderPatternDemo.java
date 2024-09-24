// This code implements the Builder design pattern, which allows the step-by-step construction of a complex object (Computer) with flexibility.
// The Computer class represents the product, while the nested Builder class provides methods to configure and build the computer.
// The ComputerConfigurator class acts as a director, managing predefined configurations (e.g., Gaming PC, Workstation).
// The BuilderPatternDemo class demonstrates how different computer configurations can be created easily using the builder pattern, and also shows custom configuration creation.


package com.designpatterns.creational;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder Pattern: Separates the construction of a complex object from its
 * representation, allowing the same construction process to create various
 * representations.
 *
 * Use Case: Custom Computer Configuration System
 *
 * This example demonstrates a flexible computer building system where different
 * configurations can be created using a step-by-step process.
 */
// Product class
class Computer {

    private String cpu;
    private String ram;
    private String storage;
    private String gpu;
    private List<String> peripherals;

    private Computer() {
        peripherals = new ArrayList<>();
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public void addPeripheral(String peripheral) {
        this.peripherals.add(peripheral);
    }

    @Override
    public String toString() {
        return "Computer{"
                + "cpu='" + cpu + '\''
                + ", ram='" + ram + '\''
                + ", storage='" + storage + '\''
                + ", gpu='" + gpu + '\''
                + ", peripherals=" + peripherals
                + '}';
    }

    // Static nested Builder class
    static class Builder {

        private Computer computer;

        public Builder() {
            computer = new Computer();
        }

        public Builder cpu(String cpu) {
            computer.setCpu(cpu);
            return this;
        }

        public Builder ram(String ram) {
            computer.setRam(ram);
            return this;
        }

        public Builder storage(String storage) {
            computer.setStorage(storage);
            return this;
        }

        public Builder gpu(String gpu) {
            computer.setGpu(gpu);
            return this;
        }

        public Builder addPeripheral(String peripheral) {
            computer.addPeripheral(peripheral);
            return this;
        }

        public Computer build() {
            return computer;
        }
    }
}

// Director class
class ComputerConfigurator {
    public Computer configureGamingPC() {
        System.out.println("Configuring a gaming PC");
        return new Computer.Builder()
                .cpu("Intel Core i9")
                .ram("32GB DDR4")
                .storage("1TB NVMe SSD")
                .gpu("NVIDIA RTX 3080")
                .addPeripheral("Gaming Mouse")
                .addPeripheral("Mechanical Keyboard")
                .build();
    }

    public Computer configureWorkstation() {
        System.out.println("Configuring a workstation");
        return new Computer.Builder()
                .cpu("AMD Ryzen Threadripper")
                .ram("64GB ECC RAM")
                .storage("2TB NVMe SSD")
                .gpu("NVIDIA Quadro RTX 5000")
                .addPeripheral("UPS")
                .build();
    }
}

// Main class to demonstrate the Builder pattern
public class BuilderPatternDemo {
    public static void main(String[] args) {
        ComputerConfigurator configurator = new ComputerConfigurator();

        Computer gamingPC = configurator.configureGamingPC();
        System.out.println("Gaming PC configuration: " + gamingPC);

        Computer workstation = configurator.configureWorkstation();
        System.out.println("Workstation configuration: " + workstation);

        // Custom configuration
        Computer customPC = new Computer.Builder()
                .cpu("AMD Ryzen 7")
                .ram("16GB DDR4")
                .storage("512GB SSD")
                .addPeripheral("Webcam")
                .build();
        System.out.println("Custom PC configuration: " + customPC);
    }
}
