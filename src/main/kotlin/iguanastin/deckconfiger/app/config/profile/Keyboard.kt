package iguanastin.deckconfiger.app.config.profile

import javafx.scene.input.KeyCode

abstract class Keyboard {

    enum class KeyMap(val jfxKey: KeyCode, val teensyKey: Int, val char: Char? = null) {
        Control(KeyCode.CONTROL, 0x01 or 0xE00),
        Shift(KeyCode.SHIFT, 0x02 or 0xE00),
        Alt(KeyCode.ALT, 0x04 or 0xE00),
        Windows(KeyCode.WINDOWS, 0x08 or 0xE00),

        Power(KeyCode.POWER, 0x81 or 0xE200),

        Play(KeyCode.PLAY, 0xB0 or 0xE400),
        Record(KeyCode.RECORD, 0xB2 or 0xE400),
        FastForward(KeyCode.FAST_FWD, 0xB3 or 0xE400),
        Rewind(KeyCode.REWIND, 0xB4 or 0xE400),
        Next(KeyCode.TRACK_NEXT, 0xB5 or 0xE400),
        Prev(KeyCode.TRACK_PREV, 0xB6 or 0xE400),
        Stop(KeyCode.STOP, 0xB7 or 0xE400),
        Eject(KeyCode.EJECT_TOGGLE, 0xB8 or 0xE400),
        Mute(KeyCode.MUTE, 0xE2 or 0xE400),
        VolumeUp(KeyCode.VOLUME_UP, 0xE9 or 0xE400),
        VolumeDown(KeyCode.VOLUME_DOWN, 0xEA or 0xE400),

        A(KeyCode.A, 4 or 0xF000, 'A'),
        B(KeyCode.B, 5 or 0xF000, 'B'),
        C(KeyCode.C, 6 or 0xF000, 'C'),
        D(KeyCode.D, 7 or 0xF000, 'D'),
        E(KeyCode.E, 8 or 0xF000, 'E'),
        F(KeyCode.F, 9 or 0xF000, 'F'),
        G(KeyCode.G, 10 or 0xF000, 'G'),
        H(KeyCode.H, 11 or 0xF000, 'H'),
        I(KeyCode.I, 12 or 0xF000, 'I'),
        J(KeyCode.J, 13 or 0xF000, 'J'),
        K(KeyCode.K, 14 or 0xF000, 'K'),
        L(KeyCode.L, 15 or 0xF000, 'L'),
        M(KeyCode.M, 16 or 0xF000, 'M'),
        N(KeyCode.N, 17 or 0xF000, 'N'),
        O(KeyCode.O, 18 or 0xF000, 'O'),
        P(KeyCode.P, 19 or 0xF000, 'P'),
        Q(KeyCode.Q, 20 or 0xF000, 'Q'),
        R(KeyCode.R, 21 or 0xF000, 'R'),
        S(KeyCode.S, 22 or 0xF000, 'S'),
        T(KeyCode.T, 23 or 0xF000, 'T'),
        U(KeyCode.U, 24 or 0xF000, 'U'),
        V(KeyCode.V, 25 or 0xF000, 'V'),
        W(KeyCode.W, 26 or 0xF000, 'W'),
        X(KeyCode.X, 27 or 0xF000, 'X'),
        Y(KeyCode.Y, 28 or 0xF000, 'Y'),
        Z(KeyCode.Z, 29 or 0xF000, 'Z'),

        Digit1(KeyCode.DIGIT1, 30 or 0xF000, '1'),
        Digit2(KeyCode.DIGIT2, 31 or 0xF000, '2'),
        Digit3(KeyCode.DIGIT3, 32 or 0xF000, '3'),
        Digit4(KeyCode.DIGIT4, 33 or 0xF000, '4'),
        Digit5(KeyCode.DIGIT5, 34 or 0xF000, '5'),
        Digit6(KeyCode.DIGIT6, 35 or 0xF000, '6'),
        Digit7(KeyCode.DIGIT7, 36 or 0xF000, '7'),
        Digit8(KeyCode.DIGIT8, 37 or 0xF000, '8'),
        Digit9(KeyCode.DIGIT9, 38 or 0xF000, '9'),
        Digit0(KeyCode.DIGIT0, 39 or 0xF000, '0'),

        Enter(KeyCode.ENTER, 40 or 0xF000),
        Escape(KeyCode.ESCAPE, 41 or 0xF000),
        BackSpace(KeyCode.BACK_SPACE, 42 or 0xF000),
        Tab(KeyCode.TAB, 43 or 0xF000, '\t'),
        Space(KeyCode.SPACE, 44 or 0xF000, ' '),
        Minus(KeyCode.MINUS, 45 or 0xF000, '-'),
        Equals(KeyCode.EQUALS, 46 or 0xF000, '='),
        LeftBrace(KeyCode.BRACELEFT, 47 or 0xF000, '['),
        RightBrace(KeyCode.BRACERIGHT, 48 or 0xF000, ']'),
        BackSlash(KeyCode.BACK_SLASH, 49 or 0xF000, '\\'),

        Semicolon(KeyCode.SEMICOLON, 51 or 0xF000, ';'),
        Quote(KeyCode.QUOTE, 52 or 0xF000, '\''),

        //        Tilde(KeyCode.DEAD_TILDE, 53 or 0xF000), // Doesn't seem to be accessible from JFX for some reason. Also tried DEAD_ACUTE.
        Comma(KeyCode.COMMA, 54 or 0xF000, ','),
        Period(KeyCode.PERIOD, 55 or 0xF000, '.'),
        Slash(KeyCode.SLASH, 56 or 0xF000, '/'),
        Caps(KeyCode.CAPS, 57 or 0xF000),

        F1(KeyCode.F1, 58 or 0xF000),
        F2(KeyCode.F2, 59 or 0xF000),
        F3(KeyCode.F3, 60 or 0xF000),
        F4(KeyCode.F4, 61 or 0xF000),
        F5(KeyCode.F5, 62 or 0xF000),
        F6(KeyCode.F6, 63 or 0xF000),
        F7(KeyCode.F7, 64 or 0xF000),
        F8(KeyCode.F8, 65 or 0xF000),
        F9(KeyCode.F9, 66 or 0xF000),
        F10(KeyCode.F10, 67 or 0xF000),
        F11(KeyCode.F11, 68 or 0xF000),
        F12(KeyCode.F12, 69 or 0xF000),

        PrintScreen(KeyCode.PRINTSCREEN, 70 or 0xF000),
        ScrollLock(KeyCode.SCROLL_LOCK, 71 or 0xF000),
        Pause(KeyCode.PAUSE, 72 or 0xF000),
        Insert(KeyCode.INSERT, 73 or 0xF000),
        Home(KeyCode.HOME, 74 or 0xF000),
        PageUp(KeyCode.PAGE_UP, 75 or 0xF000),
        Delete(KeyCode.DELETE, 76 or 0xF000),
        End(KeyCode.END, 77 or 0xF000),
        PageDown(KeyCode.PAGE_DOWN, 78 or 0xF000),
        Right(KeyCode.RIGHT, 79 or 0xF000),
        Left(KeyCode.LEFT, 80 or 0xF000),
        Down(KeyCode.DOWN, 81 or 0xF000),
        Up(KeyCode.UP, 82 or 0xF000),
        NumLock(KeyCode.NUM_LOCK, 83 or 0xF000),

        Asterisk(KeyCode.ASTERISK, 85 or 0xF000, '*'),
        Plus(KeyCode.PLUS, 87 or 0xF000, '+'),
        Numpad1(KeyCode.NUMPAD1, 89 or 0xF000, '1'),
        Numpad2(KeyCode.NUMPAD2, 90 or 0xF000, '2'),
        Numpad3(KeyCode.NUMPAD3, 91 or 0xF000, '3'),
        Numpad4(KeyCode.NUMPAD4, 92 or 0xF000, '4'),
        Numpad5(KeyCode.NUMPAD5, 93 or 0xF000, '5'),
        Numpad6(KeyCode.NUMPAD6, 94 or 0xF000, '6'),
        Numpad7(KeyCode.NUMPAD7, 95 or 0xF000, '7'),
        Numpad8(KeyCode.NUMPAD8, 96 or 0xF000, '8'),
        Numpad9(KeyCode.NUMPAD9, 97 or 0xF000, '9'),
        Numpad0(KeyCode.NUMPAD0, 98 or 0xF000, '0'),

        Menu(KeyCode.CONTEXT_MENU, 101 or 0xF000),

        F13(KeyCode.F13, 104 or 0xF000),
        F14(KeyCode.F14, 105 or 0xF000),
        F15(KeyCode.F15, 106 or 0xF000),
        F16(KeyCode.F16, 107 or 0xF000),
        F17(KeyCode.F17, 108 or 0xF000),
        F18(KeyCode.F18, 109 or 0xF000),
        F19(KeyCode.F19, 110 or 0xF000),
        F20(KeyCode.F20, 111 or 0xF000),
        F21(KeyCode.F21, 112 or 0xF000),
        F22(KeyCode.F22, 113 or 0xF000),
        F23(KeyCode.F23, 114 or 0xF000),
        F24(KeyCode.F24, 115 or 0xF000),
    }

}