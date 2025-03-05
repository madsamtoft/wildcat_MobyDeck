#
# Turn on LEDs as indicated by switches
#
#
start:
    li      x4, 0xf0010000
    li      x5, 0xf0020000
    lw      x1, 0(x5)
    sw      x1, 0(x4)
    j start
