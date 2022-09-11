package com.linngdu664.bsf.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class BSFMthUtil {
    //Calculate the cosine of the angle between 2 vectors using fast algorithm.
    public static double vec2AngleCos(double x1, double y1, double x2, double y2) {
        return Mth.fastInvSqrt(modSqr(x1, y1)) * Mth.fastInvSqrt(modSqr(x2, y2)) * (x1 * x2 + y1 * y2);
    }

    public static double vec3AngleCos(Vec3 a, Vec3 b) {
        return Mth.fastInvSqrt(modSqr(a)) * Mth.fastInvSqrt(modSqr(b)) * (a.x * b.x + a.y * b.y + a.z * b.z);
    }

    //Calculate the square of the modulus(length) of a vector.
    public static double modSqr(double x1, double y1) {
        return x1 * x1 + y1 * y1;
    }

    public static double modSqr(Vec3 a) {
        return a.x * a.x + a.y * a.y + a.z * a.z;
    }

    public static double modSqr(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    //Fucking Minecraft anti-human coordinate system.
    public static Vec3 SphericalToCartesian(float pitch, float yaw) {
        return new Vec3(-Mth.cos(pitch) * Mth.sin(yaw), -Mth.sin(pitch), Mth.cos(pitch) * Mth.cos(yaw));
    }
}
