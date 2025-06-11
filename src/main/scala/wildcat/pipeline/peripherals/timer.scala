package wildcat.pipeline.peripherals

import chisel3._
import chisel3.util._

class timer(freq: Int) extends Module{
  val io = IO(new Bundle{
    val start_timer = Input(UInt(32.W))
    val end_timer = Output(UInt(32.W))
  })

  //val PER_SECOND = 60
  val MAX_COUNT = 10000 //(freq/PER_SECOND)
  val count_bool = RegInit(0.U(32.W))
  val end_bool = RegInit(0.U(32.W))
  val count_reg = RegInit(0.U(log2Up(MAX_COUNT).W))
  when (io.start_timer(0)) {
    count_bool := 1.U
    end_bool := 0.U
  }

  when(count_bool(0)) {
    count_reg := count_reg + 1.U
    when(count_reg === MAX_COUNT.asUInt) {
      count_reg := 0.U
      count_bool := 0.U
      end_bool := 1.U
    }
  }

  io.end_timer := end_bool
}
