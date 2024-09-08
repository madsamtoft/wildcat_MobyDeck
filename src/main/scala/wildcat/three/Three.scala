package wildcat.three

import chisel3._
import wildcat.common._
import wildcat.common.Functions._

/*
 * This file is part of the RISC-V processor Wildcat.
 *
 * This is a three stage pipeline.
 *
 * Author: Martin Schoeberl (martin@jopdesign.com)
 *
 */
class Three() extends Wildcat() {

  val stall = false.B

  // Let's do following pipeline stages:
  // 0. PC generation
  // 1. Fetch
  // 2. Decode, register read
  // 3. Execute

  // The ROM has a register that is reset to 0, therefore clock cycle 1 is the first instruction.
  // Needed if we want to start from a different address.
  // PC generation
  val pcReg = RegInit(-4.S(32.W).asUInt)
  val pcNext = pcReg + 4.U
  pcReg := pcNext
  io.imem.address := pcNext

  // Fetch
  val instr = io.imem.data
  val instrReg = RegInit(0x00000033.U) // nop on reset
  instrReg := instr

  // Decode and register read
  val rs1 = instr(19, 15)
  val rs2 = instr(24, 20)
  val rd = instr(11, 7)
  // val (rs1Val, rs2Val) = registerFile(rs1, rs2, rd, res, true.B)
  val regs = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))
  val rs1Reg = RegNext(rs1)
  val rs2Reg = RegNext(rs2)
  // following Mux can be avoided when using regs with reset and mask on wen
  val rs1Val = Mux(rs1Reg =/= 0.U, regs(rs1Reg), 0.U)
  val rs2Val = Mux(rs2Reg =/= 0.U, regs(rs2Reg), 0.U)



  val (instrType, isImm) = getInstrType(instrReg)
  val imm = getImm(instrReg, instrType)
  val aluOp = getAluOp(instrReg)
  val val2 = Mux(isImm, imm.asUInt, rs2Val)

  val decEx = Wire(new Bundle() {
    val valid = Bool()
    val pc = UInt(32.W)
    val aluOp = UInt(4.W)
    val rs1 = UInt(5.W)
    val rs2 = UInt(5.W)
    val rd = UInt(5.W)
    val rs1Val = UInt(32.W)
    val val2 = UInt(32.W)
  })
  decEx.valid := true.B
  decEx.pc := pcReg
  decEx.aluOp := aluOp
  decEx.rs1 := rs1
  decEx.rs2 := rs2
  decEx.rd := rd
  decEx.rs1Val := rs1Val
  decEx.val2 := val2

  val decExReg = RegInit(0.U.asTypeOf(decEx))
  decExReg := decEx

  // Execute
  val res = alu(decExReg.aluOp, decExReg.rs1Val, decExReg.val2)
  val wrEna = true.B
  when(wrEna && decExReg.rd =/= 0.U) {
    regs(decExReg.rd) := res
  }

  // dummy connections for now
  io.dmem.rdAddress := 0.U
  io.dmem.wrAddress := 0.U
  io.dmem.wrData := RegNext(res) // to aovid optimizing everything away
  io.dmem.wrEnable := 0.U
}