package com.reclipse.projector;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ProjectorConfig {
    public static final Server SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    public static float sliderOffsetMinX, sliderOffsetMaxX;
    public static float sliderOffsetMinY, sliderOffsetMaxY;
    public static float sliderOffsetMinZ, sliderOffsetMaxZ;
    public static float sliderRotationXMin, sliderRotationXMax;
    public static float sliderRotationYMin, sliderRotationYMax;
    public static float sliderRotationZMin, sliderRotationZMax;
    public static float minOffsetX, minOffsetY, minOffsetZ;
    public static float minRotationX, minRotationY, minRotationZ;
    public static float maxOffsetX, maxOffsetY, maxOffsetZ;
    public static float maxRotationX, maxRotationY, maxRotationZ;

    static {
        Pair<Server, ModConfigSpec> serverPair = new ModConfigSpec.Builder().configure(Server::new);
        SERVER = serverPair.getLeft();
        SERVER_SPEC = serverPair.getRight();
    }

    public static void onLoad() {
        Server cfg = SERVER;

        sliderOffsetMinX = cfg.sliderOffsetMinX.get().floatValue();
        sliderOffsetMaxX = cfg.sliderOffsetMaxX.get().floatValue();
        sliderOffsetMinY = cfg.sliderOffsetMinY.get().floatValue();
        sliderOffsetMaxY = cfg.sliderOffsetMaxY.get().floatValue();
        sliderOffsetMinZ = cfg.sliderOffsetMinZ.get().floatValue();
        sliderOffsetMaxZ = cfg.sliderOffsetMaxZ.get().floatValue();
        sliderRotationXMin = cfg.sliderRotationXMin.get().floatValue();
        sliderRotationXMax = cfg.sliderRotationXMax.get().floatValue();
        sliderRotationYMin = cfg.sliderRotationYMin.get().floatValue();
        sliderRotationYMax = cfg.sliderRotationYMax.get().floatValue();
        sliderRotationZMin = cfg.sliderRotationZMin.get().floatValue();
        sliderRotationZMax = cfg.sliderRotationZMax.get().floatValue();

        maxOffsetX = cfg.maxOffsetX.get().floatValue();
        maxOffsetY = cfg.maxOffsetY.get().floatValue();
        maxOffsetZ = cfg.maxOffsetZ.get().floatValue();
        maxRotationX = cfg.maxRotationX.get().floatValue();
        maxRotationY = cfg.maxRotationY.get().floatValue();
        maxRotationZ = cfg.maxRotationZ.get().floatValue();

        minOffsetX = -maxOffsetX;
        minOffsetY = -maxOffsetY;
        minOffsetZ = -maxOffsetZ;
        minRotationX = -maxRotationX;
        minRotationY = -maxRotationY;
        minRotationZ = -maxRotationZ;

        if (sliderOffsetMaxX < sliderOffsetMinX) throw new RuntimeException("Configured sliderOffsetMaxX is less than sliderOffsetMinX");
        if (sliderOffsetMaxY < sliderOffsetMinY) throw new RuntimeException("Configured sliderOffsetMaxY is less than sliderOffsetMinY");
        if (sliderOffsetMaxZ < sliderOffsetMinZ) throw new RuntimeException("Configured sliderOffsetMaxZ is less than sliderOffsetMinZ");
        if (sliderRotationXMax < sliderRotationXMin) throw new RuntimeException("Configured sliderRotationXMax is less than sliderRotationXMin");
        if (sliderRotationYMax < sliderRotationYMin) throw new RuntimeException("Configured sliderRotationYMax is less than sliderRotationYMin");
        if (sliderRotationZMax < sliderRotationZMin) throw new RuntimeException("Configured sliderRotationZMax is less than sliderRotationZMin");

        if (sliderOffsetMinX < minOffsetX) throw new RuntimeException("Configured sliderOffsetMinX exceeds minOffsetX limit");
        if (sliderOffsetMinY < minOffsetY) throw new RuntimeException("Configured sliderOffsetMinY exceeds minOffsetY limit");
        if (sliderOffsetMinZ < minOffsetZ) throw new RuntimeException("Configured sliderOffsetMinZ exceeds minOffsetZ limit");
        if (sliderRotationXMin < minRotationX) throw new RuntimeException("Configured sliderRotationXMin exceeds minRotationX limit");
        if (sliderRotationYMin < minRotationY) throw new RuntimeException("Configured sliderRotationYMin exceeds minRotationY limit");
        if (sliderRotationZMin < minRotationZ) throw new RuntimeException("Configured sliderRotationZMin exceeds minRotationZ limit");

        if (sliderOffsetMaxX > maxOffsetX) throw new RuntimeException("Configured sliderOffsetMaxX exceeds maxOffsetX limit");
        if (sliderOffsetMaxY > maxOffsetY) throw new RuntimeException("Configured sliderOffsetMaxY exceeds maxOffsetY limit");
        if (sliderOffsetMaxZ > maxOffsetZ) throw new RuntimeException("Configured sliderOffsetMaxZ exceeds maxOffsetZ limit");
        if (sliderRotationXMax > maxRotationX) throw new RuntimeException("Configured sliderRotationXMax exceeds maxRotationX limit");
        if (sliderRotationYMax > maxRotationY) throw new RuntimeException("Configured sliderRotationYMax exceeds maxRotationY limit");
        if (sliderRotationZMax > maxRotationZ) throw new RuntimeException("Configured sliderRotationZMax exceeds maxRotationZ limit");
    }

    public static class Server {
        public final ModConfigSpec.DoubleValue sliderOffsetMinX;
        public final ModConfigSpec.DoubleValue sliderOffsetMaxX;
        public final ModConfigSpec.DoubleValue sliderOffsetMinY;
        public final ModConfigSpec.DoubleValue sliderOffsetMaxY;
        public final ModConfigSpec.DoubleValue sliderOffsetMinZ;
        public final ModConfigSpec.DoubleValue sliderOffsetMaxZ;
        public final ModConfigSpec.DoubleValue sliderRotationXMin;
        public final ModConfigSpec.DoubleValue sliderRotationXMax;
        public final ModConfigSpec.DoubleValue sliderRotationYMin;
        public final ModConfigSpec.DoubleValue sliderRotationYMax;
        public final ModConfigSpec.DoubleValue sliderRotationZMin;
        public final ModConfigSpec.DoubleValue sliderRotationZMax;

        public final ModConfigSpec.DoubleValue maxOffsetX;
        public final ModConfigSpec.DoubleValue maxOffsetY;
        public final ModConfigSpec.DoubleValue maxOffsetZ;
        public final ModConfigSpec.DoubleValue maxRotationX;
        public final ModConfigSpec.DoubleValue maxRotationY;
        public final ModConfigSpec.DoubleValue maxRotationZ;

        Server(ModConfigSpec.Builder builder) {
            builder.comment("Projector Slider Range Configuration")
                   .push("sliders");

            builder.comment("X Offset slider range");
            sliderOffsetMinX = builder.defineInRange("sliderOffsetMinX", -10.0, -1000.0, 1000.0);
            sliderOffsetMaxX = builder.defineInRange("sliderOffsetMaxX", 10.0, -1000.0, 1000.0);

            builder.comment("Y Offset slider range");
            sliderOffsetMinY = builder.defineInRange("sliderOffsetMinY", -10.0, -1000.0, 1000.0);
            sliderOffsetMaxY = builder.defineInRange("sliderOffsetMaxY", 10.0, -1000.0, 1000.0);

            builder.comment("Z Offset slider range");
            sliderOffsetMinZ = builder.defineInRange("sliderOffsetMinZ", -10.0, -1000.0, 1000.0);
            sliderOffsetMaxZ = builder.defineInRange("sliderOffsetMaxZ", 10.0, -1000.0, 1000.0);

            builder.comment("X-Rotation (pitch) slider range");
            sliderRotationXMin = builder.defineInRange("sliderRotationXMin", -90.0, -360.0, 360.0);
            sliderRotationXMax = builder.defineInRange("sliderRotationXMax", 90.0, -360.0, 360.0);

            builder.comment("Y-Rotation (yaw) slider range");
            sliderRotationYMin = builder.defineInRange("sliderRotationYMin", 0.0, -360.0, 360.0);
            sliderRotationYMax = builder.defineInRange("sliderRotationYMax", 360.0, -360.0, 720.0);

            builder.comment("Z-Rotation (roll) slider range");
            sliderRotationZMin = builder.defineInRange("sliderRotationZMin", -180.0, -360.0, 360.0);
            sliderRotationZMax = builder.defineInRange("sliderRotationZMax", 180.0, -360.0, 360.0);

            builder.pop();

            builder.comment("Absolute Maximum Values (for editbox n' stuff; need to be higher/lower than slider limit values)")
                   .push("limits");

            builder.comment("Maximum absolute X offset value (minimum is negative of this value)");
            maxOffsetX = builder.defineInRange("maxOffsetX", 100.0, 0.0, 10000.0);

            builder.comment("Maximum absolute Y offset value (minimum is negative of this value)");
            maxOffsetY = builder.defineInRange("maxOffsetY", 100.0, 0.0, 10000.0);

            builder.comment("Maximum absolute Z offset value (minimum is negative of this value)");
            maxOffsetZ = builder.defineInRange("maxOffsetZ", 100.0, 0.0, 10000.0);

            builder.comment("Maximum X rotation value (in degrees; minimum is negative of this value)");
            maxRotationX = builder.defineInRange("maxRotationX", 360.0, 0.0, 3600.0);

            builder.comment("Maximum Y rotation value (in degrees; minimum is negative of this value)");
            maxRotationY = builder.defineInRange("maxRotationY", 360.0, 0.0, 3600.0);

            builder.comment("Maximum Z rotation value (in degrees; minimum is negative of this value)");
            maxRotationZ = builder.defineInRange("maxRotationZ", 360.0, 0.0, 3600.0);

            builder.pop();
        }
    }
}
