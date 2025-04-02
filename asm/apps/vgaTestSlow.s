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
    li      x1, 0x7ff
loop:
    li      x2, 0xff
inner:
	addi	x2, x2, -1
	bnez	x2, inner
	addi	x1, x1, -1
	bnez	x1, loop
	sw      x3, 0(x4)
    addi    x3, x3, 1 # Add 1 to color
    addi    x4, x4, 1 # Add 1 to address
    
    andi    x8, x4, 0x00ff # Isolate x
    beq     x8, x6, resetX
returnX:
    li x9, 0x70
    and x8, x4, x9
    beq x8, x7, resetY
returnY:
    li      x1, 1
    bnez    x1, begin
    
    
    
    
    
resetX:
    li x9, 0xffffff00
    and x4, x4, x9
    addi x4, x4, 0x100 # Increment Y
    j returnX
    
    
resetY:
    li x9, 0xffff80ff
    and x4, x4, x9
    j returnY
    
