#
# Write r, g, and b pixels to vga screen
#
    li      x4, 0xf0050000 # VGA base address
    li      x5, 0xf005ffff # VGA end address
    li      x6, 0b10100000 # xLim
    li      x7, 0b01111000 # yLim
    li      x3, 0b000000 # RGB
    sw      x3, 0(x4)

begin:
	sw      x3, 0(x4)
    addi    x4, x4, 1 # Add 1 to address
    
    andi    x8, x4, 0x00ff # Isolate x
    beq     x8, x6, resetX
returnX:
    li      x9, 0x7f00
    and     x8, x4, x9
    srli    x8, x8, 8
    beq     x8, x7, resetY
returnY:
    j begin
    
    
    
    
    
resetX:
    li x9, 0xffffff00
    and x4, x4, x9
    addi x4, x4, 0x100 # Increment Y
    j returnX
    
    
resetY:
    addi x3, x3, 1 # Add 1 to color
    li x4, 0xf0050000 # VGA base address
    j wait


wait:
    li x10, 1666667 # 60 hz counter
    
waitLoop:
    addi x10, x10, -1
    beqz x10, returnY
    j waitLoop
