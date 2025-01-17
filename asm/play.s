#
# Very simple assembler statements to get started
#
	rdcycle	x1
	rdtime	x1
	csrr	x2, mhartid
	csrr	x3, mcycle
#	cssr    x4, mcycleh guess there is a configuraiton error with assembly
	csrr	x4, time

	csrr    x0, 0xb00
	csrr    x0, 0xb01
	csrr    x0, 0xc00
	csrr    x0, 0xc80
	csrr    x0, 0xc01

    addi a0, x0, 1
	csrr a0, marchid
	addi a0, a0, -47 # marchid of Wildcat is 47
    addi a0, x0, 1

    addi a0, x0, 0

# notify success to the simulator
    ecall
1:  beq   x0, x0, 1b
