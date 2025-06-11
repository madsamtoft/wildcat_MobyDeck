package wildcat.pipeline.peripherals

import chisel3._
import chisel3.util._

class timer(freq: Int) extends Module{
  val io = IO(new Bundle{
    val start_timer = Input(Bool())
    val end_timer = Output(Bool())
  })

  io.end_timer := false.B
  val PER_SECOND = 60
  val MAX_COUNT = (freq/PER_SECOND)
  val countBool = RegInit(false.B)
  val countReg = Reg(UInt(log2Up(MAX_COUNT).W))
  when (io.start_timer) {
    countBool := true.B
  }

  when(countBool) {
    countReg := countReg + 1.U
    when(countReg === MAX_COUNT.asUInt) {
      countReg := 0.U
      countBool := 0.U
      io.end_timer := true.B
    }
  }
}
