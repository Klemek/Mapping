package fr.klemek.mapping;

public enum Tool {
    NODE_EXTRUDER("Node mode"),
    FACE_EXTRUDER("Face mode");

    String name;

    Tool(String name) {
        this.name = name;
    }
}
