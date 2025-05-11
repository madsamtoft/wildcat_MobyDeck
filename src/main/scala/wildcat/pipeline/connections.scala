package wildcat.pipeline

import chisel3._
import wildcat.pipeline.peripherals.VGA

class TopIO extends Bundle {
  val led = Output(UInt(16.W))
  val tx = Output(UInt(1.W))
  val rx = Input(UInt(1.W))
  val sw = Input(UInt(16.W))
  val btn = Input(UInt(4.W))
  val PS2_CLK = Input(UInt(1.W))
  val PS2_DATA = Input(UInt(1.W))
  val vga = new VGA()
}

class InstrIO extends Bundle {
  val address = Output(UInt(32.W))
  val data = Input(UInt(32.W))
  val stall = Input(Bool())
}

class MemIO extends Bundle {
  val rdAddress = Output(UInt(32.W))
  val rdData = Input(UInt(32.W))
  val rdEnable = Output(Bool())
  val wrAddress = Output(UInt(32.W))
  val wrData = Output(UInt(32.W))
  val wrEnable = Output(Vec (4, Bool()))
  val stall = Input(Bool())
}

class DecodedInstr extends Bundle {
  val instrType = UInt(3.W)
  val aluOp = UInt(4.W)
  val imm = SInt(32.W)
  val isImm = Bool()
  val isLui = Bool()
  val isAuiPc = Bool()
  val isLoad = Bool()
  val isStore = Bool()
  val isBranch = Bool()
  val isJal = Bool()
  val isJalr = Bool()
  val rfWrite = Bool()
  val isECall = Bool()
  val isCssrw = Bool()
  val rs1Valid = Bool()
  val rs2Valid = Bool()
}
