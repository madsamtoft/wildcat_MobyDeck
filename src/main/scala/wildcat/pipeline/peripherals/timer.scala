package wildcat.pipeline.peripherals

import chisel3._
import chisel3.util._

class timer(freq: Int) extends Module{
  val io = IO(new Bundle{
    val target_count = Input(UInt(32.W))
    val end_timer = Output(Bool())
  })

  val CLOCK_CYCLES_MS = (freq / 1000).U

  val count_bool = RegInit(false.B)
  val end_bool = RegInit(false.B)
  val target_count = RegInit(0.U(32.W))
  val count_reg = RegInit(0.U(32.W))

  when (io.target_count =/= 0.U) {
    count_bool := true.B
    end_bool := false.B
    target_count := io.target_count * CLOCK_CYCLES_MS
  }

  when(count_bool) {
    count_reg := count_reg + 1.U
    when(count_reg === target_count) {
      count_reg := 0.U
      count_bool := false.B
      end_bool := true.B
      target_count := 0.U
    }
  }

  io.end_timer := end_bool
}
