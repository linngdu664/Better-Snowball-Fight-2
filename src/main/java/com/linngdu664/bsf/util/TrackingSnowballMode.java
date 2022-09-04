package com.linngdu664.bsf.util;

public enum TrackingSnowballMode {
    NULL(0),
    NO_DAMAGE(1),
    HAVE_DAMAGE(2),
    EXPLOSIVE(3),
    GRAVITY(4),
    REPULSION(5),
    BLACK_HOLE(6);

    private int type;

    TrackingSnowballMode(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static TrackingSnowballMode getByType(int type) {
        for (TrackingSnowballMode trackingSnowballMode : values()) {
            if (trackingSnowballMode.getType() == type) {
                return trackingSnowballMode;
            }
        }
        return NULL;
    }
}
