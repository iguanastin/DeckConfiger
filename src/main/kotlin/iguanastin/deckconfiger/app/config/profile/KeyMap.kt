@file:Suppress("unused")

package iguanastin.deckconfiger.app.config.profile

import javafx.scene.input.KeyCode
import java.awt.event.KeyEvent

abstract class KeyMap {

    companion object {
        val jfx = mutableMapOf<KeyCode, Keys>()
        val teensy = mutableMapOf<Int, Keys>()
        val awt = mutableMapOf<Int, Keys>()
        val char = mutableMapOf<Char, Keys>()

        init {
            for (key in Keys.values()) {
                jfx.put(key.jfxKey, key)
                teensy.put(key.teensyKey, key)
                key.awtKey?.also { awt.put(it, key) }
                key.char?.also { char.put(it, key) }
            }
        }
    }

    enum class Keys(val jfxKey: KeyCode, val teensyKey: Int, val awtKey: Int? = null, val char: Char? = null) {
        Control(KeyCode.CONTROL, 0x01 or 0xE00, KeyEvent.VK_CONTROL),
        Shift(KeyCode.SHIFT, 0x02 or 0xE00, KeyEvent.VK_SHIFT),
        Alt(KeyCode.ALT, 0x04 or 0xE00, KeyEvent.VK_ALT),
        Windows(KeyCode.WINDOWS, 0x08 or 0xE00, KeyEvent.VK_WINDOWS),

        Power(KeyCode.POWER, 0x81 or 0xE200),

        Play(KeyCode.PLAY, 0xB0 or 0xE400),
        Record(KeyCode.RECORD, 0xB2 or 0xE400),
        FastForward(KeyCode.FAST_FWD, 0xB3 or 0xE400),
        Rewind(KeyCode.REWIND, 0xB4 or 0xE400),
        Next(KeyCode.TRACK_NEXT, 0xB5 or 0xE400),
        Prev(KeyCode.TRACK_PREV, 0xB6 or 0xE400),
        Stop(KeyCode.STOP, 0xB7 or 0xE400, KeyEvent.VK_STOP),
        Eject(KeyCode.EJECT_TOGGLE, 0xB8 or 0xE400),
        Mute(KeyCode.MUTE, 0xE2 or 0xE400),
        VolumeUp(KeyCode.VOLUME_UP, 0xE9 or 0xE400),
        VolumeDown(KeyCode.VOLUME_DOWN, 0xEA or 0xE400),

        A(KeyCode.A, 4 or 0xF000, KeyEvent.VK_A, 'A'),
        B(KeyCode.B, 5 or 0xF000, KeyEvent.VK_B, 'B'),
        C(KeyCode.C, 6 or 0xF000, KeyEvent.VK_C, 'C'),
        D(KeyCode.D, 7 or 0xF000, KeyEvent.VK_D, 'D'),
        E(KeyCode.E, 8 or 0xF000, KeyEvent.VK_E, 'E'),
        F(KeyCode.F, 9 or 0xF000, KeyEvent.VK_F, 'F'),
        G(KeyCode.G, 10 or 0xF000, KeyEvent.VK_G, 'G'),
        H(KeyCode.H, 11 or 0xF000, KeyEvent.VK_H, 'H'),
        I(KeyCode.I, 12 or 0xF000, KeyEvent.VK_I, 'I'),
        J(KeyCode.J, 13 or 0xF000, KeyEvent.VK_J, 'J'),
        K(KeyCode.K, 14 or 0xF000, KeyEvent.VK_K, 'K'),
        L(KeyCode.L, 15 or 0xF000, KeyEvent.VK_L, 'L'),
        M(KeyCode.M, 16 or 0xF000, KeyEvent.VK_M, 'M'),
        N(KeyCode.N, 17 or 0xF000, KeyEvent.VK_N, 'N'),
        O(KeyCode.O, 18 or 0xF000, KeyEvent.VK_O, 'O'),
        P(KeyCode.P, 19 or 0xF000, KeyEvent.VK_P, 'P'),
        Q(KeyCode.Q, 20 or 0xF000, KeyEvent.VK_Q, 'Q'),
        R(KeyCode.R, 21 or 0xF000, KeyEvent.VK_R, 'R'),
        S(KeyCode.S, 22 or 0xF000, KeyEvent.VK_S, 'S'),
        T(KeyCode.T, 23 or 0xF000, KeyEvent.VK_T, 'T'),
        U(KeyCode.U, 24 or 0xF000, KeyEvent.VK_U, 'U'),
        V(KeyCode.V, 25 or 0xF000, KeyEvent.VK_V, 'V'),
        W(KeyCode.W, 26 or 0xF000, KeyEvent.VK_W, 'W'),
        X(KeyCode.X, 27 or 0xF000, KeyEvent.VK_X, 'X'),
        Y(KeyCode.Y, 28 or 0xF000, KeyEvent.VK_Y, 'Y'),
        Z(KeyCode.Z, 29 or 0xF000, KeyEvent.VK_Z, 'Z'),

        Digit1(KeyCode.DIGIT1, 30 or 0xF000, KeyEvent.VK_1, '1'),
        Digit2(KeyCode.DIGIT2, 31 or 0xF000, KeyEvent.VK_2, '2'),
        Digit3(KeyCode.DIGIT3, 32 or 0xF000, KeyEvent.VK_3, '3'),
        Digit4(KeyCode.DIGIT4, 33 or 0xF000, KeyEvent.VK_4, '4'),
        Digit5(KeyCode.DIGIT5, 34 or 0xF000, KeyEvent.VK_5, '5'),
        Digit6(KeyCode.DIGIT6, 35 or 0xF000, KeyEvent.VK_6, '6'),
        Digit7(KeyCode.DIGIT7, 36 or 0xF000, KeyEvent.VK_7, '7'),
        Digit8(KeyCode.DIGIT8, 37 or 0xF000, KeyEvent.VK_8, '8'),
        Digit9(KeyCode.DIGIT9, 38 or 0xF000, KeyEvent.VK_9, '9'),
        Digit0(KeyCode.DIGIT0, 39 or 0xF000, KeyEvent.VK_0, '0'),

        Enter(KeyCode.ENTER, 40 or 0xF000, KeyEvent.VK_ENTER),
        Escape(KeyCode.ESCAPE, 41 or 0xF000, KeyEvent.VK_ESCAPE),
        BackSpace(KeyCode.BACK_SPACE, 42 or 0xF000, KeyEvent.VK_BACK_SPACE),
        Tab(KeyCode.TAB, 43 or 0xF000, KeyEvent.VK_TAB, '\t'),
        Space(KeyCode.SPACE, 44 or 0xF000, KeyEvent.VK_SPACE, ' '),
        Minus(KeyCode.MINUS, 45 or 0xF000, KeyEvent.VK_MINUS, '-'),
        Equals(KeyCode.EQUALS, 46 or 0xF000, KeyEvent.VK_EQUALS, '='),
        LeftBrace(KeyCode.BRACELEFT, 47 or 0xF000, KeyEvent.VK_BRACELEFT, '['),
        RightBrace(KeyCode.BRACERIGHT, 48 or 0xF000, KeyEvent.VK_BRACERIGHT, ']'),
        BackSlash(KeyCode.BACK_SLASH, 49 or 0xF000, KeyEvent.VK_BACK_SLASH, '\\'),

        Semicolon(KeyCode.SEMICOLON, 51 or 0xF000, KeyEvent.VK_SEMICOLON, ';'),
        Quote(KeyCode.QUOTE, 52 or 0xF000, KeyEvent.VK_QUOTE, '\''),

        //        Tilde(KeyCode.DEAD_TILDE, 53 or 0xF000), // Doesn't seem to be accessible from JFX for some reason. Also tried DEAD_ACUTE.
        Comma(KeyCode.COMMA, 54 or 0xF000, KeyEvent.VK_COMMA, ','),
        Period(KeyCode.PERIOD, 55 or 0xF000, KeyEvent.VK_PERIOD, '.'),
        Slash(KeyCode.SLASH, 56 or 0xF000, KeyEvent.VK_SLASH, '/'),
        Caps(KeyCode.CAPS, 57 or 0xF000, KeyEvent.VK_CAPS_LOCK),

        F1(KeyCode.F1, 58 or 0xF000, KeyEvent.VK_F1),
        F2(KeyCode.F2, 59 or 0xF000, KeyEvent.VK_F2),
        F3(KeyCode.F3, 60 or 0xF000, KeyEvent.VK_F3),
        F4(KeyCode.F4, 61 or 0xF000, KeyEvent.VK_F4),
        F5(KeyCode.F5, 62 or 0xF000, KeyEvent.VK_F5),
        F6(KeyCode.F6, 63 or 0xF000, KeyEvent.VK_F6),
        F7(KeyCode.F7, 64 or 0xF000, KeyEvent.VK_F7),
        F8(KeyCode.F8, 65 or 0xF000, KeyEvent.VK_F8),
        F9(KeyCode.F9, 66 or 0xF000, KeyEvent.VK_F9),
        F10(KeyCode.F10, 67 or 0xF000, KeyEvent.VK_F10),
        F11(KeyCode.F11, 68 or 0xF000, KeyEvent.VK_F11),
        F12(KeyCode.F12, 69 or 0xF000, KeyEvent.VK_F12),

        PrintScreen(KeyCode.PRINTSCREEN, 70 or 0xF000, KeyEvent.VK_PRINTSCREEN),
        ScrollLock(KeyCode.SCROLL_LOCK, 71 or 0xF000, KeyEvent.VK_SCROLL_LOCK),
        Pause(KeyCode.PAUSE, 72 or 0xF000, KeyEvent.VK_PAUSE),
        Insert(KeyCode.INSERT, 73 or 0xF000, KeyEvent.VK_INSERT),
        Home(KeyCode.HOME, 74 or 0xF000, KeyEvent.VK_HOME),
        PageUp(KeyCode.PAGE_UP, 75 or 0xF000, KeyEvent.VK_PAGE_UP),
        Delete(KeyCode.DELETE, 76 or 0xF000, KeyEvent.VK_DELETE),
        End(KeyCode.END, 77 or 0xF000, KeyEvent.VK_END),
        PageDown(KeyCode.PAGE_DOWN, 78 or 0xF000, KeyEvent.VK_PAGE_DOWN),
        Right(KeyCode.RIGHT, 79 or 0xF000, KeyEvent.VK_RIGHT),
        Left(KeyCode.LEFT, 80 or 0xF000, KeyEvent.VK_LEFT),
        Down(KeyCode.DOWN, 81 or 0xF000, KeyEvent.VK_DOWN),
        Up(KeyCode.UP, 82 or 0xF000, KeyEvent.VK_UP),
        NumLock(KeyCode.NUM_LOCK, 83 or 0xF000, KeyEvent.VK_NUM_LOCK),

        Asterisk(KeyCode.ASTERISK, 85 or 0xF000, KeyEvent.VK_ASTERISK, '*'),
        Plus(KeyCode.PLUS, 87 or 0xF000, KeyEvent.VK_PLUS, '+'),
        Numpad1(KeyCode.NUMPAD1, 89 or 0xF000, KeyEvent.VK_NUMPAD1, '1'),
        Numpad2(KeyCode.NUMPAD2, 90 or 0xF000, KeyEvent.VK_NUMPAD2, '2'),
        Numpad3(KeyCode.NUMPAD3, 91 or 0xF000, KeyEvent.VK_NUMPAD3, '3'),
        Numpad4(KeyCode.NUMPAD4, 92 or 0xF000, KeyEvent.VK_NUMPAD4, '4'),
        Numpad5(KeyCode.NUMPAD5, 93 or 0xF000, KeyEvent.VK_NUMPAD5, '5'),
        Numpad6(KeyCode.NUMPAD6, 94 or 0xF000, KeyEvent.VK_NUMPAD6, '6'),
        Numpad7(KeyCode.NUMPAD7, 95 or 0xF000, KeyEvent.VK_NUMPAD7, '7'),
        Numpad8(KeyCode.NUMPAD8, 96 or 0xF000, KeyEvent.VK_NUMPAD8, '8'),
        Numpad9(KeyCode.NUMPAD9, 97 or 0xF000, KeyEvent.VK_NUMPAD9, '9'),
        Numpad0(KeyCode.NUMPAD0, 98 or 0xF000, KeyEvent.VK_NUMPAD0, '0'),

        Menu(KeyCode.CONTEXT_MENU, 101 or 0xF000, KeyEvent.VK_CONTEXT_MENU),

        F13(KeyCode.F13, 104 or 0xF000, KeyEvent.VK_F13),
        F14(KeyCode.F14, 105 or 0xF000, KeyEvent.VK_F14),
        F15(KeyCode.F15, 106 or 0xF000, KeyEvent.VK_F15),
        F16(KeyCode.F16, 107 or 0xF000, KeyEvent.VK_F16),
        F17(KeyCode.F17, 108 or 0xF000, KeyEvent.VK_F17),
        F18(KeyCode.F18, 109 or 0xF000, KeyEvent.VK_F18),
        F19(KeyCode.F19, 110 or 0xF000, KeyEvent.VK_F19),
        F20(KeyCode.F20, 111 or 0xF000, KeyEvent.VK_F20),
        F21(KeyCode.F21, 112 or 0xF000, KeyEvent.VK_F21),
        F22(KeyCode.F22, 113 or 0xF000, KeyEvent.VK_F22),
        F23(KeyCode.F23, 114 or 0xF000, KeyEvent.VK_F23),
        F24(KeyCode.F24, 115 or 0xF000, KeyEvent.VK_F24),
    }

}