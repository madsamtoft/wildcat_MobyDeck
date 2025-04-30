main:
    addi x2, x2, -16
    sw x9, 4(x2)
    sw x1, 12(x2)
    sw x8, 8(x2)
    addi x9, x0, 16
    addi x8, x0, 0
    addi x10, x8, 0
    jal x1, readSwitch
    addi x11, x10, 0
    addi x10, x8, 0
    addi x8, x8, 1
    jal x1, setLed
    bne x8, x9, -24
    jal x0, -32

setLeds:
    lui x15, 0x10
    blt x10, x15, 12
    lui x10, 0xa
    addi x10, x10, 1061
    sw x10, -2019(x3)
    lui x15, 0xf0010
    sw x10, 0(x15)
    jalr x0, x1, 0

setLed:
    addi x15, x0, 15
    addi x14, x10, 0
    bltu x15, x10, 64
    lui x15, 0xa
    addi x13, x0, 1
    addi x10, x15, 1061
    bltu x13, x11, 20
    lw x10, -2019(x3)
    sll x14, x13, x14
    bne x11, x13, 24
    or x10, x14, x10
    sw x10, -2019(x3)
    lui x15, 0xf0010
    sw x10, 0(x15)
    jalr x0, x1, 0
    xori x14, x14, -1
    and x10, x14, x10
    jal x0, -24
    lui x10, 0x6
    addi x10, x10, -1062
    jal x0, -36

toggleLed:
    addi x15, x0, 15
    bltu x15, x10, 32
    addi x15, x0, 1
    sll x10, x15, x10
    lw x14, -2019(x3)
    xor x10, x10, x14
    sw x10, -2019(x3)
    lui x15, 0xf0010
    sw x10, 0(x15)
    jalr x0, x1, 0

clearLeds:
    sw x0, -2019(x3)
    lui x15, 0xf0010
    sw x0, 0(x15)
    jalr x0, x1, 0

returnLeds:
    lw x12, -2019(x3)
    addi x10, x0, 0
    addi x15, x0, 0
    addi x13, x0, 16
    sra x14, x12, x15
    andi x14, x14, 1
    addi x15, x15, 1
    add x10, x10, x14
    bne x15, x13, -16
    jalr x0, x1, 0

readSwitches:
    lui x15, 0xf0020
    lw x10, 0(x15)
    jalr x0, x1, 0

readSwitch:
    addi x15, x0, 15
    bltu x15, x10, 24
    lui x15, 0xf0020
    lw x15, 0(x15)
    sra x10, x15, x10
    andi x10, x10, 1
    jalr x0, x1, 0
    addi x10, x0, 0
    jalr x0, x1, 0

readButtons:
    lui x15, 0xf0030
    lw x10, 0(x15)
    jalr x0, x1, 0

readButton:
    addi x15, x0, 3
    bltu x15, x10, 24
    lui x15, 0xf0030
    lw x15, 0(x15)
    sra x10, x15, x10
    andi x10, x10, 1
    jalr x0, x1, 0
    addi x10, x0, 0
    jalr x0, x1, 0

readPs2:
    lui x15, 0xf0040
    lw x10, 0(x15)
    jalr x0, x1, 0

setUart:
    lui x15, 0xf0000
    sw x10, 4(x15)
    lw x10, 4(x15)
    jalr x0, x1, 0

uartReady:
    lui x15, 0xf0000
    lw x10, 0(x15)
    jalr x0, x1, 0

setPixel:
    addi x15, x0, 159
    bltu x15, x10, 44
    addi x15, x0, 119
    bltu x15, x11, 36
    addi x15, x0, 63
    bltu x15, x12, 28
    slli x11, x11, 8
    or x11, x11, x10
    slli x11, x11, 2
    lui x15, 0xf0050
    add x11, x11, x15
    sw x12, 0(x11)
    jalr x0, x1, 0

readKey:
    addi x10, x10, -1
    addi x15, x0, 130
    bltu x15, x10, 24
    lui x15, 0x10
    addi x15, x15, 644
    add x15, x15, x10
    lbu x10, 0(x15)
    jalr x0, x1, 0
    addi x10, x0, 63
    jalr x0, x1, 0