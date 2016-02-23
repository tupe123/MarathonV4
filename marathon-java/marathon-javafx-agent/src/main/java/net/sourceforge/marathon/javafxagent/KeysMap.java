package net.sourceforge.marathon.javafxagent;

import javafx.scene.input.KeyCode;

// @formatter:off
public enum KeysMap {
    NULL        (JavaAgentKeys.NULL, null),
    CANCEL      (JavaAgentKeys.CANCEL, KeyCode.CANCEL),
    HELP        (JavaAgentKeys.HELP, KeyCode.HELP),
    BACK_SPACE  (JavaAgentKeys.BACK_SPACE, KeyCode.BACK_SPACE),
    TAB         (JavaAgentKeys.TAB,  KeyCode.TAB),
    CLEAR       (JavaAgentKeys.CLEAR,  KeyCode.CLEAR),
    RETURN      (JavaAgentKeys.RETURN, KeyCode.ENTER),
    ENTER       (JavaAgentKeys.ENTER,  KeyCode.ENTER),
    SHIFT       (JavaAgentKeys.SHIFT,  KeyCode.SHIFT),
    LEFT_SHIFT  (JavaAgentKeys.LEFT_SHIFT,  KeyCode.SHIFT),
    CONTROL     (JavaAgentKeys.CONTROL,  KeyCode.CONTROL),
    LEFT_CONTROL(JavaAgentKeys.LEFT_CONTROL,  KeyCode.CONTROL),
    ALT         (JavaAgentKeys.ALT, KeyCode.ALT),
    LEFT_ALT    (JavaAgentKeys.LEFT_ALT,  KeyCode.ALT),
    PAUSE       (JavaAgentKeys.PAUSE,  KeyCode.PAUSE),
    ESCAPE      (JavaAgentKeys.ESCAPE,  KeyCode.ESCAPE),
    SPACE       (JavaAgentKeys.SPACE,  KeyCode.SPACE),
    PAGE_UP     (JavaAgentKeys.PAGE_UP,  KeyCode.PAGE_UP),
    PAGE_DOWN   (JavaAgentKeys.PAGE_DOWN, KeyCode.PAGE_DOWN),
    END         (JavaAgentKeys.END,  KeyCode.END),
    HOME        (JavaAgentKeys.HOME,  KeyCode.HOME),
    LEFT        (JavaAgentKeys.LEFT, KeyCode.LEFT),
    ARROW_LEFT  (JavaAgentKeys.ARROW_LEFT,  KeyCode.KP_LEFT),
    UP          (JavaAgentKeys.UP,  KeyCode.UP),
    ARROW_UP    (JavaAgentKeys.ARROW_UP,  KeyCode.KP_UP),
    RIGHT       (JavaAgentKeys.RIGHT,  KeyCode.RIGHT),
    ARROW_RIGHT (JavaAgentKeys.ARROW_RIGHT,  KeyCode.KP_RIGHT),
    DOWN        (JavaAgentKeys.DOWN,  KeyCode.DOWN),
    ARROW_DOWN  (JavaAgentKeys.ARROW_DOWN,  KeyCode.KP_DOWN),
    INSERT      (JavaAgentKeys.INSERT,  KeyCode.INSERT),
    DELETE      (JavaAgentKeys.DELETE,  KeyCode.DELETE),
    SEMICOLON   (JavaAgentKeys.SEMICOLON,  KeyCode.SEMICOLON),
    EQUALS      (JavaAgentKeys.EQUALS, KeyCode.EQUALS),

    NUMPAD0     (JavaAgentKeys.NUMPAD0,  KeyCode.NUMPAD0),
    NUMPAD1     (JavaAgentKeys.NUMPAD1, KeyCode.NUMPAD1),
    NUMPAD2     (JavaAgentKeys.NUMPAD2, KeyCode.NUMPAD2),
    NUMPAD3     (JavaAgentKeys.NUMPAD3, KeyCode.NUMPAD3),
    NUMPAD4     (JavaAgentKeys.NUMPAD4, KeyCode.NUMPAD4),
    NUMPAD5     (JavaAgentKeys.NUMPAD5, KeyCode.NUMPAD5),
    NUMPAD6     (JavaAgentKeys.NUMPAD6, KeyCode.NUMPAD6),
    NUMPAD7     (JavaAgentKeys.NUMPAD7, KeyCode.NUMPAD7),
    NUMPAD8     (JavaAgentKeys.NUMPAD8, KeyCode.NUMPAD8),
    NUMPAD9     (JavaAgentKeys.NUMPAD9, KeyCode.NUMPAD9),
    MULTIPLY    (JavaAgentKeys.MULTIPLY, KeyCode.MULTIPLY),
    ADD         (JavaAgentKeys.ADD, KeyCode.ADD),
    SEPARATOR   (JavaAgentKeys.SEPARATOR, KeyCode.SEPARATOR),
    SUBTRACT    (JavaAgentKeys.SUBTRACT, KeyCode.SUBTRACT),
    DECIMAL     (JavaAgentKeys.DECIMAL, KeyCode.DECIMAL),
    DIVIDE      (JavaAgentKeys.DIVIDE, KeyCode.DIVIDE),

    F1          (JavaAgentKeys.F1, KeyCode.F1),
    F2          (JavaAgentKeys.F2, KeyCode.F2),
    F3          (JavaAgentKeys.F3, KeyCode.F3),
    F4          (JavaAgentKeys.F4, KeyCode.F4),
    F5          (JavaAgentKeys.F5, KeyCode.F5),
    F6          (JavaAgentKeys.F6, KeyCode.F6),
    F7          (JavaAgentKeys.F7, KeyCode.F7),
    F8          (JavaAgentKeys.F8, KeyCode.F8),
    F9          (JavaAgentKeys.F9, KeyCode.F9),
    F10         (JavaAgentKeys.F10, KeyCode.F10),
    F11         (JavaAgentKeys.F11, KeyCode.F11),
    F12         (JavaAgentKeys.F12, KeyCode.F12),

    META        (JavaAgentKeys.META, KeyCode.META),
    COMMAND     (JavaAgentKeys.COMMAND, KeyCode.META);

    // @formatter:on
    private final JavaAgentKeys keys;
    private final KeyCode code;

    KeysMap(JavaAgentKeys keys, KeyCode code) {
        this.keys = keys;
        this.code = code;
    }

    public JavaAgentKeys getKeys() {
        return keys;
    }

    public KeyCode getCode() {
        return code;
    }

    public static KeysMap findMap(JavaAgentKeys k) {
        for (KeysMap km : values())
            if (km.getKeys().equals(k))
                return km;
        return null;
    }

    public static KeysMap findMap(KeyCode k) {
        for (KeysMap km : values())
            if (km.getCode() == k)
                return km;
        return null;
    }
}
