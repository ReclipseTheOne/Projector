package com.reclipse.projector;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ProjectorConfig {
    public static final Server SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    public static float sliderOffsetMinX, sliderOffsetMaxX;
    public static float sliderOffsetMinY, sliderOffsetMaxY;
    public static float sliderOffsetMinZ, sliderOffsetMaxZ;
    public static float sliderRotationMin, sliderRotationMax;
	public static float minOffsetX, minOffsetY, minOffsetZ, minRotation;
    public static float maxOffsetX, maxOffsetY, maxOffsetZ, maxRotation;

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
        sliderRotationMin = cfg.sliderRotationMin.get().floatValue();
        sliderRotationMax = cfg.sliderRotationMax.get().floatValue();

	    maxOffsetX = cfg.maxOffsetX.get().floatValue();
	    maxOffsetY = cfg.maxOffsetY.get().floatValue();
	    maxOffsetZ = cfg.maxOffsetZ.get().floatValue();
	    maxRotation = cfg.maxRotation.get().floatValue();

	    minOffsetX = -maxOffsetX;
	    minOffsetY = -maxOffsetY;
	    minOffsetZ = -maxOffsetZ;
	    minRotation = -maxRotation;

		if (sliderOffsetMaxX < sliderOffsetMinX) throw new RuntimeException("Configured sliderOffsetMaxX is less than sliderOffsetMinX");
		if (sliderOffsetMaxY < sliderOffsetMinY) throw new RuntimeException("Configured sliderOffsetMaxY is less than sliderOffsetMinY");
		if (sliderOffsetMaxZ < sliderOffsetMinZ) throw new RuntimeException("Configured sliderOffsetMaxZ is less than sliderOffsetMinZ");
		if (sliderRotationMax < sliderRotationMin) throw new RuntimeException("Configured sliderRotationMax is less than sliderRotationMin");

		if (sliderOffsetMinX < minOffsetX) throw new RuntimeException("Configured sliderOffsetMinX exceeds minOffsetX limit");
		if (sliderOffsetMinY < minOffsetY) throw new RuntimeException("Configured sliderOffsetMinY exceeds minOffsetY limit");
		if (sliderOffsetMinZ < minOffsetZ) throw new RuntimeException("Configured sliderOffsetMinZ exceeds minOffsetZ limit");
		if (sliderRotationMin < minRotation) throw new RuntimeException("Configured sliderRotationMin exceeds minRotation limit");

		if (sliderOffsetMaxX > maxOffsetX) throw new RuntimeException("Configured sliderOffsetMaxX exceeds maxOffsetX limit");
		if (sliderOffsetMaxY > maxOffsetY) throw new RuntimeException("Configured sliderOffsetMaxY exceeds maxOffsetY limit");
		if (sliderOffsetMaxZ > maxOffsetZ) throw new RuntimeException("Configured sliderOffsetMaxZ exceeds maxOffsetZ limit");
		if (sliderRotationMax > maxRotation) throw new RuntimeException("Configured sliderRotationMax exceeds maxRotation limit");
	}

    public static class Server {
        public final ModConfigSpec.DoubleValue sliderOffsetMinX;
        public final ModConfigSpec.DoubleValue sliderOffsetMaxX;
        public final ModConfigSpec.DoubleValue sliderOffsetMinY;
        public final ModConfigSpec.DoubleValue sliderOffsetMaxY;
        public final ModConfigSpec.DoubleValue sliderOffsetMinZ;
        public final ModConfigSpec.DoubleValue sliderOffsetMaxZ;
        public final ModConfigSpec.DoubleValue sliderRotationMin;
        public final ModConfigSpec.DoubleValue sliderRotationMax;

        public final ModConfigSpec.DoubleValue maxOffsetX;
        public final ModConfigSpec.DoubleValue maxOffsetY;
        public final ModConfigSpec.DoubleValue maxOffsetZ;
        public final ModConfigSpec.DoubleValue maxRotation;

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

            builder.comment("Y-Rotation slider range");
            sliderRotationMin = builder.defineInRange("sliderRotationMin", 0.0, -360.0, 360.0);
            sliderRotationMax = builder.defineInRange("sliderRotationMax", 360.0, -360.0, 720.0);

            builder.pop();

            builder.comment("Absolute Maximum Values (for editbox n' stuff; need to be higher/lower than slider limit values)")
                   .push("limits");

            builder.comment("Maximum absolute X offset value (minimum is negative of this value)");
            maxOffsetX = builder.defineInRange("maxOffsetX", 100.0, 0.0, 10000.0);

            builder.comment("Maximum absolute Y offset value (minimum is negative of this value)");
            maxOffsetY = builder.defineInRange("maxOffsetY", 100.0, 0.0, 10000.0);

            builder.comment("Maximum absolute Z offset value (minimum is negative of this value)");
            maxOffsetZ = builder.defineInRange("maxOffsetZ", 100.0, 0.0, 10000.0);

            builder.comment("Maximum rotation value (in degrees; minimum is negative of this value)");
            maxRotation = builder.defineInRange("maxRotation", 360.0, 0.0, 3600.0);

            builder.pop();
        }
    }
}
