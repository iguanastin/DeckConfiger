package iguanastin.deckconfiger.app.config.profile

import javafx.scene.input.KeyCode

abstract class KeyboardMap {

    companion object {
        val jfxToTeensy = mapOf(
            Pair(KeyCode.CONTROL, 0x01 or 0xE00),
            Pair(KeyCode.SHIFT, 0x02 or 0xE00),
            Pair(KeyCode.ALT, 0x04 or 0xE00),
            Pair(KeyCode.WINDOWS, 0x08 or 0xE00),

            Pair(KeyCode.POWER, 0x81 or 0xE200),

            Pair(KeyCode.PLAY, 0xB0 or 0xE400),
            Pair(KeyCode.RECORD, 0xB2 or 0xE400),
            Pair(KeyCode.FAST_FWD, 0xB3 or 0xE400),
            Pair(KeyCode.REWIND, 0xB4 or 0xE400),
            Pair(KeyCode.TRACK_NEXT, 0xB5 or 0xE400),
            Pair(KeyCode.TRACK_PREV, 0xB6 or 0xE400),
            Pair(KeyCode.STOP, 0xB7 or 0xE400),
            Pair(KeyCode.EJECT_TOGGLE, 0xB8 or 0xE400),
            Pair(KeyCode.MUTE, 0xE2 or 0xE400),
            Pair(KeyCode.VOLUME_UP, 0xE9 or 0xE400),
            Pair(KeyCode.VOLUME_DOWN, 0xEA or 0xE400),

            Pair(KeyCode.A, 4 or 0xF000),
            Pair(KeyCode.B, 5 or 0xF000),
            Pair(KeyCode.C, 6 or 0xF000),
            Pair(KeyCode.D, 7 or 0xF000),
            Pair(KeyCode.E, 8 or 0xF000),
            Pair(KeyCode.F, 9 or 0xF000),
            Pair(KeyCode.G, 10 or 0xF000),
            Pair(KeyCode.H, 11 or 0xF000),
            Pair(KeyCode.I, 12 or 0xF000),
            Pair(KeyCode.J, 13 or 0xF000),
            Pair(KeyCode.K, 14 or 0xF000),
            Pair(KeyCode.L, 15 or 0xF000),
            Pair(KeyCode.M, 16 or 0xF000),
            Pair(KeyCode.N, 17 or 0xF000),
            Pair(KeyCode.O, 18 or 0xF000),
            Pair(KeyCode.P, 19 or 0xF000),
            Pair(KeyCode.Q, 20 or 0xF000),
            Pair(KeyCode.R, 21 or 0xF000),
            Pair(KeyCode.S, 22 or 0xF000),
            Pair(KeyCode.T, 23 or 0xF000),
            Pair(KeyCode.U, 24 or 0xF000),
            Pair(KeyCode.V, 25 or 0xF000),
            Pair(KeyCode.W, 26 or 0xF000),
            Pair(KeyCode.X, 27 or 0xF000),
            Pair(KeyCode.Y, 28 or 0xF000),
            Pair(KeyCode.Z, 29 or 0xF000),

            Pair(KeyCode.DIGIT1, 30 or 0xF000),
            Pair(KeyCode.DIGIT2, 31 or 0xF000),
            Pair(KeyCode.DIGIT3, 32 or 0xF000),
            Pair(KeyCode.DIGIT4, 33 or 0xF000),
            Pair(KeyCode.DIGIT5, 34 or 0xF000),
            Pair(KeyCode.DIGIT6, 35 or 0xF000),
            Pair(KeyCode.DIGIT7, 36 or 0xF000),
            Pair(KeyCode.DIGIT8, 37 or 0xF000),
            Pair(KeyCode.DIGIT9, 38 or 0xF000),
            Pair(KeyCode.DIGIT0, 39 or 0xF000),

            Pair(KeyCode.ENTER, 40 or 0xF000),
            Pair(KeyCode.ESCAPE, 41 or 0xF000),
            Pair(KeyCode.BACK_SPACE, 42 or 0xF000),
            Pair(KeyCode.TAB, 43 or 0xF000),
            Pair(KeyCode.SPACE, 44 or 0xF000),
            Pair(KeyCode.MINUS, 45 or 0xF000),
            Pair(KeyCode.EQUALS, 46 or 0xF000),
            Pair(KeyCode.BRACELEFT, 47 or 0xF000),
            Pair(KeyCode.BRACERIGHT, 48 or 0xF000),
            Pair(KeyCode.BACK_SLASH, 49 or 0xF000),

            Pair(KeyCode.SEMICOLON, 51 or 0xF000),
            Pair(KeyCode.QUOTE, 52 or 0xF000),
//            Pair(KeyCode.DEAD_TILDE, 53 or 0xF000), // Doesn't seem to be accessible from JFX for some reason. Also tried DEAD_ACUTE.
            Pair(KeyCode.COMMA, 54 or 0xF000),
            Pair(KeyCode.PERIOD, 55 or 0xF000),
            Pair(KeyCode.SLASH, 56 or 0xF000),
            Pair(KeyCode.CAPS, 57 or 0xF000),

            Pair(KeyCode.F1, 58 or 0xF000),
            Pair(KeyCode.F2, 59 or 0xF000),
            Pair(KeyCode.F3, 60 or 0xF000),
            Pair(KeyCode.F4, 61 or 0xF000),
            Pair(KeyCode.F5, 62 or 0xF000),
            Pair(KeyCode.F6, 63 or 0xF000),
            Pair(KeyCode.F7, 64 or 0xF000),
            Pair(KeyCode.F8, 65 or 0xF000),
            Pair(KeyCode.F9, 66 or 0xF000),
            Pair(KeyCode.F10, 67 or 0xF000),
            Pair(KeyCode.F11, 68 or 0xF000),
            Pair(KeyCode.F12, 69 or 0xF000),

            Pair(KeyCode.PRINTSCREEN, 70 or 0xF000),
            Pair(KeyCode.SCROLL_LOCK, 71 or 0xF000),
            Pair(KeyCode.PAUSE, 72 or 0xF000),
            Pair(KeyCode.INSERT, 73 or 0xF000),
            Pair(KeyCode.HOME, 74 or 0xF000),
            Pair(KeyCode.PAGE_UP, 75 or 0xF000),
            Pair(KeyCode.DELETE, 76 or 0xF000),
            Pair(KeyCode.END, 77 or 0xF000),
            Pair(KeyCode.PAGE_DOWN, 78 or 0xF000),
            Pair(KeyCode.RIGHT, 79 or 0xF000),
            Pair(KeyCode.LEFT, 80 or 0xF000),
            Pair(KeyCode.DOWN, 81 or 0xF000),
            Pair(KeyCode.UP, 82 or 0xF000),
            Pair(KeyCode.NUM_LOCK, 83 or 0xF000),

            Pair(KeyCode.ASTERISK, 85 or 0xF000),
            Pair(KeyCode.PLUS, 87 or 0xF000),
            Pair(KeyCode.NUMPAD1, 89 or 0xF000),
            Pair(KeyCode.NUMPAD2, 90 or 0xF000),
            Pair(KeyCode.NUMPAD3, 91 or 0xF000),
            Pair(KeyCode.NUMPAD4, 92 or 0xF000),
            Pair(KeyCode.NUMPAD5, 93 or 0xF000),
            Pair(KeyCode.NUMPAD6, 94 or 0xF000),
            Pair(KeyCode.NUMPAD7, 95 or 0xF000),
            Pair(KeyCode.NUMPAD8, 96 or 0xF000),
            Pair(KeyCode.NUMPAD9, 97 or 0xF000),
            Pair(KeyCode.NUMPAD0, 98 or 0xF000),

            Pair(KeyCode.CONTEXT_MENU, 101 or 0xF000),

            Pair(KeyCode.F13, 104 or 0xF000),
            Pair(KeyCode.F14, 105 or 0xF000),
            Pair(KeyCode.F15, 106 or 0xF000),
            Pair(KeyCode.F16, 107 or 0xF000),
            Pair(KeyCode.F17, 108 or 0xF000),
            Pair(KeyCode.F18, 109 or 0xF000),
            Pair(KeyCode.F19, 110 or 0xF000),
            Pair(KeyCode.F20, 111 or 0xF000),
            Pair(KeyCode.F21, 112 or 0xF000),
            Pair(KeyCode.F22, 113 or 0xF000),
            Pair(KeyCode.F23, 114 or 0xF000),
            Pair(KeyCode.F24, 115 or 0xF000),
        )

        val teensyToJFX = jfxToTeensy.map { Pair(it.value, it.key) }.toMap()
    }

}